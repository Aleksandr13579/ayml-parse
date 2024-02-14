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

import main.groovy.layout.IPropertyContainer;
import main.groovy.styledxmlparser.node.IStylesContainer;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.impl.layout.BodyHtmlStylesContainer;
import html2pdf.css.page.attach.impl.layout.Html2PdfProperty;
import html2pdf.css.page.css.apply.ICssApplier;
import html2pdf.css.page.css.apply.util.*;

import java.util.Map;

/**
 * {@link ICssApplier} implementation for Body elements.
 */
public class BodyTagCssApplier implements ICssApplier {

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.css.apply.ICssApplier#apply(main.groovy.html2pdf.attach.ProcessorContext, main.groovy.html2pdf.html.node.IStylesContainer, main.groovy.html2pdf.attach.ITagWorker)
     */
    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssProps = stylesContainer.getStyles();
        BodyHtmlStylesContainer styleProperty = new BodyHtmlStylesContainer();
        IPropertyContainer container = tagWorker.getElementResult();
        if (container != null) {
            BackgroundApplierUtil.applyBackground(cssProps, context, styleProperty);
            MarginApplierUtil.applyMargins(cssProps, context, styleProperty);
            PaddingApplierUtil.applyPaddings(cssProps, context, styleProperty);
            BorderStyleApplierUtil.applyBorders(cssProps, context, styleProperty);
            if (styleProperty.hasStylesToApply()) {
                container.setProperty(Html2PdfProperty.BODY_STYLING, styleProperty);
            }
            FontStyleApplierUtil.applyFontStyles(cssProps, context, stylesContainer, container);
        }
    }
}
