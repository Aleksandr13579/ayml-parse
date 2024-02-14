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
import main.groovy.styledxmlparser.css.util.CssDimensionParsingUtils;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.wrapelement.ColWrapper;
import html2pdf.css.page.html.AttributeConstants;

/**
 * TagWorker class for a column.
 */
public class ColTagWorker implements ITagWorker {
    
    /** The column. */
    private ColWrapper col;

    /**
     * Creates a new {@link ColTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public ColTagWorker(IElementNode element, ProcessorContext context) {
        Integer span = CssDimensionParsingUtils.parseInteger(element.getAttribute(AttributeConstants.SPAN));
        col = new ColWrapper(span != null ? (int)span : 1);
        col.setLang(element.getAttribute(AttributeConstants.LANG));
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processEnd(main.groovy.html2pdf.html.node.IElementNode, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processContent(java.lang.String, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return content == null || content.trim().isEmpty();
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processTagChild(main.groovy.html2pdf.attach.ITagWorker, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return null;
    }

    /**
     * Gets the column.
     *
     * @return the column
     */
    public ColWrapper getColumn() {
        return col;
    }
}
