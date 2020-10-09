package it.tests.pdfmanager;

import it.tests.pdfmanager.bean.VehicleServiceBO;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.VerticalTextCell;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

public class PdfManagerV2 {

    private static final int COLUMNS = 8;

    private NumberFormat formatter;

    public PdfManagerV2() {
        formatter = NumberFormat.getCurrencyInstance();
    }

    public void createPdf() throws IOException {
        PDDocument overlayDoc = new PDDocument();
        PDPage overlayPage = new PDPage(PDRectangle.A3);
        overlayPage.setRotation(90);
        overlayDoc.addPage(overlayPage);
        Overlay overlayObj = new Overlay();

//
//        PDDocument templateDocument = PDDocument.load(new File(ApplicationConstants.PATH_TMP + "start_contract_it.pdf"));
//
//        COSDictionary pageDict = templateDocument.getPages().get(1).getCOSObject();
//        COSDictionary newPageDict = new COSDictionary(pageDict);
//        newPageDict.removeItem(COSName.ANNOTS);
//
//        PDPage newPage = new PDPage(newPageDict);
//        newPage.setRotation(90);

//        float startY = overlayPage.getMediaBox().getHeight() - 50f;

        //        overlayPage.setRotation(-90);

        Table table = createTable(getVehicleServiceBOList());
//        Table table = createTable(transformToMatrix(getVehicleServiceBOList()));
        try(final PDPageContentStream contentStream = new PDPageContentStream(overlayDoc, overlayPage)) {
            TableDrawer.builder()
                    .page(overlayPage)
                    .contentStream(contentStream)
                    .table(table)
//                    .startX(overlayPage.getMediaBox().getLowerLeftX()+125)
//                    .startY(overlayPage.getMediaBox().getUpperRightY()-39)
                    .startX(overlayPage.getMediaBox().getLowerLeftX()+200)
                    .startY(overlayPage.getMediaBox().getUpperRightY()-490)

//                    .endY(50f)
                    .build()
                    .draw();

//            startY -= (table.getHeight() + 50f);
        }

        overlayPage.setRotation(-90);

        PDDocument originalDoc = PDDocument.load(new File(ApplicationConstants.PATH_TMP + "start_contract_it.pdf"));
        overlayObj.setOverlayPosition(Overlay.Position.FOREGROUND);
        overlayObj.setInputPDF(originalDoc);
        overlayObj.setAllPagesOverlayPDF(overlayDoc);
        Map<Integer, String> ovmap = new HashMap<>(); // empty map is a dummy
        overlayObj.overlay(ovmap);

        originalDoc.save(new File(ApplicationConstants.PATH_RESULTS + System.currentTimeMillis() + ".pdf"));
        overlayObj.close();
        originalDoc.close();
    }

//    public Table createTable(Vector<Vector<String>> matrix) {
//        System.out.println("Matrix size: " + matrix.size());
//        Table.TableBuilder tableBuilder = Table.builder()
////                .addColumnOfWidth(50f)
////                .numberOfColumns(matrix.size())
//                .addColumnsOfWidth(50f, 50f, 50f, 50f, 50f, 50f, 50f, 50f)
//                .fontSize(8)
//                .font(PDType1Font.HELVETICA);
//
//        matrix.forEach(x -> {
//            System.out.println("--------------------------------");
//            Row.RowBuilder rowBuilder = Row.builder();
//            x.forEach(y -> {
//                System.out.println("-> " + y);
//                rowBuilder.add(createVerticalCell(y));
//            });
//            System.out.println("--------------------------------");
//            tableBuilder.addRow(rowBuilder.build());
//        });
//
//        return tableBuilder.build();
//    }

    public Table createTable(List<VehicleServiceBO> vehicleServiceBOList) {
        Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(50f, 50f, 50f, 50f, 50f, 50f, 50f, 50f)
                .fontSize(8)
                .font(PDType1Font.HELVETICA);

        vehicleServiceBOList.forEach(vehicleServiceBO -> {
            tableBuilder.addRow(Row.builder()
                .add(createCell(vehicleServiceBO.getModel()))
                .add(createCell(vehicleServiceBO.getVin()))
                .add(createCell(vehicleServiceBO.getPlate()))
                .add(createCell(""))
                .add(createCell(""))
                .add(createCell(""))
                .add(createCell(""))
                .add(createCurrencyCell(vehicleServiceBO.getPrice()))
                .build());
        });

        return tableBuilder.build();
    }

    private AbstractCell createVerticalCell(String value) {
        return VerticalTextCell.builder().borderWidth(1).text(value).build();
    }

    private AbstractCell createCell(String value) {
        return TextCell.builder().borderWidth(1).text(value).verticalAlignment(VerticalAlignment.MIDDLE).build();
    }

    private AbstractCell createCurrencyCell(Double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return TextCell.builder().borderWidth(1).text(formatter.format(value)).verticalAlignment(VerticalAlignment.MIDDLE).build();
    }

    private AbstractCell createVerticalCurrencyCell(Double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return VerticalTextCell.builder().borderWidth(1).text(formatter.format(value)).build();
    }

    private List<VehicleServiceBO> getVehicleServiceBOList() {
        List<VehicleServiceBO> vehicleServiceBOList = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            VehicleServiceBO vehicleServiceBO = new VehicleServiceBO();
            vehicleServiceBO.setModel("Model #" + i);
            vehicleServiceBO.setPlate("XX" + StringUtils.leftPad(String.valueOf(i), 3, "0") + "XX");
            vehicleServiceBO.setVin("VIN" + StringUtils.leftPad(String.valueOf(i), 7, "0"));

            vehicleServiceBO.setDuration("");
            vehicleServiceBO.setPrice(RandomUtils.nextDouble(0d, 1999d));

            vehicleServiceBOList.add(vehicleServiceBO);
        }

        return vehicleServiceBOList;
    }

    public Vector<Vector<String>> transformToMatrix(List<VehicleServiceBO> vehicleServiceBOList) {
        Vector<Vector<String>> matrix = new Vector<>();

        for(int x = 0; x < vehicleServiceBOList.size(); x++) {
            Vector<String> innerM = new Vector<>();
            for(int y = 0; y < COLUMNS; y++) {
                switch (y) {
                    case 0:
                        innerM.add(formatter.format(vehicleServiceBOList.get(x).getPrice()));
                        break;
                    case 1:
                        innerM.add(vehicleServiceBOList.get(x).getDuration());
                        break;
                    case 2:
                        innerM.add("31/12/2020");
                        break;
                    case 3:
                        innerM.add("01/01/2020");
                        break;
                    case 4:
                        innerM.add("Services");
                        break;
                    case 5:
                        innerM.add(vehicleServiceBOList.get(x).getPlate());
                        break;
                    case 6:
                        innerM.add(vehicleServiceBOList.get(x).getVin());
                        break;
                    case 7:
                        innerM.add(vehicleServiceBOList.get(x).getModel());
                        break;
                }
            }

            matrix.add(innerM);
        }

        return matrix;
    }

}
