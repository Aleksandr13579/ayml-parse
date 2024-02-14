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

import styledxmlparser.node.IStylesContainer;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.impl.tags.ColTagWorker;
import html2pdf.css.page.css.apply.ICssApplier;
import html2pdf.css.page.css.apply.util.SupportedColColgroupPropertiesUtil;

import java.util.Map;

/**
 * {@link ICssApplier} implementation for columns.
 */
public class ColTagCssApplier implements ICssApplier {

    /* (non-Javadoc)
     * @see html2pdf.css.apply.ICssApplier#apply(html2pdf.attach.ProcessorContext, html2pdf.html.node.IStylesContainer, html2pdf.attach.ITagWorker)
     */
    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssProps = stylesContainer.getStyles();

        if (cssProps != null && tagWorker instanceof ColTagWorker) {
            ((ColTagWorker) tagWorker).getColumn()
                    .setCellCssProps(SupportedColColgroupPropertiesUtil.getCellProperties(cssProps))
                    .setOwnCssProps(SupportedColColgroupPropertiesUtil.getOwnProperties(cssProps))
                    .setWidth(SupportedColColgroupPropertiesUtil.getWidth(cssProps, context));
        }
    }
}
