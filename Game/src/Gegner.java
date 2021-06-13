import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

public class Gegner {
    private CollisionBox collisionBox;
    private Point2D.Double position;
    private Point2D.Double altePosition;
    private int breite;
    private int hoehe;
    private Color farbe;
    private int maxLeben;
    private int leben = maxLeben;
    private GameController main;
    private GegnerWaffe meineWaffe;
    private Spieler spieler;
    private boolean amLeben = true;
    private Timer timer;
    private boolean kannSchiessen = true;

    public Gegner( Point2D.Double position, int breite, int hoehe, Color farbe, int maxLeben, GameController main, Spieler spieler) {

        this.position = position;
        this.breite = breite;
        this.hoehe = hoehe;
        this.farbe = farbe;
        this.maxLeben = maxLeben;
        this.leben = maxLeben;
        this.main = main;
        this.collisionBox = new CollisionBox(this.breite, this.hoehe, this.position,"Gegner", null, null, this, null);
        this.spieler = spieler;
        meineWaffe = new GegnerWaffe();
        }

    public void shoot(){
        if(kannSchiessen == true) {
            timer = new Timer();
            Gegner.ShootTask task = new Gegner.ShootTask();
            task.setGegner(this);
            meineWaffe.shoot(main, main.getGUI(), this, spieler);
            kannSchiessen = false;
            timer.schedule(task, (long) (meineWaffe.getSchussrate() * 1000));
        }

    }


    public void OnCollision(CollisionCircle collider){
        if(collider.getTag().equals("Projektil") && collider.getMeinProjektil().isGegner() == false){
            leben -= collider.getMeinProjektil().getSchaden();
            main.ProjektilLoeschen(collider.getMeinProjektil());
            if(leben <= 0 && this.amLeben == true){
                this.amLeben = false;
                timer.cancel();
                main.GegnerLoeschen(this);
                collider.getMeinProjektil().getMeinSpieler().addPunkte(10);
            }
        }
    }
    public class ShootTask extends TimerTask {
        Gegner gegner;
        public void setGegner(Gegner gegner){this.gegner = gegner;}

        @Override
        public void run() {
            gegner.setKannSchiessen(true);
        }


    }

    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    public void setKannSchiessen(boolean kannSchiessen) {
        this.kannSchiessen = kannSchiessen;
    }

    public boolean kannSchiessen() {
        return kannSchiessen;
    }

    public Point2D.Double getPosition() {
        return position;
    }


    public int getBreite() {
        return breite;
    }


    public int getHoehe() {
        return hoehe;
    }


    public Color getFarbe() {
        return farbe;
    }


    public int getMaxLeben() {
        return maxLeben;
    }


    public int getLeben() {
        return leben;
    }

}
