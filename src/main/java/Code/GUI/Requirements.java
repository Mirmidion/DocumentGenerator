package Code.GUI;

import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Requirements extends JDialog {
    JTabbedPane useCases;
    ArrayList<JTable> view;

    public Requirements(){
        final int BUTTON_WIDTH = 121;
        final int BUTTON_HEIGHT = 40;

        final int SMALL_BUTTON_WIDTH = 40;
        final int SMALL_BUTTON_HEIGHT = 40;

        final int LIST_WIDTH = 250;

        final int WINDOW_MINIMUM_WIDTH = 500;
        final int WINDOW_MINIMUM_HEIGHT = 500;

        final float BIG_FONT_SIZE = 25f;
        final float MEDIUM_FONT_SIZE = 13f;
        final float SMALL_FONT_SIZE = 10f;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        view = new ArrayList<>();
        setBackground(ColorScheme.firstBackgroundColor);
        setLayout(new MigLayout("align 50% 50%"));
        setMinimumSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
        setFont(Main.font.deriveFont(SMALL_FONT_SIZE));


        useCases = new JTabbedPane(JTabbedPane.LEFT);
        useCases.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        useCases.setPreferredSize(new Dimension(1000,800));
        useCases.setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));
        //useCases.setMinimumSize(new Dimension(500,500));
        int counter = 1;
        int tableCounter = 0;
        for (String name : Main.con.getUseCasesNames()){
            JPanel properties = new JPanel();
            properties.setLayout(new MigLayout("align 50% 50%"));

            JLabel requirementLabel = new JLabel("Requirement:");
            requirementLabel.setForeground(ColorScheme.detailColor);
            requirementLabel.setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));
            properties.add(requirementLabel, "wrap, span 1");

            JTextArea requirement = new JTextArea();
            requirement.setText(Main.con.getUseCaseRequirement(name));
            requirement.setMinimumSize(new Dimension(LIST_WIDTH, 40));
            requirement.setMaximumSize(new Dimension(LIST_WIDTH,80));
            requirement.setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));
            requirement.setLineWrap(true);
            requirement.setWrapStyleWord(true);
            properties.add(requirement, "wrap, span 2");

            JScrollPane criteriaList = new JScrollPane();
            criteriaList.setMinimumSize(new Dimension(LIST_WIDTH, 80));

            Object[][] data = new Object[Main.con.getUseCaseCriteria(name).size()][1];
            int critCounter = 0;
            for (String crit : Main.con.getUseCaseCriteria(name)){
                data[critCounter][0] = crit;
                critCounter++;
            }

            DefaultTableModel model = new DefaultTableModel(data, new String[]{"Critera:"});
            view.add(new JTable(model));
            view.get(tableCounter).setFont(Main.font.deriveFont(SMALL_FONT_SIZE));
            view.get(tableCounter).getTableHeader().setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));

            criteriaList.setViewportView(view.get(tableCounter));
            properties.add(criteriaList, "wrap, span 2, align 100%");

            JPanel buttons = new JPanel();
            buttons.setMinimumSize(new Dimension(110, 60));
            buttons.setMaximumSize(new Dimension(110, 60));
            buttons.setLayout(new MigLayout("align 100%"));

            JButton addCriteria = new JButton();
            ObjectDecorator.decorateSmallButton(addCriteria);
            addCriteria.setText("+");
            int finalTableCounter2 = tableCounter;
            addCriteria.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int size = view.get(finalTableCounter2).getRowCount();
                    Object[][] data = new Object[size+1][1];
                    for (int i = 0; i < view.get(finalTableCounter2).getModel().getRowCount(); i++){
                        data[i][0] = view.get(finalTableCounter2).getModel().getValueAt(i, 0);
                    }
                    int critCounter = 0;

                    data[size][0] = "edit this";

                    view.get(finalTableCounter2).setModel(new DefaultTableModel(data, new String[]{"Critera:"}));

                }
            });
            buttons.add(addCriteria);

            JButton removeCriteria = new JButton("-");
            ObjectDecorator.decorateSmallButton(removeCriteria);
            int finalTableCounter1 = tableCounter;
            removeCriteria.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = view.get(finalTableCounter1).getSelectedRow();
                    if (row == -1){
                        return;
                    }
                    ((DefaultTableModel) view.get(finalTableCounter1).getModel()).removeRow(row);
                }
            });
            buttons.add(removeCriteria);

            properties.add(buttons, "wrap, span 1, align 100%");

            JButton save = new JButton("Save");
            ObjectDecorator.decorateMediumButton(save);
            int finalTableCounter = tableCounter;
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.con.updateUseCaseRequirement(requirement.getText(), name);
                    ArrayList<String> data = new ArrayList<String>();
                    for (int i = 0; i < view.get(finalTableCounter).getModel().getRowCount(); i++){
                        data.add((String) view.get(finalTableCounter).getModel().getValueAt(i, 0));
                    }
                    Main.con.updateUseCaseCriteria(data, name);
                }
            });
            properties.add(save, "align 50% 50%, span 2");


            useCases.addTab("US" + counter + " " + name, properties);
            counter++;
            tableCounter++;
        }

        add(useCases);
        setVisible(true);
    }
}
