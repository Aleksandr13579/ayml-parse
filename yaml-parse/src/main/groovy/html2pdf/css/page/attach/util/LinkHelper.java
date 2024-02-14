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
package html2pdf.css.page.attach.util;

import main.groovy.commons.utils.MessageFormatUtil;
import main.groovy.kernel.geom.Rectangle;
import main.groovy.kernel.pdf.PdfArray;
import main.groovy.kernel.pdf.action.PdfAction;
import main.groovy.kernel.pdf.annot.PdfAnnotation;
import main.groovy.kernel.pdf.annot.PdfLinkAnnotation;
import main.groovy.kernel.pdf.tagging.StandardRoles;
import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.element.ILeafElement;
import main.groovy.layout.properties.Property;
import main.groovy.layout.tagging.IAccessibleElement;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.impl.tags.SpanTagWorker;
import html2pdf.css.page.html.AttributeConstants;
import html2pdf.css.page.logs.Html2PdfLogMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Helper class for links.
 */
public class LinkHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkHelper.class);

    /**
     * Creates a new {@link LinkHelper} class.
     */
    private LinkHelper() {
    }

    /**
     * Applies a link annotation.
     *
     * @param container the containing object
     * @param url       the destination
     */
    public static void applyLinkAnnotation(IPropertyContainer container, String url) {
        if (container != null) {
            PdfLinkAnnotation linkAnnotation;
            if (url.startsWith("#")) {
                String name = url.substring(1);
                linkAnnotation = (PdfLinkAnnotation) new PdfLinkAnnotation(new Rectangle(0, 0, 0, 0)).setAction(PdfAction.createGoTo(name)).setFlags(PdfAnnotation.PRINT);
            } else {
                linkAnnotation = (PdfLinkAnnotation) new PdfLinkAnnotation(new Rectangle(0, 0, 0, 0)).setAction(PdfAction.createURI(url)).setFlags(PdfAnnotation.PRINT);
            }
            linkAnnotation.setBorder(new PdfArray(new float[]{0, 0, 0}));
            container.setProperty(Property.LINK_ANNOTATION, linkAnnotation);
            if (container instanceof ILeafElement && container instanceof IAccessibleElement) {
                ((IAccessibleElement) container).getAccessibilityProperties().setRole(StandardRoles.LINK);
            }
        }
    }

    /**
     * Creates a destination
     *
     * @param tagWorker the tagworker that is building the (iText) element
     * @param element   the (HTML) element being converted
     * @param context   the Processor context
     */
    public static void createDestination(ITagWorker tagWorker, IElementNode element, ProcessorContext context) {
        String id = element.getAttribute(AttributeConstants.ID);
        if (id == null)
            return;

        final IPropertyContainer propertyContainer = getPropertyContainer(tagWorker);

        if (context.getLinkContext().isUsedLinkDestination(id)) {
            if (propertyContainer == null) {
                String tagWorkerClassName = tagWorker != null ? tagWorker.getClass().getName() : "null";
                LOGGER.warn(MessageFormatUtil.format(
                        Html2PdfLogMessageConstant.ANCHOR_LINK_NOT_HANDLED, element.name(), id, tagWorkerClassName));
                return;
            }
            propertyContainer.setProperty(Property.DESTINATION, id);
        }
        if (propertyContainer != null) {
            propertyContainer.setProperty(Property.ID, id);
        }
    }

    private static IPropertyContainer getPropertyContainer(ITagWorker tagWorker) {
        if (tagWorker != null) {
            if (tagWorker instanceof SpanTagWorker) {
                List<IPropertyContainer> spanElements = ((SpanTagWorker) tagWorker).getAllElements();
                if (!spanElements.isEmpty()) {
                    return spanElements.get(0);
                }
            } else {
                return tagWorker.getElementResult();
            }
        }
        return null;
    }
}
