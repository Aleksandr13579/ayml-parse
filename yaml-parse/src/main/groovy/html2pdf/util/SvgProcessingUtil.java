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
package html2pdf.util;

import kernel.geom.Rectangle;
import kernel.pdf.PdfDocument;
import kernel.pdf.canvas.PdfCanvas;
import kernel.pdf.xobject.PdfFormXObject;
import layout.element.Image;
import styledxmlparser.resolver.resource.ResourceResolver;
import svg.converter.SvgConverter;
import svg.processors.ISvgProcessorResult;
import svg.processors.impl.SvgProcessorResult;
import svg.renderers.ISvgNodeRenderer;
import svg.renderers.SvgDrawContext;
import svg.renderers.impl.PdfRootSvgNodeRenderer;

/**
 * Utility class for handling operations related to SVG
 */
public class SvgProcessingUtil {
    private ResourceResolver resourceResolver;

    /**
     * Creates a new {@link SvgProcessingUtil} instance based on {@link ResourceResolver}
     * which is used to resolve a variety of resources.
     *
     * @param resourceResolver the resource resolver
     */
    public SvgProcessingUtil(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    /**
     * Create an {@code Image} layout object tied to the passed {@code PdfDocument} using the SVG processing result.
     * @param result Processing result containing the SVG information
     * @param pdfDocument pdf that shall contain the image
     * @return image layout object
     */
    public Image createImageFromProcessingResult(ISvgProcessorResult result, PdfDocument pdfDocument) {
        PdfFormXObject xObject = createXObjectFromProcessingResult(result, pdfDocument);
        return new Image(xObject);
    }

    /**
     * Create an {@link PdfFormXObject} tied to the passed {@code PdfDocument} using the SVG processing result.
     * @param result Processing result containing the SVG information
     * @param pdfDocument pdf that shall contain the image
     * @return PdfFormXObject instance
     */
    public PdfFormXObject createXObjectFromProcessingResult(ISvgProcessorResult result, PdfDocument pdfDocument){
        ISvgNodeRenderer topSvgRenderer = result.getRootRenderer();
        float width, height;
        float[] wh = SvgConverter.extractWidthAndHeight(topSvgRenderer);
        width = wh[0];
        height = wh[1];
        PdfFormXObject pdfForm = new PdfFormXObject(new Rectangle(0, 0, width, height));
        PdfCanvas canvas = new PdfCanvas(pdfForm, pdfDocument);

        ResourceResolver tempResolver = new ResourceResolver(null, resourceResolver.getRetriever());
        // TODO DEVSIX-4107 pass the resourceResolver variable (not tempResolver variable) to the
        //  SvgDrawContext constructor so that the SVG inside the SVG is processed.
        SvgDrawContext context = new SvgDrawContext(tempResolver, result.getFontProvider());
        if (result instanceof SvgProcessorResult) {
            context.setCssContext(((SvgProcessorResult) result).getContext().getCssContext());
        }
        context.addNamedObjects(result.getNamedObjects());
        context.pushCanvas(canvas);

        ISvgNodeRenderer root = new PdfRootSvgNodeRenderer(topSvgRenderer);

        root.draw(context);

        return pdfForm;
    }
}
