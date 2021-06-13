import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Shotgun extends Waffe{
    private final String name = "Shotgun";
    private final int schaden = 10;
    private final int projektilradius = 7;
    private final int projektilgeschwindigkeit = 15;
    private final double projektildistanz = 450;
    private final int projektilanzahl = 20;
    private final double schussrate = 0.8;
    private final double nachladezeit = 0.7;
    private final int magazingröße = 1;
    private int geschosseneKugeln = 0;
    private final double spray = 0.03;
    private boolean laednach = false;

    @Override
    public void shoot(GameController main, GUI gui, Spieler schießenderSpieler){
        if(laednach == false) {
            if (geschosseneKugeln < magazingröße) {
                salveSchiessen(main, gui, schießenderSpieler);
                Timer timer = new Timer();
                timer.schedule(new Delaytask(main, gui, schießenderSpieler, this), 10);
                geschosseneKugeln++;
            }
            if (geschosseneKugeln == magazingröße) {
                laednach = true;
                nachladen(this);

            }
        }
    }

    private void salveSchiessen(GameController main, GUI gui, Spieler schießenderSpieler){
        for (int i = 0; i < projektilanzahl/2; i++) {
            Random rnd = new Random();
            Point2D.Double position = new Point2D.Double();
            position.x = schießenderSpieler.getPosition().getX() + (schießenderSpieler.getBreite() / 2);
            position.y = schießenderSpieler.getPosition().getY() + (schießenderSpieler.getHoehe() / 2);
            double deltaX = (MouseInfo.getPointerInfo().getLocation().x - projektilradius) - position.x;
            double deltaY = (MouseInfo.getPointerInfo().getLocation().y - projektilradius) - position.y;
            double alpha = Math.atan2(deltaY, deltaX);
            Projektil projektil = new Projektil(projektilradius, new Point2D.Double(position.x, position.y), schießenderSpieler.getFarbe(), projektilgeschwindigkeit, schaden, main, schießenderSpieler, gui, false);
            projektil.setAlpha(alpha + (i - projektilanzahl / 2) * spray);
            projektil.setDistanz(projektildistanz);
            main.ProjektilHinzufuegen(projektil);
        }
    }

    @Override
    public void setGeschosseneKugeln(int geschosseneKugeln){ this.geschosseneKugeln = geschosseneKugeln;}

    @Override
    public void setLaednach(boolean laednach){
        this.laednach = laednach;
    }
    @Override
    public boolean getLaednach(){return this.laednach;};
    @Override
    public String getName(){
        return name;
    }

    @Override
    public int getMagazingröße() {
        return this.magazingröße;
    }

    @Override
    public int getGeschosseneKugeln() {
        return this.geschosseneKugeln;
    }

    @Override
    public double getNachladezeit() {
        return this.nachladezeit;
    }

    private class Delaytask extends TimerTask{
        GameController main;
        GUI gui;
        Spieler schießenderSpieler;
        Shotgun shotgun;
        public Delaytask(GameController main, GUI gui, Spieler schießenderSpieler, Shotgun shotgun){
            this.main = main;
            this.gui = gui;
            this.schießenderSpieler = schießenderSpieler;
            this.shotgun = shotgun;
        }
        @Override
        public void run() {
            shotgun.salveSchiessen(main, gui, schießenderSpieler);
        }
    }
}
