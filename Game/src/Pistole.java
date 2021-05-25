import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

    @Override
    public void shoot(GameController main, GUI gui, Spieler schießenderSpieler){

        if(geschosseneKugeln < magazingröße) {
            Random rnd = new Random();
            Point2D.Double position = new Point2D.Double();
            position.x = schießenderSpieler.getPosition().getX() + (schießenderSpieler.getBreite() / 2);
            position.y = schießenderSpieler.getPosition().getY() + (schießenderSpieler.getHoehe() / 2);
            double versatz = rnd.nextDouble() * spray;
            boolean plus = rnd.nextBoolean();
            if (!plus) versatz = versatz * -1;
            System.out.println(versatz);
            double deltaX = (MouseInfo.getPointerInfo().getLocation().x - projektilradius) - position.x;
            double deltaY = (MouseInfo.getPointerInfo().getLocation().y - projektilradius) - position.y;
            double alpha = Math.atan2(deltaY, deltaX);
            alpha += versatz;
            Projektil projektil = new Projektil(projektilradius, new Point2D.Double(position.x, position.y), Color.RED, projektilgeschwindigkeit, schaden, main, schießenderSpieler, gui, false);
            projektil.setAlpha(alpha);
            projektil.setDistanz(projektildistanz);
            main.ProjektilHinzufuegen(projektil);
            System.out.println("SHOOT");
            geschosseneKugeln++;
        }
        if(geschosseneKugeln == magazingröße) {
            Timer timer = new Timer();
            NachladeTask task = new NachladeTask();
            task.setWaffe(this);
            timer.schedule(task, (long) nachladezeit * 1000);
        }
    }

    @Override
    public void nachladen(){
        geschosseneKugeln = 0;
    }

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
}
