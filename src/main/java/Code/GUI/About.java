package Code.GUI;

import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class About extends JDialog {
    public About(int nameIndex){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new MigLayout());
        setMinimumSize(new Dimension(280, 400));

        JLabel aboutLabel = new JLabel("Over het document:");
        aboutLabel.setFont(Main.font.deriveFont(12f));
        aboutLabel.setForeground(ColorScheme.detailColor);
        add(aboutLabel,"wrap");

        JTextArea aboutText = new JTextArea();
        aboutText.setText(Main.con.getAbout(nameIndex));
        aboutText.setFont(Main.font.deriveFont(10f));
        aboutText.setForeground(ColorScheme.detailColor);
        aboutText.setMinimumSize(new Dimension(250,125));
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        add(aboutText, "wrap, span 2");

        JLabel byLabel = new JLabel("Door:");
        byLabel.setFont(Main.font.deriveFont(12f));
        byLabel.setForeground(ColorScheme.detailColor);
        add(byLabel, "wrap");

        JTextArea byText = new JTextArea();
        byText.setText(Main.con.getBy(nameIndex));
        byText.setFont(Main.font.deriveFont(10f));
        byText.setForeground(ColorScheme.detailColor);
        byText.setMinimumSize(new Dimension(250,50));
        byText.setWrapStyleWord(true);
        byText.setLineWrap(true);
        add(byText, "wrap, span 2");

        JButton exit = new JButton("Exit");
        exit.setFont(Main.font.deriveFont(10f));
        exit.setForeground(ColorScheme.detailColor);
        exit.setMinimumSize(new Dimension(121,40));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(exit);

        JButton save = new JButton("Save");
        save.setFont(Main.font.deriveFont(10f));
        save.setForeground(ColorScheme.detailColor);
        save.setMinimumSize(new Dimension(121,40));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.con.updateAbout(nameIndex, aboutText.getText());
                Main.con.updateBy(nameIndex, byText.getText());
            }
        });
        add(save, "wrap");

        setVisible(true);
    }
}
