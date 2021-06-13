import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GegnerWaffe{

    private final int schaden = 10;
    private final int projektilradius = 20;
    private final int projektilgeschwindigkeit = 10;
    private final double projektildistanz = 1000;
    private final double nachladezeit = 1;
    private final double schussrate = 0.3;
    private final int magazingröße = 16;
    private int geschosseneKugeln = 0;
    private final double spray = 0.04;
    private boolean laednach = false;





    public void shoot(GameController main, GUI gui, Gegner schießenderGegner, Spieler ziel){

        if(laednach == false) {
            if (geschosseneKugeln < magazingröße) {

                Random rnd = new Random();
                Point2D.Double position = new Point2D.Double();
                position.x = schießenderGegner.getPosition().getX() + (schießenderGegner.getBreite() / 2);
                position.y = schießenderGegner.getPosition().getY() + (schießenderGegner.getHoehe() / 2);
                double versatz = rnd.nextDouble() * spray;
                boolean plus = rnd.nextBoolean();
                if (!plus) versatz = versatz * -1;

                double deltaX = (ziel.getPosition().x + ziel.getBreite()/2) - position.x;
                double deltaY = (ziel.getPosition().y + ziel.getHoehe()/2) - position.y;
                double alpha = Math.atan2(deltaY, deltaX);
                alpha += versatz;
                Projektil projektil = new Projektil(projektilradius, new Point2D.Double(position.x, position.y), Color.RED, projektilgeschwindigkeit, schaden, main, null, gui, true);
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

    public void setGeschosseneKugeln(int geschosseneKugeln) {
        this.geschosseneKugeln = geschosseneKugeln;
    }

    public void setLaednach(boolean laednach) {
        this.laednach = laednach;
    }

    public double getNachladezeit() {
        return nachladezeit;
    }

    public void nachladen(GegnerWaffe waffe){
        Timer timer = new Timer();
        GegnerWaffe.NachladeTask task = new GegnerWaffe.NachladeTask();
        task.setWaffe(waffe);
        timer.schedule(task, (long) (waffe.getNachladezeit() * 1000));
    }


    public double getSchussrate() {
        return schussrate;
    }
    private class NachladeTask extends TimerTask {
        GegnerWaffe waffe;
        public void setWaffe(GegnerWaffe waffe){
            this.waffe = waffe;
        }
        @Override
        public void run() {
            waffe.setGeschosseneKugeln(0);
            waffe.setLaednach(false);
            System.out.println("nachgeladen");
        }

    }

}
