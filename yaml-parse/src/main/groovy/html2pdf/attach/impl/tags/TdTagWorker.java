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
import layout.IPropertyContainer;
import layout.element.Cell;
import layout.element.IBlockElement;
import layout.element.ILeafElement;
import styledxmlparser.css.util.CssDimensionParsingUtils;
import styledxmlparser.node.IElementNode;
import html2pdf.attach.ProcessorContext;
import html2pdf.attach.util.AccessiblePropHelper;
import html2pdf.attach.util.WaitingInlineElementsHelper;
import html2pdf.css.CssConstants;
import html2pdf.html.AttributeConstants;

/**
 * TagWorker class for the {@code td} element.
 */
public class TdTagWorker implements ITagWorker, IDisplayAware {

    /** The cell. */
    private Cell cell;

    /** The inline helper. */
    private WaitingInlineElementsHelper inlineHelper;

    /** The display. */
    private String display;

    /**
     * Creates a new {@link TdTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public TdTagWorker(IElementNode element, ProcessorContext context) {
        Integer colspan = CssDimensionParsingUtils.parseInteger(element.getAttribute(AttributeConstants.COLSPAN));
        Integer rowspan = CssDimensionParsingUtils.parseInteger(element.getAttribute(AttributeConstants.ROWSPAN));
        colspan = colspan != null ? colspan : 1;
        rowspan = rowspan != null ? rowspan : 1;

        cell = new Cell((int)rowspan, (int)colspan);
        cell.setPadding(0);
        inlineHelper = new WaitingInlineElementsHelper(element.getStyles().get(CssConstants.WHITE_SPACE), element.getStyles().get(CssConstants.TEXT_TRANSFORM));
        display = element.getStyles() != null ? element.getStyles().get(CssConstants.DISPLAY) : null;

        AccessiblePropHelper.trySetLangAttribute(cell, element);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processEnd(html2pdf.html.node.IElementNode, html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        inlineHelper.flushHangingLeaves(cell);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processContent(java.lang.String, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        inlineHelper.add(content);
        return true;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        boolean processed = false;
        if (childTagWorker instanceof IDisplayAware && CssConstants.INLINE_BLOCK.equals(((IDisplayAware) childTagWorker).getDisplay()) && childTagWorker.getElementResult() instanceof IBlockElement) {
            inlineHelper.add((IBlockElement) childTagWorker.getElementResult());
            processed = true;
        } else if (childTagWorker instanceof SpanTagWorker) {
            boolean allChildrenProcesssed = true;
            for (IPropertyContainer propertyContainer : ((SpanTagWorker) childTagWorker).getAllElements()) {
                if (propertyContainer instanceof ILeafElement) {
                    inlineHelper.add((ILeafElement) propertyContainer);
                } else if (propertyContainer instanceof IBlockElement && CssConstants.INLINE_BLOCK.equals(((SpanTagWorker) childTagWorker).getElementDisplay(propertyContainer))) {
                    inlineHelper.add((IBlockElement) propertyContainer);
                } else {
                    allChildrenProcesssed = processChild(propertyContainer) && allChildrenProcesssed;
                }
            }
            processed = allChildrenProcesssed;
        } else if (childTagWorker.getElementResult() instanceof ILeafElement) {
            inlineHelper.add((ILeafElement)childTagWorker.getElementResult());
            processed = true;
        } else {
            processed = processChild(childTagWorker.getElementResult());
        }
        return processed;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return cell;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.impl.tags.IDisplayAware#getDisplay()
     */
    @Override
    public String getDisplay() {
        return display;
    }

    /**
     * Processes a child element.
     *
     * @param propertyContainer the property container
     * @return true, if successful
     */
    private boolean processChild(IPropertyContainer propertyContainer) {
        boolean processed = false;
        inlineHelper.flushHangingLeaves(cell);
        if (propertyContainer instanceof IBlockElement) {
            cell.add((IBlockElement) propertyContainer);
            processed = true;
        }
        return processed;
    }
}
