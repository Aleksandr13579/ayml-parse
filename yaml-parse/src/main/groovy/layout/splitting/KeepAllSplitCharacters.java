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
package layout.splitting;

import com.itextpdf.io.font.otf.GlyphLine;

/**
 * The implementation of {@link ISplitCharacters} that prevents breaking within words.
 */
public class KeepAllSplitCharacters implements ISplitCharacters {

    @Override
    public boolean isSplitCharacter(GlyphLine text, int glyphPos) {
        if (!text.get(glyphPos).hasValidUnicode()) {
            return false;
        }
        int charCode = text.get(glyphPos).getUnicode();
        //Check if a hyphen proceeds a digit to denote negative value
        // TODO: DEVSIX-4863 why is glyphPos == 0? negative value could be preceded by a whitespace!
        if ((glyphPos == 0) && (charCode == '-') && (text.size() - 1 > glyphPos) && (isADigitChar(text, glyphPos + 1))) {
            return false;
        }

        return charCode <= ' ' || charCode == '-' || charCode == '\u2010'
                // block of whitespaces
                || (charCode >= 0x2002 && charCode <= 0x200b);
    }

    private static boolean isADigitChar(GlyphLine text, int glyphPos) {
        return Character.isDigit(text.get(glyphPos).getChars()[0]);
    }
}
