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
package html2pdf.css.page.css.resolve;

import commons.utils.MessageFormatUtil;
import styledxmlparser.css.CommonCssConstants;
import styledxmlparser.css.page.PageMarginBoxContextNode;
import styledxmlparser.css.parse.CssDeclarationValueTokenizer;
import styledxmlparser.css.pseudo.CssPseudoElementNode;
import styledxmlparser.css.resolve.CssQuotes;
import styledxmlparser.css.util.CssGradientUtil;
import styledxmlparser.css.util.CssUtils;
import styledxmlparser.css.util.EscapeGroup;
import styledxmlparser.node.IElementNode;
import styledxmlparser.node.INode;
import styledxmlparser.node.IStylesContainer;
import styledxmlparser.node.ITextNode;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.css.page.PageMarginRunningElementNode;
import html2pdf.css.page.css.resolve.func.counter.CounterDigitsGlyphStyle;
import html2pdf.css.page.css.resolve.func.counter.CssCounterManager;
import html2pdf.css.page.css.resolve.func.counter.PageCountElementNode;
import html2pdf.css.page.css.resolve.func.counter.PageTargetCountElementNode;
import html2pdf.css.page.html.AttributeConstants;
import html2pdf.css.page.html.HtmlUtils;
import html2pdf.css.page.html.TagConstants;
import html2pdf.css.page.logs.Html2PdfLogMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The Class CssContentPropertyResolver.
 */
class CssContentPropertyResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CssContentPropertyResolver.class);

    private static final EscapeGroup[] ALLOWED_ESCAPE_CHARACTERS = {
            new EscapeGroup('\''), new EscapeGroup('\"'),
    };
    private static final int COUNTERS_MIN_PARAMS_SIZE = 2;
    private static final int COUNTER_MIN_PARAMS_SIZE = 1;
    private static final int TARGET_COUNTERS_MIN_PARAMS_SIZE = 3;
    private static final int TARGET_COUNTER_MIN_PARAMS_SIZE = 2;

    /**
     * Resolves content.
     *
     * @param styles           the styles map
     * @param contentContainer the content container
     * @param context          the CSS context
     * @return a list of {@link INode} instances
     */
    static List<INode> resolveContent(Map<String, String> styles, INode contentContainer, CssContext context) {
        String contentStr = styles.get(CssConstants.CONTENT);
        List<INode> result = new ArrayList<>();
        if (contentStr == null || CssConstants.NONE.equals(contentStr) || CssConstants.NORMAL.equals(contentStr)) {
            return null;
        }
        CssDeclarationValueTokenizer tokenizer = new CssDeclarationValueTokenizer(contentStr);
        CssDeclarationValueTokenizer.Token token;
        CssQuotes quotes = null;
        while ((token = tokenizer.getNextValidToken()) != null) {
            if (token.isString()) {
                result.add(new ContentTextNode(contentContainer, token.getValue()));
                continue;
            }
            if (token.getValue().startsWith(CssConstants.COUNTERS + "(")) {
                String paramsStr = token.getValue().substring(CssConstants.COUNTERS.length() + 1, token.getValue().length() - 1);
                final List<String> params = CssUtils.splitString(paramsStr, ',', ALLOWED_ESCAPE_CHARACTERS);
                if (params.size() < COUNTERS_MIN_PARAMS_SIZE) {
                    return errorFallback(contentStr);
                }
                // Counters are denoted by case-sensitive identifiers
                String counterName = params.get(0).trim();
                String counterSeparationStr = params.get(1).trim();
                counterSeparationStr = counterSeparationStr.substring(1, counterSeparationStr.length() - 1);
                final CounterDigitsGlyphStyle listStyleType = HtmlUtils.convertStringCounterGlyphStyleToEnum(
                        params.size() > COUNTERS_MIN_PARAMS_SIZE ? params.get(COUNTERS_MIN_PARAMS_SIZE).trim() : null);
                CssCounterManager counterManager = context.getCounterManager();
                INode scope = contentContainer;
                if (CssConstants.PAGE.equals(counterName)) {
                    result.add(new PageCountElementNode(false, contentContainer).setDigitsGlyphStyle(listStyleType));
                } else if (CssConstants.PAGES.equals(counterName)) {
                    result.add(new PageCountElementNode(true, contentContainer).setDigitsGlyphStyle(listStyleType));
                } else {
                    final String resolvedCounter =
                            counterManager.resolveCounters(counterName, counterSeparationStr, listStyleType);
                    result.add(new ContentTextNode(scope, resolvedCounter));
                }
            } else if (token.getValue().startsWith(CssConstants.COUNTER + "(")) {
                final String paramsStr = token.getValue()
                        .substring(CssConstants.COUNTER.length() + 1, token.getValue().length() - 1);
                final List<String> params = CssUtils.splitString(paramsStr, ',', ALLOWED_ESCAPE_CHARACTERS);
                if (params.size() < COUNTER_MIN_PARAMS_SIZE) {
                    return errorFallback(contentStr);
                }
                // Counters are denoted by case-sensitive identifiers
                String counterName = params.get(0).trim();
                final CounterDigitsGlyphStyle listStyleType = HtmlUtils.convertStringCounterGlyphStyleToEnum(
                        params.size() > COUNTER_MIN_PARAMS_SIZE ? params.get(COUNTER_MIN_PARAMS_SIZE).trim() : null);
                CssCounterManager counterManager = context.getCounterManager();
                INode scope = contentContainer;
                if (CssConstants.PAGE.equals(counterName)) {
                    result.add(new PageCountElementNode(false, contentContainer)
                            .setDigitsGlyphStyle(listStyleType));
                } else if (CssConstants.PAGES.equals(counterName)) {
                    result.add(new PageCountElementNode(true, contentContainer)
                            .setDigitsGlyphStyle(listStyleType));
                } else {
                    final String resolvedCounter = counterManager.resolveCounter(counterName, listStyleType);
                    result.add(new ContentTextNode(scope, resolvedCounter));
                }
            } else if (token.getValue().startsWith(CssConstants.TARGET_COUNTER + "(")) {
                String paramsStr = token.getValue()
                        .substring(CssConstants.TARGET_COUNTER.length() + 1, token.getValue().length() - 1);
                List<String> params = CssUtils.splitString(paramsStr, ',', ALLOWED_ESCAPE_CHARACTERS);
                if (params.size() < TARGET_COUNTER_MIN_PARAMS_SIZE) {
                    return errorFallback(contentStr);
                }
                final String target = params.get(0).startsWith(CommonCssConstants.ATTRIBUTE + "(") ? CssUtils
                        .extractAttributeValue(params.get(0), (IElementNode) contentContainer.parentNode())
                        : CssUtils.extractUrl(params.get(0));
                if (target == null) {
                    return errorFallback(contentStr);
                }
                final String counterName = params.get(1).trim();
                final CounterDigitsGlyphStyle listStyleType = HtmlUtils.convertStringCounterGlyphStyleToEnum(
                        params.size() > TARGET_COUNTER_MIN_PARAMS_SIZE ?
                        params.get(TARGET_COUNTER_MIN_PARAMS_SIZE).trim() : null);
                if (CssConstants.PAGE.equals(counterName)) {
                    result.add(new PageTargetCountElementNode(contentContainer, target)
                            .setDigitsGlyphStyle(listStyleType));
                } else if (CssConstants.PAGES.equals(counterName)) {
                    result.add(new PageCountElementNode(true, contentContainer)
                            .setDigitsGlyphStyle(listStyleType));
                } else {
                    final String counter = context.getCounterManager().resolveTargetCounter
                            (target.replace("'", "").replace("#", ""), counterName, listStyleType);
                    final ContentTextNode node = new ContentTextNode(contentContainer, counter == null ? "0" : counter);
                    result.add(node);
                }
            } else if (token.getValue().startsWith(CssConstants.TARGET_COUNTERS + "(")) {
                final String paramsStr = token.getValue()
                        .substring(CssConstants.TARGET_COUNTERS.length() + 1, token.getValue().length() - 1);
                final List<String> params = CssUtils.splitString(paramsStr, ',', ALLOWED_ESCAPE_CHARACTERS);
                if (params.size() < TARGET_COUNTERS_MIN_PARAMS_SIZE) {
                    return errorFallback(contentStr);
                }
                final String target = params.get(0).startsWith(CommonCssConstants.ATTRIBUTE + "(") ? CssUtils
                        .extractAttributeValue(params.get(0), (IElementNode) contentContainer.parentNode())
                        : CssUtils.extractUrl(params.get(0));
                if (target == null) {
                    return errorFallback(contentStr);
                }
                final String counterName = params.get(1).trim();
                String counterSeparator = params.get(2).trim();
                counterSeparator = counterSeparator.substring(1, counterSeparator.length() - 1);
                final CounterDigitsGlyphStyle listStyleType = HtmlUtils.convertStringCounterGlyphStyleToEnum(
                        params.size() > TARGET_COUNTERS_MIN_PARAMS_SIZE ?
                        params.get(TARGET_COUNTERS_MIN_PARAMS_SIZE).trim() : null);
                if (CssConstants.PAGE.equals(counterName)) {
                    result.add(new PageTargetCountElementNode(contentContainer, target)
                            .setDigitsGlyphStyle(listStyleType));
                } else if (CssConstants.PAGES.equals(counterName)) {
                    result.add(new PageCountElementNode(true, contentContainer)
                            .setDigitsGlyphStyle(listStyleType));
                } else {
                    final String counters = context.getCounterManager().resolveTargetCounters
                            (target.replace(",", "").replace("#", ""), counterName, counterSeparator, listStyleType);
                    final ContentTextNode node =
                            new ContentTextNode(contentContainer, counters == null ? "0" : counters);
                    result.add(node);
                }
            } else if (token.getValue().startsWith("url(")) {
                Map<String, String> attributes = new HashMap<>();
                attributes.put(AttributeConstants.SRC, CssUtils.extractUrl(token.getValue()));
                attributes.put(AttributeConstants.STYLE, CssConstants.DISPLAY + ":" + CssConstants.INLINE_BLOCK);
                result.add(new CssContentElementNode(contentContainer, TagConstants.IMG, attributes));
            } else if (CssGradientUtil.isCssLinearGradientValue(token.getValue())) {
                Map<String, String> attributes = new HashMap<>();
                attributes.put(AttributeConstants.STYLE, CssConstants.BACKGROUND_IMAGE + ":" + token.getValue() + ";"
                        + CssConstants.HEIGHT + ":" + CssConstants.INHERIT + ";"
                        + CssConstants.WIDTH + ":" + CssConstants.INHERIT + ";");
                result.add(new CssContentElementNode(contentContainer, TagConstants.DIV, attributes));
            } else if (token.getValue().startsWith(CommonCssConstants.ATTRIBUTE + "(")
                    && contentContainer instanceof CssPseudoElementNode) {
                String value = CssUtils
                        .extractAttributeValue(token.getValue(), (IElementNode) contentContainer.parentNode());
                if (value == null) {
                    return errorFallback(contentStr);
                }
                result.add(new ContentTextNode(contentContainer, value));
            } else if (token.getValue().endsWith("quote") && contentContainer instanceof IStylesContainer) {
                if (quotes == null) {
                    quotes = CssQuotes.createQuotes(styles.get(CssConstants.QUOTES), true);
                }
                String value = quotes.resolveQuote(token.getValue(), context);
                if (value == null) {
                    return errorFallback(contentStr);
                }
                result.add(new ContentTextNode(contentContainer, value));
            } else if (token.getValue().startsWith(CssConstants.ELEMENT + "(") && contentContainer instanceof PageMarginBoxContextNode) {
                String paramsStr = token.getValue().substring(CssConstants.ELEMENT.length() + 1, token.getValue().length() - 1);
                String[] params = paramsStr.split(",");
                if (params.length == 0) {
                    return errorFallback(contentStr);
                }
                String name = params[0].trim();
                String runningElementOccurrence = null;
                if (params.length > 1) {
                    runningElementOccurrence = params[1].trim();
                }

                result.add(new PageMarginRunningElementNode(name, runningElementOccurrence));
            } else {
                return errorFallback(contentStr);
            }
        }
        return result;
    }

    /**
     * Resolves content in case of errors.
     *
     * @param contentStr the content
     * @return the resulting list of {@link INode} instances
     */
    private static List<INode> errorFallback(String contentStr) {
        int logMessageParameterMaxLength = 100;
        if (contentStr.length() > logMessageParameterMaxLength) {
            contentStr = contentStr.substring(0, logMessageParameterMaxLength) + ".....";
        }

        LOGGER.error(MessageFormatUtil.format(Html2PdfLogMessageConstant.CONTENT_PROPERTY_INVALID, contentStr));
        return null;
    }

    /**
     * {@link ITextNode} implementation for content text.
     */
    private static class ContentTextNode implements ITextNode {

        /**
         * The parent.
         */
        private final INode parent;

        /**
         * The content.
         */
        private String content;

        /**
         * Creates a new {@link ContentTextNode} instance.
         *
         * @param parent  the parent
         * @param content the content
         */
        ContentTextNode(INode parent, String content) {
            this.parent = parent;
            this.content = content;
        }

        /* (non-Javadoc)
         * @see html2pdf.html.node.INode#childNodes()
         */
        @Override
        public List<INode> childNodes() {
            return Collections.<INode>emptyList();
        }

        /* (non-Javadoc)
         * @see html2pdf.html.node.INode#addChild(html2pdf.html.node.INode)
         */
        @Override
        public void addChild(INode node) {
            throw new UnsupportedOperationException();
        }

        /* (non-Javadoc)
         * @see html2pdf.html.node.INode#parentNode()
         */
        @Override
        public INode parentNode() {
            return parent;
        }

        /* (non-Javadoc)
         * @see html2pdf.html.node.ITextNode#wholeText()
         */
        @Override
        public String wholeText() {
            return content;
        }

    }

}
