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
package html2pdf.css.page.css.apply.impl;

import layout.IPropertyContainer;
import layout.properties.ListSymbolPosition;
import layout.properties.Property;
import styledxmlparser.node.IElementNode;
import styledxmlparser.node.INode;
import styledxmlparser.node.IStylesContainer;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.css.apply.ICssApplier;
import html2pdf.css.page.css.apply.util.ListStyleApplierUtil;
import html2pdf.css.page.html.TagConstants;

/**
 * {@link ICssApplier} implementation for Li elements.
 */
public class LiTagCssApplier extends BlockCssApplier {

    /* (non-Javadoc)
     * @see html2pdf.css.apply.impl.BlockCssApplier#apply(html2pdf.attach.ProcessorContext, html2pdf.html.node.IStylesContainer, html2pdf.attach.ITagWorker)
     */
    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        super.apply(context, stylesContainer, tagWorker);
        IPropertyContainer propertyContainer = tagWorker.getElementResult();

        if (propertyContainer != null && stylesContainer instanceof INode) {
            INode parent = ((INode) stylesContainer).parentNode();
            boolean parentIsDl = parent instanceof IElementNode && TagConstants.DL.equals(((IElementNode) parent).name());
            if (CssConstants.INSIDE.equals(stylesContainer.getStyles().get(CssConstants.LIST_STYLE_POSITION)) || parentIsDl) {
                propertyContainer.setProperty(Property.LIST_SYMBOL_POSITION, ListSymbolPosition.INSIDE);
            } else {
                propertyContainer.setProperty(Property.LIST_SYMBOL_POSITION, ListSymbolPosition.OUTSIDE);
            }

            ListStyleApplierUtil.applyListStyleTypeProperty(stylesContainer, stylesContainer.getStyles(), context, propertyContainer);
            ListStyleApplierUtil.applyListStyleImageProperty(stylesContainer.getStyles(), context, propertyContainer);
        }
    }

}
