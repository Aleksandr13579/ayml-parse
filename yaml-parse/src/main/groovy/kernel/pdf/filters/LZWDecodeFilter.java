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
package kernel.pdf.filters;

import kernel.pdf.MemoryLimitsAwareFilter;
import kernel.pdf.PdfDictionary;
import kernel.pdf.PdfName;
import kernel.pdf.PdfObject;

import java.io.ByteArrayOutputStream;

/**
 * Handles LZWDECODE filter
 */
public class LZWDecodeFilter extends MemoryLimitsAwareFilter {

    /**
     * Decodes a byte[] according to the LZW encoding.
     *
     * @param in byte[] to be decoded
     * @return decoded byte[]
     */
    public static byte[] LZWDecode(byte[] in) {
        return LZWDecodeInternal(in, new ByteArrayOutputStream());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) {
        ByteArrayOutputStream outputStream = enableMemoryLimitsAwareHandler(streamDictionary);
        b = LZWDecodeInternal(b, outputStream);
        b = FlateDecodeFilter.decodePredictor(b, decodeParams);
        return b;
    }

    /**
     * Decodes a byte[] according to the LZW encoding.
     *
     * @param in  byte[] to be decoded
     * @param out the out stream which will be used to write the bytes.
     * @return decoded byte[]
     */
    private static byte[] LZWDecodeInternal(byte[] in, ByteArrayOutputStream out) {
        LZWDecoder lzw = new LZWDecoder();
        lzw.decode(in, out);
        return out.toByteArray();
    }
}
