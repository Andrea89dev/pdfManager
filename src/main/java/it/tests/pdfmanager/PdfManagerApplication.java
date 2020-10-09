package it.tests.pdfmanager;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfManagerApplication {

    public static void main(String[] args) throws IOException {
        System.out.println("PdfManagerApplication.main() - Start PdfManager");

//        PdfManager pdfManager = new PdfManager();
//        pdfManager.mergeDocuments(getFileList());

//        test();

        PdfManagerV2 pdfManager = new PdfManagerV2();
        pdfManager.createPdf();

        System.out.println("PdfManagerApplication.main() - End PdfManager");
    }

    private static File copyPdfFromTemplate(File templateFile) throws IOException {
        System.out.println("PdfManagerApplication.copyPdfFromTemplate() - Copying file " + templateFile.getName());

        File copiedFile = new File(ApplicationConstants.PATH_TMP + templateFile.getName());
        FileUtils.copyFile(templateFile, copiedFile);

        return copiedFile;
    }

    private static List<File> getFileList() throws IOException {
        List<File> fileList = new ArrayList<>();
        fileList.add(copyPdfFromTemplate(new File(ApplicationConstants.PATH_TEMPLATES + "start_contract_it.pdf")));
        fileList.add(copyPdfFromTemplate(new File(ApplicationConstants.PATH_TEMPLATES + "end_contract_it.pdf")));

        return fileList;
    }

    private static void test() throws IOException {
        PDDocument overlayDoc = new PDDocument();
        PDPage page = new PDPage();
        overlayDoc.addPage(page);
        Overlay overlayObj = new Overlay();
        PDFont font = PDType1Font.COURIER_OBLIQUE;

        PDPageContentStream contentStream = new PDPageContentStream(overlayDoc, page);
        contentStream.setFont(font, 12);
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();

        Matrix matrix = Matrix.getRotateInstance(Math.toRadians(90), 140, 50);
        contentStream.setTextMatrix(matrix);

        contentStream.drawString("deprecated");  // deprecated. Use showText(String text)
        contentStream.endText();
        contentStream.close();

        PDDocument originalDoc = PDDocument.load(new File(ApplicationConstants.PATH_TMP + "start_contract_it.pdf"));
        overlayObj.setOverlayPosition(Overlay.Position.FOREGROUND);
        overlayObj.setInputPDF(originalDoc);
        overlayObj.setAllPagesOverlayPDF(overlayDoc);
        Map<Integer, String> ovmap = new HashMap<>(); // empty map is a dummy
//        overlayObj.setOutputFile(ApplicationConstants.PATH_RESULTS + System.currentTimeMillis() + ".pdf");
        overlayObj.overlay(ovmap);
        originalDoc.save(new File(ApplicationConstants.PATH_RESULTS + System.currentTimeMillis() + ".pdf"));
        overlayDoc.close();
        originalDoc.close();
    }
}
