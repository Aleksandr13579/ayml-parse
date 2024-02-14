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

import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.borders.Border;
import main.groovy.layout.element.Cell;
import main.groovy.layout.element.IBlockElement;
import main.groovy.layout.element.ILeafElement;
import main.groovy.layout.element.Table;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.util.AccessiblePropHelper;
import html2pdf.css.page.attach.util.WaitingInlineElementsHelper;
import html2pdf.css.page.attach.wrapelement.TableRowWrapper;
import html2pdf.css.page.attach.wrapelement.TableWrapper;
import html2pdf.css.page.css.CssConstants;

/**
 * TagWorker class for a table element.
 */
public class DisplayTableTagWorker implements ITagWorker {

    /**
     * The table.
     */
    private Table table;

    /**
     * The table wrapper.
     */
    private TableWrapper tableWrapper = new TableWrapper();

    /**
     * The helper class for waiting inline elements.
     */
    private WaitingInlineElementsHelper inlineHelper;

    /**
     * The cell waiting for flushing.
     */
    private Cell waitingCell = null;

    /**
     * The flag which indicates whether.
     */
    private boolean currentRowIsFinished = false;

    /**
     * Creates a new {@link DisplayTableTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public DisplayTableTagWorker(IElementNode element, ProcessorContext context) {
        inlineHelper = new WaitingInlineElementsHelper(element.getStyles().get(CssConstants.WHITE_SPACE),
                element.getStyles().get(CssConstants.TEXT_TRANSFORM));
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processEnd(main.groovy.html2pdf.html.node.IElementNode, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        flushWaitingCell();
        table = tableWrapper.toTable(null);
        AccessiblePropHelper.trySetLangAttribute(table, element);
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processContent(java.lang.String, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        inlineHelper.add(content);
        return true;
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processTagChild(main.groovy.html2pdf.attach.ITagWorker, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        boolean displayTableCell = childTagWorker instanceof IDisplayAware && CssConstants.TABLE_CELL.equals(((IDisplayAware) childTagWorker).getDisplay());
        if (currentRowIsFinished) {
            tableWrapper.newRow();
        }
        if (childTagWorker instanceof DisplayTableRowTagWorker) {
            flushWaitingCell();
            if (!currentRowIsFinished) {
                tableWrapper.newRow();
            }
            TableRowWrapper wrapper = ((DisplayTableRowTagWorker) childTagWorker).getTableRowWrapper();
            for (Cell cell : wrapper.getCells()) {
                tableWrapper.addCell(cell);
            }
            currentRowIsFinished = true;
            return true;
        } else if (childTagWorker.getElementResult() instanceof IBlockElement) {
            IBlockElement childResult = (IBlockElement) childTagWorker.getElementResult();
            Cell curCell = childResult instanceof Cell ? (Cell) childResult : createWrapperCell().add(childResult);
            processCell(curCell, displayTableCell);
            currentRowIsFinished = false;
            return true;
        } else if (childTagWorker.getElementResult() instanceof ILeafElement) {
            inlineHelper.add((ILeafElement) childTagWorker.getElementResult());
            currentRowIsFinished = false;
            return true;
        } else if (childTagWorker instanceof SpanTagWorker) {
            // the previous one
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
            // the current one
            if (displayTableCell) {
                flushWaitingCell();
            }
            currentRowIsFinished = false;
            return allChildrenProcessed;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return table;
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
                tableWrapper.addCell(cell);
            } else if (!cell.isEmpty()) {
                tableWrapper.addCell(cell);
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
