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

import forms.form.element.IPlaceholderable;
import layout.IPropertyContainer;
import styledxmlparser.css.pseudo.CssPseudoElementNode;
import styledxmlparser.node.IStylesContainer;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.css.apply.ICssApplier;
import html2pdf.css.page.css.apply.util.BackgroundApplierUtil;
import html2pdf.css.page.css.apply.util.FontStyleApplierUtil;
import html2pdf.css.page.css.apply.util.OpacityApplierUtil;

import java.util.Map;

/**
 * {@link ICssApplier} implementation for input's placeholder.
 */
public class PlaceholderCssApplier implements ICssApplier {

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssStyles = stylesContainer.getStyles();
        IStylesContainer parentToBeProcessed = (IStylesContainer) ((CssPseudoElementNode) stylesContainer).parentNode();
        IPropertyContainer elementResult = context.getState().top().getElementResult();
        if (elementResult instanceof IPlaceholderable) {
            IPropertyContainer element = ((IPlaceholderable) elementResult).getPlaceholder();
            FontStyleApplierUtil.applyFontStyles(cssStyles, context, parentToBeProcessed, element);
            BackgroundApplierUtil.applyBackground(cssStyles, context, element);
            OpacityApplierUtil.applyOpacity(cssStyles, context, element);
        }
    }
}
