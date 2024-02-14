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

import main.groovy.layout.IPropertyContainer;
import main.groovy.layout.element.*;
import main.groovy.layout.renderer.FlexContainerRenderer;
import main.groovy.styledxmlparser.node.IElementNode;
import html2pdf.css.page.attach.ITagWorker;
import html2pdf.css.page.attach.ProcessorContext;
import html2pdf.css.page.attach.util.AccessiblePropHelper;
import html2pdf.css.page.attach.util.WaitingInlineElementsHelper;
import html2pdf.css.page.css.CssConstants;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * {@link ITagWorker} implementation for elements with {@code display: flex}.
 */
public class DisplayFlexTagWorker implements ITagWorker, IDisplayAware {

    private static final Pattern ANY_SYMBOL_PATTERN;

    private final Div flexContainer;

    private final WaitingInlineElementsHelper inlineHelper;

    static {
        ANY_SYMBOL_PATTERN = Pattern.compile("\\S+");
    }

    /**
     * Creates instance of {@link DisplayFlexTagWorker}.
     *
     * @param element the element with defined styles
     * @param context the context of the converter processor
     */
    public DisplayFlexTagWorker(IElementNode element, ProcessorContext context) {
        flexContainer = new Div();
        flexContainer.setNextRenderer(new FlexContainerRenderer(flexContainer));
        final Map<String, String> styles = element.getStyles();
        inlineHelper = new WaitingInlineElementsHelper(styles == null ? null : styles.get(CssConstants.WHITE_SPACE),
                styles == null ? null : styles.get(CssConstants.TEXT_TRANSFORM));
        AccessiblePropHelper.trySetLangAttribute(flexContainer, element);
    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        if (inlineHelperContainsText()) {
            addInlineWaitingLeavesToFlexContainer();
        }
    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        if (ANY_SYMBOL_PATTERN.matcher(content).find()) {
            inlineHelper.add(content);
        }
        return true;
    }

    @Override
    public IPropertyContainer getElementResult() {
        return flexContainer;
    }

    @Override
    public String getDisplay() {
        return CssConstants.FLEX;
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        final IPropertyContainer element = childTagWorker.getElementResult();
        if (childTagWorker instanceof BrTagWorker) {
            inlineHelper.add((ILeafElement) element);
        } else {
            if (inlineHelperContainsText()) {
                addInlineWaitingLeavesToFlexContainer();
            }
            if (element instanceof IBlockElement) {
                flexContainer.add((IBlockElement) element);
            } else if (element instanceof Image) {
                flexContainer.add((Image) element);
            } else if (element instanceof AreaBreak) {
                flexContainer.add((AreaBreak) element);
            } else {
                return false;
            }
        }
        return true;
    }

    private void addInlineWaitingLeavesToFlexContainer() {
        inlineHelper.flushHangingLeaves(flexContainer);
        inlineHelper.clearWaitingLeaves();
    }

    private boolean inlineHelperContainsText() {
        boolean containsText = false;
        for (final IElement element : inlineHelper.getWaitingLeaves()) {
            if (element instanceof Text && ANY_SYMBOL_PATTERN.matcher(((Text) element).getText()).find()) {
                containsText = true;
            }
        }
        return containsText;
    }
}
