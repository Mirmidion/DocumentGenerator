package Code.Word;

import Code.Database.Connection;
import Code.Main.Main;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

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
        createPreface(document, documentID);
        createActivityDiagram(document);
        createRequirements(document);
        createUseCases(document);
        createDomainModel(document);
        createWireframes(document, documentID);

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
        String version = Double.toString(con.getDocVersion(documentId));
        if (con.getDocVersion(documentId) < 1.0){
            version = "1.0";
        }
        tableRow.addNewTableCell().setText(version);

        tableRow = table.createRow();
        tableRow.setHeight(Math.round(twipsPerInch * 0.1f));
        tableRow.getCell(0).setText("Datum:");
        tableRow.getCell(1).setText(LocalDate.now().toString());

        tableRow = table.createRow();
        tableRow.setHeight(Math.round(twipsPerInch * 0.1f));
        tableRow.getCell(0).setText("Groep:");
        String projectGroep = con.getProjectGroup();
        if (projectGroep == null){
            projectGroep = "None";
        }
        tableRow.getCell(1).setText(projectGroep);

        tableRow = table.createRow();
        tableRow.setHeight(Math.round(twipsPerInch * 0.1f));
        tableRow.getCell(0).setText("Auteurs:");
        String authors = con.getProjectGroupMembers(documentId);
        if (authors == null){
            authors = "None";
        }
        tableRow.getCell(1).setText(authors);
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
        String[] authorsSeperated;
        if (authors != null) {
            authorsSeperated = authors.split(",");
        }
        else{
            authorsSeperated = new String[0];
        }

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

    public void addText(XWPFDocument document, String text){
        XWPFParagraph para = document.createParagraph();
        para.setStyle("Geenafstand");
        XWPFRun run = para.createRun();
        run.setText(text);
    }

    public void addBoldText(XWPFDocument document, String text){
        XWPFParagraph para = document.createParagraph();
        para.setStyle("Geenafstand");
        XWPFRun run = para.createRun();
        run.setBold(true);
        run.setText(text);
    }

    public void createPreface(XWPFDocument document, int documentID){

        createChapter(document, "Inleiding");

        createSubChapter(document, "De casus");
        addText(document, Main.con.getCasus());

        createSubChapter(document, "Missie en Visie");
        addText(document, Main.con.getMissionAndVision());

        createSubChapter(document, "Over project");
        addText(document, Main.con.getAboutProject());

        createSubChapter(document, "Over document");
        addText(document, Main.con.getAboutDocumentByID(documentID));
    }

    public void addPicture(XWPFDocument document, String type){
        String filePath = Main.con.getFile(documentID, type);
        try {

            FileInputStream fis = new FileInputStream(filePath);
            BufferedImage bimg = ImageIO.read(fis);
            int width          = bimg.getWidth();
            int height         = bimg.getHeight();
            System.out.println(width);

            FileInputStream is = new FileInputStream(filePath);
            if (filePath.endsWith("png")) {
                document.createParagraph().createRun().addPicture(is, Document.PICTURE_TYPE_PNG, "", Units.toEMU(width), Units.toEMU(height));
            }
            else if (filePath.endsWith("jpeg")) {
                document.createParagraph().createRun().addPicture(is, Document.PICTURE_TYPE_JPEG, "", Units.toEMU(width), Units.toEMU(height));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addWireframePicture(XWPFDocument document, int documentID, int wireframeNumber){
        String filePath = Main.con.getWireframeFilePathByDocumentID(documentID, wireframeNumber);
        try {

            FileInputStream fis = new FileInputStream(filePath);
            BufferedImage bimg = ImageIO.read(fis);
            int width          = bimg.getWidth();
            int height         = bimg.getHeight();
            System.out.println(width);

            FileInputStream is = new FileInputStream(filePath);
            if (filePath.endsWith("png")) {
                document.createParagraph().createRun().addPicture(is, Document.PICTURE_TYPE_PNG, "", Units.toEMU(width), Units.toEMU(height));
            }
            else if (filePath.endsWith("jpeg")) {
                document.createParagraph().createRun().addPicture(is, Document.PICTURE_TYPE_JPEG, "", Units.toEMU(width), Units.toEMU(height));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addPNGPicture(XWPFDocument document){
        String filePath = "C:\\Users\\yorbe\\Desktop\\DocumentGenerator\\src\\main\\java\\Templates\\img.png";
        System.out.println(filePath.toString());
        try {

            FileInputStream fis = new FileInputStream(filePath);
            BufferedImage bimg = ImageIO.read(fis);
            int width          = Math.round(bimg.getWidth() * 0.7f);
            int height         = Math.round(bimg.getHeight() * 0.7f);
            System.out.println(width);

            FileInputStream is = new FileInputStream(filePath);
            document.createParagraph().createRun().addPicture(is, Document.PICTURE_TYPE_PNG, "", Units.toEMU(width), Units.toEMU(height));

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void createActivityDiagram(XWPFDocument document){

        createChapter(document, "Activity Diagram");
        addPicture(document, "activityDiagram");

    }

    public void createRequirements(XWPFDocument document) {

        createChapter(document, "Requirements");
        createSubChapter(document, "User Stories en acceptatiecriteria");

        int rows = Main.con.getUseCasesNames().size()+1;
        int columns = 2;

        ArrayList<String> requirements = Main.con.getAllRequirements();
        ArrayList<String> usecases = Main.con.getUseCasesNames();

        XWPFTable table = document.createTable(rows ,columns);
        table.setWidth("100%");

        table.getRow(0).getCell(0).setText("Nr");
        table.getRow(0).getCell(1).setText("Beschrijving");

        for (String usecase : usecases){

            int r = usecases.indexOf(usecase) + 1;
            table.getRow(r).getCell(0).setText("US" + r);

            XWPFTableCell cellPar = table.getRow(r).getCell(1);
            XWPFParagraph para = cellPar.addParagraph();
            para.setStyle("Geenafstand");
            XWPFRun run = para.createRun();
            run.setText(usecases.get(r-1));
            run.setBold(true);

            para = cellPar.addParagraph();
            para.setStyle("Geenafstand");
            run = para.createRun();
            String requirement = "";
            if (r-1 < requirements.size()) {
                requirement = requirements.get(r-1);
            }
            run.setText(requirement);

            para = cellPar.addParagraph();
            para.setStyle("Geenafstand");
            run = para.createRun();

            para = cellPar.addParagraph();
            para.setStyle("Geenafstand");
            run = para.createRun();
            run.setBold(true);
            run.setText("Acceptatiecriteria:");

            for (String crit : Main.con.getUseCaseCriteria(usecases.get(r-1))){
                para = cellPar.addParagraph();
                para.setStyle("Geenafstand");
                run = para.createRun();
                run.setText("  -  " + crit);
            }
        }

    }

    public void createUseCases(XWPFDocument document){
        createChapter(document,"Use Cases");
        createUseCaseDiagram(document);
        createUseCaseBeschrijvingen(document);
    }

    public void createUseCaseDiagram(XWPFDocument document){

        createSubChapter(document, "Use Case Diagram");
        addPicture(document,"useCaseDiagram");
    }

    public void createUseCaseBeschrijvingen(XWPFDocument document){
        createSubChapter(document, "Use Case Beschrijvingen");
        for (String name : Main.con.getUseCasesNames()){
            int usecaseID = Main.con.getUseCaseIDByName(name);
            XWPFTable table = document.createTable(6 ,2);
            table.setWidth("100%");

            table.getRow(0).getCell(0).setText("Naam");
            table.getRow(0).getCell(1).setText(name);

            table.getRow(1).getCell(0).setText("Actoren");
            table.getRow(1).getCell(1).setText(Main.con.getActorsUsedInUseCase(usecaseID));

            String[] properties = Main.con.getBeschrijving(usecaseID);

            table.getRow(2).getCell(0).setText("Preconditie(s)");
            table.getRow(2).getCell(1).setText(properties[0]);

            table.getRow(3).getCell(0).setText("Postconditie(s)");
            table.getRow(3).getCell(1).setText(properties[1]);

            table.getRow(4).getCell(0).setText("Hoofdscenario");
            int counter = 1;
            for (String row : Main.con.getBeschrijvingScenario(usecaseID)){
                XWPFRun run = table.getRow(4).getCell(1).addParagraph().createRun();
                run.setText(counter + " " + row);
            }

            table.getRow(5).getCell(0).setText("Uitzonderingen");
            table.getRow(5).getCell(1).setText(properties[2]);
        }
    }

    public void createDomainModelExplanation(XWPFDocument document){
        addText(document, "Een domeinmodel is een model wat objecten, eigenschappen en de onderlinge relaties tussen elkaar weergeeft binnen de applicaties. Het bestaat uit klassen, attributen, associaties en overervingen. ");
        createSpacing(1,document);
        createSubChapter(document, "Legenda");
        FileInputStream fis;
        try {

            addPNGPicture(document);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public void createDomainModel(XWPFDocument document){

        createChapter(document, "Domeinmodel");
        createDomainModelExplanation(document);
        createSubChapter(document, "Model");
        addPicture(document, "domeinModel");

    }

    public void createWireframes(XWPFDocument document, int documentID){
        createChapter(document, "Wireframes");
        int counter = 1;
        for (String name : Main.con.getWireframesByDocumentID(documentID)){
            createSubChapter(document, name);
            addWireframePicture(document, documentID, counter);
            counter++;
        }
    }
}
