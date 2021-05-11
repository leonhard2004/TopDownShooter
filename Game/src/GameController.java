import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameController {
    //Liste mit Spielern
    private ArrayList<Spieler> spieler = new ArrayList<Spieler>();
    //Liste mit Kollisionsrechtecken
    private  ArrayList<CollisionBox> collisionBoxes = new ArrayList<CollisionBox>();
    //Liste mit Kollisionskreisen
    private  ArrayList<CollisionCircle> collisionCircles = new ArrayList<CollisionCircle>();
    //Liste mit Projektilen
    private  ArrayList<Projektil> projektile = new ArrayList<Projektil>();
    private GameController main = this;
    private  GUI gui = new GUI();
    public  void start() {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        //Bildschirmgröße holen
        gui.start(dim.width, dim.height);
        //Spieler erstellen
        InputController spieler1input = new InputController(gui);
        Spieler spieler1 = new Spieler(gui, 1000, 400, 60, 60, new Color(0, 72, 255), spieler1input, main);
        spieler1input.setMeinSpieler(spieler1);
        spieler.add(spieler1);
        gui.SpielerHinzufuegen(spieler1);
        collisionBoxes.add(spieler1.getCollisionBox());

        //Wand erstellen
        Wand wand1 = new Wand(1000, 60, new Color(104, 104, 104), new Point2D.Double(128, 128));
        collisionBoxes.add(wand1.getCollisionBox());
        gui.WandHinzufuegen(wand1);
        //Wand erstellen
        Wand wand2 = new Wand(1000, 60, new Color(104, 104, 104), new Point2D.Double(128, 600));
        collisionBoxes.add(wand2.getCollisionBox());
        gui.WandHinzufuegen(wand2);
        //Wand erstellen
        Wand wand3 = new Wand(60, 1000, new Color(104, 104, 104), new Point2D.Double(128, 120));
        collisionBoxes.add(wand3.getCollisionBox());
        gui.WandHinzufuegen(wand3);
    }

    public void FixedUpdate() {
        //Spieler bewegen
        for (int i = 0; i < spieler.size(); i++) {
            spieler.get(i).move();
        }
        //Projektil bewegen
        for (int i = 0; i < projektile.size(); i++) {
            projektile.get(i).move();
            }
        //Koliisionen überprüfen
        for (int i = 0; i < collisionBoxes.size(); i++) {
            for (int j = i + 1; j < collisionBoxes.size(); j++) {
                //System.out.println("i: "+Collider.get(i).getTag());
                //System.out.println("j: "+Collider.get(j).getTag());
                if (collisionBoxes.get(i).CollidesWith(collisionBoxes.get(j))) {
                    //System.out.println(CollisionBoxes.get(i).getTag() + " ist mit " + CollisionBoxes.get(j).getTag() + " kollidiert");
                    if (collisionBoxes.get(i).getSpieler() != null) {
                        collisionBoxes.get(i).getSpieler().OnCollision(collisionBoxes.get(j));
                    }
                }
            }
            for (int j = 0; j < collisionCircles.size(); j++) {
                if (collisionBoxes.get(i).CollidesWith(collisionCircles.get(j))) {
                    //System.out.println(CollisionBoxes.get(i).getTag() + " ist mit " + CollisionCircles.get(j).getTag() + " kollidiert");
                    if (collisionBoxes.get(i).getSpieler() != null) {
                        collisionBoxes.get(i).getSpieler().OnCollision(collisionCircles.get(j));
                    }
                    if (collisionCircles.get(j).getMeinProjektil() != null) {
                        collisionCircles.get(j).getMeinProjektil().OnCollision(collisionBoxes.get(i));
                    }
                }
            }
        }
    }
    public void FrameUpdate(){
        //Szene malen
        gui.malen();
    }


    public void ProjektilHinzufügen(Projektil projektil){
        projektile.add(projektil);
        collisionCircles.add(projektil.getCollisionCircle());
        gui.ProjektiilHinzufügen(projektil);
    }
    public void Projektillöschen(Projektil projektil){
        collisionCircles.remove(projektil.getCollisionCircle());
        gui.Projektile.remove(projektil);
        projektile.remove(projektil);
    }
    public Point2D.Double MoveInDirection(double distanz,double richtung, double steigung, Point2D.Double startpostion){
        Point2D.Double endposition = new Point2D.Double(0,0);
        double deltaX = Math.sqrt((Math.pow(distanz, 2) / (1+steigung)));
        double deltaY = steigung * deltaX;
        endposition.x = startpostion.x + richtung * deltaX;
        endposition.y = startpostion.y + deltaY;
        return  endposition;
    }
}
