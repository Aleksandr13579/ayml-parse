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
package html2pdf.css.page.attach.impl.tags;

import main.groovy.io.font.PdfEncodings;
import main.groovy.kernel.pdf.PdfDocument;
import main.groovy.kernel.pdf.PdfString;
import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.element.Div;
import main.groovy.layout.element.IElement;
import main.groovy.layout.tagging.IAccessibleElement;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.util.AccessiblePropHelper;
import html2pdf.css.page.html.AttributeConstants;

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
     * @see main.groovy.html2pdf.attach.ITagWorker#processEnd(main.groovy.html2pdf.html.node.IElementNode, main.groovy.html2pdf.attach.ProcessorContext)
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
     * @see main.groovy.html2pdf.attach.ITagWorker#processContent(java.lang.String, main.groovy.html2pdf.attach.ProcessorContext)
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
     * @see main.groovy.html2pdf.attach.ITagWorker#processTagChild(main.groovy.html2pdf.attach.ITagWorker, main.groovy.html2pdf.attach.ProcessorContext)
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
     * @see main.groovy.html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return parentTagWorker == null ? super.getElementResult() : parentTagWorker.getElementResult();
    }
}
