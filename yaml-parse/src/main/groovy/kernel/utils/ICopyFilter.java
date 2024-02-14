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
package kernel.utils;

import kernel.pdf.PdfName;
import kernel.pdf.PdfObject;

/**
 * A filter class to be used while copying pdf components.
 */
public interface ICopyFilter {

    /**
     * Verifies whether a PdfObject should be copied in the copying flow.
     * The filter class has to take care of alternative ways to process the PdfObject if needed.
     * When more than one filter should be used, it is upon the user to chain them together.
     *
     * @param newParent the parent in the target of the PdfObject to be checked
     * @param name      the name of the PdfObject if the parent is a PdfDictionary
     * @param value     the PdfObject toi be checked
     *
     * @return true, the PdfObject will be copied, false it will not be copied
     */
    boolean shouldProcess(PdfObject newParent, PdfName name, PdfObject value);
}
