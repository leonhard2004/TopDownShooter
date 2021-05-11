import java.awt.*;
import java.awt.geom.Point2D;

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

    public Gegner( Point2D.Double position, int breite, int hoehe, Color farbe, int maxLeben, GameController main) {

        this.position = position;
        this.breite = breite;
        this.hoehe = hoehe;
        this.farbe = farbe;
        this.maxLeben = maxLeben;
        this.leben = maxLeben;
        this.main = main;
        this.collisionBox = new CollisionBox(this.breite, this.hoehe, this.position,"Gegner", null, null, this);
    }

    public void OnCollision(CollisionCircle collider){
        if(collider.getTag().equals("Projektil") && collider.getMeinProjektil().isGegner() == false){
            System.out.println("mit Projektil kollidiert");
            leben -= collider.getMeinProjektil().getSchaden();
            main.ProjektilLöschen(collider.getMeinProjektil());
            if(leben <= 0){
                main.GegnerLoeschen(this);
                collider.getMeinProjektil().getMeinSpieler().addPunkte(10);
            }
        }
    }

    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    public void setCollisionBox(CollisionBox collisionBox) {
        this.collisionBox = collisionBox;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    public Point2D.Double getAltePosition() {
        return altePosition;
    }

    public void setAltePosition(Point2D.Double altePosition) {
        this.altePosition = altePosition;
    }

    public int getBreite() {
        return breite;
    }

    public void setBreite(int breite) {
        this.breite = breite;
    }

    public int getHoehe() {
        return hoehe;
    }

    public void setHoehe(int hoehe) {
        this.hoehe = hoehe;
    }

    public Color getFarbe() {
        return farbe;
    }

    public void setFarbe(Color farbe) {
        this.farbe = farbe;
    }

    public int getMaxLeben() {
        return maxLeben;
    }

    public void setMaxLeben(int maxLeben) {
        this.maxLeben = maxLeben;
    }

    public int getLeben() {
        return leben;
    }

    public void setLeben(int leben) {
        this.leben = leben;
    }

    public GameController getMain() {
        return main;
    }

    public void setMain(GameController main) {
        this.main = main;
    }
}
