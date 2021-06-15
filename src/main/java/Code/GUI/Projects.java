package Code.GUI;

import Code.Database.Connection;
import Code.Main.Main;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Projects extends JPanel {

    public static Projects instance;
    ArrayList<JButton> allButtons;
    JButton addProject;
    JPanel viewport;

    public Projects(){
        setBackground(ColorScheme.firstBackgroundColor);

        LC layout = new LC();
        layout.setFillX(true);
        setLayout(new MigLayout(layout));
        JScrollPane allProjects = getProjects();
        allProjects.setPreferredSize(new Dimension(500,1000));
        allProjects.setBackground(Color.CYAN);
        allProjects.setBorder(new BevelBorder(BevelBorder.LOWERED, ColorScheme.primaryColor, ColorScheme.secondaryColor));

        add(allProjects, "span, align 50% 50%");

        instance = this;
    }

    public JScrollPane getProjects(){
        JScrollPane panel = new JScrollPane();
        panel.getVerticalScrollBar().setBackground(ColorScheme.firstBackgroundColor);
        panel.getVerticalScrollBar().setForeground(ColorScheme.detailColor);

        //panel.add(new JScrollBar());
        allButtons = new ArrayList<>();

        viewport = new JPanel();
        viewport.setLayout(new MigLayout("wrap 1, center center, gapy 20, aligny 50%"));
        for (String name : Connection.projectNames){
            JButton projectButton = new JButton();

            projectButton.setText(name);
            projectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    Main.currentProjectName = projectButton.getText();
                    System.out.println(projectButton.getText());
                    Main.currentProjectID = Connection.projectIDs.get(Connection.projectNames.indexOf(projectButton.getText()));
                    Documents.showDocumentsOfProject(Connection.projectIDs.indexOf(name));
                }
            });
            viewport.add(projectButton);
            allButtons.add(projectButton);
            projectButton.setFont(Main.font.deriveFont(20f));
            projectButton.setPreferredSize(new Dimension(250, 50));
            //projectButton.setBorder(new LineBorder(ColorScheme.detailColor, 2));
            projectButton.setForeground(ColorScheme.detailColor);
            projectButton.setBackground(ColorScheme.secondaryColor);
            projectButton.addMouseListener(new PopClickListener());
            projectButton.setFocusable(false);
        }



        addProject = new JButton();
        addProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProject dialog = new AddProject();
                dialog.setVisible(true);
            }
        });
        addProject.setPreferredSize(new Dimension(250, 50));
        addProject.setText("Add new project");
        addProject.setFont(Main.font.deriveFont(20f));
        addProject.setForeground(ColorScheme.detailColor);
        //addProject.setBorder(new LineBorder(ColorScheme.detailColor, 2));
        addProject.setBackground(ColorScheme.secondaryColor);
        addProject.setFocusable(false);
        viewport.add(addProject);
        viewport.setBackground(ColorScheme.primaryColor);
        panel.setBackground(ColorScheme.primaryColor);
        panel.setViewportView(viewport);
        panel.setBackground(Color.YELLOW);

        return panel;
    }

    public void refreshList(){
        viewport.removeAll();
        allButtons = new ArrayList<>();
        Connection db = new Connection();
        for (String name : db.projectNames){
            if (!name.equals("")) {
                JButton projectButton = new JButton();
                projectButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Main.currentProjectName = projectButton.getText();
                        System.out.println(projectButton.getText());
                        Main.currentProjectID = Connection.projectIDs.get(Connection.projectNames.indexOf(projectButton.getText()));
                        Documents.showDocumentsOfProject(Connection.projectIDs.indexOf(name));
                    }
                });
                projectButton.setText(name);
                viewport.add(projectButton);
                allButtons.add(projectButton);
                projectButton.setFont(Main.font.deriveFont(20f));
                projectButton.setPreferredSize(new Dimension(250, 50));
                projectButton.setBorder(new LineBorder(ColorScheme.detailColor, 2));
                projectButton.setForeground(ColorScheme.detailColor);
                projectButton.setBackground(ColorScheme.primaryColor);
                projectButton.addMouseListener(new PopClickListener());
                projectButton.setFocusable(false);
            }
        }

        viewport.add(addProject);
        repaint();
        revalidate();
    }

    class PopUpMenu extends JPopupMenu{
        public PopUpMenu(JButton source){
            JMenuItem remove = new JMenuItem();
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.con.getProjects();
                    Main.con.removeProject(Connection.projectIDs.get(Connection.projectNames.indexOf(source.getText())));
                    refreshList();
                    repaint();
                    revalidate();
                }
            });
            remove.setText("Remove");
            remove.setFont(Main.font.deriveFont(15f));
            remove.setBackground(ColorScheme.firstBackgroundColor);
            setBackground(ColorScheme.firstBackgroundColor);
            setBorder(new LineBorder(ColorScheme.borderColor, 2));
            remove.setBorder(new EmptyBorder(5,5,5,5));
            remove.setForeground(ColorScheme.detailColor);
            add(remove);

            JMenuItem change = new JMenuItem();
            change.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EditProject changeName = new EditProject(source);
                }
            });
            change.setText("Change Name");
            change.setFont(Main.font.deriveFont(15f));
            change.setBackground(ColorScheme.firstBackgroundColor);
            change.setBorder(new EmptyBorder(5,5,5,5));
            change.setForeground(ColorScheme.detailColor);
            add(change);
        }

        public PopUpMenu(JTabbedPane source){
            JMenuItem remove = new JMenuItem();
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.con.getProjects();
                    refreshList();
                    repaint();
                    revalidate();
                }
            });
            remove.setText("Remove");
            remove.setFont(Main.font.deriveFont(15f));
            remove.setBackground(ColorScheme.firstBackgroundColor);
            setBackground(ColorScheme.firstBackgroundColor);
            setBorder(new LineBorder(ColorScheme.borderColor, 2));
            remove.setBorder(new EmptyBorder(5,5,5,5));
            remove.setForeground(ColorScheme.detailColor);
            add(remove);

            JMenuItem change = new JMenuItem();
            change.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            });
            change.setText("Change Name");
            change.setFont(Main.font.deriveFont(15f));
            change.setBackground(ColorScheme.firstBackgroundColor);
            change.setBorder(new EmptyBorder(5,5,5,5));
            change.setForeground(ColorScheme.detailColor);
            add(change);
        }
    }

    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            PopUpMenu menu = new PopUpMenu((JButton) e.getSource());
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    class AddProject extends JDialog{
        public AddProject(){
            this.setPreferredSize(new Dimension(280,160));
            this.setMinimumSize(new Dimension(280,160));
            this.setLayout(new MigLayout());
            this.setTitle("Add new project");
            this.setLocation(820, 460);
            this.getContentPane().setBackground(ColorScheme.primaryColor);
            this.getContentPane().setForeground(ColorScheme.detailColor);

            JTextField name = new JTextField();
            name.setMinimumSize(new Dimension(250,60));
            name.setBorder(new CompoundBorder(new LineBorder(ColorScheme.detailColor, 2),new EmptyBorder(0,10,0,10)));

            JButton add = new JButton("Add");
            add.setMinimumSize(new Dimension(121, 40));

            JButton cancel = new JButton("Cancel");
            cancel.setMinimumSize(new Dimension(121, 40));

            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!name.getText().equals("")) {
                        Connection con = new Connection();
                        Main.con.addProject(name.getText());
                        Projects.instance.refreshList();
                        setVisible(false);
                    }
                }
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });

            name.setFont(Main.font.deriveFont(25f));
            add.setFont(Main.font.deriveFont(15f));
            cancel.setFont(Main.font.deriveFont(15f));

            name.setForeground(ColorScheme.detailColor);
            add.setForeground(ColorScheme.detailColor);
            cancel.setForeground(ColorScheme.detailColor);

            name.setBackground(ColorScheme.primaryColor);
            add.setBackground(ColorScheme.primaryColor);
            cancel.setBackground(ColorScheme.primaryColor);

            add(name, "wrap, span 2");
            add(cancel);
            add(add);

            setVisible(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }

    class EditProject extends JDialog{
        public EditProject(JButton source){
            this.setPreferredSize(new Dimension(280,160));
            this.setMinimumSize(new Dimension(280,160));
            this.setLayout(new MigLayout());
            this.setTitle("Edit name of project");
            this.setLocation(820, 460);
            this.getContentPane().setBackground(ColorScheme.primaryColor);
            this.getContentPane().setForeground(ColorScheme.detailColor);

            JTextField name = new JTextField();
            name.setMinimumSize(new Dimension(250,60));
            name.setBorder(new CompoundBorder(new LineBorder(ColorScheme.detailColor, 2),new EmptyBorder(0,10,0,10)));

            JButton add = new JButton("Change");
            add.setMinimumSize(new Dimension(121, 40));

            JButton cancel = new JButton("Cancel");
            cancel.setMinimumSize(new Dimension(121, 40));

            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!name.getText().equals("")) {
                        Connection con = new Connection();
                        Main.con.changeProjectName(name.getText(), source.getText());
                        Projects.instance.refreshList();
                        setVisible(false);
                    }
                }
            });
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });

            name.setFont(Main.font.deriveFont(25f));
            add.setFont(Main.font.deriveFont(15f));
            cancel.setFont(Main.font.deriveFont(15f));

            name.setForeground(ColorScheme.detailColor);
            add.setForeground(ColorScheme.detailColor);
            cancel.setForeground(ColorScheme.detailColor);

            name.setBackground(ColorScheme.primaryColor);
            add.setBackground(ColorScheme.primaryColor);
            cancel.setBackground(ColorScheme.primaryColor);

            add(name, "wrap, span 2");
            add(cancel);
            add(add);

            setVisible(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }
}

