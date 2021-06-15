package Code.GUI;

import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UseCases extends JDialog {
    public static UseCases instance;
    JTable table;
    public UseCases() {
        final int WINDOW_MINIMUM_WIDTH = 350;
        final int WINDOW_MINIMUM_HEIGHT = 500;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        instance = this;
        setLayout(new MigLayout("align 50% 50%"));
        setMinimumSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));
        setBackground(ColorScheme.secondaryColor);
        setTitle("Use Cases");

        refreshList();

        setVisible(true);
    }

    public void removeComponents(Component[] components){
        for (Component component : components){
            if (component instanceof JRootPane){
                removeComponents(((JRootPane) component).getComponents());
            }
            else if (component instanceof JLayeredPane){
                removeComponents(((JLayeredPane) component).getComponents());
            }
            else if (component instanceof JPanel){
                removeComponents(((JPanel) component).getComponents());
            }
            else{
                remove(component);
            }
        }
    }

    public void refreshList(){
        final float FONT_SIZE = 15f;
        final int BUTTON_WIDTH = 121;
        final int BUTTON_HEIGHT = 30;

        removeComponents(getComponents());

        Object[][] data = new Object[Main.con.getUseCasesNames().size()][2];
        int counter = 0;
        for (String useCaseName : Main.con.getUseCasesNames()) {
            data[counter][0] = "US" + (counter+1);
            data[counter][1] = useCaseName;
            counter++;
        }
        table = new JTable(data,new String[]{"Nr.", "Name"} );
        table.setTableHeader(new JTableHeader());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(Main.font.deriveFont(FONT_SIZE));
        table.getColumnModel().getColumn(1).setMinWidth(200);
        add(table, "span, wrap, align 50% 50%");

        JButton up = new JButton("Move up");
        up.setFont(Main.font.deriveFont(FONT_SIZE));
        up.setMinimumSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.con.moveUseCase(table.getSelectedRow() , -1);
                refreshList();
            }
        });
        add(up, "align 50% 50%");

        JButton down = new JButton("Move down");
        down.setFont(Main.font.deriveFont(FONT_SIZE));
        down.setMinimumSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.con.moveUseCase(table.getSelectedRow() , 1);
                refreshList();
            }
        });
        add(down, "wrap, align 50% 50%");

        JButton addButton = new JButton("Add Use Case");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddUseCase addUseCase = new AddUseCase(instance);
            }
        });
        addButton.setFont(Main.font.deriveFont(FONT_SIZE));
        addButton.setMinimumSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
        add(addButton, "span, wrap, align 50% 50%");

        JButton removeButton = new JButton("Remove Use Case");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.con.removeUseCase(table.getSelectedRow());
                refreshList();
            }
        });
        removeButton.setFont(Main.font.deriveFont(FONT_SIZE));
        removeButton.setMinimumSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
        add(removeButton, "span, wrap, align 50% 50%");

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        exitButton.setFont(Main.font.deriveFont(FONT_SIZE));
        add(exitButton, "span, align 50% 50%");

        repaint();
        revalidate();
    }

    class AddUseCase extends JDialog {
        public AddUseCase(UseCases useCases){
            final int WINDOW_MINIMUM_WIDTH = 200;
            final int WINDOW_MINIMUM_HEIGHT = 300;

            setLayout(new MigLayout("wrap 1"));
            setMinimumSize(new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT));

            JTextField field = new JTextField();
            add(field, "span 2, wrap");

            JButton save = new JButton("Save");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.con.addUseCase(field.getText());
                    useCases.refreshList();
                }
            });
            add(save);

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    useCases.refreshList();
                }
            });
            add(cancel);

            setVisible(true);
        }
    }
}