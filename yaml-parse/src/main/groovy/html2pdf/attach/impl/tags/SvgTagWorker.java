/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    This program is offered under a commercial and under the AGPL license.
    For commercial licensing, contact us at https://itextpdf.com/sales.  For AGPL licensing, see below.

    AGPL licensing:
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package html2pdf.attach.impl.tags;

import html2pdf.attach.ITagWorker;
import html2pdf.attach.ProcessorContext;
import html2pdf.attach.util.ContextMappingHelper;
import html2pdf.logs.Html2PdfLogMessageConstant;
import html2pdf.util.SvgProcessingUtil;
import layout.IPropertyContainer;
import layout.element.Image;
import styledxmlparser.node.IElementNode;
import styledxmlparser.node.INode;
import svg.exceptions.SvgProcessingException;
import svg.processors.ISvgProcessorResult;
import svg.processors.impl.DefaultSvgProcessor;
import svg.processors.impl.SvgConverterProperties;
import html2pdf.attach.util.AccessiblePropHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TagWorker class for the {@code svg} element.
 */
public class SvgTagWorker implements ITagWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(SvgTagWorker.class);

    private Image svgImage;
    private ISvgProcessorResult processingResult;

    /**
     * Creates a new {@link SvgTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public SvgTagWorker(IElementNode element, ProcessorContext context) {
        svgImage = null;
        SvgConverterProperties props = ContextMappingHelper.mapToSvgConverterProperties(context);
        try {
            processingResult = new DefaultSvgProcessor().process((INode) element, props);
        } catch (SvgProcessingException spe) {
            LOGGER.error(Html2PdfLogMessageConstant.UNABLE_TO_PROCESS_SVG_ELEMENT, spe);
        }
        context.startProcessingInlineSvg();
    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        if (context.getPdfDocument() != null && processingResult != null) {
            SvgProcessingUtil util = new SvgProcessingUtil(context.getResourceResolver());
            svgImage = util.createImageFromProcessingResult(processingResult, context.getPdfDocument());

            AccessiblePropHelper.trySetLangAttribute(svgImage, element);
            context.endProcessingInlineSvg();
        }
    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return false;
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    @Override
    public IPropertyContainer getElementResult() {
        return svgImage;
    }
}
