package it.tests.pdfmanager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDAppearanceContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfManager {

    private PdfFileInspector pdfFileInspector;

    public PdfManager() {
        pdfFileInspector = new PdfFileInspector();
    }

    public void mergeDocuments(List<File> fileList) throws IOException {
        if(CollectionUtils.isNotEmpty(fileList)) {
            System.out.println("PdfManager.mergeDocuments() - Start merging pdf");

            PDFMergerUtility pdfMerger = new PDFMergerUtility();

            pdfMerger.setDestinationFileName(ApplicationConstants.PATH_RESULTS + System.currentTimeMillis() + ".pdf");

            for(File file : fileList) {
                System.out.println("PdfManager.mergeDocuments() - add file to merging list: " + file.getAbsolutePath());
//                System.out.println("PdfManager - filename: " + file.getName());
                pdfMerger.addSource(fillDocument(file, pdfFileInspector.getFileFields(file)));
            }

            System.out.println("PdfManager.mergeDocuments() - Merging " + fileList.size() + " files");
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

            System.out.println("PdfManager.mergeDocuments() - Done");
        }
    }



    public File fillDocument(File file, Map<String, String> valueList) throws IOException {
//        printPdfFields(file);
        PDDocument pdDocument = PDDocument.load(file);
        PDDocumentCatalog pdCatalog = pdDocument.getDocumentCatalog();
        PDAcroForm pdAcroForm = pdCatalog.getAcroForm();

        if(MapUtils.isNotEmpty(valueList)) {
            for (Map.Entry<String, String> entry : valueList.entrySet()) {
                PDTextField pdField = (PDTextField) pdAcroForm.getField(entry.getKey());
                if (ObjectUtils.isNotEmpty(pdField)) {
//                    if(entry.getKey().equals("vehiclesdata")) {
//                    }

                    pdField.setValue(entry.getValue());
                }
            }
        }

        pdDocument.save(file);
        pdAcroForm.flatten();
        pdDocument.close();

        return file;
    }

    private void printPdfFields(File file) throws IOException {
        System.out.println("PdfManager.printPdfFields() - Start to search pdf fields");

        PDDocument pdDocument = PDDocument.load(file);
        PDDocumentCatalog pdCatalog = pdDocument.getDocumentCatalog();
        PDAcroForm pdAcroForm = pdCatalog.getAcroForm();

        List<PDField> fields = pdAcroForm.getFields();
        fields.forEach(field -> {
            System.out.println("PdfManager.printPdfFields() - Found field: " + field.getPartialName());
        });


        System.out.println("PdfManager.printPdfFields() - Done");
    }

}
