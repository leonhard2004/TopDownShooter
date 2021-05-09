import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Spieler {

    private int breite;
    private int  hoehe;
    private Color farbe;
    private double geschwindigkeit = 3.5;
    private double maxLeben = 100;
    private double leben = maxLeben;
    private InputController meinInputController;
    private Point2D.Double position = new Point2D.Double(0,0);
    private CollisionBox collisionBox;
    private Point2D.Double altePosition = new Point2D.Double(0,0);
    private GUI gui;
    private GameController main;



    public Spieler(GUI gui, int posX, int posY, int breite, int hoehe, Color farbe, InputController input, GameController main) {
        this.gui = gui;
        this.position.x = posX;
        this.position.y = posY;
        this.breite = breite;
        this.hoehe = hoehe;
        this.farbe = farbe;
        this.meinInputController = input;
        this.collisionBox = new CollisionBox(this.breite, this.hoehe, this.position,"Spieler", this, null);
        this.main = main;
    }
    public void move(){
        double dirx = meinInputController.getxAxis();
        double diry = meinInputController.getyAxis();
        if (dirx != 0 && diry != 0){
            dirx *= Math.sqrt(0.5);
            diry *= Math.sqrt(0.5);
        }
        altePosition.x = position.x;
        altePosition.y = position.y;
        position.x = position.x + dirx * geschwindigkeit;
        position.y = position.y + diry * geschwindigkeit;
        if(position.x < 0)  position.x = 0;
        if(position.x + breite > gui.getResX()) position.x = gui.getResX() - breite;
        if(position.y < 0)  position.y = 0;
        if(position.y + hoehe > gui.getResY())  position.y = gui.getResY() - hoehe;

        collisionBox.setPosition(position);
    }

    public void shoot(){
        Projektil projektil = new Projektil(20, new Point2D.Double(position.x,position.y), Color.RED, 10, main, this, gui);
        projektil.setTarget(MouseInfo.getPointerInfo().getLocation());
        main.ProjektilHinzufügen(projektil);
        System.out.println("SHOOT");
    }

    public void OnCollision(CollisionBox collider){
        if(collider.getTag().equals("Wand")){
            System.out.println("mit Wand kollidiert");
            //wenn auf der yAchse eine Kollision besteht, nicht auf der yAchse bewegen
            collisionBox.setPosition(new Point2D.Double(altePosition.x, position.y));
            if (collisionBox.CollidesWith(collider))position.y = altePosition.y;
            //wenn auf der xAchse eine Kollision beseteht, nicht auf der xAchse bewegen
            collisionBox.setPosition(new Point2D.Double(position.x, altePosition.y));
            if(collisionBox.CollidesWith(collider))position.x = altePosition.x;
            //die Collisionbox wieder auf die Position des Spielers zurücksetzen
            collisionBox.setPosition(position);
        }
    }
    public void OnCollision(CollisionCircle collider){
        if(collider.getTag().equals("Projektil") && collider.getMeinProjektil().getMeinSpieler() != this){
            System.out.println("mit Projektil kollidiert");
            leben -= 5;
            main.Projektillöschen(collider.getMeinProjektil());
        }
    }

    public double getLeben() {
        return leben;
    }

    public Point2D.Double getPosition() {
        return position;
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

}