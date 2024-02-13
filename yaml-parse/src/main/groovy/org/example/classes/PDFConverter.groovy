package main.groovy.org.example.classes

import com.itextpdf.html2pdf.HtmlConverter
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument

class PDFConverter {

    private PdfWriter pdfWriter
    private PdfDocument pdfDocument
    private ByteArrayOutputStream baos
    private OutputStream file

    PDFConverter() {
        try {
            baos = new ByteArrayOutputStream()
            pdfWriter = new PdfWriter(baos)
            pdfDocument = new PdfDocument(pdfWriter)
            pdfDocument.setDefaultPageSize(new PageSize(PageSize.A4))
        } catch (Exception e) { e.printStackTrace() }
    }

    @NonCPS
    void fromHtmlToPdfConverter(String htmlFile, String fileName = "report.pdf") {
        file = new FileOutputStream(new File(fileName))
        pdfDocument.open()
        HtmlConverter.convertToPdf(htmlFile,file)
        pdfDocument.close()
    }

}
