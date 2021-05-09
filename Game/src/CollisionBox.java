import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class CollisionBox extends Rectangle2D {
    private String tag;
    private int breite;
    private int  hoehe;
    private Point2D.Double position;
    private Spieler meinSpieler;
    private Wand meineWand;

    public CollisionBox(int breite, int hoehe, Point2D.Double position, String tag, Spieler spieler, Wand wand) {
        this.breite = breite;
        this.hoehe = hoehe;
        this.position = position;
        this.tag = tag;
        this.meinSpieler = spieler;
        this.meineWand = wand;
    }

    public boolean CollidesWith(CollisionBox collider){
        boolean collides = false;
        //if-Abfrage ob Ecken vom Collider in der CollisionBox sind
        if(this.getX() + this.getWidth() > collider.getX() && this.getY()+this.getHeight() > collider.getY() && collider.getX()+collider.getWidth() > this.getX() && collider.getY()+ collider.getHeight() > this.getY()){
            collides = true;
        }
        return collides;
    }
    public boolean CollidesWith(CollisionCircle collider){
        boolean collides = false;
        //if-Abfrage ob Ecken vom Collider in der CollisionBox sind oder ob Ecken von  der CollisionBox im Collider sind
        if(Point2D.distance(position.x, position.y, collider.getCenterX(), collider.getCenterY()) < collider.getRadius() || Point2D.distance(position.x, position.y + hoehe, collider.getCenterX(), collider.getCenterY()) < collider.getRadius() ||
           Point2D.distance(position.x + breite, position.y, collider.getCenterX(), collider.getCenterY()) < collider.getRadius() || Point2D.distance(position.x + breite, position.y + hoehe, collider.getCenterX(), collider.getCenterY()) < collider.getRadius() ||
                (position.x < collider.getCenterX() + collider.getRadius() && collider.getCenterX() + collider.getRadius() < position.x + breite && position.y < collider.getCenterY() && collider.getCenterY() < position.y + hoehe) ||
                (position.x < collider.getCenterX() && collider.getCenterX() < position.x + breite && position.y < collider.getCenterY() - collider.getRadius() && collider.getCenterY() - collider.getRadius() < position.y + hoehe) ||
                (position.x < collider.getCenterX() - collider.getRadius() && collider.getCenterX() - collider.getRadius() < position.x + breite && position.y < collider.getCenterY() && collider.getCenterY() < position.y + hoehe) ||
                (position.x < collider.getCenterX() && collider.getCenterX() < position.x + breite && position.y < collider.getCenterY() + collider.getRadius() && collider.getCenterY() + collider.getRadius() < position.y + hoehe)) {
            collides = true;
        }
        return collides;
    }



    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPosition(Point2D.Double position){
        this.position = position;
    }
    public Spieler getSpieler() {
        return meinSpieler;
    }
    public Wand getMeineWand(){
        return  meineWand;
    }

    @Override
    public void setRect(double x, double y, double w, double h) {

    }

    @Override
    public int outcode(double x, double y) {
        return 0;
    }

    @Override
    public Rectangle2D createIntersection(Rectangle2D r) {
        return null;
    }

    @Override
    public Rectangle2D createUnion(Rectangle2D r) {
        return null;
    }

    @Override
    public double getX() {
        return this.position.x;
    }

    @Override
    public double getY() {
        return this.position.y;
    }

    @Override
    public double getWidth() {
        return this.breite;
    }

    @Override
    public double getHeight() {
        return this.hoehe;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


}
