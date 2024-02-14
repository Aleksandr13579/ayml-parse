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

import forms.form.FormProperty;
import forms.form.element.AbstractSelectField;
import forms.form.element.ComboBoxField;
import forms.form.element.ListBoxField;
import layout.IPropertyContainer;
import layout.element.IBlockElement;
import styledxmlparser.css.util.CssDimensionParsingUtils;
import styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.css.CssConstants;
import html2pdf.css.page.html.AttributeConstants;

/**
 * TagWorker class for the {@code select} element.
 */
public class SelectTagWorker implements ITagWorker, IDisplayAware {

    /** The form element. */
    private AbstractSelectField selectElement;

    /** The display. */
    private String display;

    /**
     * Creates a new {@link SelectTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public SelectTagWorker(IElementNode element, ProcessorContext context) {
        String name = context.getFormFieldNameResolver().resolveFormName(element.getAttribute(AttributeConstants.NAME));

        boolean multipleAttr = element.getAttribute(AttributeConstants.MULTIPLE) != null;
        Integer sizeAttr = CssDimensionParsingUtils.parseInteger(element.getAttribute(AttributeConstants.SIZE));
        int size = getSelectSize(sizeAttr, multipleAttr);

        if (size > 1 || multipleAttr) {
            selectElement = new ListBoxField(name, size, multipleAttr);
        } else {
            selectElement = new ComboBoxField(name);
        }
        String lang = element.getAttribute(AttributeConstants.LANG);
        selectElement.setProperty(FormProperty.FORM_ACCESSIBILITY_LANGUAGE, lang);
        selectElement.setProperty(FormProperty.FORM_FIELD_FLATTEN, !context.isCreateAcroForm());
        display = element.getStyles() != null ? element.getStyles().get(CssConstants.DISPLAY) : null;
    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {

    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return content == null || content.trim().isEmpty();
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        if (childTagWorker instanceof OptionTagWorker || childTagWorker instanceof OptGroupTagWorker) {
            if (childTagWorker.getElementResult() instanceof IBlockElement) {
                selectElement.addOption((IBlockElement) childTagWorker.getElementResult());
                return true;
            }
        }
        return false;
    }

    @Override
    public IPropertyContainer getElementResult() {
        return selectElement;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    private int getSelectSize(Integer size, boolean multiple) {
        if (size != null && size > 0) {
            return (int) size;
        }

        return multiple ? 4 : 1;
    }
}
