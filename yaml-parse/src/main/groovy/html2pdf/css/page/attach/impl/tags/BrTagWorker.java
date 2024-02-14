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

import layout.IPropertyContainer;
import layout.element.Text;
import styledxmlparser.node.IElementNode;
import styledxmlparser.util.FontFamilySplitterUtil;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.util.AccessiblePropHelper;
import html2pdf.css.page.css.CssConstants;

import java.util.List;

/**
 * TagWorker class for the {@code br} element.
 */
public class BrTagWorker implements ITagWorker {

    /** A new line Text element. */
    private Text newLine = new Text("\n");

    /**
     * Creates a new {@link BrTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public BrTagWorker(IElementNode element, ProcessorContext context) {
        //There is no mappings for BR element in DefaultTagCssApplierMapping,
        // because only font-family should be applied to <br /> element.
        String fontFamily = element.getStyles().get(CssConstants.FONT_FAMILY);
        // TODO DEVSIX-2534
        List<String> splitFontFamily = FontFamilySplitterUtil.splitFontFamily(fontFamily);
        newLine.setFontFamily(splitFontFamily.toArray(new String[splitFontFamily.size()]));

        AccessiblePropHelper.trySetLangAttribute(newLine, element);
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
        return false;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return newLine;
    }
}
