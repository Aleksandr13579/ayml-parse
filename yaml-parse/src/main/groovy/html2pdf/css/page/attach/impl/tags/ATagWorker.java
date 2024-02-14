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

import main.groovy.kernel.pdf.tagging.StandardRoles;
import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.element.Div;
import main.groovy.layout.element.IBlockElement;
import main.groovy.layout.properties.FloatPropertyValue;
import main.groovy.layout.properties.Property;
import main.groovy.layout.properties.Transform;
import main.groovy.styledxmlparser.node.IElementNode;
import main.groovy.styledxmlparser.resolver.resource.UriResolver;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.impl.layout.RunningElement;
import html2pdf.css.page.attach.util.LinkHelper;
import html2pdf.css.page.html.AttributeConstants;

import java.net.MalformedURLException;


/**
 * TagWorker class for the {@code a} element.
 */
public class ATagWorker extends SpanTagWorker {

    /**
     * Creates a new {@link ATagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public ATagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
    }

    /* (non-Javadoc)
     * @see main.groovy.html2pdf.attach.impl.tags.SpanTagWorker#processEnd(main.groovy.html2pdf.html.node.IElementNode, main.groovy.html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        super.processEnd(element, context);

        String url = element.getAttribute(AttributeConstants.HREF);
        if (url != null) {
            String base = context.getBaseUri();
            if (base != null) {
                UriResolver uriResolver = new UriResolver(base);
                if (!(url.startsWith("#") && uriResolver.isLocalBaseUri()))
                    try {
                        String resolvedUri = uriResolver.resolveAgainstBaseUri(url).toExternalForm();
                        if (!url.endsWith("/") && resolvedUri.endsWith("/"))
                            resolvedUri = resolvedUri.substring(0, resolvedUri.length() - 1);
                        if (!resolvedUri.startsWith("file:"))
                            url = resolvedUri;
                    } catch (MalformedURLException exception) {
                    }
            }
            for (int i = 0; i < getAllElements().size(); i++) {
                if (getAllElements().get(i) instanceof RunningElement) {
                    continue;
                }
                if (getAllElements().get(i) instanceof IBlockElement) {
                    Div simulatedDiv = new Div();
                    simulatedDiv.getAccessibilityProperties().setRole(StandardRoles.LINK);
                    Transform cssTransform = getAllElements().get(i).<Transform>getProperty(Property.TRANSFORM);
                    if (cssTransform != null) {
                        getAllElements().get(i).deleteOwnProperty(Property.TRANSFORM);
                        simulatedDiv.setProperty(Property.TRANSFORM, cssTransform);
                    }
                    FloatPropertyValue floatPropVal = getAllElements().get(i).<FloatPropertyValue>getProperty(Property.FLOAT);
                    if (floatPropVal != null) {
                        getAllElements().get(i).deleteOwnProperty(Property.FLOAT);
                        simulatedDiv.setProperty(Property.FLOAT, floatPropVal);
                    }
                    simulatedDiv.add((IBlockElement) getAllElements().get(i));
                    String display = childrenDisplayMap.remove(getAllElements().get(i));
                    if (display != null) {
                        childrenDisplayMap.put(simulatedDiv, display);
                    }
                    getAllElements().set(i, simulatedDiv);
                }
                LinkHelper.applyLinkAnnotation(getAllElements().get(i), url);
            }
        }

        if (!getAllElements().isEmpty()) {
            String name = element.getAttribute(AttributeConstants.NAME);
            IPropertyContainer firstElement = getAllElements().get(0);
            firstElement.setProperty(Property.DESTINATION, name);
            firstElement.setProperty(Property.ID, name);
        }
    }
}
