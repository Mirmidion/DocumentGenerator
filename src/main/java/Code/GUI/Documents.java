package Code.GUI;

import Code.Database.Connection;
import Code.Main.Main;
import Code.Word.GenerateNew;
import Code.Files.FileChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Documents extends JPanel {

    public static Documents instance;
    private JTabbedPane docList;
    private GenerateNew docGenerator;
    JDialog dia;

    //Document types: 1. "FO", 2. "TO", 3. "Testontwerp"

    public Documents(int projectID){
        docGenerator = new GenerateNew();
        setBackground(ColorScheme.firstBackgroundColor);
        setLayout(new MigLayout("align 50% 50%"));
        docList = new JTabbedPane(JTabbedPane.LEFT);
        docList.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        docList.setPreferredSize(new Dimension(1000,800));
        Main.instance.setMinimumSize(new Dimension(600,300));

        Insets insets = UIManager.getInsets("TabbedPane.contentBorderInsets");
        insets.set(-1,-1,-1,-1);
        UIManager.put("TabbedPane.contentBorderInsets", insets);
        UIManager.put("TabbedPane.selectHighlight", ColorScheme.primaryColor);
        refreshList();
        add(docList);
        instance = this;
    }

    public static void showDocumentsOfProject(int projectID){
        Documents doc = new Documents(projectID);
        Main.instance.switchPanel(Projects.instance, Documents.instance);
    }


    String path = "";
    FileChooser fileChooser;

    public void refreshList(){
        docList.removeAll();
        Connection con = new Connection();
        for (String name : con.getDocuments(Main.currentProjectName)){
            int index = name.indexOf(" - ");
            int nameIndex = Connection.projectDocumentNames.indexOf(name.substring(0, index));

            JPanel docProperties = new JPanel();
            docProperties.setLayout(new MigLayout("wrap 1"));
            docProperties.setBackground(ColorScheme.primaryColor);
            docProperties.setForeground(ColorScheme.detailColor);
            //docProperties.setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
            docProperties.setMinimumSize(new Dimension(1000,800));

            switch (Connection.projectDocumentTypes.get(nameIndex)){
                case "FO":{
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setOpaque(false);
                    buttonPanel.setLayout(new MigLayout("align 50% 50%, wrap 2"));
                    buttonPanel.setMinimumSize(new Dimension(975,700));

                    ArrayList<JButton> allButtons = new ArrayList<>();
                    JButton about = new JButton("Over");
                    about.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            About about = new About(nameIndex);
                        }
                    });
                    allButtons.add(about);

                    JButton requirements = new JButton("Requirements");
                    requirements.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Requirements req = new Requirements();
                        }
                    });
                    allButtons.add(requirements);

                    JButton activityDiagram = new JButton("Activity Diagram");
                    activityDiagram.addActionListener(chooseFileListener("activityDiagram", nameIndex, activityDiagram));
                    allButtons.add(activityDiagram);

                    JButton useCaseDiagram = new JButton("UseCase Diagram");
                    useCaseDiagram.addActionListener(chooseFileListener("useCaseDiagram", nameIndex, useCaseDiagram));
                    allButtons.add(useCaseDiagram);

                    JButton useCaseInfo = new JButton("UseCase Beschrijvingen");
                    useCaseInfo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Beschrijvingen besch = new Beschrijvingen();
                        }
                    });
                    allButtons.add(useCaseInfo);

                    JButton domeinModel = new JButton("Domeinmodel");
                    domeinModel.addActionListener(chooseFileListener("domeinModel", nameIndex, domeinModel));
                    allButtons.add(domeinModel);

                    JButton wireFrames = new JButton("Wireframes");
                    allButtons.add(wireFrames);

                    JButton designKeuzes = new JButton("Designkeuzes");
                    allButtons.add(designKeuzes);

                    for(JButton button : allButtons){
                        button.setBackground(ColorScheme.secondaryColor);
                        button.setForeground(ColorScheme.detailColor);
                        button.setMinimumSize(new Dimension(300,80));
                        //button.setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
                        button.setFont(Main.font.deriveFont(20f));
                        buttonPanel.add(button);
                    }

                    docProperties.add(buttonPanel, "align 50% 50%");

                    break;
                }
                case "TO":{
                    docProperties.add(new JLabel("TO"), "wrap");
                    break;
                }
                case "Testontwerp":{
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setOpaque(false);
                    buttonPanel.setLayout(new MigLayout("align 50% 50%, wrap 2"));
                    buttonPanel.setMinimumSize(new Dimension(975,700));

                    ArrayList<JButton> allButtons = new ArrayList<>();
                    JButton useCaseTest = new JButton("Testen");
                    allButtons.add(useCaseTest);

                    for(JButton button : allButtons){
                        button.setBackground(ColorScheme.secondaryColor);
                        button.setForeground(ColorScheme.detailColor);
                        button.setMinimumSize(new Dimension(300,80));
                        button.setFont(Main.font.deriveFont(20f));
                        //button.setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
                        buttonPanel.add(button);
                    }

                    docProperties.add(buttonPanel, "align 50% 50%");

                    break;
                }
            }




            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new MigLayout("align 100% 100%"));
            buttonPanel.setMinimumSize(new Dimension(900,0));
            buttonPanel.setPreferredSize(new Dimension(975,700));
            buttonPanel.setBackground(ColorScheme.primaryColor);
            buttonPanel.setForeground(ColorScheme.detailColor);

            JButton remove = new JButton();
            remove.setText("Remove");
            remove.setBackground(ColorScheme.secondaryColor);
            remove.setForeground(ColorScheme.detailColor);
            remove.setMinimumSize(new Dimension(100,40));
            //remove.setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
            remove.setFont(Main.font.deriveFont(17f));
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Connection con = new Connection();
                    con.removeDocument(Connection.projectDocumentIDs.get(Connection.projectDocumentNames.indexOf(name.substring(0,name.indexOf(" -")))));
                    refreshList();
                }
            });
            buttonPanel.add(remove);

            JButton generate = new JButton();
            generate.setText("Generate Document");
            generate.setBackground(ColorScheme.secondaryColor);
            generate.setForeground(ColorScheme.detailColor);
            generate.setMinimumSize(new Dimension(200,40));
            //generate.setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
            generate.setFont(Main.font.deriveFont(17f));
            generate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (Connection.projectDocumentTypes.get(nameIndex)){
                        case "FO":{
                            int documentID = Connection.projectDocumentIDs.get(nameIndex);
                            docGenerator.createFO(documentID, name, Main.currentProjectID, "src/main/java/generated/");
                            break;
                        }
                        case "TO":{
                            //docGenerator.createTO();
                            break;
                        }
                        case "Testontwerp":{
                            //docGenerator.createTestOntwerp();
                            break;
                        }
                    }
                }
            });
            buttonPanel.add(generate);
            docProperties.add(buttonPanel);

            docList.addTab(name, docProperties);
        }
        addNewDocument addDoc = new addNewDocument();
        docList.addTab("Add new document", addDoc);
        docList.setBackground(ColorScheme.primaryColor);
        docList.setForeground(ColorScheme.detailColor);

        editProjectWideInfo projectWideInfo = new editProjectWideInfo();
        docList.addTab("General Info", projectWideInfo);


        //docList.setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
        for (int i = 0; i < docList.getTabCount(); i++){
            int a = i;
            docList.setBackgroundAt(i, ColorScheme.secondaryColor);
            docList.setForegroundAt(i, ColorScheme.detailColor);
            docList.setFont(Main.font.deriveFont(15f));

        }
    }

    public ActionListener chooseFileListener(String type, int nameIndex, JButton source){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection con = new Connection();

                if (dia != null){
                    dia.setVisible(false);
                    dia = null;
                }

                dia = new JDialog();
                dia.setLayout(new MigLayout());
                dia.setMinimumSize(new Dimension(280,140));
                dia.setLocation(820,470);
                dia.setTitle("Select your " + source.getText());

                JTextArea filePath = new JTextArea();
                //filePath.setBorder(new CompoundBorder(new LineBorder(ColorScheme.primaryColor, 2), new EmptyBorder(3,3,3,3)));
                filePath.setMinimumSize(new Dimension(250,40));
                filePath.setText(con.getFile(Connection.projectDocumentIDs.get(nameIndex), type));
                filePath.setEditable(false);
                filePath.setFont(Main.font.deriveFont(9f));

                JButton open = new JButton("Choose");
                open.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fileChooser = new FileChooser();
                        path = fileChooser.getPath();
                        filePath.setText(path);
                    }
                });
                open.setBackground(ColorScheme.secondaryColor);
                open.setFont(Main.font.deriveFont(10f));
                open.setForeground(ColorScheme.detailColor);
                open.setMinimumSize(new Dimension(121,40));

                JButton save = new JButton("Save");
                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (fileChooser != null) {
                            if (fileChooser.isImage()) {
                                boolean saved = con.addFile(path, Connection.projectDocumentIDs.get(nameIndex), type);
                                if (saved) {
                                    dia.setVisible(false);
                                } else {
                                    filePath.setText(filePath.getText() + "\nnot possible");
                                }
                            } else if (!filePath.getText().endsWith("not possible")) {
                                filePath.setText(filePath.getText() + "\nnot possible");
                            }
                        }
                    }
                });
                save.setBackground(ColorScheme.secondaryColor);
                save.setFont(Main.font.deriveFont(10f));
                save.setForeground(ColorScheme.detailColor);
                save.setMinimumSize(new Dimension(121,40));

                dia.add(open);
                dia.add(save, "wrap");
                dia.add(filePath, "span 2");
                dia.setVisible(true);
            }
        };
    }

    class editProjectWideInfo extends JPanel{
        public editProjectWideInfo(){
            setBackground(ColorScheme.primaryColor);
            setForeground(ColorScheme.detailColor);
            //setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
            setLayout(new MigLayout("wrap 2, align 50% 50%"));

            JButton useCases = new JButton("Use Cases");
            useCases.setMinimumSize(new Dimension(121,40));
            useCases.setFont(Main.font.deriveFont(20f));
            useCases.setBackground(ColorScheme.secondaryColor);
            useCases.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    UseCases useCaseDialog = new UseCases();
                }
            });
            add(useCases);
        }
    }

    class addNewDocument extends JPanel{
        public addNewDocument(){
            setBackground(ColorScheme.primaryColor);
            setForeground(ColorScheme.detailColor);
            //setBorder(new LineBorder(ColorScheme.secondaryColor, 3));
            setLayout(new MigLayout("wrap 1, align 50% 50%"));

            JLabel textFieldLabel = new JLabel("Name:");
            textFieldLabel.setForeground(ColorScheme.detailColor);
            textFieldLabel.setOpaque(false);
            textFieldLabel.setFont(Main.font.deriveFont(20f));
            textFieldLabel.setAlignmentX(0.5f);

            JTextField name = new JTextField();
            name.setMinimumSize(new Dimension(250, 60));
            name.setFont(Main.font.deriveFont(20f));
            //name.setBorder(new CompoundBorder(new LineBorder(ColorScheme.borderColor, 3), new EmptyBorder(0,10,0,10)));
            name.setBackground(ColorScheme.firstBackgroundColor);
            name.setForeground(ColorScheme.detailColor);

            JComboBox type = new JComboBox(new String[]{"Functioneel Ontwerp", "Technisch Ontwerp", "Testontwerp"});
            type.setMinimumSize(new Dimension(250, 60));
            type.setFont(Main.font.deriveFont(20f));
            //type.setBorder(new CompoundBorder(new LineBorder(ColorScheme.borderColor, 3), new EmptyBorder(0,0,0,0)));
            type.setBackground(ColorScheme.firstBackgroundColor);
            type.setForeground(ColorScheme.detailColor);
            type.setFocusable(false);

            JButton addNewDoc = new JButton("Save");
            addNewDoc.setMinimumSize(new Dimension(250, 60));
            addNewDoc.setFont(Main.font.deriveFont(20f));
            //addNewDoc.setBorder(new CompoundBorder(new LineBorder(ColorScheme.borderColor, 3), new EmptyBorder(0,0,0,0)));
            addNewDoc.setBackground(ColorScheme.firstBackgroundColor);
            addNewDoc.setForeground(ColorScheme.detailColor);
            addNewDoc.setFocusable(false);
            addNewDoc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Connection con = new Connection();
                    con.addDocument(name.getText(), type.getSelectedIndex()+1);
                    Documents.instance.refreshList();
                }
            });

            add(textFieldLabel);
            add(name);
            add(type);
            add(addNewDoc);
        }
    }
}


