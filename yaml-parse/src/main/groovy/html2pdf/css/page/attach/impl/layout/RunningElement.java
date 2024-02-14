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

import kernel.pdf.tagging.StandardRoles;
import layout.element.Div;
import layout.layout.LayoutContext;
import layout.layout.LayoutResult;
import layout.properties.Property;
import layout.properties.RenderingMode;
import layout.renderer.DivRenderer;
import layout.renderer.DrawContext;
import layout.renderer.IRenderer;

/**
 * An {@link layout.element.IElement} that serves as a placeholder for removed running element
 * from the normal flow. This element is designed to register where particular running element would have been placed.
 */
public class RunningElement extends Div {
    private RunningElementContainer runningElementContainer;

    /**
     * Creates a new instance of {@link RunningElement}.
     * @param runningElementContainer a container for the actual running element removed from the normal flow.
     */
    public RunningElement(RunningElementContainer runningElementContainer) {
        this.runningElementContainer = runningElementContainer;
        getAccessibilityProperties().setRole(StandardRoles.ARTIFACT);
    }

    @Override
    protected IRenderer makeNewRenderer() {
        return new RunningElementRenderer(this, runningElementContainer);
    }

    /**
     * It's an empty div so it's not expected to be ever split between areas.
     */
    static class RunningElementRenderer extends DivRenderer {
        private RunningElementContainer runningElementContainer;
        private boolean isFirstOnRootArea;

        public RunningElementRenderer(Div modelElement, RunningElementContainer runningElementContainer) {
            super(modelElement);
            this.runningElementContainer = runningElementContainer;
            // LineRenderer uses html logic only if there is at least one child renderer in html
            // mode. So the case when the line contains only running elements should be
            // processed in the default mode, since for this line the line-height should not be calculated.
            setProperty(Property.RENDERING_MODE, RenderingMode.DEFAULT_LAYOUT_MODE);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            this.isFirstOnRootArea = isFirstOnRootArea();
            return super.layout(layoutContext);
        }

        @Override
        public void draw(DrawContext drawContext) {
            runningElementContainer.setOccurrencePage(getOccupiedArea().getPageNumber(), isFirstOnRootArea);
            super.draw(drawContext);
        }
    }
}
