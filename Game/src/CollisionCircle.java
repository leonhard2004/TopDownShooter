import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class CollisionCircle extends Ellipse2D {
    private Point2D.Double position;
    private double radius;
    private Projektil meinProjektil;
    private String tag;

    public CollisionCircle(Point2D.Double position, double radius,String tag, Projektil meinProjektil) {
        this.position = position;
        this.radius = radius;
        this.tag = tag;
        this.meinProjektil = meinProjektil;
    }

    public String getTag() {
        return tag;
    }
    public double getRadius(){
        return radius;
    }

    public Projektil getMeinProjektil() {
        return meinProjektil;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    @Override
    public double getX() {
        return position.getX();
    }
    @Override
    public double getY() {
        return position.getY();
    }
    @Override
    public double getWidth() {
        return radius;
    }
    @Override
    public double getHeight() {
        return radius;
    }

    @Override
    public double getCenterX(){
        double centerX = position.x + radius;
        return centerX;
    }
    @Override
    public double getCenterY(){
        double centerY = position.y + radius;
        return centerY;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
    @Override
    public void setFrame(double x, double y, double w, double h) {

    }
    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }
}
