import java.awt.*;
import java.awt.geom.Point2D;

public class Wand {
    private int breite;
    private int  hoehe;
    private Color farbe;
    private Point2D.Double position = new Point2D.Double(0,0);
    CollisionBox collisionBox;

    public Wand(int breite, int hoehe, Color farbe, Point2D.Double position) {
        this.breite = breite;
        this.hoehe = hoehe;
        this.farbe = farbe;
        this.position = position;
        collisionBox = new CollisionBox(this.breite, this.hoehe, this.position, "Wand", null,this);

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
