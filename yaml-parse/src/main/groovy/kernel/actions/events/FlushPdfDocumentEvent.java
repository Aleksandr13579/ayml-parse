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
package kernel.actions.events;

import commons.actions.AbstractITextConfigurationEvent;
import commons.actions.AbstractProductProcessITextEvent;
import commons.actions.EventManager;
import commons.actions.confirmations.ConfirmEvent;
import commons.actions.confirmations.ConfirmedEventWrapper;
import commons.actions.confirmations.EventConfirmationType;
import commons.actions.data.ProductData;
import commons.actions.processors.ITextProductEventProcessor;
import commons.actions.producer.ProducerBuilder;
import commons.actions.sequence.SequenceId;
import commons.utils.MessageFormatUtil;
import kernel.actions.data.ITextCoreProductData;
import kernel.logs.KernelLogMessageConstant;
import kernel.pdf.PdfDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class represents events notifying that {@link PdfDocument} was flushed.
 */
public final class FlushPdfDocumentEvent extends AbstractITextConfigurationEvent {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlushPdfDocumentEvent.class);

    private final WeakReference<PdfDocument> document;

    /**
     * Creates a new instance of the flushing event.
     *
     * @param document is a document to be flushed
     */
    public FlushPdfDocumentEvent(PdfDocument document) {
        super();
        this.document = new WeakReference<>(document);
    }

    /**
     * Prepares document for flushing.
     */
    @Override
    protected void doAction() {
        final PdfDocument pdfDocument = (PdfDocument) document.get();
        if (pdfDocument == null) {
            return;
        }
        List<AbstractProductProcessITextEvent> events = getEvents(pdfDocument.getDocumentIdWrapper());

        if (events == null || events.isEmpty()) {
            final ProductData productData = ITextCoreProductData.getInstance();
            final String noEventProducer = "iText\u00ae \u00a9" + productData.getSinceCopyrightYear() + "-"
                    + productData.getToCopyrightYear() + " Apryse Group NV (no registered products)";
            pdfDocument.getDocumentInfo().setProducer(noEventProducer);
            return;
        }

        final Set<String> products = new HashSet<>();
        for (final AbstractProductProcessITextEvent event : events) {
            pdfDocument.getFingerPrint().registerProduct(event.getProductData());
            if (event.getConfirmationType() == EventConfirmationType.ON_CLOSE) {
                EventManager.getInstance().onEvent(new ConfirmEvent(pdfDocument.getDocumentIdWrapper(), event));
            }
            products.add(event.getProductName());
        }

        for (final String product : products) {
            final ITextProductEventProcessor processor = getActiveProcessor(product);
            if (processor == null && LOGGER.isWarnEnabled()) {
                LOGGER.warn(MessageFormatUtil.format(KernelLogMessageConstant.UNKNOWN_PRODUCT_INVOLVED, product));
            }
        }

        final String oldProducer = pdfDocument.getDocumentInfo().getProducer();
        final String newProducer =
                ProducerBuilder.modifyProducer(getConfirmedEvents(pdfDocument.getDocumentIdWrapper()), oldProducer);
        pdfDocument.getDocumentInfo().setProducer(newProducer);
    }

    private List<ConfirmedEventWrapper> getConfirmedEvents(SequenceId sequenceId) {
        final List<AbstractProductProcessITextEvent> events = getEvents(sequenceId);
        final List<ConfirmedEventWrapper> confirmedEvents = new ArrayList<>();
        for (AbstractProductProcessITextEvent event : events) {
            if (event instanceof ConfirmedEventWrapper) {
                confirmedEvents.add((ConfirmedEventWrapper) event);
            } else {
                LOGGER.warn(MessageFormatUtil.format(KernelLogMessageConstant.UNCONFIRMED_EVENT,
                        event.getProductName(), event.getEventType()));
            }
        }
        return confirmedEvents;
    }
}
