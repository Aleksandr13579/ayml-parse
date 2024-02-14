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
package html2pdf.css.page.css.apply.util;

import main.groovy.commons.utils.MessageFormatUtil;
import main.groovy.kernel.colors.Color;
import main.groovy.kernel.colors.gradients.StrategyBasedLinearGradientBuilder;
import main.groovy.kernel.geom.Rectangle;
import main.groovy.kernel.numbering.AlphabetNumbering;
import main.groovy.kernel.pdf.PdfDocument;
import main.groovy.kernel.pdf.canvas.PdfCanvas;
import main.groovy.kernel.pdf.xobject.PdfFormXObject;
import main.groovy.kernel.pdf.xobject.PdfImageXObject;
import main.groovy.kernel.pdf.xobject.PdfXObject;
import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.element.*;
import main.groovy.layout.properties.IListSymbolFactory;
import main.groovy.layout.properties.ListNumberingType;
import main.groovy.layout.properties.ListSymbolPosition;
import main.groovy.layout.properties.Property;
import main.groovy.styledxmlparser.css.util.CssDimensionParsingUtils;
import main.groovy.styledxmlparser.css.util.CssGradientUtil;
import main.groovy.styledxmlparser.css.util.CssUtils;
import main.groovy.styledxmlparser.exceptions.StyledXMLParserException;
import main.groovy.styledxmlparser.node.IElementNode;
import main.groovy.styledxmlparser.node.IStylesContainer;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.html.TagConstants;
import html2pdf.css.page.logs.Html2PdfLogMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Utilities class to apply list styles to an element.
 */
