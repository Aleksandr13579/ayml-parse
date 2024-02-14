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

import main.groovy.forms.form.FormProperty;
import main.groovy.forms.form.element.Button;
import main.groovy.forms.form.element.IFormField;
import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.element.Div;
import main.groovy.layout.element.IBlockElement;
import main.groovy.layout.element.IElement;
import main.groovy.layout.element.Image;
import main.groovy.layout.tagging.IAccessibleElement;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.util.AccessiblePropHelper;
import html2pdf.css.page.html.AttributeConstants;

/**
 * TagWorker class for a button element.
 */
public class ButtonTagWorker extends DivTagWorker {

    /** The Constant DEFAULT_BUTTON_NAME. */
    private static final String DEFAULT_BUTTON_NAME = "Button";

    /** The button. */
    private IFormField formField;

    /** The lang attribute value. */
    private String lang;

    private StringBuilder fallbackContent = new StringBuilder();

    private String name;

    private boolean flatten;

    private boolean hasChildren = false;

    /**
     * Creates a new {@link ButtonTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public ButtonTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
        String name = element.getAttribute(AttributeConstants.ID);
        if (name == null) {
            name = DEFAULT_BUTTON_NAME;
        }
        this.name = context.getFormFieldNameResolver().resolveFormName(name);
        flatten = !context.isCreateAcroForm();

        lang = element.getAttribute(AttributeConstants.LANG);
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processContent(java.lang.String, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        fallbackContent.append(content);
        return super.processContent(content, context);
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#processTagChild(main.groovy.html2pdf.attach.ITagWorker, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        hasChildren = true;
        return super.processTagChild(childTagWorker, context);
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        if (formField == null) {
            if (hasChildren) {
                Button button = new Button(name);
                button.setProperty(FormProperty.FORM_ACCESSIBILITY_LANGUAGE, lang);
                Div div = (Div) super.getElementResult();
                for (IElement element : div.getChildren()) {
                    if (element instanceof IAccessibleElement) {
                        AccessiblePropHelper.trySetLangAttribute((IAccessibleElement) element, lang);
                    }
                    if (element instanceof IBlockElement) {
                        button.add((IBlockElement) element);
                    } else if (element instanceof Image) {
                        button.add((Image) element);
                    }
                }
                div.getChildren().clear();
                formField = button;
            } else {
                Button inputButton = new Button(name);
                inputButton.setProperty(FormProperty.FORM_ACCESSIBILITY_LANGUAGE, lang);
                inputButton.setValue(fallbackContent.toString().trim());
                formField = inputButton;
            }
        }
        formField.setProperty(FormProperty.FORM_FIELD_FLATTEN, flatten);
        return formField;
    }
}
