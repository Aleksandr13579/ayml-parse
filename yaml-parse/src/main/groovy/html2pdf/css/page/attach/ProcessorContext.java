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
package html2pdf.css.page.attach;

import main.groovy.commons.actions.contexts.IMetaInfo;
import main.groovy.html2pdf.ConverterProperties;
import main.groovy.html2pdf.resolver.font.DefaultFontProvider;
import main.groovy.html2pdf.resolver.form.FormFieldNameResolver;
import main.groovy.html2pdf.resolver.form.RadioCheckResolver;
import main.groovy.html2pdf.resolver.resource.HtmlResourceResolver;
import main.groovy.io.font.FontProgram;
import main.groovy.kernel.pdf.PdfDocument;
import main.groovy.layout.font.FontInfo;
import main.groovy.layout.font.FontProvider;
import main.groovy.layout.font.FontSet;
import main.groovy.layout.font.Range;
import main.groovy.styledxmlparser.css.media.MediaDeviceDescription;
import main.groovy.styledxmlparser.resolver.resource.ResourceResolver;
import html2pdf.css.page.attach.impl.DefaultTagWorkerFactory;
import html2pdf.css.page.attach.impl.HtmlMetaInfoContainer;
import html2pdf.css.page.attach.impl.LinkContext;
import html2pdf.css.page.attach.impl.OutlineHandler;
import html2pdf.css.page.css.apply.ICssApplierFactory;
import html2pdf.css.page.css.apply.impl.DefaultCssApplierFactory;
import html2pdf.css.page.css.resolve.CssContext;

/**
 * Keeps track of the context of the processor.
 */
public class ProcessorContext {

    /**
     * The font provider.
     */
    private FontProvider fontProvider;

    /**
     * Temporary set of fonts used in the PDF.
     */
    private FontSet tempFonts;

    /**
     * The resource resolver.
     */
    private ResourceResolver resourceResolver;

    /**
     * The device description.
     */
    private MediaDeviceDescription deviceDescription;

    /**
     * The tag worker factory.
     */
    private ITagWorkerFactory tagWorkerFactory;

    /**
     * The CSS applier factory.
     */
    private ICssApplierFactory cssApplierFactory;

    /**
     * The base URI.
     */
    private String baseUri;

    /**
     * Indicates whether an AcroForm needs to be created.
     */
    private boolean createAcroForm;

    /**
     * The form field name resolver.
     */
    private FormFieldNameResolver formFieldNameResolver;

    /**
     * The radio check resolver.
     */
    private RadioCheckResolver radioCheckResolver;

    /**
     * The outline handler.
     */
    private OutlineHandler outlineHandler;

    /**
     * Indicates whether the document should be opened in immediate flush or not
     */
    private boolean immediateFlush;

    // Variable fields

    /**
     * The state.
     */
    private State state;

    /**
     * The CSS context.
     */
    private CssContext cssContext;

    /**
     * The link context
     */
    private LinkContext linkContext;

    /**
     * The PDF document.
     */
    private PdfDocument pdfDocument;

    /**
     * The Processor meta info
     */
    private IMetaInfo metaInfo;

    /**
     * Internal state variable to keep track of whether the processor is currently inside an inlineSvg
     */
    private boolean processingInlineSvg;

    private final int limitOfLayouts;

    /**
     * Instantiates a new {@link ProcessorContext} instance.
     *
     * @param converterProperties a {@link ConverterProperties} instance
     */
    public ProcessorContext(ConverterProperties converterProperties) {
        if (converterProperties == null) {
            converterProperties = new ConverterProperties();
        }
        state = new State();

        deviceDescription = converterProperties.getMediaDeviceDescription();
        if (deviceDescription == null) {
            deviceDescription = MediaDeviceDescription.getDefault();
        }

        fontProvider = converterProperties.getFontProvider();
        if (fontProvider == null) {
            fontProvider = new DefaultFontProvider();
        }

        tagWorkerFactory = converterProperties.getTagWorkerFactory();
        if (tagWorkerFactory == null) {
            tagWorkerFactory = DefaultTagWorkerFactory.getInstance();
        }

        cssApplierFactory = converterProperties.getCssApplierFactory();
        if (cssApplierFactory == null) {
            cssApplierFactory = DefaultCssApplierFactory.getInstance();
        }

        baseUri = converterProperties.getBaseUri();
        if (baseUri == null) {
            baseUri = "";
        }

        outlineHandler = converterProperties.getOutlineHandler();
        if (outlineHandler == null) {
            outlineHandler = new OutlineHandler();
        }

        resourceResolver = new HtmlResourceResolver(baseUri, this, converterProperties.getResourceRetriever());

        limitOfLayouts = converterProperties.getLimitOfLayouts();
        cssContext = new CssContext();
        linkContext = new LinkContext();

        createAcroForm = converterProperties.isCreateAcroForm();
        formFieldNameResolver = new FormFieldNameResolver();
        radioCheckResolver = new RadioCheckResolver();
        immediateFlush = converterProperties.isImmediateFlush();
        processingInlineSvg = false;
    }

    /**
     * Gets maximum number of layouts.
     *
     * @return layouts limit
     */
    public int getLimitOfLayouts() {
        return limitOfLayouts;
    }

