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
    //Liste mit Spielern
    private static ArrayList<Spieler> SpielerListe = new ArrayList<Spieler>();
    //Liste mit Gegnern
    private static ArrayList<Gegner> Gegner = new ArrayList<Gegner>();
    //Liste mit Wänden
    private static ArrayList<Wand> Waende = new ArrayList<Wand>();
    private static GameController main = new GameController();
    private static GUI gui = new GUI();
    public static void main(String[] args) {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        //Bildschirmgröße holen
        gui.start(dim.width, dim.height);
        //Spieler erstellen
        InputController spieler1input =  new InputController(gui);
        Spieler spieler1 = new Spieler(gui,1000,400,60,60, new Color(0, 72, 255),spieler1input, main);
        spieler1input.setMeinSpieler(spieler1);
        gui.SpielerHinzufuegen(spieler1);
        CollisionBoxes.add(spieler1.getCollisionBox());
        SpielerListe.add(spieler1);

        main.WandErstellen(1000, 60, new Color(104, 104, 104), new Point2D.Double(128,600));
        main.WandErstellen(60, 1000, new Color(104, 104, 104), new Point2D.Double(128,120));

        //Gegner erstellen
        main.GegnerErstellen(new Point2D.Double(300,300), 40, 40, 100, new Color(255, 132, 0));

        //Projektil erstellen
        //Projektil proj1 = new Projektil(30, new Point2D.Double(500, 500), new Color(255, 0, 0), 5, main);
        //Projektile.add(proj1);
        //CollisionCircles.add(proj1.getCollisionCircle());
        //gui.ProjektiilHinzufügen(proj1);
        //proj1 = null;

        /*for (int i = 0; i < Collider.size(); i++) {
            System.out.println(Collider.get(i).getX()+", "+Collider.get(i).getY()+", "+Collider.get(i).getWidth()+", "+Collider.get(i).getHeight()+", "+Collider.get(i).getTag());
        }
        */

        while(true) {
            //Spieler bewegen
            spieler1.move();
            //Projektil bewegen
            for (int i = 0; i < Projektile.size(); i++) {
                Projektile.get(i).move();
            }
            //Koliisionen überprüfen
            for (int i = 0; i < CollisionBoxes.size(); i++) {
                for (int j = i+1; j < CollisionBoxes.size(); j++) {
                    //System.out.println("i: "+Collider.get(i).getTag());
                    //System.out.println("j: "+Collider.get(j).getTag());
                    if(CollisionBoxes.get(i).CollidesWith(CollisionBoxes.get(j))){
                        //System.out.println(CollisionBoxes.get(i).getTag() + " ist mit " + CollisionBoxes.get(j).getTag() + " kollidiert");
                        if(CollisionBoxes.get(i).getSpieler() != null){
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


            //Szene malen
            gui.malen();
        }

    }
    public void ProjektilHinzufügen(Projektil projektil){
        Projektile.add(projektil);
        CollisionCircles.add(projektil.getCollisionCircle());
        gui.ProjektilHinzufügen(projektil);
    }
    public void ProjektilLöschen(Projektil projektil){
        CollisionCircles.remove(projektil.getCollisionCircle());
        gui.getProjektile().remove(projektil);
        Projektile.remove(projektil);
    }

    public void WandErstellen(int breite, int hoehe, Color farbe, Point2D.Double position){
        Wand wand = new Wand(breite, hoehe, farbe, position);
        Waende.add(wand);
        CollisionBoxes.add(wand.getCollisionBox());
        gui.WandHinzufuegen(wand);
    }
    public void GegnerErstellen(Point2D.Double position, int breite, int hoehe, int maxLeben, Color farbe){
        Gegner gegner = new Gegner(position, breite, hoehe, farbe, maxLeben, main);
        Gegner.add(gegner);
        CollisionBoxes.add(gegner.getCollisionBox());
        gui.GegnerHinzufuegen(gegner);
    }

    public void GegnerLoeschen(Gegner gegner){
        CollisionBoxes.remove(gegner.getCollisionBox());
        gui.getGegner().remove(gegner);
        Gegner.remove(gegner);
    }
}
