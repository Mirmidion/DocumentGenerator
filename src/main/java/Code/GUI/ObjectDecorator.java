package Code.GUI;

import Code.Main.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ObjectDecorator {

    private final static float BIG_FONT_SIZE = 25f;
    private final static float SEMI_BIG_FONT_SIZE = 20f;
    private final static float MEDIUM_FONT_SIZE = 13f;
    private final static float SMALL_FONT_SIZE = 10f;

    private final static int SMALL_BUTTON_WIDTH = 40;
    private final static int SMALL_BUTTON_HEIGHT = 40;

    private final static int MEDIUM_BUTTON_WIDTH = 121;
    private final static int MEDIUM_BUTTON_HEIGHT = 40;

    private final static int BIG_BUTTON_WIDTH = 300;
    private final static int BIG_BUTTON_HEIGHT = 80;

    private final static int TEXTAREA_WIDTH = 121;
    private final static int TEXTAREA_HEIGHT = 40;

    private static final int DISPOSE_ON_CLOSE = 2;


    public static void decorateNormalLabel(JLabel label){
        changeObjectColor(label);
        label.setOpaque(false);
        label.setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));
    }

    public static void decorateSmallButton(JButton button){
        button.setMinimumSize(new Dimension(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT));
        button.setFont(Main.font.deriveFont(BIG_FONT_SIZE));
        button.setForeground(ColorScheme.detailColor);
    }

    public static void decorateSmallButton(JButton button, String text){
        decorateSmallButton(button);
        button.setText(text);
    }

    public static void decorateBigButton(JButton button){
        button.setMinimumSize(new Dimension(BIG_BUTTON_WIDTH, BIG_BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(BIG_BUTTON_WIDTH, BIG_BUTTON_HEIGHT));
        button.setFont(Main.font.deriveFont(SEMI_BIG_FONT_SIZE));
        changeObjectColor(button);
    }

    public static void decorateMediumButton(JButton button){
        button.setMinimumSize(new Dimension(MEDIUM_BUTTON_WIDTH, MEDIUM_BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(MEDIUM_BUTTON_WIDTH, MEDIUM_BUTTON_HEIGHT));
        button.setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));
        button.setForeground(ColorScheme.detailColor);
    }

    public static void decorateJDialog(JDialog dialog){
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        changeObjectColor(dialog);
        dialog.setLayout(new MigLayout("align 50% 50%"));
        dialog.setFont(Main.font.deriveFont(10f));
    }

    public static void changeObjectColor(Component component){
        component.setBackground(ColorScheme.secondaryColor);
        component.setForeground(ColorScheme.detailColor);
    }

    public static void decorateMediumTextArea(JTextArea textArea){
        textArea.setMinimumSize(new Dimension(TEXTAREA_WIDTH, TEXTAREA_HEIGHT));
        textArea.setMaximumSize(new Dimension(TEXTAREA_WIDTH, TEXTAREA_HEIGHT));
        textArea.setFont(Main.font.deriveFont(MEDIUM_FONT_SIZE));
    }
}
