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

import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.impl.layout.Html2PdfProperty;
import html2pdf.css.page.attach.impl.layout.PageCountElement;
import html2pdf.css.page.attach.impl.layout.PageCountType;
import html2pdf.css.page.attach.impl.layout.PageTargetCountElement;
import html2pdf.css.page.css.resolve.func.counter.CounterDigitsGlyphStyle;
import html2pdf.css.page.css.resolve.func.counter.PageCountElementNode;
import html2pdf.css.page.css.resolve.func.counter.PageTargetCountElementNode;

/**
 * TagWorker class for the page count.
 */
public class PageCountWorker extends SpanTagWorker {

    private IPropertyContainer pageCountElement;

    /**
     * Creates a new {@link PageCountWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public PageCountWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
        if (element instanceof PageCountElementNode) {
            final CounterDigitsGlyphStyle digitsStyle = ((PageCountElementNode) element).getDigitsGlyphStyle();
            if (element instanceof PageTargetCountElementNode) {
                pageCountElement =
                        new PageTargetCountElement(((PageTargetCountElementNode) element).getTarget(), digitsStyle);
            } else {
                final boolean totalPageCount = ((PageCountElementNode) element).isTotalPageCount();
                pageCountElement = new PageCountElement(digitsStyle);
                pageCountElement.setProperty(Html2PdfProperty.PAGE_COUNT_TYPE,
                        totalPageCount ? PageCountType.TOTAL_PAGE_COUNT : PageCountType.CURRENT_PAGE_NUMBER);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.itextpdf.html2pdf.attach.impl.tags.SpanTagWorker#processEnd(com.itextpdf.html2pdf.html.node.IElementNode, com.itextpdf.html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        super.processTagChild(this, context);
        super.processEnd(element, context);
    }

    @Override
    public IPropertyContainer getElementResult() {
        return pageCountElement;
    }
}
