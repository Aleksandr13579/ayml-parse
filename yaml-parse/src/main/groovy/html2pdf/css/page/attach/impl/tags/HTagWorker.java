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

import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.tagging.IAccessibleElement;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ProcessorContext;

public class HTagWorker extends DivTagWorker {

    private String role;

    /**
     * Creates a new {@link HTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public HTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
        this.role = element.name().toUpperCase();
    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        super.processEnd(element, context);
        IPropertyContainer elementResult = super.getElementResult();
        if (elementResult instanceof IAccessibleElement) {
            ((IAccessibleElement) elementResult).getAccessibilityProperties().setRole(role);
        }
    }
}
