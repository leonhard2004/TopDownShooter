import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Pistole extends Waffe{
    private final String name = "Pistole";
    private final int schaden = 15;
    private final int projektilradius = 13;
    private final int projektilgeschwindigkeit = 20;
    private final double projektildistanz = 1000;
    private final double schussrate = 0.1;
    private final double nachladezeit = 1.4;
    private final int magazingröße = 16;
    private int geschosseneKugeln = 0;
    private final double spray = 0.04;
    private boolean laednach = false;

    @Override
    public void shoot(GameController main, GUI gui, Spieler schießenderSpieler){
        if(laednach == false) {
            if (geschosseneKugeln < magazingröße) {
                Random rnd = new Random();
                Point2D.Double position = new Point2D.Double();
                position.x = schießenderSpieler.getPosition().getX() + (schießenderSpieler.getBreite() / 2);
                position.y = schießenderSpieler.getPosition().getY() + (schießenderSpieler.getHoehe() / 2);
                double versatz = rnd.nextDouble() * spray;
                boolean plus = rnd.nextBoolean();
                if (!plus) versatz = versatz * -1;
                double deltaX = (MouseInfo.getPointerInfo().getLocation().x + gui.getCurserWidth()/2 ) - position.x;
                double deltaY = (MouseInfo.getPointerInfo().getLocation().y + gui.getCurserHeight()/2 ) - position.y;
                double alpha = Math.atan2(deltaY, deltaX);
                alpha += versatz;
                Projektil projektil = new Projektil(projektilradius, new Point2D.Double(position.x, position.y), schießenderSpieler.getFarbe(), projektilgeschwindigkeit, schaden, main, schießenderSpieler, gui, false);
                projektil.setAlpha(alpha);
                projektil.setDistanz(projektildistanz);
                main.ProjektilHinzufuegen(projektil);
                geschosseneKugeln++;
            }
            if (geschosseneKugeln == magazingröße) {
                laednach = true;
                nachladen(this);
            }
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
}
