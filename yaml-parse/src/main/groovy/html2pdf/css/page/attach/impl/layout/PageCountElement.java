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
package html2pdf.css.page.attach.impl.layout;

import layout.element.Text;
import layout.renderer.IRenderer;
import html2pdf.css.page.css.resolve.func.counter.CounterDigitsGlyphStyle;
import html2pdf.css.page.html.HtmlUtils;

/**
 * {@link Text} implementation to be used for the page count.
 */
public class PageCountElement extends Text {

    private final CounterDigitsGlyphStyle digitsGlyphStyle;

    /**
     * Instantiates a new {@link PageCountElement}.
     */
    public PageCountElement() {
        // Workaround to match correct font containing number glyphs
        super("1234567890");
        digitsGlyphStyle = CounterDigitsGlyphStyle.DEFAULT;
    }

    /**
     * Instantiates a new {@link PageCountElement}.
     *
     * @param digitsGlyphStyle digits glyph style
     */
    public PageCountElement(CounterDigitsGlyphStyle digitsGlyphStyle) {
        // Workaround to match correct font containing number glyphs
        super(HtmlUtils.getAllNumberGlyphsForStyle(digitsGlyphStyle));
        this.digitsGlyphStyle = digitsGlyphStyle;
    }

    /**
     * Gets glyph style for digits.
     *
     * @return name of the glyph style
     */
    public CounterDigitsGlyphStyle getDigitsGlyphStyle() {
        return digitsGlyphStyle;
    }

    /* (non-Javadoc)
     * @see layout.element.Text#makeNewRenderer()
     */
    @Override
    public IRenderer makeNewRenderer() {
        return new PageCountRenderer(this);
    }

}
