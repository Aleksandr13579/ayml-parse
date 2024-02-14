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
import styledxmlparser.node.IStylesContainer;
import styledxmlparser.node.impl.jsoup.node.JsoupElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.impl.tags.ImgTagWorker;
import html2pdf.css.page.attach.impl.tags.SpanTagWorker;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.css.apply.ICssApplier;
import html2pdf.css.page.css.apply.util.*;

import java.util.Map;

/**
 * {@link ICssApplier} implementation for Block elements.
 */
public class BlockCssApplier implements ICssApplier {

    /* (non-Javadoc)
     * @see html2pdf.css.apply.ICssApplier#apply(html2pdf.attach.ProcessorContext, html2pdf.html.node.IStylesContainer, html2pdf.attach.ITagWorker)
     */
    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssProps = stylesContainer.getStyles();

        IPropertyContainer container = tagWorker.getElementResult();
        if (container != null) {
            WidthHeightApplierUtil.applyWidthHeight(cssProps, context, container);
            BackgroundApplierUtil.applyBackground(cssProps, context, container);
            MarginApplierUtil.applyMargins(cssProps, context, container);
            PaddingApplierUtil.applyPaddings(cssProps, context, container);
            FontStyleApplierUtil.applyFontStyles(cssProps, context, stylesContainer, container);
            BorderStyleApplierUtil.applyBorders(cssProps, context, container);
            HyphenationApplierUtil.applyHyphenation(cssProps, context, stylesContainer, container);
            PositionApplierUtil.applyPosition(cssProps, context, container);
            OpacityApplierUtil.applyOpacity(cssProps, context, container);
            PageBreakApplierUtil.applyPageBreakProperties(cssProps, context, container);
            OverflowApplierUtil.applyOverflow(cssProps, container);
            TransformationApplierUtil.applyTransformation(cssProps, context, container);
            OutlineApplierUtil.applyOutlines(cssProps, context, container);
            OrphansWidowsApplierUtil.applyOrphansAndWidows(cssProps, container);
            VerticalAlignmentApplierUtil.applyVerticalAlignmentForBlocks(cssProps, container, isInlineItem(tagWorker));
            if (isFlexItem(stylesContainer)) {
                FlexApplierUtil.applyFlexItemProperties(cssProps, context, container);
            } else {
                // Floating doesn't work for flex items.
                // See CSS Flexible Box Layout Module Level 1 W3C Candidate Recommendation, 19 November 2018,
                // 3. Flex Containers: the flex and inline-flex display values
                FloatApplierUtil.applyFloating(cssProps, context, container);
            }
        }
    }

    private static boolean isInlineItem(ITagWorker tagWorker) {
        return tagWorker instanceof SpanTagWorker ||
                tagWorker instanceof ImgTagWorker;
    }

    private static boolean isFlexItem(IStylesContainer stylesContainer) {
        if (stylesContainer instanceof JsoupElementNode
                && ((JsoupElementNode) stylesContainer).parentNode() instanceof JsoupElementNode) {
            final Map<String, String> parentStyles = ((JsoupElementNode) ((JsoupElementNode) stylesContainer)
                    .parentNode()).getStyles();
            return CssConstants.FLEX.equals(parentStyles.get(CssConstants.DISPLAY));
        }
        return false;
    }
}
