package Code.Main;

import Code.Database.Connection;
import Code.GUI.ColorScheme;
import Code.GUI.Projects;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {

    public static Main instance;
    public static Font font;
    public static String currentProjectName;
    public static int currentProjectID;
    public static Connection con;

    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        }
        catch(Exception e){

        }

        con = new Connection();
        Main frame = new Main();
        Projects window = new Projects();

        frame.add(window);
        frame.setVisible(true);
        frame.repaint();
        frame.revalidate();
    }

    public Main(){
        final int MINIMUM_WINDOW_WIDTH = 300;
        final int MINIMUM_WINDOW_HEIGHT = 200;

        setSize(new Dimension(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width,GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height));
        setBackground(ColorScheme.firstBackgroundColor);
        setForeground(ColorScheme.detailColor);
        setTitle("Document Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
        setMinimumSize(new Dimension(MINIMUM_WINDOW_WIDTH,MINIMUM_WINDOW_HEIGHT));
        instance = this;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/Assets/Fonts/Comfort.ttf"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void switchPanel(JPanel toRemove, JPanel toAdd){
        this.remove(toRemove);
        this.add(toAdd);

        repaint();
        revalidate();
    }

}
