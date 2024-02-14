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
package html2pdf.css.apply.impl;

import html2pdf.attach.ITagWorker;
import layout.IPropertyContainer;
import layout.element.Div;
import styledxmlparser.node.IStylesContainer;
import html2pdf.attach.ProcessorContext;
import html2pdf.css.CssConstants;
import html2pdf.css.apply.ICssApplier;
import html2pdf.css.apply.util.BackgroundApplierUtil;
import html2pdf.css.apply.util.WidthHeightApplierUtil;

import java.util.Map;

/**
 * {@link ICssApplier} implementation for linear-gradient elements in content CSS property.
 */
public class CssContentLinearGradientApplier implements ICssApplier {
    /** The default width of the div content in the points, and this will be 300 pixels. */
    private static final float DEFAULT_CONTENT_WIDTH_PT = 225;

    /** The default height of the div content in the points, and this will be 150 pixels. */
    private static final float DEFAULT_CONTENT_HEIGHT_PT = 112.5f;

    /* (non-Javadoc)
     * @see html2pdf.css.apply.ICssApplier#apply(html2pdf.attach.ProcessorContext, html2pdf.html.node.IStylesContainer, html2pdf.attach.ITagWorker)
     */
    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssProps = stylesContainer.getStyles();

        IPropertyContainer container = tagWorker.getElementResult();
        if (container != null) {
            if (container instanceof Div) {
                if (!cssProps.containsKey(CssConstants.WIDTH) || CssConstants.AUTO.equals(cssProps.get(CssConstants.WIDTH))) {
                    ((Div) container).setWidth(DEFAULT_CONTENT_WIDTH_PT);
                }
                if (!cssProps.containsKey(CssConstants.HEIGHT) || CssConstants.AUTO.equals(cssProps.get(CssConstants.HEIGHT))) {
                    ((Div) container).setHeight(DEFAULT_CONTENT_HEIGHT_PT);
                }
            }

            WidthHeightApplierUtil.applyWidthHeight(cssProps, context, container);
            BackgroundApplierUtil.applyBackground(cssProps, context, container);
        }
    }
}
