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

import commons.utils.MessageFormatUtil;
import io.font.otf.GlyphLine;
import io.logs.IoLogMessageConstant;
import kernel.font.PdfFont;
import layout.layout.LayoutContext;
import layout.layout.LayoutResult;
import layout.properties.Property;
import layout.renderer.DrawContext;
import layout.renderer.IRenderer;
import layout.renderer.TargetCounterHandler;
import layout.renderer.TextRenderer;
import html2pdf.css.page.css.resolve.func.counter.CounterDigitsGlyphStyle;
import html2pdf.css.page.html.HtmlUtils;
import html2pdf.css.page.logs.Html2PdfLogMessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link TextRenderer} implementation for the page target-counter.
 */
class PageTargetCountRenderer extends TextRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageTargetCountRenderer.class);

    private static final String UNDEFINED_VALUE = "0";

    private final String target;
    private final CounterDigitsGlyphStyle digitsGlyphStyle;

    /**
     * Instantiates a new {@link PageTargetCountRenderer}.
     *
     * @param textElement the text element
     */
    PageTargetCountRenderer(PageTargetCountElement textElement) {
        super(textElement);
        digitsGlyphStyle = textElement.getDigitsGlyphStyle();
        target = textElement.getTarget();
    }

    protected PageTargetCountRenderer(TextRenderer other) {
        super(other);
        this.digitsGlyphStyle = ((PageTargetCountRenderer)other).digitsGlyphStyle;
        this.target = ((PageTargetCountRenderer)other).target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutResult layout(LayoutContext layoutContext) {
        final String previousText = getText().toString();
        final Integer page = TargetCounterHandler.getPageByID(this, target);
        if (page == null) {
            setText(UNDEFINED_VALUE);
        } else {
            setText(HtmlUtils.convertNumberAccordingToGlyphStyle(digitsGlyphStyle, (int) page));
        }
        final LayoutResult result = super.layout(layoutContext);
        setText(previousText);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(DrawContext drawContext) {
        if (!TargetCounterHandler.isValueDefinedForThisId(this, target)) {
            LOGGER.warn(MessageFormatUtil.format(
                    Html2PdfLogMessageConstant.CANNOT_RESOLVE_TARGET_COUNTER_VALUE, target));
        }
        super.draw(drawContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRenderer getNextRenderer() {
        if (PageTargetCountRenderer.class != this.getClass()) {
            Logger logger = LoggerFactory.getLogger(PageTargetCountRenderer.class);
            logger.error(MessageFormatUtil.format(
                    IoLogMessageConstant.GET_NEXT_RENDERER_SHOULD_BE_OVERRIDDEN));
        }
        return new PageTargetCountRenderer((PageTargetCountElement) modelElement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TextRenderer createCopy(GlyphLine gl, PdfFont font) {
        if (PageTargetCountRenderer.class != this.getClass()) {
            Logger logger = LoggerFactory.getLogger(PageTargetCountRenderer.class);
            logger.error(MessageFormatUtil.format(
                    IoLogMessageConstant.CREATE_COPY_SHOULD_BE_OVERRIDDEN));
        }
        PageTargetCountRenderer copy = new PageTargetCountRenderer(this);
        copy.setProcessedGlyphLineAndFont(gl, font);
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean resolveFonts(List<IRenderer> addTo) {
        final List<IRenderer> dummyList = new ArrayList<>();
        super.resolveFonts(dummyList);
        setProperty(Property.FONT, dummyList.get(0).<Object>getProperty(Property.FONT));
        addTo.add(this);
        return true;
    }
}
