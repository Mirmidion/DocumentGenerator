package Code.Word;

import Code.Database.Connection;
import Code.Main.Main;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public class GenerateNew {

    static XWPFDocument style;
    static int tableOfContentsIndex = 0;
    private XWPFDocument document;
    int documentID;
    int chapterCount;
    int subchapterCount;

    public static void main(String[] args) {
        GenerateNew gn = new GenerateNew();
        try {
            gn.createTemplate(1, "FO", 2, "src/main/java/generated/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFO(int documentID, String documentName, int projectID, String filePath) {
        chapterCount = 0;
        subchapterCount = 0;
        try {
            createTemplate(documentID, documentName, projectID, filePath);
            createGeneralInformation(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTemplate(int documentID, String documentName, int projectID, String filePath) throws IOException {

        Connection con = new Connection();
        String projectName = Connection.projectNames.get(Connection.projectIDs.indexOf(projectID));
        this.documentID = documentID;

        //Check the generated path. If it is not there, create it.
        if (!Paths.get(filePath).toFile().exists()) Files.createDirectories(Paths.get(filePath));

        document = new XWPFDocument(new FileInputStream("src/main/java/Templates/Style.docx"));
        document.enforceUpdateFields();

        for (int i = 0; i < 13; i++) {
            document.removeBodyElement(0);
        }
        XWPFParagraph para;
        XWPFRun run;

        createFooter(document);

        para = document.createParagraph();
        para.setSpacingAfter(0);
        para.setFontAlignment(2);
        run = para.createRun();
        run.setFontSize(20);
        run.setText(projectName);

        para = document.createParagraph();
        para.setFontAlignment(2);
        run = para.createRun();
        run.setFontSize(15);
        run.setText(documentName);

        createSpacing(24, document);
        for (int i = 0; i < 24; i++) {

        }

        createDocInfo(document, documentID);
        createTableOfContents(document);

        //Create Word docs.
        createGeneralInformation(document);

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(filePath + projectName + "." + documentName + ".docx");
        document.write(out);

        //Close document
        out.close();

        Desktop desktop = Desktop.getDesktop();
        File doc = new File(filePath + projectName + "." + documentName + ".docx");
        if (doc.exists()) {
            desktop.open(doc);
        } else {
            System.out.println("Does not exist");
        }
        System.out.println(projectName + "." + documentName + ".docx" + " written successfully");
    }

    public void createFooter(XWPFDocument document) {
        // create footer
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);
        CTP ctpFooter = CTP.Factory.newInstance();

        XWPFParagraph[] parsFooter;

// add style (s.th.)
        CTPPr ctppr = ctpFooter.addNewPPr();
        CTString pst = ctppr.addNewPStyle();
        pst.setVal("style21");
        CTJc ctjc = ctppr.addNewJc();
        ctjc.setVal(STJc.RIGHT);
        ctppr.addNewRPr();

// Add in word "Page "
        CTR ctr = ctpFooter.addNewR();
        CTText t = ctr.addNewT();
        t.setStringValue("Page ");
        t.setSpace(SpaceAttribute.Space.PRESERVE);

// add everything from the footerXXX.xml you need
        ctr = ctpFooter.addNewR();
        ctr.addNewRPr();
        CTFldChar fch = ctr.addNewFldChar();
        fch.setFldCharType(STFldCharType.BEGIN);

        ctr = ctpFooter.addNewR();
        ctr.addNewInstrText().setStringValue(" Page ");

        ctpFooter.addNewR().addNewFldChar().setFldCharType(STFldCharType.SEPARATE);

        ctpFooter.addNewR().addNewT().setStringValue("1");

        ctpFooter.addNewR().addNewFldChar().setFldCharType(STFldCharType.END);

        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);

        parsFooter = new XWPFParagraph[1];

        parsFooter[0] = footerParagraph;

        policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);
    }

    public void createDocInfo(XWPFDocument document, int documentId) {
        Connection con = new Connection();
        XWPFTable table;
        XWPFTableRow tableRow;

        table = document.createTable();
        table.setWidth("70%");
        table.setBottomBorder(XWPFTable.XWPFBorderType.NONE, 1, 1, "FFFFFF");
        table.setLeftBorder(XWPFTable.XWPFBorderType.NONE, 1, 1, "FFFFFF");
        table.setRightBorder(XWPFTable.XWPFBorderType.NONE, 1, 1, "FFFFFF");
        table.setTopBorder(XWPFTable.XWPFBorderType.NONE, 1, 1, "FFFFFF");
        table.setInsideHBorder(XWPFTable.XWPFBorderType.NONE, 1, 1, "FFFFFF");
        table.setInsideVBorder(XWPFTable.XWPFBorderType.NONE, 1, 1, "FFFFFF");

        final int twipsPerInch = 1440;

        tableRow = table.getRow(0);
        tableRow.setHeight(Math.round(twipsPerInch * 0.1f));
        tableRow.getCell(0).setText("Versie:");
        tableRow.addNewTableCell().setText(Double.toString(con.getDocVersion(documentId)));

        tableRow = table.createRow();
        tableRow.setHeight(Math.round(twipsPerInch * 0.1f));
        tableRow.getCell(0).setText("Datum:");
        tableRow.getCell(1).setText(LocalDate.now().toString());

        tableRow = table.createRow();
        tableRow.setHeight(Math.round(twipsPerInch * 0.1f));
        tableRow.getCell(0).setText("Groep:");
        tableRow.getCell(1).setText(con.getProjectGroup());

        tableRow = table.createRow();
        tableRow.setHeight(Math.round(twipsPerInch * 0.1f));
        tableRow.getCell(0).setText("Auteurs:");
        tableRow.getCell(1).setText(con.getProjectGroupMembers(documentId));
    }

    public void createTableOfContents(XWPFDocument document) {
        XWPFParagraph p = document.createParagraph();
        XWPFRun run = p.createRun();
        run.setText("Inhoudsopgave");
        p.setStyle("Kopvaninhoudsopgave");
        CTP ctP = p.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();
        toc.setInstr("TOC \\h");
        toc.setDirty(STOnOff1.ON);
    }

    public void createRequirements(XWPFDocument document) {
        XWPFParagraph p = document.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        p.setStyle("Kop1");
        p.setPageBreak(true);
        p.setFontAlignment(2);

        XWPFRun run = p.createRun();

        run.setText("1. Requirements");


    }

    public void createChapter(XWPFDocument document, String title) {
        chapterCount++;
        subchapterCount = 0;
        XWPFParagraph p = document.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        p.setStyle("Kop1");
        p.setPageBreak(true);

        XWPFRun run = p.createRun();

        run.setText(chapterCount + ". " + title);
        run.setStyle("Kop1");

    }

    public void createSubChapter(XWPFDocument document, String title) {
        subchapterCount++;
        XWPFParagraph p = document.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        p.setStyle("Kop2");

        XWPFRun run = p.createRun();

        run.setText(chapterCount + "." + subchapterCount + ". " + title);
        run.setStyle("Kop2");
    }

    public void createSpacing(int amount, XWPFDocument document) {
        for (int i = 0; i < amount; i++) {
            XWPFParagraph para = document.createParagraph();
            XWPFRun run = para.createRun();
        }
    }

    public void createGeneralInformation(XWPFDocument document) {
        XWPFTable table;
        XWPFTableRow tableRow;
        String authors = Main.con.getProjectGroupMembers(documentID);
        String[] authorsSeperated = authors.split(",");

        createChapter(document, "Algemene Informatie");

        createSpacing(1, document);

        createSubChapter(document, "Projectorganisatie");

        table = document.createTable();
        table.setWidth("70%");

        tableRow = table.getRow(0);
        tableRow.getCell(0).setText("Naam:");
        tableRow.addNewTableCell().setText("Team:");
        tableRow.addNewTableCell().setText("Rol:");

        for (int i = 0; i < authorsSeperated.length; i++) {

            tableRow = table.createRow();
            tableRow.getCell(0).setText(authorsSeperated[i]);
            tableRow.getCell(1).setText(Main.con.getProjectGroup());
            tableRow.getCell(2).setText("Projectlid");
        }

        createSpacing(1, document);

        createSubChapter(document, "Distributielijst");

        table = document.createTable();
        table.setWidth("70%");

        tableRow = table.getRow(0);
        tableRow.getCell(0).setText("Aan:");
        tableRow.addNewTableCell().setText("Versie:");
        tableRow.addNewTableCell().setText("Datum:");

    }
}
