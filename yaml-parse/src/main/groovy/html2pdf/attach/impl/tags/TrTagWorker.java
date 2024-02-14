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
import html2pdf.attach.wrapelement.TableRowWrapper;
import layout.IPropertyContainer;
import layout.element.Cell;
import styledxmlparser.node.IElementNode;
import html2pdf.attach.util.AccessiblePropHelper;
import html2pdf.html.AttributeConstants;

/**
 * TagWorker class for the {@code tr} element.
 */
public class TrTagWorker implements ITagWorker {

    /**
     * The row wrapper.
     */
    private TableRowWrapper rowWrapper;

    /**
     * The parent tag worker.
     */
    private ITagWorker parentTagWorker;

    /**
     * The lang attribute value.
     */
    private String lang;

    /**
     * Creates a new {@link TrTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public TrTagWorker(IElementNode element, ProcessorContext context) {
        rowWrapper = new TableRowWrapper();
        parentTagWorker = context.getState().empty() ? null : context.getState().top();
        if (parentTagWorker instanceof TableTagWorker) {
            ((TableTagWorker) parentTagWorker).applyColStyles();
        }
        lang = element.getAttribute(AttributeConstants.LANG);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processEnd(html2pdf.html.node.IElementNode, html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {

    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processContent(java.lang.String, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return parentTagWorker != null && parentTagWorker.processContent(content, context);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        if (childTagWorker.getElementResult() instanceof Cell) {
            Cell cell = (Cell) childTagWorker.getElementResult();
            AccessiblePropHelper.trySetLangAttribute(cell, lang);
            rowWrapper.addCell(cell);
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return null;
    }

    /**
     * Gets the table row wrapper.
     *
     * @return the table row wrapper
     */
    TableRowWrapper getTableRowWrapper() {
        return rowWrapper;
    }
}
