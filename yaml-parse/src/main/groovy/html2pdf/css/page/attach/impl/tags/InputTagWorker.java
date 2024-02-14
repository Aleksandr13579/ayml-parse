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
package html2pdf.css.page.attach.impl.tags;

import commons.utils.MessageFormatUtil;
import forms.form.FormProperty;
import forms.form.element.Button;
import forms.form.element.CheckBox;
import forms.form.element.InputField;
import forms.form.element.Radio;
import kernel.colors.ColorConstants;
import layout.IPropertyContainer;
import layout.borders.Border;
import layout.borders.SolidBorder;
import layout.element.IElement;
import layout.element.Paragraph;
import layout.properties.Property;
import styledxmlparser.css.util.CssDimensionParsingUtils;
import styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.html.AttributeConstants;
import html2pdf.css.page.logs.Html2PdfLogMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * TagWorker class for the {@code input} element.
 */
public class InputTagWorker implements ITagWorker, IDisplayAware {

    private static final Pattern NUMBER_INPUT_ALLOWED_VALUES =
            Pattern.compile("^(((-?[0-9]+)(\\.[0-9]+)?)|(-?\\.[0-9]+))$");

    private static int radioNameIdx = 0;

    /**
     * The form element.
     */
    private IElement formElement;

    /**
     * The display.
     */
    private String display;

    /**
     * Creates a new {@link InputTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public InputTagWorker(IElementNode element, ProcessorContext context) {
        String lang = element.getAttribute(AttributeConstants.LANG);
        String inputType = element.getAttribute(AttributeConstants.TYPE);
        if (!AttributeConstants.INPUT_TYPE_VALUES.contains(inputType)) {
            if (null != inputType && 0 != inputType.length()) {
                Logger logger = LoggerFactory.getLogger(InputTagWorker.class);
                logger.warn(MessageFormatUtil.format(Html2PdfLogMessageConstant.INPUT_TYPE_IS_INVALID, inputType));
            }
            inputType = AttributeConstants.TEXT;
        }
        String value = element.getAttribute(AttributeConstants.VALUE);
        String name = context.getFormFieldNameResolver().resolveFormName(element.getAttribute(AttributeConstants.NAME));
        // Default input type is text
        if (inputType == null || AttributeConstants.TEXT.equals(inputType) || AttributeConstants.EMAIL.equals(inputType)
                || AttributeConstants.PASSWORD.equals(inputType) || AttributeConstants.NUMBER.equals(inputType)) {
            Integer size = CssDimensionParsingUtils.parseInteger(element.getAttribute(AttributeConstants.SIZE));
            formElement = new InputField(name);

            // Default html2pdf input field appearance differs from the default one for form fields.
            // That's why we need to get rid of several properties we set by default during InputField instance creation.
            formElement.deleteOwnProperty(Property.BOX_SIZING);

            value = preprocessInputValue(value, inputType);
            // process placeholder instead
            String placeholder = element.getAttribute(AttributeConstants.PLACEHOLDER);
            if (null != placeholder) {
                Paragraph paragraph;
                if (placeholder.isEmpty()) {
                    paragraph = new Paragraph();
                } else {
                    if (placeholder.trim().isEmpty()) {
                        paragraph = new Paragraph("\u00A0");
                    } else {
                        paragraph = new Paragraph(placeholder);
                    }
                }
                ((InputField) formElement).setPlaceholder(paragraph.setMargin(0));
            }
            formElement.setProperty(FormProperty.FORM_FIELD_VALUE, value);
            formElement.setProperty(FormProperty.FORM_FIELD_SIZE, size);
            if (AttributeConstants.PASSWORD.equals(inputType)) {
                formElement.setProperty(FormProperty.FORM_FIELD_PASSWORD_FLAG, true);
            }
        } else if (AttributeConstants.SUBMIT.equals(inputType) || AttributeConstants.BUTTON.equals(inputType)) {
            formElement = new Button(name).setSingleLineValue(value);
        } else if (AttributeConstants.CHECKBOX.equals(inputType)) {
            CheckBox cb = new CheckBox(name);
            String checked = element.getAttribute(AttributeConstants.CHECKED);
            // so in the previous implementation the width was 8.25 and the borders .75,
            // but the borders got drawn on the outside of the box, so the actual size was 9.75
            // because 8.25 + 2 * .75 = 9.75
            final float widthWithBordersOnTheInside = 9.75f;
            final float defaultBorderWith = .75f;
            cb.setSize(widthWithBordersOnTheInside);
            cb.setBorder(new SolidBorder(ColorConstants.DARK_GRAY, defaultBorderWith));
            cb.setBackgroundColor(ColorConstants.WHITE);
            // has attribute == is checked
            cb.setChecked(checked != null);
            formElement = cb;
        } else if (AttributeConstants.RADIO.equals(inputType)) {
            String radioGroupName = element.getAttribute(AttributeConstants.NAME);
            if (radioGroupName == null || radioGroupName.isEmpty()) {
                ++radioNameIdx;
                radioGroupName = "radio" + radioNameIdx;
            }
            Radio radio = new Radio(name, radioGroupName);

            // Gray circle border
            Border border = new SolidBorder(1);
            border.setColor(ColorConstants.LIGHT_GRAY);
            radio.setBorder(border);

            String checked = element.getAttribute(AttributeConstants.CHECKED);
            if (null != checked) {
                context.getRadioCheckResolver().checkField(radioGroupName, radio);
                radio.setChecked(true);
            }

            formElement = radio;
        } else {
            Logger logger = LoggerFactory.getLogger(InputTagWorker.class);
            logger.error(MessageFormatUtil.format(Html2PdfLogMessageConstant.INPUT_TYPE_IS_NOT_SUPPORTED, inputType));
        }
        if (formElement != null) {
            formElement.setProperty(FormProperty.FORM_FIELD_FLATTEN, !context.isCreateAcroForm());
            formElement.setProperty(FormProperty.FORM_ACCESSIBILITY_LANGUAGE, lang);
        }
        display = element.getStyles() != null ? element.getStyles().get(CssConstants.DISPLAY) : null;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processEnd(html2pdf.html.node.IElementNode, html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {

    }

    /* (non-Javadoc)
     * @see html2pdf.attach.impl.tags.IDisplayAware#getDisplay()
     */
    @Override
    public String getDisplay() {
        return display;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processContent(java.lang.String, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return false;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return childTagWorker instanceof PlaceholderTagWorker && null != ((InputField) formElement).getPlaceholder();
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return formElement;
    }

    static String preprocessInputValue(String value, String inputType) {
        if (AttributeConstants.NUMBER.equals(inputType) && value != null &&
                !NUMBER_INPUT_ALLOWED_VALUES.matcher(value).matches()) {
            value = "";
        }
        return value;
    }

}
