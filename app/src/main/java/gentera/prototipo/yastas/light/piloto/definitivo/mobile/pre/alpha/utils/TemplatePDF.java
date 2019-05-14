package gentera.prototipo.yastas.light.piloto.definitivo.mobile.pre.alpha.utils;

import android.content.Context;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfWriter;

public class TemplatePDF {
    private Context context;
    private Document document;
    private PdfWriter pdfWriter;
    private PdfImage pdfImage;

    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void crearDocumentoPDF(){
        document = new Document(PageSize.A5);
        try {
            pdfWriter = PdfWriter.getInstance(document, null);
            document.open();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String title, String body) throws DocumentException {
        document.addTitle(title);
        //pdfImage.getImage().
        //document.add((Element) pdfImage);

    }

}
