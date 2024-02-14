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
import styledxmlparser.css.util.CssDimensionParsingUtils;
import styledxmlparser.node.IElementNode;
import html2pdf.attach.ProcessorContext;
import html2pdf.attach.wrapelement.ColgroupWrapper;
import html2pdf.html.AttributeConstants;

/**
 * TagWorker class for a column group.
 */
public class ColgroupTagWorker implements ITagWorker {
    
    /** The column group. */
    private ColgroupWrapper colgroup;

    /**
     * Creates a new {@link ColgroupTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public ColgroupTagWorker(IElementNode element, ProcessorContext context) {
        Integer span = CssDimensionParsingUtils.parseInteger(element.getAttribute(AttributeConstants.SPAN));
        colgroup = new ColgroupWrapper(span != null ? (int)span : 1);
        colgroup.setLang(element.getAttribute(AttributeConstants.LANG));
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
        return content == null || content.trim().isEmpty();
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        if (childTagWorker instanceof ColTagWorker) {
            colgroup.getColumns().add(((ColTagWorker)childTagWorker).getColumn());
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
     * Gets the column group.
     *
     * @return the column group
     */
    public ColgroupWrapper getColgroup() {
        return colgroup;
    }
}
