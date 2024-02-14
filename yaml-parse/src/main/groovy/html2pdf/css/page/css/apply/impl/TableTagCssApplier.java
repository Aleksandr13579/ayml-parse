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

import layout.element.Table;
import layout.properties.BorderCollapsePropertyValue;
import layout.properties.Property;
import styledxmlparser.css.util.CssDimensionParsingUtils;
import styledxmlparser.node.IStylesContainer;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.css.apply.ICssApplier;

/**
 * {@link ICssApplier} implementation for table elements.
 */
public class TableTagCssApplier extends BlockCssApplier {

    /* (non-Javadoc)
     * @see html2pdf.css.apply.impl.BlockCssApplier#apply(html2pdf.attach.ProcessorContext, html2pdf.html.node.IStylesContainer, html2pdf.attach.ITagWorker)
     */
    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker worker) {
        super.apply(context, stylesContainer, worker);

        Table table = (Table) worker.getElementResult();
        if (table != null) {
            String tableLayout = stylesContainer.getStyles().get(CssConstants.TABLE_LAYOUT);
            if (tableLayout != null) {
                table.setProperty(Property.TABLE_LAYOUT, tableLayout);
            }
            String borderCollapse = stylesContainer.getStyles().get(CssConstants.BORDER_COLLAPSE);
            // BorderCollapsePropertyValue.COLLAPSE is default in iText layout
            if (null == borderCollapse || CssConstants.SEPARATE.equals(borderCollapse)) {
                table.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
            }
            String borderSpacing = stylesContainer.getStyles().get(CssConstants.BORDER_SPACING);
            if (null != borderSpacing) {
                String[] props = borderSpacing.split("\\s+");
                if (1 == props.length) {
                    table.setHorizontalBorderSpacing(CssDimensionParsingUtils.parseAbsoluteLength(props[0]));
                    table.setVerticalBorderSpacing(CssDimensionParsingUtils.parseAbsoluteLength(props[0]));
                } else if (2 == props.length) {
                    table.setHorizontalBorderSpacing(CssDimensionParsingUtils.parseAbsoluteLength(props[0]));
                    table.setVerticalBorderSpacing(CssDimensionParsingUtils.parseAbsoluteLength(props[1]));
                }
            }
        }
    }
}
