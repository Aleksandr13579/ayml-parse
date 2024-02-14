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
package html2pdf.css.page.attach.impl;

import html2pdf.util.TagProcessorMapping;
import styledxmlparser.css.page.PageMarginBoxContextNode;
import styledxmlparser.css.pseudo.CssPseudoElementUtil;
import styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.impl.tags.*;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.css.resolve.func.counter.PageCountElementNode;
import html2pdf.css.page.html.TagConstants;

/**
 * Contains the actual mapping of the {@link DefaultTagWorkerFactory}.
 */
class DefaultTagWorkerMapping {

    /**
     * The worker mapping.
     */
    private static TagProcessorMapping<ITagWorkerCreator> workerMapping;

    static {
        workerMapping = new TagProcessorMapping<>();
        workerMapping.putMapping(TagConstants.A, (lhs, rhs) -> new ATagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.ABBR, (lhs, rhs) -> new AbbrTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.ADDRESS, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.ARTICLE, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.ASIDE, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.B, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.BDI, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.BDO, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.BLOCKQUOTE, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.BODY, (lhs, rhs) -> new BodyTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.BR, (lhs, rhs) -> new BrTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.BUTTON, (lhs, rhs) -> new ButtonTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.CAPTION, (lhs, rhs) -> new CaptionTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.CENTER, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.CITE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.CODE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.COL, (lhs, rhs) -> new ColTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.COLGROUP, (lhs, rhs) -> new ColgroupTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DD, (lhs, rhs) -> new LiTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DEL, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DFN, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DIV, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DL, (lhs, rhs) -> new UlOlTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DT, (lhs, rhs) -> new LiTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.EM, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.FIELDSET, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.FIGCAPTION, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.FIGURE, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.FONT, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.FOOTER, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.FORM, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.H1, (lhs, rhs) -> new HTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.H2, (lhs, rhs) -> new HTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.H3, (lhs, rhs) -> new HTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.H4, (lhs, rhs) -> new HTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.H5, (lhs, rhs) -> new HTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.H6, (lhs, rhs) -> new HTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.HEADER, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.HR, (lhs, rhs) -> new HrTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.HTML, (lhs, rhs) -> new HtmlTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.I, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.IMG, (lhs, rhs) -> new ImgTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.INPUT, (lhs, rhs) -> new InputTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.INS, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.KBD, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LABEL, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LEGEND, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LI, (lhs, rhs) -> new LiTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LINK, (lhs, rhs) -> new LinkTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.MAIN, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.MARK, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.META, (lhs, rhs) -> new MetaTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.NAV, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.OBJECT, (lhs, rhs) -> new ObjectTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.OL, (lhs, rhs) -> new UlOlTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.OPTGROUP, (lhs, rhs) -> new OptGroupTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.OPTION, (lhs, rhs) -> new OptionTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.P, (lhs, rhs) -> new PTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.PRE, (lhs, rhs) -> new PreTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.Q, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.S, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SAMP, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SECTION, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SELECT, (lhs, rhs) -> new SelectTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SMALL, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SPAN, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.STRIKE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.STRONG, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SUB, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SUP, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SVG, (lhs, rhs) -> new SvgTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TABLE, (lhs, rhs) -> new TableTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TD, (lhs, rhs) -> new TdTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TEXTAREA, (lhs, rhs) -> new TextAreaTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TFOOT, (lhs, rhs) -> new TableFooterTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TH, (lhs, rhs) -> new ThTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.THEAD, (lhs, rhs) -> new TableHeaderTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TIME, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TITLE, (lhs, rhs) -> new TitleTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TR, (lhs, rhs) -> new TrTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.TT, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.U, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.UL, (lhs, rhs) -> new UlOlTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.VAR, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));

        String placeholderPseudoElemName = CssPseudoElementUtil.createPseudoElementTagName(CssConstants.PLACEHOLDER);
        workerMapping.putMapping(placeholderPseudoElemName, (lhs, rhs) -> new PlaceholderTagWorker(lhs, rhs));

