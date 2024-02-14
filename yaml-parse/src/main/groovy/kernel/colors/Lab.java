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
package kernel.colors;

import kernel.pdf.colorspace.PdfCieBasedCs;

public class Lab extends Color {


    public Lab(PdfCieBasedCs.Lab cs) {
        this(cs, new float[cs.getNumberOfComponents()]);
    }

    public Lab(PdfCieBasedCs.Lab cs, float[] value) {
        super(cs, value);
    }

    public Lab(float[] whitePoint, float[] value) {
        super(new PdfCieBasedCs.Lab(whitePoint), value);
    }

    public Lab(float[] whitePoint, float[] blackPoint, float[] range, float[] value) {
        this(new PdfCieBasedCs.Lab(whitePoint, blackPoint, range), value);
    }

}
