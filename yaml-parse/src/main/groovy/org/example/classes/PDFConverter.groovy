package main.groovy.org.example.classes

import com.itextpdf.html2pdf.ConverterProperties
import com.itextpdf.html2pdf.HtmlConverter
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document

import javax.swing.text.html.HTMLDocument

class PDFConverter {

    private PdfWriter pdfWriter
    private PdfDocument pdfDocument
    private ByteArrayOutputStream baos
    private OutputStream file

    PDFConverter(String fileName = "report.pdf") {
        try {
            baos = new ByteArrayOutputStream()
            pdfWriter = new PdfWriter(baos)
            pdfDocument = new PdfDocument(pdfWriter)
            pdfDocument.setDefaultPageSize(new PageSize(PageSize.A4))
            file = new FileOutputStream(new File(fileName))
        } catch (Exception e) { e.printStackTrace() }
    }

    void fromHtmlToPdfConverter(String htmlFile) {
        pdfDocument.open()
        HtmlConverter.convertToPdf(htmlFile,file)
        pdfDocument.close()
    }

}
