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

import main.groovy.commons.utils.MessageFormatUtil;
import main.groovy.kernel.pdf.PdfDictionary;
import main.groovy.kernel.pdf.PdfName;
import main.groovy.kernel.pdf.tagging.PdfStructureAttributes;
import main.groovy.kernel.pdf.tagging.StandardRoles;
import main.groovy.kernel.pdf.tagutils.AccessibilityProperties;
import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.tagging.IAccessibleElement;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.html.AttributeConstants;
import html2pdf.css.page.logs.Html2PdfLogMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThTagWorker extends TdTagWorker {
    /**
     * Creates a new {@link ThTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public ThTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        super.processEnd(element, context);
        IPropertyContainer elementResult = super.getElementResult();
        if (elementResult instanceof IAccessibleElement) {
            ((IAccessibleElement) elementResult).getAccessibilityProperties().setRole(StandardRoles.TH);
            if (context.getPdfDocument() == null || context.getPdfDocument().isTagged()) {
                String scope = element.getAttribute(AttributeConstants.SCOPE);
                AccessibilityProperties properties = ((IAccessibleElement) elementResult).getAccessibilityProperties();
                PdfDictionary attributes = new PdfDictionary();
                attributes.put(PdfName.O, PdfName.Table);
                if (scope != null && (AttributeConstants.ROW.equalsIgnoreCase(scope) || AttributeConstants.ROWGROUP.equalsIgnoreCase(scope))) {
                    attributes.put(PdfName.Scope, PdfName.Row);
                    properties.addAttributes(new PdfStructureAttributes(attributes));
                } else if (scope != null && (AttributeConstants.COL.equalsIgnoreCase(scope) || AttributeConstants.COLGROUP.equalsIgnoreCase(scope))) {
                    attributes.put(PdfName.Scope, PdfName.Column);
                    properties.addAttributes(new PdfStructureAttributes(attributes));
                } else {
                    Logger logger = LoggerFactory.getLogger(ThTagWorker.class);
                    logger.warn(MessageFormatUtil.format(
                            Html2PdfLogMessageConstant.NOT_SUPPORTED_TH_SCOPE_TYPE, scope));
                }

            }

        }
    }

}
