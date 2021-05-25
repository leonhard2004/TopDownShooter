import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;

public class Shotgun extends Waffe{
    private final String name = "Shotgun";
    private final int schaden = 10;
    private final int projektilradius = 10;
    private final int projektilgeschwindigkeit = 30;
    private final double projektildistanz = 350;
    private final int projektilanzahl = 10;
    private final double schussrate = 0.8;
    private final double nachladezeit = 1.6;
    private final int magazingröße = 4;
    private int geschosseneKugeln = 0;
    private final double spray = 0.05;

    @Override
    public void shoot(GameController main, GUI gui, Spieler schießenderSpieler){

        if(geschosseneKugeln < magazingröße) {
            for (int i = 0; i < projektilanzahl; i++) {
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
                projektil.setAlpha(alpha + (i - projektilanzahl / 2) * 0.05);
                projektil.setDistanz(projektildistanz);
                main.ProjektilHinzufuegen(projektil);
            }
            System.out.println("SHOOT");
            geschosseneKugeln++;
        }
        if(geschosseneKugeln == magazingröße){
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
