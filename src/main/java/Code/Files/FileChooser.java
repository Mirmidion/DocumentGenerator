package Code.Files;

import Code.GUI.ColorScheme;
import Code.Main.Main;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;

public class FileChooser {
    static FileChooser instance;
    int returnFile;
    CustomFileChooser fileChooser;

    public FileChooser(){
        instance = this;
    }

    public String getPath(){
        String path = "";
        fileChooser = new CustomFileChooser();
//        changeColor(fileChooser.getComponents());
//        setFileChooserColors(fileChooser, ColorScheme.primaryColor, ColorScheme.detailColor);
        returnFile = fileChooser.showDialog(Main.instance, "Choose");

        if (returnFile == JFileChooser.APPROVE_OPTION){
            path = fileChooser.getSelectedFile().getPath();
            return path;
        }

        return path;
    }

    public String getPathToSave(){
        String path = "";
        if (returnFile == JFileChooser.APPROVE_OPTION){
            path = this.fileChooser.getSelectedFile().getPath();
        }
        return path;
    }

    public void changeColor(Component[] array){
        for (Component component : array){
            component.setBackground(ColorScheme.primaryColor);
        }
    }

    public boolean isImage(){
        if (getPathToSave().endsWith("png") || getPathToSave().endsWith("jpeg")|| getPathToSave().endsWith("jpg")){
            return true;
        }
        return false;
    }

    private static void setColors(Component c, Color fg, Color bg) {
        setColors0(c.getAccessibleContext(), fg, bg);
    }

    private static void setColors0(AccessibleContext ac, Color fg, Color bg) {
        ac.getAccessibleComponent().setForeground(fg);
        ac.getAccessibleComponent().setBackground(bg);
        int n = ac.getAccessibleChildrenCount();
        for (int i=0; i<n; i++) {
            setColors0(ac.getAccessibleChild(i).getAccessibleContext(), fg, bg);
        }
    }

    public void setFileChooserColors(JFileChooser jfc, Color f, Color b){
        jfc.setForeground(f);
        jfc.setBackground(b);


        for (int index1 = 0; index1 < jfc.getAccessibleContext().getAccessibleChildrenCount(); index1++){
            jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleComponent().setForeground(f);
            jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleComponent().setBackground(b);
            for (int index2 = 0; index2 < jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChildrenCount(); index2++){
                jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleComponent().setForeground(f);
                jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleComponent().setBackground(b);
                for (int index3 = 0; index3 < jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChildrenCount(); index3++){
                    jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleComponent().setBackground(b);
                    jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleComponent().setForeground(f);
                    for (int index4 = 0; index4 < jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChildrenCount(); index4++){
                        jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleComponent().setBackground(b);
                        jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleComponent().setForeground(f);
                        for (int index5 = 0; index5 < jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChildrenCount(); index5++){
                            jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleComponent().setBackground(b);
                            jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleComponent().setForeground(f);
                            for (int index6 = 0; index6 < jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChildrenCount(); index6++){
                                jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleComponent().setBackground(b);
                                jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleComponent().setForeground(f);
                                for (int index7 = 0; index7 < jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleChildrenCount(); index7++){
                                    jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleChild(index7).getAccessibleContext().getAccessibleComponent().setBackground(b);
                                    jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleChild(index7).getAccessibleContext().getAccessibleComponent().setForeground(f);
                                    for (int index8 = 0; index8 < jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleChild(index7).getAccessibleContext().getAccessibleChildrenCount(); index8++){
                                        jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleChild(index7).getAccessibleContext().getAccessibleChild(index8).getAccessibleContext().getAccessibleComponent().setBackground(b);
                                        jfc.getAccessibleContext().getAccessibleChild(index1).getAccessibleContext().getAccessibleChild(index2).getAccessibleContext().getAccessibleChild(index3).getAccessibleContext().getAccessibleChild(index4).getAccessibleContext().getAccessibleChild(index5).getAccessibleContext().getAccessibleChild(index6).getAccessibleContext().getAccessibleChild(index7).getAccessibleContext().getAccessibleChild(index8).getAccessibleContext().getAccessibleComponent().setForeground(f);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
