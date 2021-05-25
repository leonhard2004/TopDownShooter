import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class WaffenPickup {
    private int breite;
    private int  hoehe;
    private Color farbe;
    private Point2D.Double position;
    CollisionBox collisionBox;
    private Waffe waffe;

    public WaffenPickup(int breite, int hoehe, Color farbe, Point2D.Double position) {
        this.breite = breite;
        this.hoehe = hoehe;
        this.farbe = farbe;
        this.position = position;
        collisionBox = new CollisionBox(this.breite, this.hoehe, this.position, "WaffenPickup", null,null, null);

    }

    public void setrandomWaffe(){
        Random rnd = new Random();
        int i = rnd.nextInt(2) + 1;
        switch (i){
            case 1 : this.waffe = new Pistole();
            case 2 : this.waffe = new Shotgun();
        }
    }

    public void setWaffe(Waffe waffe){
        this.waffe = waffe;
    }

    public Waffe getWaffe(){
        return waffe;
    }

    public CollisionBox getCollisionBox() {
        return collisionBox;
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

    public Point2D.Double getPosition() {
        return position;
    }

}
