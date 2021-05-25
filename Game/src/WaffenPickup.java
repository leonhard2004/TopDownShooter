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
        collisionBox = new CollisionBox(this.breite, this.hoehe, this.position, "WaffenPickup", null,null, null, this);

    }

    public void setrandomWaffe(){
        Random rnd = new Random();
        int i = rnd.nextInt(2);
        if(i == 0)this.waffe = new Pistole();
        if(i == 1)this.waffe = new Shotgun();

        System.out.println("waffe:"+i);
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
