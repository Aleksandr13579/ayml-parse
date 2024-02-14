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

import commons.utils.FileUtil;
import commons.utils.MessageFormatUtil;
import html2pdf.attach.ITagWorker;
import html2pdf.logs.Html2PdfLogMessageConstant;
import html2pdf.util.SvgProcessingUtil;
import kernel.pdf.PdfDocument;
import layout.IPropertyContainer;
import layout.element.Image;
import styledxmlparser.node.IElementNode;
import styledxmlparser.resolver.resource.ResourceResolver;
import svg.converter.SvgConverter;
import svg.exceptions.SvgProcessingException;
import svg.processors.ISvgProcessorResult;
import svg.processors.impl.SvgConverterProperties;
import html2pdf.attach.ProcessorContext;
import html2pdf.attach.util.AccessiblePropHelper;
import html2pdf.attach.util.ContextMappingHelper;
import html2pdf.html.AttributeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * TagWorker class for the {@code object} element.
 */
public class ObjectTagWorker implements ITagWorker {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectTagWorker.class);

    /**
     * Helper for conversion of SVG processing results.
     */
    private final SvgProcessingUtil processUtil;

    /**
     * An Outcome of the worker. The Svg as image.
     */
    private Image image;

    /**
     * Output of SVG processing.  Intermediate result.
     */
    private ISvgProcessorResult res;

    /**
     * Creates a new {@link ImgTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public ObjectTagWorker(IElementNode element, ProcessorContext context) {
        this.processUtil = new SvgProcessingUtil(context.getResourceResolver());

        //Retrieve object type
        String type = element.getAttribute(AttributeConstants.TYPE);
        if (isSvgImage(type)) {
            String dataValue = element.getAttribute(AttributeConstants.DATA);
            try (InputStream svgStream = context.getResourceResolver().retrieveResourceAsInputStream(dataValue)) {
                if (svgStream != null) {
                    SvgConverterProperties props = ContextMappingHelper.mapToSvgConverterProperties(context);
                    if (!ResourceResolver.isDataSrc(dataValue)) {
                        URL fullURL = context.getResourceResolver().resolveAgainstBaseUri(dataValue);
                        String dir = FileUtil.parentDirectory(fullURL);
                        props.setBaseUri(dir);
                    }
                    res = SvgConverter.parseAndProcess(svgStream, props);
                }
            } catch (SvgProcessingException spe) {
                LOGGER.error(spe.getMessage());
            } catch (IOException | URISyntaxException ie) {
                LOGGER.error(
                        MessageFormatUtil.format(
                                Html2PdfLogMessageConstant.UNABLE_TO_RETRIEVE_STREAM_WITH_GIVEN_BASE_URI,
                                context.getBaseUri(),
                                element.getAttribute(AttributeConstants.DATA),
                                ie));
            }
        }
    }

    // TODO DEVSIX-4460. According specs 'At least one of the "data" or "type" attribute MUST be defined.'
    private boolean isSvgImage(String typeAttribute) {
        return AttributeConstants.ObjectTypes.SVGIMAGE.equals(typeAttribute);
    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        if (context.getPdfDocument() != null) {
            PdfDocument document = context.getPdfDocument();
            //Create Image object

            if (res != null) {
                image = processUtil.createImageFromProcessingResult(res, document);
                AccessiblePropHelper.trySetLangAttribute(image, element);
            }

        } else {
            LOGGER.error(Html2PdfLogMessageConstant.PDF_DOCUMENT_NOT_PRESENT);
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
        return image;
    }
}
