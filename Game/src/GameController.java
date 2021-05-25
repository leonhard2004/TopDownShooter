import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameController {
    //Liste mit Kollisionsrechtecken
    private static ArrayList<CollisionBox> CollisionBoxes = new ArrayList<CollisionBox>();
    //Liste mit Kollisionskreisen
    private static ArrayList<CollisionCircle> CollisionCircles = new ArrayList<CollisionCircle>();
    //Liste mit Projektilen
    private static ArrayList<Projektil> Projektile = new ArrayList<Projektil>();
    //Liste mit zu löschenden Projektilen
    private static ArrayList<Projektil> zuLoeschendeProjektile = new ArrayList<Projektil>();
    //Liste mit Spielern
    private static ArrayList<Spieler> SpielerListe = new ArrayList<Spieler>();
    //Liste mit Gegnern
    private static ArrayList<Gegner> Gegner = new ArrayList<Gegner>();
    //Liste mit zu löschenden Gegnern
    private static ArrayList<Gegner> zuLoeschendeGegner = new ArrayList<Gegner>();
    //Liste mit Wänden
    private static ArrayList<Wand> Waende = new ArrayList<Wand>();
    //liste mit WaffenPickups
    private static ArrayList<WaffenPickup> WaffenPickups = new ArrayList<WaffenPickup>();

    private GameController main = this;
    private  GUI gui = new GUI();
    public  void start() {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        //Bildschirmgröße holen
        gui.start(dim.width, dim.height);
        //Spieler erstellen
        InputController spieler1input = new InputController(gui);
        Spieler spieler1 = new Spieler(gui, 1000, 400, 60, 60, new Color(0, 72, 255), spieler1input, main);
        spieler1input.setMeinSpieler(spieler1);
        gui.SpielerHinzufuegen(spieler1);
        CollisionBoxes.add(spieler1.getCollisionBox());
        SpielerListe.add(spieler1);
        //Wände erstellen
        main.WandErstellen(1000, 60, new Color(104, 104, 104), new Point2D.Double(128, 600));
        main.WandErstellen(60, 1000, new Color(104, 104, 104), new Point2D.Double(128, 120));
        //Gegner erstellen
        main.GegnerErstellen(new Point2D.Double(300, 300), 40, 40, 100, new Color(255, 132, 0));
        //Waffenpickups erstellen
        WaffenPickupErstellen(60, 60, new Color(39, 108, 5), new Point2D.Double(300, 500));

    }


    public void FixedUpdate(){
        //Spieler bewegen
        for (int i = 0; i < SpielerListe.size(); i++) {
            SpielerListe.get(i).move();
        }
        //Projektil bewegen
        for (int i = 0; i < Projektile.size(); i++) {
            Projektile.get(i).move();
        }
        //Koliisionen überprüfen
        for (int i = 0; i < CollisionBoxes.size(); i++) {
            for (int j = i+1; j < CollisionBoxes.size(); j++) {
                //System.out.println("i: "+Collider.get(i).getTag());
                //System.out.println("j: "+Collider.get(j).getTag());
                if(CollisionBoxes.get(i).CollidesWith(CollisionBoxes.get(j))) {
                    //System.out.println(CollisionBoxes.get(i).getTag() + " ist mit " + CollisionBoxes.get(j).getTag() + " kollidiert");
                    if (CollisionBoxes.get(i).getSpieler() != null) {
                        CollisionBoxes.get(i).getSpieler().OnCollision(CollisionBoxes.get(j));
                    }
                }
            }
            for (int j = 0; j < CollisionCircles.size(); j++) {
                if(CollisionBoxes.get(i).CollidesWith(CollisionCircles.get(j))){
                    if (CollisionCircles.get(j).getMeinProjektil() != null) {
                        CollisionCircles.get(j).getMeinProjektil().OnCollision(CollisionBoxes.get(i));
                    }
                    if(CollisionBoxes.get(i).getSpieler() != null){
                        CollisionBoxes.get(i).getSpieler().OnCollision(CollisionCircles.get(j));
                    }
                    if(CollisionBoxes.get(i).getmeinGegner() != null){
                        CollisionBoxes.get(i).getmeinGegner().OnCollision(CollisionCircles.get(j));
                    }
                }
            }
        }
        //Projektile löschen
        for (int i = 0; i < zuLoeschendeProjektile.size(); i++) {
            Projektil projektil = zuLoeschendeProjektile.get(i);
            CollisionCircles.remove(projektil.getCollisionCircle());
            gui.getProjektile().remove(projektil);
            Projektile.remove(projektil);
        }
        //Genger löschen
        for (int i = 0; i < zuLoeschendeGegner.size(); i++) {
            Gegner gegner = zuLoeschendeGegner.get(i);
            CollisionBoxes.remove(gegner.getCollisionBox());
            gui.getGegner().remove(gegner);
            Gegner.remove(gegner);
        }

    }
    public void ProjektilHinzufügen(Projektil projektil){
        Projektile.add(projektil);
        CollisionCircles.add(projektil.getCollisionCircle());
        gui.ProjektilHinzufuegen(projektil);
    }
    public void ProjektilLöschen(Projektil projektil){
        zuLoeschendeProjektile.add(projektil);
    }

    public void WandErstellen(int breite, int hoehe, Color farbe, Point2D.Double position){
        Wand wand = new Wand(breite, hoehe, farbe, position);
        Waende.add(wand);
        CollisionBoxes.add(wand.getCollisionBox());
        gui.WandHinzufuegen(wand);
    }
    public void WaffenPickupErstellen(int breite, int hoehe, Color farbe, Point2D.Double position){
        WaffenPickup waffenPickup = new WaffenPickup(breite, hoehe, farbe, position);
        waffenPickup.setrandomWaffe();
        WaffenPickups.add(waffenPickup);
        CollisionBoxes.add(waffenPickup.getCollisionBox());
        gui.WaffenPickupHinzufuegen(waffenPickup);
    }

    public void GegnerErstellen(Point2D.Double position, int breite, int hoehe, int maxLeben, Color farbe){
        Gegner gegner = new Gegner(position, breite, hoehe, farbe, maxLeben, main);
        Gegner.add(gegner);
        CollisionBoxes.add(gegner.getCollisionBox());
        gui.GegnerHinzufuegen(gegner);
    }


    public void GegnerLoeschen(Gegner gegner){
        zuLoeschendeGegner.add(gegner);
    }
    public GUI getGUI(){
        return gui;
    }
}
