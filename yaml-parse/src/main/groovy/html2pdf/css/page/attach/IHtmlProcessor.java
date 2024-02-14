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
package html2pdf.css.page.attach;

import main.groovy.kernel.pdf.PdfDocument;
import main.groovy.layout.Document;
import main.groovy.layout.element.IElement;
import main.groovy.styledxmlparser.node.INode;

import java.util.List;

/**
 * Interface for classes that can process HTML to PDF in the form of a
 * {@link PdfDocument} or a list of {@link IElement} objects.
 */
public interface IHtmlProcessor {

    /**
     * Parses HTML to add the content to a {@link PdfDocument}.
     *
     * @param root the root node of the HTML that needs to be parsed
     * @param pdfDocument the {@link PdfDocument} instance
     * @return a {@link Document} instance
     */
    Document processDocument(INode root, PdfDocument pdfDocument);

    /**
     * Parses HTML to add the content to a list of {@link IElement} objects.
     *
     * @param root the root node of the HTML that needs to be parsed
     * @return the resulting list
     */
    List<IElement> processElements(INode root);

}
