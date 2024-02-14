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
import io.font.PdfEncodings;
import kernel.pdf.PdfDocument;
import kernel.pdf.PdfString;
import layout.IPropertyContainer;
import layout.element.Div;
import layout.element.IElement;
import layout.tagging.IAccessibleElement;
import styledxmlparser.node.IElementNode;
import html2pdf.attach.util.AccessiblePropHelper;
import html2pdf.html.AttributeConstants;

/**
 * TagWorker class for the {@code body} element.
 */
public class BodyTagWorker extends DivTagWorker {

    /** The parent tag worker. */
    private ITagWorker parentTagWorker;

    /** The lang attribute value. */
    private String lang;

    /**
     * Creates a new {@link BodyTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public BodyTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
        parentTagWorker = context.getState().empty() ? null : context.getState().top();

        PdfDocument pdfDocument = context.getPdfDocument();
        if (pdfDocument != null) {
            lang = element.getAttribute(AttributeConstants.LANG);
            if (lang != null) {
                pdfDocument.getCatalog().setLang(new PdfString(lang, PdfEncodings.UNICODE_BIG));
            }
        } else {
            lang = element.getLang();
        }
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processEnd(html2pdf.html.node.IElementNode, html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        if(parentTagWorker == null) {
            super.processEnd(element, context);
            if (context.getPdfDocument() == null) {
                for (IElement child : ((Div) super.getElementResult()).getChildren()) {
                    if (child instanceof IAccessibleElement) {
                        AccessiblePropHelper.trySetLangAttribute((IAccessibleElement) child, lang);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processContent(java.lang.String, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        if (parentTagWorker == null) {
            return super.processContent(content, context);
        } else {
            return parentTagWorker.processContent(content, context);
        }
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        if (parentTagWorker == null) {
            return super.processTagChild(childTagWorker, context);
        } else {
            return parentTagWorker.processTagChild(childTagWorker, context);
        }
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return parentTagWorker == null ? super.getElementResult() : parentTagWorker.getElementResult();
    }
}
