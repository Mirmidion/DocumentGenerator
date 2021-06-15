package Code.GUI;

import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class Beschrijvingen extends JDialog{
    JTabbedPane useCases;
    public Beschrijvingen(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(ColorScheme.firstBackgroundColor);
        setLayout(new MigLayout("align 50% 50%"));
        setMinimumSize(new Dimension(500,500));
        setFont(Main.font.deriveFont(10f));


        useCases = new JTabbedPane(JTabbedPane.LEFT);
        useCases.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        useCases.setPreferredSize(new Dimension(1000,800));
        useCases.setFont(Main.font.deriveFont(13f));
        //useCases.setMinimumSize(new Dimension(500,500));
        int counter = 1;
        int tableCounter = 0;
        for (String name : Main.con.getUseCasesNames()){
            JPanel properties = new JPanel();
            properties.setLayout(new MigLayout("align 50% 50%"));

            useCases.addTab("US" + counter + " " + name, properties);
            counter++;
            tableCounter++;
        }

        add(useCases);
        setVisible(true);
    }
}
