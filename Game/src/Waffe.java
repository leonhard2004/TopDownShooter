import java.awt.*;
import java.awt.geom.Point2D;

public class Waffe {
    private String name;
    private int schaden = 20;
    private int projektilradius = 20;
    private int projektilgeschwindigkeit = 7;
    private double schussrate = 1;
    private double nachladezeit;
    private Spieler meinSpieler;

    public Waffe(Spieler meinSpieler) {
        this.meinSpieler = meinSpieler;
    }

    public void shoot(GameController main, Point2D.Double position, GUI gui){
        Projektil projektil = new Projektil(projektilradius, new Point2D.Double(position.x,position.y), Color.RED, projektilgeschwindigkeit, schaden, main, meinSpieler, gui, false);
        projektil.setTarget(new Point2D.Double(MouseInfo.getPointerInfo().getLocation().x - projektilradius, MouseInfo.getPointerInfo().getLocation().y - projektilradius));
        main.ProjektilHinzufügen(projektil);
        System.out.println("SHOOT");
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
