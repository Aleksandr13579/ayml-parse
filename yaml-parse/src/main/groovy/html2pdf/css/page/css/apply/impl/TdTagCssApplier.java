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
import layout.borders.Border;
import layout.properties.Property;
import styledxmlparser.css.util.CssDimensionParsingUtils;
import styledxmlparser.node.IStylesContainer;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.css.apply.ICssApplier;
import html2pdf.css.page.css.apply.util.BorderStyleApplierUtil;
import html2pdf.css.page.css.apply.util.VerticalAlignmentApplierUtil;

import java.util.Map;

/**
 * {@link ICssApplier} implementation for Td elements.
 */
public class TdTagCssApplier extends BlockCssApplier {
    
    /* (non-Javadoc)
     * @see html2pdf.css.apply.impl.BlockCssApplier#apply(html2pdf.attach.ProcessorContext, html2pdf.html.node.IStylesContainer, html2pdf.attach.ITagWorker)
     */
    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker worker) {
        super.apply(context, stylesContainer, worker);

        IPropertyContainer cell = worker.getElementResult();
        if (cell != null) {
            Map<String, String> cssProps = stylesContainer.getStyles();
            VerticalAlignmentApplierUtil.applyVerticalAlignmentForCells(cssProps, context, cell);

            float em = CssDimensionParsingUtils.parseAbsoluteLength(cssProps.get(CssConstants.FONT_SIZE));
            float rem = context.getCssContext().getRootFontSize();

            Border[] bordersArray = BorderStyleApplierUtil.getBordersArray(cssProps, em, rem);
            if (bordersArray[0] == null) {
                cell.setProperty(Property.BORDER_TOP, Border.NO_BORDER);
            }
            if (bordersArray[1] == null) {
                cell.setProperty(Property.BORDER_RIGHT, Border.NO_BORDER);
            }
            if (bordersArray[2] == null) {
                cell.setProperty(Property.BORDER_BOTTOM, Border.NO_BORDER);
            }
            if (bordersArray[3] == null) {
                cell.setProperty(Property.BORDER_LEFT, Border.NO_BORDER);
            }
        }
    }
}