public final class ListStyleApplierUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListStyleApplierUtil.class);

    /**
     * The Constant LIST_ITEM_MARKER_SIZE_COEFFICIENT.
     *
     * The coefficient value of 2/5 is chosen in such a way that the result
     * of the converting is as similar as possible to the browsers displaying.
     */
    private static final float LIST_ITEM_MARKER_SIZE_COEFFICIENT = 2 / 5f;

    //private static final String HTML_SYMBOL_FONT = "Sans-serif";

    /** The Constant GREEK_ALPHABET_LENGTH. */
    private static final int GREEK_ALPHABET_LENGTH = 24;

    /** The Constant GREEK_LOWERCASE. */
    private static final char[] GREEK_LOWERCASE = new char[GREEK_ALPHABET_LENGTH];

    /** The Constant DISC_SYMBOL. */
    private static final String DISC_SYMBOL = "\u2022";

    /** The Constant CIRCLE_SYMBOL. */
    private static final String CIRCLE_SYMBOL = "\u25cb";

    /** The Constant SQUARE_SYMBOL. */
    private static final String SQUARE_SYMBOL = "\u25a0";

    static {
        for (int i = 0; i < GREEK_ALPHABET_LENGTH; i++) {
            GREEK_LOWERCASE[i] = (char) (945 + i + (i > 16 ? 1 : 0));
        }
    }

    /**
     * Creates a new {@link ListStyleApplierUtil} instance.
     */
    private ListStyleApplierUtil() {
    }

    /**
     * Applies an image list style to an element.
     *
     * @param cssProps the CSS properties
     * @param context the processor context
     * @param element the element
     */
    public static void applyListStyleImageProperty(Map<String, String> cssProps, ProcessorContext context, IPropertyContainer element) {
        String listStyleImageStr = cssProps.get(CssConstants.LIST_STYLE_IMAGE);

        PdfXObject imageXObject = null;
        if (listStyleImageStr != null && !CssConstants.NONE.equals(listStyleImageStr)) {
            if (CssGradientUtil.isCssLinearGradientValue(listStyleImageStr)) {
                float em = CssDimensionParsingUtils.parseAbsoluteLength(cssProps.get(CssConstants.FONT_SIZE));
                float rem = context.getCssContext().getRootFontSize();
                try {
                    StrategyBasedLinearGradientBuilder gradientBuilder =
                            CssGradientUtil.parseCssLinearGradient(listStyleImageStr, em, rem);
                    if (gradientBuilder != null) {
                        Rectangle formBBox = new Rectangle(0, 0, em * LIST_ITEM_MARKER_SIZE_COEFFICIENT,
                                em * LIST_ITEM_MARKER_SIZE_COEFFICIENT);

                        PdfDocument pdfDocument = context.getPdfDocument();
                        Color gradientColor = gradientBuilder.buildColor(formBBox, null, pdfDocument);
                        if (gradientColor != null) {
                            imageXObject = new PdfFormXObject(formBBox);
                            new PdfCanvas((PdfFormXObject) imageXObject, context.getPdfDocument())
                                    .setColor(gradientColor, true)
                                    .rectangle(formBBox)
                                    .fill();
                        }
                    }
                } catch (StyledXMLParserException e) {
                    LOGGER.warn(MessageFormatUtil.format(
                            Html2PdfLogMessageConstant.INVALID_GRADIENT_DECLARATION, listStyleImageStr));
                }
            } else {
                imageXObject = context.getResourceResolver().retrieveImage(CssUtils.extractUrl(listStyleImageStr));
            }

            if (imageXObject != null) {
                Image image = null;
                if (imageXObject instanceof PdfImageXObject) {
                    image = new Image((PdfImageXObject) imageXObject);
                } else if (imageXObject instanceof PdfFormXObject) {
                    image = new Image((PdfFormXObject) imageXObject);
                } else {
                    throw new IllegalStateException();
                }
                element.setProperty(Property.LIST_SYMBOL, image);
                element.setProperty(Property.LIST_SYMBOL_INDENT, 5);
            }
        }
    }

    /**
     * Applies a list style to an element.
     *
     * @param stylesContainer the styles container
     * @param cssProps the CSS properties
     * @param context the processor context
     * @param element the element
     */
    public static void applyListStyleTypeProperty(IStylesContainer stylesContainer, Map<String, String> cssProps, ProcessorContext context, IPropertyContainer element) {
        float em = CssDimensionParsingUtils.parseAbsoluteLength(cssProps.get(CssConstants.FONT_SIZE));

        String style = cssProps.get(CssConstants.LIST_STYLE_TYPE);
        if (CssConstants.DISC.equals(style)) {
            setDiscStyle(element, em);
        } else if (CssConstants.CIRCLE.equals(style)) {
            setCircleStyle(element, em);
        } else if (CssConstants.SQUARE.equals(style)) {
            setSquareStyle(element, em);
        } else if (CssConstants.DECIMAL.equals(style)) {
            setListSymbol(element, ListNumberingType.DECIMAL);
        } else if (CssConstants.DECIMAL_LEADING_ZERO.equals(style)) {
            setListSymbol(element, ListNumberingType.DECIMAL_LEADING_ZERO);
        } else if (CssConstants.UPPER_ALPHA.equals(style) || CssConstants.UPPER_LATIN.equals(style)) {
            setListSymbol(element, ListNumberingType.ENGLISH_UPPER);
        } else if (CssConstants.LOWER_ALPHA.equals(style) || CssConstants.LOWER_LATIN.equals(style)) {
            setListSymbol(element, ListNumberingType.ENGLISH_LOWER);
        } else if (CssConstants.UPPER_ROMAN.equals(style)) {
            setListSymbol(element, ListNumberingType.ROMAN_UPPER);
        } else if (CssConstants.LOWER_ROMAN.equals(style)) {
            setListSymbol(element, ListNumberingType.ROMAN_LOWER);
        } else if (CssConstants.LOWER_GREEK.equals(style)) {
            element.setProperty(Property.LIST_SYMBOL, new HtmlAlphabetSymbolFactory(GREEK_LOWERCASE));
        } else if (CssConstants.NONE.equals(style)) {
            setListSymbol(element, new Text(""));
        } else {
            if (style != null) {
                Logger logger = LoggerFactory.getLogger(ListStyleApplierUtil.class);
                logger.error(MessageFormatUtil.format(Html2PdfLogMessageConstant.NOT_SUPPORTED_LIST_STYLE_TYPE, style));
            }

            // Fallback style
            if (stylesContainer instanceof IElementNode) {
                String elementName = ((IElementNode)stylesContainer).name();
                if (TagConstants.UL.equals(elementName)) {
                    setDiscStyle(element, em);
                } else if (TagConstants.OL.equals(elementName)) {
                    setListSymbol(element, ListNumberingType.DECIMAL);
                }
            }
        }
    }

    /**
     * Applies the "disc" list style to an element.
     *
     * @param element the element
     * @param em the em value
     */
    public static void setDiscStyle(IPropertyContainer element, float em) {
        Text symbol = new Text(DISC_SYMBOL);
        element.setProperty(Property.LIST_SYMBOL, symbol);
        setListSymbolIndent(element, em);
    }

    /**
     * Sets the list symbol for a {@link List} or {@link ListItem} element.
     *
     * @param container the container element ({@link List} or {@link ListItem})
     * @param text the list symbol
     */
    private static void setListSymbol(IPropertyContainer container, Text text) {
        if (container instanceof List) {
            ((List) container).setListSymbol(text);
        } else if (container instanceof ListItem) {
            ((ListItem) container).setListSymbol(text);
        }
    }

    /**
     * Sets the list symbol for a {@link List} or {@link ListItem} element.
     *
     * @param container the container element ({@link List} or {@link ListItem})
     * @param listNumberingType the list numbering type
     */
    private static void setListSymbol(IPropertyContainer container, ListNumberingType listNumberingType) {
        if (container instanceof List) {
            ((List) container).setListSymbol(listNumberingType);
        } else if (container instanceof ListItem) {
            ((ListItem) container).setListSymbol(listNumberingType);
        }
    }

    /**
     * Applies the "square" list style to an element.
     *
     * @param element the element
     * @param em the em value
     */
    private static void setSquareStyle(IPropertyContainer element, float em) {
        Text symbol = new Text(SQUARE_SYMBOL);
        symbol.setTextRise(1.5f * em / 12);
        symbol.setFontSize(4.5f * em / 12);
        element.setProperty(Property.LIST_SYMBOL, symbol);
        setListSymbolIndent(element, em);
    }

    /**
     * Applies the "circle" list style to an element.
     *
     * @param element the element
     * @param em the em value
     */
    private static void setCircleStyle(IPropertyContainer element, float em) {
        Text symbol = new Text(CIRCLE_SYMBOL);
        symbol.setTextRise(1.5f * em / 12);
        symbol.setFontSize(4.5f * em / 12);
        element.setProperty(Property.LIST_SYMBOL, symbol);
        setListSymbolIndent(element, em);
    }

    /**
     * Sets the list symbol indentation.
     *
     * @param element the element
     * @param em the em value
     */
    private static void setListSymbolIndent(IPropertyContainer element, float em) {
        if  (ListSymbolPosition.INSIDE == element.<ListSymbolPosition>getProperty(Property.LIST_SYMBOL_POSITION)) {
            element.setProperty(Property.LIST_SYMBOL_INDENT, 1.5f * em);
        } else {
            element.setProperty(Property.LIST_SYMBOL_INDENT, 7.75f);
        }
    }

    /**
     * A factory for creating {@code HtmlAlphabetSymbol} objects.
     */
    private static class HtmlAlphabetSymbolFactory implements IListSymbolFactory {

        /** The alphabet. */
        private final char[] alphabet;

        /**
         * Creates a new {@link HtmlAlphabetSymbolFactory} instance.
         *
         * @param alphabet the alphabet
         */
        public HtmlAlphabetSymbolFactory(char[] alphabet) {
            this.alphabet = alphabet;
        }

        /* (non-Javadoc)
         * @see main.groovy.layout.property.IListSymbolFactory#createSymbol(int, main.groovy.layout.IPropertyContainer, main.groovy.layout.IPropertyContainer)
         */
        @Override
        public IElement createSymbol(int index, IPropertyContainer list, IPropertyContainer listItem) {
            Object preValue = getListItemOrListProperty(listItem, list, Property.LIST_SYMBOL_PRE_TEXT);
            Object postValue = getListItemOrListProperty(listItem, list, Property.LIST_SYMBOL_POST_TEXT);
            Text result = new Text(preValue + AlphabetNumbering.toAlphabetNumber(index, alphabet) + postValue);
            return result;
        }

        /**
         * Gets the a property from a {@link ListItem}, or from the {@link List}
         * (if the property) isn't declared for the list item.
         *
         * @param listItem the list item
         * @param list the list
         * @param propertyId the property id
         * @return the property value
         */
        private static Object getListItemOrListProperty(IPropertyContainer listItem, IPropertyContainer list, int propertyId) {
            return listItem.hasProperty(propertyId) ? listItem.<Object>getProperty(propertyId) : list.<Object>getProperty(propertyId);
        }
    }
}
