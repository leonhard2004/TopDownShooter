import java.awt.*;
import java.awt.geom.Point2D;

public class Projektil {
    private double radius;
    private Point2D.Double position;
    private Point2D.Double altePosition = new Point2D.Double(0,0);
    private CollisionCircle collisionCircle;
    private Color farbe;
    private double geschwindigkeit;
    private double schaden;
    private GameController main;
    Point target;
    private Spieler meinSpieler;
    private boolean isGegner;
    private GUI gui;
    double deltaX;
    double deltaY;
    double alpha;


    public Projektil(double radius, Point2D.Double position, Color farbe, double geschwindigkeit,double schaden, GameController main, Spieler spieler, GUI gui, boolean isGegner) {
        this.radius = radius;
        this.position = position;
        this.farbe = farbe;
        this.collisionCircle = new CollisionCircle(position, radius, "Projektil", this);
        this.geschwindigkeit = geschwindigkeit;
        this.schaden = schaden;
        this.main = main;
        this.target = new Point((int)position.x, (int)position.y);
        this.meinSpieler = spieler;
        this.gui = gui;
        this.isGegner = isGegner;

    }
    public boolean isGegner(){
        return isGegner;
    }
    public void setTarget(Point2D.Double moveto){
        this.target.setLocation(moveto.x, moveto.y);
        deltaX = this.target.x - this.position.x;
        deltaY = this.target.y - this.position.y;
        alpha = Math.atan2(deltaY, deltaX);
    }

    public void move(){

        this.altePosition.x = this.position.x;
        this.altePosition.y = this.position.y;

        this.position.x += geschwindigkeit * Math.cos(alpha);
        this.position.y += geschwindigkeit * Math.sin(alpha);
        if(position.x < 0)  main.ProjektilLöschen(this);
        if(position.x + collisionCircle.getWidth() > gui.getResX())main.ProjektilLöschen(this);
        if(position.y < 0)main.ProjektilLöschen(this);
        if(position.y + collisionCircle.getHeight() > gui.getResY())main.ProjektilLöschen(this);
        this.collisionCircle.setPosition(this.position);
    }

    public void OnCollision(CollisionBox collider){
        if(collider.getTag().equals("Wand")){
            main.ProjektilLöschen(this);
        }
    }

    public double getSchaden() {
        return schaden;
    }

    public void setSchaden(double schaden) {
        this.schaden = schaden;
    }

    public Color getFarbe() {
        return farbe;
    }

    public Spieler getMeinSpieler() {
        return meinSpieler;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    public CollisionCircle getCollisionCircle() {
        return collisionCircle;
    }

    public void setCollisionCircle(CollisionCircle collisionCircle) {
        this.collisionCircle = collisionCircle;
    }
}
