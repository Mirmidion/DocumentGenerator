package Code.GUI;

import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class About extends JDialog {
    public About(int nameIndex){

        final float MEDIUM_FONT_SIZE = 13f;
        final float SMALL_FONT_SIZE = 10f;

        ObjectDecorator.decorateJDialog(this);

        setMinimumSize(new Dimension(290, 500));

        JLabel aboutLabel = new JLabel("Over het document:");
        ObjectDecorator.decorateNormalLabel(aboutLabel);
        add(aboutLabel,"wrap");

        JTextArea aboutText = new JTextArea();
        aboutText.setText(Main.con.getAboutDocumentByIndex(nameIndex));
        aboutText.setFont(Main.font.deriveFont(10f));
        aboutText.setForeground(ColorScheme.detailColor);
        aboutText.setMinimumSize(new Dimension(250,125));
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        add(aboutText, "wrap, span 2, align 50% 50%");

        JLabel byLabel = new JLabel("Door:");
        ObjectDecorator.decorateNormalLabel(byLabel);
        add(byLabel, "wrap");

        JTextArea byText = new JTextArea();
        byText.setText(Main.con.getBy(nameIndex));
        byText.setFont(Main.font.deriveFont(10f));
        byText.setForeground(ColorScheme.detailColor);
        byText.setMinimumSize(new Dimension(250,50));
        byText.setWrapStyleWord(true);
        byText.setLineWrap(true);
        add(byText, "wrap, span 2, align 50% 50%");

        Object[][] data = new Object[Main.con.getActors(nameIndex).size()][2];
        int actorCounter = 0;
        for (String[] actor : Main.con.getActors(nameIndex)){
            data[actorCounter][0] = actor[0];
            data[actorCounter][1] = actor[1];
            actorCounter++;
        }

        DefaultTableModel model = new DefaultTableModel(data, new String[]{"Actor:", "Primary:"});
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setMinimumSize(new Dimension(250, 100));

        JScrollPane pane = new JScrollPane();
        pane.setViewportView(table);

        add(pane, "wrap, span 2");
        table.setFont(Main.font.deriveFont(SMALL_FONT_SIZE));
        table.getTableHeader().setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));

        JPanel buttons = new JPanel();
        buttons.setMinimumSize(new Dimension(110, 60));
        buttons.setMaximumSize(new Dimension(110, 60));
        buttons.setLayout(new MigLayout("align 100%"));

        JButton addActor = new JButton("+");
        ObjectDecorator.decorateSmallButton(addActor);
        addActor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = table.getRowCount();
                Object[][] data = new Object[size+1][2];
                for (int i = 0; i < table.getModel().getRowCount(); i++){
                    data[i][0] = table.getModel().getValueAt(i, 0);
                    data[i][1] = table.getModel().getValueAt(i, 1);
                }

                data[size][0] = "edit this";
                data[size][1] = "false";

                table.setModel(new DefaultTableModel(data, new String[]{"Actor:", "Primary:"}));

            }
        });
        buttons.add(addActor, "align 50% 50%");

        JButton removeActor = new JButton("-");
        ObjectDecorator.decorateSmallButton(removeActor);
        removeActor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1){
                    return;
                }
                ((DefaultTableModel) table.getModel()).removeRow(row);
            }
        });
        buttons.add(removeActor);

        add(buttons, "wrap, span 2, align 100%");

        JPanel saveAndExitPanel = new JPanel();
        saveAndExitPanel.setLayout(new MigLayout());

        JButton exit = new JButton("Exit");
        ObjectDecorator.decorateMediumButton(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        saveAndExitPanel.add(exit, "align 100%, span 1");

        JButton save = new JButton("Save");
        ObjectDecorator.decorateMediumButton(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.con.updateAbout(nameIndex, aboutText.getText());
                Main.con.updateBy(nameIndex, byText.getText());
                ArrayList<String> actors = new ArrayList<>();
                ArrayList<Boolean> isPrimary = new ArrayList<>();
                for (int i = 0; i < table.getRowCount(); i++){
                    actors.add(table.getModel().getValueAt(i, 0).toString());
                    String primary = table.getModel().getValueAt(i, 1).toString();
                    if (primary.equals("true")){
                        isPrimary.add(true);
                    }
                    else if (primary.equals("false")){
                        isPrimary.add(false);
                    }
                    else{
                        isPrimary.add(false);
                    }
                }
                Main.con.updateActors(nameIndex, actors, isPrimary);
            }
        });

        saveAndExitPanel.add(save, "wrap, span 1");
        add(saveAndExitPanel, "span 2, align 50% 50%");

        setVisible(true);
    }
}
