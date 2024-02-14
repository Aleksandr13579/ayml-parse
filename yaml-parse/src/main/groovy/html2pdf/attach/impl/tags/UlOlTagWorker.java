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
package html2pdf.attach.impl.tags;

import html2pdf.attach.ITagWorker;
import html2pdf.attach.ProcessorContext;
import html2pdf.attach.util.AccessiblePropHelper;
import html2pdf.attach.util.WaitingInlineElementsHelper;
import html2pdf.css.CssConstants;
import html2pdf.html.AttributeConstants;
import layout.IPropertyContainer;
import layout.element.*;
import layout.properties.Property;
import styledxmlparser.css.util.CssDimensionParsingUtils;
import styledxmlparser.node.IElementNode;

/**
 * TagWorker class for the {@code ul} and {@code ol} elements.
 */
public class UlOlTagWorker implements ITagWorker {

    /**
     * The list object.
     */
    private List list;

    /**
     * Helper class for waiting inline elements.
     */
    private WaitingInlineElementsHelper inlineHelper;

    /**
     * Creates a new {@link UlOlTagWorker} instance.
     *
     * @param element the element
     * @param context the context
     */
    public UlOlTagWorker(IElementNode element, ProcessorContext context) {
        list = new List().setListSymbol("");
        //In the case of an ordered list, see if the start attribute can be found
        if (element.getAttribute(AttributeConstants.START) != null) {
            Integer startValue = CssDimensionParsingUtils.parseInteger(element.getAttribute(AttributeConstants.START));
            if (startValue != null) list.setItemStartIndex((int) startValue);
        }
        inlineHelper = new WaitingInlineElementsHelper(element.getStyles().get(CssConstants.WHITE_SPACE), element.getStyles().get(CssConstants.TEXT_TRANSFORM));

        AccessiblePropHelper.trySetLangAttribute(list, element);
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processEnd(html2pdf.html.node.IElementNode, html2pdf.attach.ProcessorContext)
     */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        processUnlabeledListItem();
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processContent(java.lang.String, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        inlineHelper.add(content);
        return true;
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#processTagChild(html2pdf.attach.ITagWorker, html2pdf.attach.ProcessorContext)
     */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        IPropertyContainer child = childTagWorker.getElementResult();
        if (child instanceof ILeafElement) {
            inlineHelper.add((ILeafElement) child);
            return true;
        } else if (childTagWorker instanceof SpanTagWorker) {
            boolean allChildrenProcessed = true;
            for (IPropertyContainer propertyContainer : ((SpanTagWorker) childTagWorker).getAllElements()) {
                if (propertyContainer instanceof ILeafElement) {
                    inlineHelper.add((ILeafElement) propertyContainer);
                } else if (propertyContainer instanceof IBlockElement && CssConstants.INLINE_BLOCK.equals(((SpanTagWorker) childTagWorker).getElementDisplay(propertyContainer))) {
                    inlineHelper.add((IBlockElement) propertyContainer);
                } else {
                    allChildrenProcessed = addBlockChild(propertyContainer) && allChildrenProcessed;
                }
            }
            return allChildrenProcessed;
        } else if (childTagWorker instanceof IDisplayAware && CssConstants.INLINE_BLOCK.equals(((IDisplayAware) childTagWorker).getDisplay()) && childTagWorker.getElementResult() instanceof IBlockElement) {
            inlineHelper.add((IBlockElement) childTagWorker.getElementResult());
            return true;
        } else {
            return addBlockChild(child);
        }
    }

    /* (non-Javadoc)
     * @see html2pdf.attach.ITagWorker#getElementResult()
     */
    @Override
    public IPropertyContainer getElementResult() {
        return list;
    }

    /**
     * Processes an unlabeled list item.
     */
    private void processUnlabeledListItem() {
        Paragraph p = inlineHelper.createParagraphContainer();
        inlineHelper.flushHangingLeaves(p);
        if (p.getChildren().size() > 0) {
            addUnlabeledListItem(p);
        }
    }

    /**
     * Adds an unlabeled list item.
     *
     * @param item the item
     */
    private void addUnlabeledListItem(IBlockElement item) {
        ListItem li = new ListItem();
        li.add(item);
        li.setProperty(Property.LIST_SYMBOL, null);
        list.add(li);
    }

    /**
     * Adds a child.
     *
     * @param child the child
     * @return true, if successful
     */
    private boolean addBlockChild(IPropertyContainer child) {
        processUnlabeledListItem();
        if (child instanceof ListItem) {
            list.add((ListItem) child);
            return true;
        } else if (child instanceof IBlockElement) {
            addUnlabeledListItem((IBlockElement) child);
            return true;
        }
        return false;
    }

}
