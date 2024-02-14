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
package html2pdf.attach.impl.tags;

import forms.form.FormProperty;
import html2pdf.attach.ITagWorker;
import layout.IPropertyContainer;
import layout.element.Div;
import layout.element.Paragraph;
import layout.properties.OverflowPropertyValue;
import layout.properties.Property;
import layout.tagging.IAccessibleElement;
import styledxmlparser.node.IElementNode;
import html2pdf.attach.ProcessorContext;
import html2pdf.attach.util.AccessiblePropHelper;
import html2pdf.html.AttributeConstants;

/**
 * TagWorker class for the {@code optgroup} element.
 */
public class OptGroupTagWorker extends DivTagWorker {

    /**
     * Creates a new {@link OptGroupTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public OptGroupTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
        String label = element.getAttribute(AttributeConstants.LABEL);
        if (label == null || label.isEmpty()) {
            label = "\u00A0";
        }
        getElementResult().setProperty(FormProperty.FORM_FIELD_LABEL, label);
        Paragraph p = new Paragraph(label).setMargin(0);
        p.setProperty(Property.OVERFLOW_X, OverflowPropertyValue.VISIBLE);
        p.setProperty(Property.OVERFLOW_Y, OverflowPropertyValue.VISIBLE);
        ((Div) getElementResult()).add(p);
    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return content == null || content.trim().isEmpty();
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        if (childTagWorker instanceof OptionTagWorker) {
            IPropertyContainer element = childTagWorker.getElementResult();
            IPropertyContainer propertyContainer = getElementResult();
            if (propertyContainer instanceof IAccessibleElement) {
                String lang = ((IAccessibleElement) propertyContainer).getAccessibilityProperties().getLanguage();
                AccessiblePropHelper.trySetLangAttribute((Div) childTagWorker.getElementResult(), lang);
            }
            return addBlockChild((layout.element.IElement) element);
        } else {
            return super.processTagChild(childTagWorker, context);
        }
    }
}
