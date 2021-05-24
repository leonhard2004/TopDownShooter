import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Waffe {
    private String name;
    private int schaden;
    private int projektilradius;
    private int projektilgeschwindigkeit;
    private double schussrate;
    private double nachladezeit;
    private int magazingröße;
    private int spray;

    public void shoot(GameController main, GUI gui, Spieler schießenderSpieler){

    }

    public String getName() {
        return name;
    }

    public int getSchaden() {
        return schaden;
    }

    public int getProjektilradius() {
        return projektilradius;
    }

    public int getProjektilgeschwindigkeit() {
        return projektilgeschwindigkeit;
    }

    public double getSchussrate() {
        return schussrate;
    }

    public double getNachladezeit() {
        return nachladezeit;


}
}


