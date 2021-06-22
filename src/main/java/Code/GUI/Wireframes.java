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
        for (String wireframeName : Main.con.getWireframes(nameIndex)){
            JPanel wirePanel = new JPanel();
            wirePanel.setLayout(new MigLayout());

            JButton chooseFile = new JButton();
            chooseFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FileChooser file = new FileChooser();
                    String path = file.getPath();
                    Main.con.updateWireframeFilePath(nameIndex, path);
                }
            });


            wireframes.addTab(wireframeName, wirePanel);
        }
        JPanel addNew = new JPanel();
        wireframes.addTab("Add new wireframe", addNew);
    }
}
