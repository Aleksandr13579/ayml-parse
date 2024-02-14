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

import styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;

/**
 * TagWorker class for the {@code pre} element.
 */
public class PreTagWorker extends DivTagWorker {

    /** Keeps track to see if any content was processed. */
    private boolean anyContentProcessed = false;

    /**
     * Creates a new {@link PreTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public PreTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.impl.tags.DivTagWorker#processContent(java.lang.String, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        // It seems that browsers just skip first newline symbol, if any
        if (!anyContentProcessed) {
            if (content.startsWith("\r\n")) {
                content = content.substring(2);
            }
        }
        anyContentProcessed = true;
        return super.processContent(content, context);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.impl.tags.DivTagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        anyContentProcessed = true;
        return super.processTagChild(childTagWorker, context);
    }
}