    /**
     * Sets the font provider.
     *
     * @param fontProvider the new font provider
     */
    public void setFontProvider(FontProvider fontProvider) {
        this.fontProvider = fontProvider;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * Gets the PDF document.
     *
     * @return the PDF document
     */
    public PdfDocument getPdfDocument() {
        return pdfDocument;
    }

    /**
     * Gets the font provider.
     *
     * @return the font provider
     */
    public FontProvider getFontProvider() {
        return fontProvider;
    }

    /**
     * Gets the temporary set of fonts.
     *
     * @return the set of fonts
     */
    public FontSet getTempFonts() {
        return tempFonts;
    }

    /**
     * Gets the resource resolver.
     *
     * @return the resource resolver
     */
    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    /**
     * Gets the device description.
     *
     * @return the device description
     */
    public MediaDeviceDescription getDeviceDescription() {
        return deviceDescription;
    }

    /**
     * Gets the tag worker factory.
     *
     * @return the tag worker factory
     */
    public ITagWorkerFactory getTagWorkerFactory() {
        return tagWorkerFactory;
    }

    /**
     * Gets the CSS applier factory.
     *
     * @return the CSS applier factory
     */
    public ICssApplierFactory getCssApplierFactory() {
        return cssApplierFactory;
    }

    /**
     * Gets the CSS context.
     *
     * @return the CSS context
     */
    public CssContext getCssContext() {
        return cssContext;
    }

    /**
     * Gets the link context.
     *
     * @return the link context
     */
    public LinkContext getLinkContext() {
        return linkContext;
    }

    /**
     * Checks if is an AcroForm needs to be created.
     *
     * @return true, an AcroForm should be created
     */
    public boolean isCreateAcroForm() {
        return createAcroForm;
    }

    /**
     * Gets the form field name resolver.
     *
     * @return the form field name resolver
     */
    public FormFieldNameResolver getFormFieldNameResolver() {
        return formFieldNameResolver;
    }

    /**
     * Gets the radio check resolver.
     *
     * @return the radio check resolver
     */
    public RadioCheckResolver getRadioCheckResolver() {
        return radioCheckResolver;
    }


    /**
     * Gets the outline handler.
     *
     * @return the outline handler
     */
    public OutlineHandler getOutlineHandler() {
        return outlineHandler;
    }

    /**
     * Add temporary font from @font-face.
     *
     * @param fontInfo the font info
     * @param alias    the alias
     */
    public void addTemporaryFont(FontInfo fontInfo, String alias) {
        if (tempFonts == null) {
            tempFonts = new FontSet();
        }
        tempFonts.addFont(fontInfo, alias);
    }

    /**
     * Add temporary font from @font-face.
     *
     * @param fontProgram the font program
     * @param encoding    the encoding
     * @param alias       the alias
     */
    public void addTemporaryFont(FontProgram fontProgram, String encoding, String alias) {
        if (tempFonts == null) {
            tempFonts = new FontSet();
        }
        tempFonts.addFont(fontProgram, encoding, alias);
    }

    /**
     * Add temporary font from @font-face.
     *
     * @param fontProgram  the font program
     * @param encoding     the encoding
     * @param alias        the alias
     * @param unicodeRange the unicode range
     */
    public void addTemporaryFont(FontProgram fontProgram, String encoding, String alias, Range unicodeRange) {
        if (tempFonts == null) {
            tempFonts = new FontSet();
        }
        tempFonts.addFont(fontProgram, encoding, alias, unicodeRange);
    }

    /**
     * Check fonts in font provider and temporary font set.
     *
     * @return true, if there is at least one font either in FontProvider or temporary FontSet.
     * @see #addTemporaryFont(FontInfo, String)
     * @see #addTemporaryFont(FontProgram, String, String)
     */
    public boolean hasFonts() {
        return !fontProvider.getFontSet().isEmpty()
                || (tempFonts != null && !tempFonts.isEmpty());
    }

    /**
     * Resets the context.
     */
    public void reset() {
        this.pdfDocument = null;
        this.state = new State();
        this.resourceResolver.resetCache();
        this.cssContext = new CssContext();
        this.linkContext = new LinkContext();
        this.formFieldNameResolver.reset();
        //Reset font provider. PdfFonts shall be reseted.
        this.fontProvider.reset();
        this.tempFonts = null;
        this.outlineHandler.reset();
        this.processingInlineSvg = false;
    }

    /**
     * Resets the context, and assigns a new PDF document.
     *
     * @param pdfDocument the new PDF document for the context
     */
    public void reset(PdfDocument pdfDocument) {
        reset();
        this.pdfDocument = pdfDocument;
    }

    /**
     * Gets the baseURI: the URI which has been set manually or the directory of the html file in case when baseURI hasn't been set manually.
     *
     * @return the baseUri
     */
    public String getBaseUri() {
        return baseUri;
    }


    /**
     * Checks if immediateFlush is set
     *
     * @return true if immediateFlush is set, false if not.
     */
    public boolean isImmediateFlush() {
        return immediateFlush;
    }

    /**
     * Gets html meta info container.
     *
     * <p>Meta info will be used to determine event origin.
     *
     * @return html meta info container
     */
    public HtmlMetaInfoContainer getMetaInfoContainer() {
        return new HtmlMetaInfoContainer(metaInfo);
    }

    /**
     * Sets IMetaInfo to processor context.
     *
     * @param metaInfo the IMetaInfo object
     */
    public void setMetaInfo(IMetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    /**
     * Check if the processor is currently processing an inline svg
     *
     * @return True if the processor is processing an inline Svg, false otherwise.
     */
    public boolean isProcessingInlineSvg() {
        return processingInlineSvg;
    }

    /**
     * Set the processor to processing Inline Svg state
     */
    public void startProcessingInlineSvg() {
        processingInlineSvg = true;
    }

    /**
     * End the processing Svg State
     */
    public void endProcessingInlineSvg(){
        processingInlineSvg = false;
    }
}
