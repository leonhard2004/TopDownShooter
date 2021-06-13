import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;

public class GameController {
    MainMenu mainMenu;
    public GameController(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    //Liste mit Kollisionsrechtecken
    private static final ArrayList<CollisionBox> CollisionBoxes = new ArrayList<>();
    //Liste mit Kollisionskreisen
    private static final ArrayList<CollisionCircle> CollisionCircles = new ArrayList<>();
    //Liste mit Projektilen
    private static final ArrayList<Projektil> Projektile = new ArrayList<>();
    //Liste mit zu löschenden Projektilen
    private static final ArrayList<Projektil> zuLoeschendeProjektile = new ArrayList<>();
    //Liste mit Spielern
    private static final ArrayList<Spieler> SpielerListe = new ArrayList<>();
    //Liste mit Gegnern
    private static final ArrayList<Gegner> Gegner = new ArrayList<>();
    //Liste mit zu löschenden Gegnern
    private static final ArrayList<Gegner> zuLoeschendeGegner = new ArrayList<>();
    //Liste mit Wänden
    private static final ArrayList<Wand> Waende = new ArrayList<>();
    //liste mit WaffenPickups
    private static final ArrayList<WaffenPickup> WaffenPickups = new ArrayList<>();
    //Liste mit zu löschenden WaffenPickups
    private static final ArrayList<WaffenPickup> zuLoeschendeWaffenPickups = new ArrayList<>();

    private int highscore;

    private final GameController main = this;
    private final GUI gui = new GUI();
    private Update update;
    public  void start(Update update) {
        this.update = update;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        //Bildschirmgröße holen
        gui.start(dim.width, dim.height);
        highscore = mainMenu.getHighscore();
        System.out.println("highscore: "+highscore);
        lvl1(0, -1);
    }
    InputController spieler1input = new InputController(gui);
    public void lvl1(int punkte, int leben){
        //Spieler erstellen

        Spieler spieler1 = new Spieler(gui, 910, 800, 60, 60, new Color(0, 72, 255), spieler1input, main);
        spieler1.setPunkte(punkte);
        if(leben == -1)leben = (int) spieler1.getMaxLeben();
        spieler1.setLeben(leben);
        spieler1input.setMeinSpieler(spieler1);
        gui.SpielerHinzufuegen(spieler1);
        CollisionBoxes.add(spieler1.getCollisionBox());
        SpielerListe.add(spieler1);


        //Wände erstellen
        main.WandErstellen(600, 60, new Color(104, 104, 104), new Point2D.Double(0, 700));
        main.WandErstellen(600, 60, new Color(104, 104, 104), new Point2D.Double(1320, 700));
        main.WandErstellen(500, 60, new Color(104, 104, 104), new Point2D.Double(0, 200));
        main.WandErstellen(500, 60, new Color(104, 104, 104), new Point2D.Double(1420, 200));
        main.WandErstellen(150, 60, new Color(104, 104, 104), new Point2D.Double(860, 350));

        //Gegner erstellen
        main.GegnerErstellen(new Point2D.Double(900, 200), 80, 80, 400, new Color(255, 132, 0));
        main.GegnerErstellen(new Point2D.Double(500, 500), 60, 60, 100, new Color(255, 132, 0));
        main.GegnerErstellen(new Point2D.Double(1400, 500), 60, 60, 100, new Color(255, 132, 0));
        //Waffenpickups erstellen
        WaffenPickupErstellen(60, 60, new Color(39, 108, 5), new Point2D.Double(900, 550), new Shotgun());
    }

    public void FixedUpdate(){

        //Spieler bewegen
        for (int i = 0; i < SpielerListe.size(); i++) {
            SpielerListe.get(i).move();
        }
        //Gegner schießen
        for (int i = 0; i < Gegner.size(); i++) {
            Gegner.get(i).shoot();
        }
        //Projektil bewegen
        for (int i = 0; i < Projektile.size(); i++) {
            Projektile.get(i).move();
        }
        //Koliisionen überprüfen
        for (int i = 0; i < CollisionBoxes.size(); i++) {
            for (int j = i+1; j < CollisionBoxes.size(); j++) {
                if(CollisionBoxes.get(i).CollidesWith(CollisionBoxes.get(j))) {

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
                    if(!CollisionBoxes.isEmpty()){
                        if(CollisionBoxes.get(i).getmeinGegner() != null){
                            CollisionBoxes.get(i).getmeinGegner().OnCollision(CollisionCircles.get(j));
                        }
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
        //WaffenpickupsLöschen
        for (int i = 0; i < zuLoeschendeWaffenPickups.size(); i++) {
            WaffenPickup waffenPickup = zuLoeschendeWaffenPickups.get(i);
            CollisionBoxes.remove(waffenPickup.getCollisionBox());
            gui.getWaffenPickups().remove(waffenPickup);
            WaffenPickups.remove(waffenPickup);
        }
        //Wenn Level gecleared ist
        if(Gegner.isEmpty()){
            int punkte = SpielerListe.get(0).getPunkte();
            int leben = (int) SpielerListe.get(0).getLeben();
            spielBeenden();
            lvl1(punkte, leben);

        }

    }

    public void spielPausieren(){
        synchronized (update){
            System.out.println("PAUSIERT");
            update.pausieren();
        }

        int auswahl = JOptionPane.showOptionDialog(gui.getFrame(), "Spiel ist pausiert", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Spiel fortsetzen", "Zurück zum Menü", "Spiel schließen"},"yes");
        //zurück zum Spiel
        if(auswahl == 0 || auswahl == -1){
            spielWeiterspielen();
        }
        //ZUm Menü zurückkehren
        if(auswahl == 1){
            datenSpeichern();
            spielBeenden();
            gui.getFrame().dispatchEvent(new WindowEvent(gui.getFrame(), WindowEvent.WINDOW_CLOSING));
            mainMenu.showMenu();

        }
        //spiel verlassen
        if(auswahl == 2){
            datenSpeichern();
            spielBeenden();
            gui.getFrame().dispatchEvent(new WindowEvent(gui.getFrame(), WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }

    }

    public void datenSpeichern(){
        try {
            FileWriter fileWriter = new FileWriter("D:\\OneDrive - Landrat-Gruber-Schule\\00_TopDownShooter\\data.txt");
            BufferedWriter out = new BufferedWriter(fileWriter);
            System.out.println("high: "+highscore);
            int punkte = SpielerListe.get(0).getPunkte();
            System.out.println("p: "+punkte);
            if(punkte >= highscore){
                System.out.println("neuer Highscore!");
                highscore = punkte;
                mainMenu.setHighscore(highscore);
                String highscoreString =Integer.toString(highscore);
                out.write(highscoreString);
                out.flush();
                out.close();
                System.out.println("printed");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void spielBeenden(){
        SpielerListe.clear();
        Gegner.clear();
        Projektile.clear();
        Waende.clear();
        CollisionBoxes.clear();
        CollisionCircles.clear();
        WaffenPickups.clear();
        zuLoeschendeGegner.clear();
        zuLoeschendeProjektile.clear();
        zuLoeschendeWaffenPickups.clear();
        gui.getGegner().clear();
        gui.getProjektile().clear();
        gui.getWaende().clear();
        gui.getWaffenPickups().clear();
        gui.getZuMalendeSpieler().clear();

    }
    public void spielWeiterspielen(){
        synchronized (update){
            System.out.println("WEITER");
            update.weiter();
        }
    }
    public void spielerTod(){

        int auswahl = JOptionPane.showOptionDialog(gui.getFrame(), "Du bist gestorben", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"neues Spiel starten", "Zurück zum Menü", "Spiel schließen"},"yes");

        //neues Spiel
        if(auswahl == 0 || auswahl == -1){
            datenSpeichern();
            spielBeenden();
            lvl1(0, -1);
        }
        //ZUm Menü zurückkehren
        if(auswahl == 1){
            datenSpeichern();
            spielBeenden();
            gui.getFrame().dispatchEvent(new WindowEvent(gui.getFrame(), WindowEvent.WINDOW_CLOSING));
            mainMenu.showMenu();
        }
        //spiel verlassen
        if(auswahl == 2){
            datenSpeichern();
            spielBeenden();
            gui.getFrame().dispatchEvent(new WindowEvent(gui.getFrame(), WindowEvent.WINDOW_CLOSING));
            System.exit(0);
        }}

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public void ProjektilHinzufuegen(Projektil projektil){
        Projektile.add(projektil);
        CollisionCircles.add(projektil.getCollisionCircle());
        gui.ProjektilHinzufuegen(projektil);
    }
    public void ProjektilLoeschen(Projektil projektil){
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
    public void WaffenPickupErstellen(int breite, int hoehe, Color farbe, Point2D.Double position, Waffe waffe){
        WaffenPickup waffenPickup = new WaffenPickup(breite, hoehe, farbe, position);
        waffenPickup.setWaffe(waffe);
        WaffenPickups.add(waffenPickup);
        CollisionBoxes.add(waffenPickup.getCollisionBox());
        gui.WaffenPickupHinzufuegen(waffenPickup);
    }

    public void WaffenPickupLoeschen(WaffenPickup waffenPickup){
        zuLoeschendeWaffenPickups.add(waffenPickup);
    }

    public void GegnerErstellen(Point2D.Double position, int breite, int hoehe, int maxLeben, Color farbe){
        Gegner gegner = new Gegner(position, breite, hoehe, farbe, maxLeben, main, SpielerListe.get(0));
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
