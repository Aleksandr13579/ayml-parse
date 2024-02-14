package org.example.classes

import html2pdf.HtmlConverter
import io.source.ByteArrayOutputStream
import kernel.geom.PageSize
import kernel.pdf.PdfWriter
import kernel.pdf.PdfDocument

class PDFConverter {

    PDFConverter() {
        try {
            this.baos = new ByteArrayOutputStream()
            this.pdfWriter = new PdfWriter(baos)
            this.pdfDocument = new PdfDocument(pdfWriter)
            this.pdfDocument.setDefaultPageSize(new PageSize(PageSize.A4))
        } catch (Exception e) { e.printStackTrace() }
    }

    @NonCPS
    void fromHtmlToPdfConverter(String htmlFile, String fileName = "report.pdf") {
        this.file = new FileOutputStream(new File(fileName))
        pdfDocument.open()
        HtmlConverter.convertToPdf(htmlFile,file)
        pdfDocument.close()
    }

    private PdfWriter pdfWriter
    private PdfDocument pdfDocument
    private ByteArrayOutputStream baos
    private OutputStream file

}
