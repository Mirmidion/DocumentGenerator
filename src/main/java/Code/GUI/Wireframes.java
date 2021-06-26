package Code.GUI;

import Code.Files.FileChooser;
import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Wireframes extends JDialog {
    JTabbedPane wireframes;



    final int BUTTON_WIDTH = 121;
    final int BUTTON_HEIGHT = 40;

    final float MEDIUM_FONT_SIZE = 13f;

    public Wireframes(int nameIndex){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(ColorScheme.firstBackgroundColor);
        setLayout(new MigLayout("align 50% 50%"));
        setMinimumSize(new Dimension(500,700));
        setFont(Main.font.deriveFont(10f));

        wireframes = new JTabbedPane(JTabbedPane.LEFT);
        wireframes.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        wireframes.setPreferredSize(new Dimension(1000,800));
        wireframes.setFont(Main.font.deriveFont(13f));

        refreshList(nameIndex);

        setVisible(true);
    }

    public void refreshList(int nameIndex){
        wireframes.removeAll();
        int counter = 1;
        for (String wireframeName : Main.con.getWireframes(nameIndex)){
            JPanel wirePanel = new JPanel();
            wirePanel.setLayout(new MigLayout("align 50% 50%"));

            JLabel filePath = new JLabel(Main.con.getWireframeFilePath(nameIndex, counter));
            ObjectDecorator.decorateNormalLabel(filePath);
            if (filePath.getText() == null){
                filePath.setText("No File Added");
            }
            wirePanel.add(filePath, "wrap");

            JButton chooseFile = new JButton("Add File");
            chooseFile.setLayout(new MigLayout("align 50% 50%"));
            int finalCounter = counter;
            chooseFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FileChooser file = new FileChooser();
                    String path = file.getPath();
                    Main.con.updateWireframeFilePath(nameIndex, path, finalCounter);
                    filePath.setText(Main.con.getWireframeFilePath(nameIndex, finalCounter));
                }
            });
            ObjectDecorator.decorateMediumButton(chooseFile);
            wirePanel.add(chooseFile, "align 50% 50%");


            wireframes.addTab(wireframeName, wirePanel);
            counter++;
        }

        JPanel addNew = new JPanel();
        addNew.setLayout(new MigLayout("wrap 1, align 50% 50%"));

        JTextArea wireframeName = new JTextArea();
        ObjectDecorator.decorateMediumTextArea(wireframeName);
        addNew.add(wireframeName);

        JButton addButton = new JButton("Add");
        addButton.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addButton.setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.con.addWireframe(nameIndex, wireframeName.getText());
                refreshList(nameIndex);
            }
        });
        addNew.add(addButton);

        wireframes.addTab("Add new wireframe", addNew);
        add(wireframes);
    }
}