        workerMapping.putMapping(TagConstants.UL, CssConstants.INLINE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LI, CssConstants.INLINE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LI, CssConstants.INLINE_BLOCK, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LI, CssConstants.BLOCK, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DD, CssConstants.INLINE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DT, CssConstants.INLINE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));

        workerMapping.putMapping(TagConstants.SPAN, CssConstants.BLOCK, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SPAN, CssConstants.INLINE_BLOCK,
                (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.A, CssConstants.BLOCK, (lhs, rhs) -> new ABlockTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.A, CssConstants.INLINE_BLOCK,
                (lhs, rhs) -> new ABlockTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.A, CssConstants.TABLE_CELL, (lhs, rhs) -> new ABlockTagWorker(lhs, rhs));

        workerMapping.putMapping(TagConstants.LABEL, CssConstants.BLOCK, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.LABEL, CssConstants.INLINE_BLOCK,
                (lhs, rhs) -> new DivTagWorker(lhs, rhs));

        workerMapping.putMapping(TagConstants.DIV, CssConstants.TABLE,
                (lhs, rhs) -> new DisplayTableTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DIV, CssConstants.TABLE_ROW,
                (lhs, rhs) -> new DisplayTableRowTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DIV, CssConstants.INLINE, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DIV, CssConstants.INLINE_TABLE,
                (lhs, rhs) -> new DisplayTableTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DIV, CssConstants.TABLE_CELL, (lhs, rhs) -> new TdTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.DIV, CssConstants.FLEX,
                (lhs, rhs) -> new DisplayFlexTagWorker(lhs, rhs));
        workerMapping.putMapping(TagConstants.SPAN, CssConstants.FLEX,
                (lhs, rhs) -> new DisplayFlexTagWorker(lhs, rhs));

        // pseudo elements mapping
        String beforePseudoElemName = CssPseudoElementUtil.createPseudoElementTagName(CssConstants.BEFORE);
        String afterPseudoElemName = CssPseudoElementUtil.createPseudoElementTagName(CssConstants.AFTER);
        workerMapping.putMapping(beforePseudoElemName, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(afterPseudoElemName, (lhs, rhs) -> new SpanTagWorker(lhs, rhs));
        workerMapping.putMapping(beforePseudoElemName, CssConstants.INLINE_BLOCK,
                (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(afterPseudoElemName, CssConstants.INLINE_BLOCK,
                (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(beforePseudoElemName, CssConstants.BLOCK, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(afterPseudoElemName, CssConstants.BLOCK, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        // For now behaving like display:block in display:table case is sufficient
        workerMapping.putMapping(beforePseudoElemName, CssConstants.TABLE, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(afterPseudoElemName, CssConstants.TABLE, (lhs, rhs) -> new DivTagWorker(lhs, rhs));
        workerMapping.putMapping(CssPseudoElementUtil.createPseudoElementTagName(TagConstants.IMG),
                (lhs, rhs) -> new ImgTagWorker(lhs, rhs));
        workerMapping.putMapping(CssPseudoElementUtil.createPseudoElementTagName(TagConstants.DIV),
                (lhs, rhs) -> new DivTagWorker(lhs, rhs));

        // custom elements mapping, implementation-specific
        workerMapping.putMapping(PageCountElementNode.PAGE_COUNTER_TAG,
                (lhs, rhs) -> new PageCountWorker(lhs, rhs));
        workerMapping.putMapping(PageMarginBoxContextNode.PAGE_MARGIN_BOX_TAG,
                (lhs, rhs) -> new PageMarginBoxWorker(lhs, rhs));
    }

    /**
     * Gets the default tag worker mapping.
     *
     * @return the default mapping
     */
    TagProcessorMapping<ITagWorkerCreator> getDefaultTagWorkerMapping() {
        return workerMapping;
    }

    /**
     * Instantiates a new {@link DefaultTagWorkerMapping} instance.
     */
    DefaultTagWorkerMapping() {
    }

    /**
     * Represents a function, which accepts {@link IElementNode} and {@link ProcessorContext},
     * and creates {@link ITagWorker} instance based on these parameters.
     */
    @FunctionalInterface
    public interface ITagWorkerCreator {
        /**
         * Creates an {@link ITagWorker} instance.
         * @param elementNode tag worker element node.
         * @param processorContext processor context.
         * @return {@link ITagWorker} instance.
         */
        ITagWorker create(IElementNode elementNode, ProcessorContext processorContext);
    }

}
