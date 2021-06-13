import java.awt.*;

public class Start {
    public static void main(String[] args) {
        //Die Windows Einstellungen zum GUI Scaling ignorieren
        System.setProperty("sun.java2d.uiScale", "1.0");
        MainMenu mainmenu = new MainMenu();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        //Bildschirmgröße holen
        mainmenu.start(dim.width, dim.height);

    }
}
