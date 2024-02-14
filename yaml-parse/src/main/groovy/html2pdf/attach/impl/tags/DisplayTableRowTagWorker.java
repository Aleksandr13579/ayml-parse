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
import html2pdf.attach.util.AccessiblePropHelper;
import html2pdf.attach.util.WaitingInlineElementsHelper;
import html2pdf.attach.wrapelement.TableRowWrapper;
import html2pdf.css.CssConstants;
import layout.IPropertyContainer;
import layout.borders.Border;
import layout.element.Cell;
import layout.element.IBlockElement;
import layout.element.ILeafElement;
import styledxmlparser.node.IElementNode;
import html2pdf.attach.wrapelement.TableWrapper;
import html2pdf.html.AttributeConstants;

/**
 * TagWorker class for a table row element.
 */
public class DisplayTableRowTagWorker implements ITagWorker {

    /**
     * The row wrapper.
     */
    private TableRowWrapper rowWrapper = new TableRowWrapper();

    /**
     * The helper class for waiting inline elements.
     */
    private WaitingInlineElementsHelper inlineHelper;

    /**
     * The cell waiting for flushing.
     */
    private Cell waitingCell = null;

    /**
     * The lang attribute value.
     */
    private String lang;

    /**
     * Creates a new {@link DisplayTableTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public DisplayTableRowTagWorker(IElementNode element, ProcessorContext context) {
        inlineHelper = new WaitingInlineElementsHelper(element.getStyles().get(CssConstants.WHITE_SPACE),
                element.getStyles().get(CssConstants.TEXT_TRANSFORM));
        lang = element.getAttribute(AttributeConstants.LANG);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processEnd(html2pdf.html.node.IElementNode, html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        flushWaitingCell();
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processContent(java.lang.String, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        inlineHelper.add(content);
        return true;
    }

    /* (non-JavadDioc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        boolean displayTableCell = childTagWorker instanceof IDisplayAware && CssConstants.TABLE_CELL.equals(((IDisplayAware) childTagWorker).getDisplay());
        if (childTagWorker.getElementResult() instanceof IBlockElement) {
            IBlockElement childResult = (IBlockElement) childTagWorker.getElementResult();
            Cell curCell = childResult instanceof Cell ? (Cell) childResult : createWrapperCell().add(childResult);
            processCell(curCell, displayTableCell);
            return true;
        } else if (childTagWorker.getElementResult() instanceof ILeafElement) {
            inlineHelper.add((ILeafElement) childTagWorker.getElementResult());
            return true;
        } else if (childTagWorker instanceof SpanTagWorker) {
            if (displayTableCell) {
                flushWaitingCell();
            }
            boolean allChildrenProcessed = true;
            for (IPropertyContainer propertyContainer : ((SpanTagWorker) childTagWorker).getAllElements()) {
                if (propertyContainer instanceof ILeafElement) {
                    inlineHelper.add((ILeafElement) propertyContainer);
                } else {
                    allChildrenProcessed = false;
                }
            }
            if (displayTableCell) {
                flushWaitingCell();
            }
            return allChildrenProcessed;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        TableWrapper tableWrapper = new TableWrapper();
        for (Cell cell : rowWrapper.getCells()) {
            AccessiblePropHelper.trySetLangAttribute(cell, lang);
            tableWrapper.addCell(cell);
        }
        return tableWrapper.toTable(null);
    }

    /**
     * Gets the table row wrapper.
     *
     * @return the table row wrapper
     */
    public TableRowWrapper getTableRowWrapper() {
        return rowWrapper;
    }

    /**
     * Processes a cell.
     *
     * @param cell the cell
     */
    private void processCell(Cell cell, boolean displayTableCell) {
        if (displayTableCell) {
            if (waitingCell != cell) {
                flushWaitingCell();
                rowWrapper.addCell(cell);
            } else if (!cell.isEmpty()) {
                rowWrapper.addCell(cell);
                waitingCell = null;
            }
        } else {
            flushInlineElementsToWaitingCell();
            waitingCell.add(cell);
        }
    }

    /**
     * Flushes inline elements to the waiting cell.
     */
    private void flushInlineElementsToWaitingCell() {
        if (null == waitingCell) {
            waitingCell = createWrapperCell();
        }
        inlineHelper.flushHangingLeaves(waitingCell);
    }


    /**
     * Flushes the waiting cell.
     */
    private void flushWaitingCell() {
        flushInlineElementsToWaitingCell();
        if (null != waitingCell) {
            processCell(waitingCell, true);
        }
    }

    /**
     * Creates a wrapper cell.
     *
     * @return the cell
     */
    private Cell createWrapperCell() {
        return new Cell().setBorder(Border.NO_BORDER).setPadding(0);
    }

}
