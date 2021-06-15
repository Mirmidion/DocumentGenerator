package Code.Files;

import Code.GUI.ColorScheme;
import Code.Main.Main;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.io.File;

public class CustomFileChooser extends JFileChooser {
    public CustomFileChooser (){
        super();
        initComponent();
    }

    public CustomFileChooser (String currentDirPath){
        super(currentDirPath);
        initComponent();
    }

    public void initComponent(){
        Component c[] = getComponents();
        Icon icon = new ImageIcon("src/main/java/Assets/Icons/folder.png");
//        this.setFileView(new FileView() {
//            @Override
//            public Icon getIcon(File f) {
//                return FileSystemView.getFileSystemView().getSystemIcon(f);
//            }
//        });
        buttonCount = 0;

        try{
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        }
        catch(Exception e){

        }

        customizeUI(c);
    }

    static int buttonCount;

    public void customizeUI(Component[] c){

        Icon[] icons = new Icon[5];
        icons[0] = new ImageIcon("src/main/java/Assets/Icons/up.png");

        for(int i=0;i<c.length;i++){
            c[i].setBackground(ColorScheme.secondaryColor);
            c[i].setFont(Main.font.deriveFont(10f));
            c[i].setForeground(ColorScheme.detailColor);
            if(c[i] instanceof JPanel){
                if(((JPanel)c[i]).getComponentCount() !=0){
                    customizeUI(((JPanel)c[i]).getComponents());
                }
            }
            else if(c[i] instanceof JTextField){
                System.out.println("JTextField");
                ((JTextField) c[i]).setBorder(new CompoundBorder(new LineBorder(ColorScheme.detailColor), new EmptyBorder(5,5,5,5)));
            }
            else if(c[i] instanceof JTable){
                System.out.println("JTable");

                ((JTable) c[i]).setBorder(new CompoundBorder(new LineBorder(ColorScheme.detailColor), new EmptyBorder(5,5,5,5)));
                if(((JTable)c[i]).getComponentCount() !=0){
                    customizeUI(((JTable)c[i]).getComponents());
                }
            }
            else if(c[i] instanceof JToggleButton){
                ((JToggleButton) c[i]).setBorder(new CompoundBorder(new LineBorder(ColorScheme.detailColor), new EmptyBorder(3,3,3,3)));
                if(((JToggleButton)c[i]).getComponentCount() !=0){
                    customizeUI(((JToggleButton)c[i]).getComponents());
                }
                System.out.println("JToggleButton");
            }
            else if(c[i] instanceof JScrollPane){
                if(((JScrollPane)c[i]).getComponentCount() !=0){
                    customizeUI(((JScrollPane)c[i]).getComponents());
                }
                System.out.println("JScrollPane");
            }
            else if(c[i] instanceof JViewport){
                if(((JViewport)c[i]).getComponentCount() !=0){
                    customizeUI(((JViewport)c[i]).getComponents());
                }
                System.out.println("JViewport");
            }
            else if(c[i] instanceof JScrollBar){
                ((JScrollBar) c[i]).setBorder(new LineBorder(ColorScheme.detailColor));
                System.out.println("JScrollBar");
            }
            else if (c[i] instanceof JLabel){
                System.out.println("JLabel");
            }
            else if (c[i] instanceof JButton){
                ((JButton) c[i]).setBorder(new CompoundBorder(new LineBorder(ColorScheme.detailColor), new EmptyBorder(3,3,3,3)));
                if (icons[buttonCount] != null && buttonCount == 2 || buttonCount == 0) {
                    ((JButton) c[i]).setIcon(icons[buttonCount]);
                }
                System.out.println("JButton");
                buttonCount++;
            }
            else if (c[i] instanceof JComboBox){
                System.out.println("JComboBox");
            }
            else if (c[i] instanceof JPopupMenu){
                System.out.println("JPopupMenu");
            }
            else if (c[i] instanceof Box.Filler){
                System.out.println("Filler");
                if(((Box.Filler)c[i]).getComponentCount() !=0){
                    customizeUI(((Box.Filler)c[i]).getComponents());
                }
            }
            else if(c[i] != null){
                System.out.println(c[i].getClass().getName());
            }





        }
    }
    @Override
    protected JDialog createDialog(Component parent) throws HeadlessException {
        FileChooserUI ui = getUI();
        String title = ui.getDialogTitle(this);
        putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY,
                title);

        JDialog dialog;

        if (parent instanceof Frame) {
            dialog = new JDialog((Frame)parent, title, true);
        } else {
            dialog = new JDialog((Dialog)parent, title, true);
        }
        dialog.setComponentOrientation(this.getComponentOrientation());

        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        contentPane.setBackground(ColorScheme.secondaryColor);
        contentPane.setForeground(ColorScheme.detailColor);


        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog;
    }}
