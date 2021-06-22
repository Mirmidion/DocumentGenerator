package Code.GUI;

import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class Beschrijvingen extends JDialog{
    JTabbedPane useCases;
    int counter;
    final float FONT_SIZE = 15f;
    final int BUTTON_WIDTH = 121;
    final int BUTTON_HEIGHT = 30;
    final int SMALL_BUTTON_WIDTH = 40;
    final int SMALL_BUTTON_HEIGHT = 40;

    public Beschrijvingen(int nameIndex){



        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(ColorScheme.firstBackgroundColor);
        setLayout(new MigLayout("align 50% 50%"));
        setMinimumSize(new Dimension(500,700));
        setFont(Main.font.deriveFont(10f));


        useCases = new JTabbedPane(JTabbedPane.LEFT);
        useCases.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        useCases.setPreferredSize(new Dimension(1000,800));
        useCases.setFont(Main.font.deriveFont(13f));
        //useCases.setMinimumSize(new Dimension(500,500));

        int tableCounter = 0;
        for (String name : Main.con.getUseCasesNames()){
            ArrayList<JTextArea> areas = new ArrayList<>();
            ArrayList<JLabel> actors = new ArrayList<>();
            ArrayList<JCheckBox> checkboxes = new ArrayList<>();
            JPanel properties = new JPanel();
            properties.setLayout(new MigLayout("align 50% 50%"));

            JLabel actorLabel = new JLabel("Actors:");
            actorLabel.setFont(Main.font.deriveFont(FONT_SIZE));
            properties.add(actorLabel,"wrap, align 50% 50%");

            JPanel actorPanel = new JPanel();
            actorPanel.setLayout(new MigLayout("wrap 2"));

            for (String[] actor : Main.con.actorUsedInUseCase(name, nameIndex)){
                boolean addedToUseCase = actor[1].equals("true");
                JLabel actorName = new JLabel(actor[0]);
                actors.add(actorName);

                JCheckBox added = new JCheckBox();
                checkboxes.add(added);
                added.setSelected(addedToUseCase);

                actorPanel.add(actorName);
                actorPanel.add(added);
            }
            JScrollPane actorScrollable = new JScrollPane();
            actorScrollable.setViewportView(actorPanel);
            actorScrollable.setMinimumSize(new Dimension(110,100));
            actorScrollable.setMaximumSize(new Dimension(110,200));
            properties.add(actorScrollable, "wrap, align 50% 50%");

            String[] beschrijving = Main.con.getBeschrijving(name);

            JLabel preLabel = new JLabel("Preconditie:");
            preLabel.setFont(Main.font.deriveFont(FONT_SIZE));
            properties.add(preLabel,"wrap, align 50% 50%");

            JTextArea preconditie = new JTextArea();
            preconditie.setMinimumSize(new Dimension(150,25));
            preconditie.setMaximumSize(new Dimension(150,300));
            preconditie.setLineWrap(true);
            preconditie.setWrapStyleWord(true);
            preconditie.setText(beschrijving[0]);

            JScrollPane prePane = new JScrollPane();
            prePane.setMinimumSize(new Dimension(150,25));
            prePane.setMaximumSize(new Dimension(150,300));
            prePane.setViewportView(preconditie);
            properties.add(prePane, "wrap, align 50% 50%");

            JLabel postLabel = new JLabel("Postconditie:");
            postLabel.setFont(Main.font.deriveFont(FONT_SIZE));
            properties.add(postLabel,"wrap, align 50% 50%");

            JTextArea postconditie = new JTextArea();
            postconditie.setMinimumSize(new Dimension(150,25));
            postconditie.setMaximumSize(new Dimension(150,300));
            postconditie.setLineWrap(true);
            postconditie.setWrapStyleWord(true);
            postconditie.setText(beschrijving[1]);

            JScrollPane postPane = new JScrollPane();
            postPane.setMinimumSize(new Dimension(150,25));
            postPane.setMaximumSize(new Dimension(150,300));
            postPane.setViewportView(postconditie);
            properties.add(postPane, "wrap, align 50% 50%");

            JLabel scenarioLabel = new JLabel("Hoofdscenario:");
            scenarioLabel.setFont(Main.font.deriveFont(FONT_SIZE));
            properties.add(scenarioLabel,"wrap, align 50% 50%");

            JPanel scenario = new JPanel();
            scenario.setLayout(new MigLayout());

            areas = refreshScenario(scenario, name, areas);

            JScrollPane scenarioPane = new JScrollPane();
            scenarioPane.setViewportView(scenario);
            scenarioPane.setMinimumSize(new Dimension(240,100));
            scenarioPane.setMaximumSize(new Dimension(240,300));
            properties.add(scenarioPane, "span, wrap, align 50% 50%");

            JButton addButton = new JButton("+");
            ArrayList<JTextArea> finalAreas = areas;
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.con.addScenarioRow(name);
                    refreshScenario(scenario, name, finalAreas);
                    repaint();
                    revalidate();
                }
            });
            addButton.setFont(Main.font.deriveFont(FONT_SIZE));
            addButton.setMinimumSize(new Dimension(SMALL_BUTTON_WIDTH,SMALL_BUTTON_HEIGHT));
            properties.add(addButton, "span, wrap, align 100%");

            JLabel uitzonderingLabel = new JLabel("Uitzondering:");
            uitzonderingLabel.setFont(Main.font.deriveFont(FONT_SIZE));
            properties.add(uitzonderingLabel,"wrap, align 50% 50%");

            JTextArea exception = new JTextArea();
            exception.setMinimumSize(new Dimension(110,30));
            exception.setMaximumSize(new Dimension(110,300));
            exception.setLineWrap(true);
            exception.setWrapStyleWord(true);
            exception.setText(beschrijving[2]);

            JScrollPane exceptPane = new JScrollPane();
            exceptPane.setMinimumSize(new Dimension(110,30));
            exceptPane.setMaximumSize(new Dimension(110,300));
            exceptPane.setViewportView(exception);
            properties.add(exceptPane, "wrap, align 50% 50%");

            JButton saveButton = new JButton("Save");
            ArrayList<JTextArea> finalAreas1 = areas;
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] beschrijving = new String[3];
                    beschrijving[0] = preconditie.getText();
                    beschrijving[1] = postconditie.getText();
                    beschrijving[2] = exception.getText();
                    Main.con.updateBeschrijving(name, beschrijving);

                    ArrayList<String> actorsToAdd = new ArrayList<>();
                    for (JLabel actor : actors){
                        if (checkboxes.get(actors.indexOf(actor)).isSelected()){
                            actorsToAdd.add(actor.getText());
                        }
                    }
                    Main.con.addActorsUsedInUseCase(actorsToAdd, name, nameIndex);

                    ArrayList<String> rows = new ArrayList<>();
                    for (JTextArea row : finalAreas1){
                        rows.add(row.getText());
                    }
                    Main.con.updateScenarioRows(rows, Main.con.getUseCaseIDByName(name));
                }
            });
            saveButton.setFont(Main.font.deriveFont(FONT_SIZE));
            saveButton.setMinimumSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
            properties.add(saveButton, "span, wrap, align 50% 50%");

            useCases.addTab("US" + counter + " " + name, properties);
            counter++;
            tableCounter++;
        }

        add(useCases);
        setVisible(true);
    }

    public ArrayList<JTextArea> refreshScenario(JPanel scenario, String name, ArrayList<JTextArea> areas){
        scenario.removeAll();
        areas = new ArrayList<>();
        counter = 0;
        for (String row : Main.con.getBeschrijvingScenario(name)) {
            counter++;
            JLabel number = new JLabel(counter + ".");
            scenario.add(number);

            JTextArea rowArea = new JTextArea();
            areas.add(rowArea);

            rowArea.setMinimumSize(new Dimension(150,20));
            rowArea.setText(row);
            scenario.add(rowArea, "align 50% 50%");
            JButton down = new JButton("V");
            down.setFont(Main.font.deriveFont(FONT_SIZE));
            down.setMinimumSize(new Dimension(SMALL_BUTTON_WIDTH,SMALL_BUTTON_HEIGHT));
            ArrayList<JTextArea> finalAreas = areas;
            down.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.con.moveScenarioRow( counter-1, -1, name);
                    refreshScenario(scenario, name, finalAreas);
                }
            });
            scenario.add(down, "wrap, align 50% 50%");
        }
        return areas;
    }

}
