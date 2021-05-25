import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.TimerTask;

public class Waffe {
    private String name;
    private int schaden;
    private int projektilradius;
    private int projektilgeschwindigkeit;
    private double schussrate;
    private double nachladezeit;
    private int magazingröße;
    private int geschosseneKugeln;
    private int spray;

    public void shoot(GameController main, GUI gui, Spieler schießenderSpieler){}
    public void nachladen(){}



    public String getName() {
        return name;
    }

    public int getMagazingröße(){
        return magazingröße;
    }

    public int getGeschosseneKugeln(){
        return geschosseneKugeln;
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

    public class NachladeTask extends TimerTask {
        Waffe waffe;
        public void setWaffe(Waffe waffe){
            this.waffe = waffe;
        }
        @Override
        public void run() {
            waffe.nachladen();
        }

    }
}


