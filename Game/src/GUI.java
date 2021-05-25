import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GUI {
    //Fenster erstellen
    JFrame frame = new JFrame();
    //Canvas um das Spiel zu zeichnen erstellen
    Canvas canvas = new Canvas();
    //graphics objekte erstellen
    Graphics graphics = null;
    Graphics2D g2d = null;
    // Graphics konfigurieren
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gd.getDefaultConfiguration();
    //buffer erstellen
    BufferStrategy buffer = null;
    // nicht sichtbares Bild zum  bufferen erstellen
    BufferedImage bi;

    //Liste mit den zu mamlenden Objekten
    private ArrayList<Spieler> zuMalendeSpieler = new ArrayList<>();
    private ArrayList<Gegner> Gegner = new ArrayList<>();
    private ArrayList<Wand> Waende = new ArrayList<>();
    private ArrayList<Projektil> Projektile = new ArrayList<>();
    private ArrayList<WaffenPickup> WaffenPickups = new ArrayList<>();

    //Auflösung der Leinwand
    int resX = 0;
    int resY = 0;
    //Multiplikatoren für andere Auflösung
    double resXMultiplikator;
    double resYMultiplikator;

    //Farben
    Color hintergrund = new Color(61, 61, 61);


    public void start(int resX, int resY){

        //Fenster konfigurieren
        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);


        gd.setFullScreenWindow(this.frame);


        canvas.setIgnoreRepaint(true);
        //Größe mit mitgegeben Variablen einstellen
        canvas.setSize(resX, resY);

        this.resX = resX;
        this.resY = resY;
        //canvas zu Frame hinzufügen
        frame.add(canvas);


        //Fenster anzeigen
        frame.pack();

        frame.setVisible(true);
        // Buffering erstellen
        canvas.createBufferStrategy( 2 );
        buffer = canvas.getBufferStrategy();
        //back-Buffering deklarieren
        bi = gc.createCompatibleImage( resX, resY );

        //Positionen an Bildschirmauflösung anpassen
        this.resXMultiplikator = 1920 / resX;
        this.resYMultiplikator = 1080 / resY;

    }
    int fps = 0;
    int frames = 0;
    long totalTime = 0;
    long curTime = System.currentTimeMillis();
    long lastTime = curTime;


    public void malen(){

        try{
            // count Frames per second...
            lastTime = curTime;
            curTime = System.currentTimeMillis();
            totalTime += curTime - lastTime;
            if( totalTime > 1000 ) {
                totalTime -= 1000;
                fps = frames;
                frames = 0;
            }
            ++frames;
            // clear back buffer
            g2d = bi.createGraphics();
            //Hintergrund malen
            g2d.setColor( hintergrund );
            g2d.fillRect( 0, 0, (int)(resX * resXMultiplikator), (int) (resY * resYMultiplikator));
            //sachen malen
            if (!zuMalendeSpieler.isEmpty()) {
                for (int i = 0; i < zuMalendeSpieler.size(); i++) {
                    Spieler spieler = zuMalendeSpieler.get(i);
                    g2d.setColor(spieler.getFarbe());
                    double spielerposx = spieler.getPosition().x * resXMultiplikator;
                    double spielerposy = spieler.getPosition().y * resYMultiplikator;
                    double spielerbreite = spieler.getBreite() * resXMultiplikator;
                    double spielerhöhe = spieler.getHoehe() * resYMultiplikator;
                    Rectangle2D rect = new Rectangle2D.Double(spielerposx, spielerposy, spielerbreite, spielerhöhe);
                    g2d.fill(rect);
                    //Leben über Spieler schreiben
                    g2d.setFont( new Font( "Serif", Font.PLAIN, 13 ) );
                    g2d.setColor( Color.WHITE );
                    g2d.drawString( String.format( "HP: %s", spieler.getLeben() ), (int) spielerposx, (int) spielerposy);
                    //Waffentyp in die Ecke schreiben
                    g2d.setFont( new Font( "Serif", Font.PLAIN, 30 ) );
                    g2d.setColor( Color.WHITE );
                    String waffenname = "";
                    if(spieler.getMeineWaffe() != null){
                        waffenname = spieler.getMeineWaffe().getName();
                    }
                    g2d.drawString( String.format( "Waffe: %s", waffenname),(int) (1700 * resXMultiplikator), (int) (1000 * resYMultiplikator));

                }
            }
            if (!Gegner.isEmpty()) {
                for (int i = 0; i < Gegner.size(); i++) {
                    Gegner gegner = Gegner.get(i);
                    g2d.setColor(gegner.getFarbe());
                    double gegnerposx = gegner.getPosition().x * resXMultiplikator;
                    double gegnerposy = gegner.getPosition().y * resYMultiplikator;
                    double gegnerbreite = gegner.getBreite() * resXMultiplikator;
                    double gegnerhöhe = gegner.getHoehe() * resYMultiplikator;
                    Rectangle2D rect = new Rectangle2D.Double(gegnerposx, gegnerposy, gegnerbreite, gegnerhöhe);
                    g2d.fill(rect);
                    g2d.setFont( new Font( "Serif", Font.PLAIN, 13 ) );
                    g2d.setColor( Color.WHITE );
                    g2d.drawString( String.format( "HP: %s", gegner.getLeben() ), (int) gegnerposx, (int) gegnerposy);

                }
            }
            if (!Waende.isEmpty()){
                for (int i = 0; i < Waende.size(); i++) {
                    Wand wand = Waende.get(i);
                    g2d.setColor(wand.getFarbe());
                    double wandposx = wand.getPosition().x * resXMultiplikator;
                    double wandposy = wand.getPosition().y * resYMultiplikator;
                    double wandbreite = wand.getBreite() * resXMultiplikator;
                    double wandhoehe = wand.getHoehe() * resYMultiplikator;
                    Rectangle2D wandrect = new Rectangle2D.Double(wandposx, wandposy, wandbreite, wandhoehe);
                    g2d.fill(wandrect);
                }
            }
            if (!Projektile.isEmpty()){
                for (int i = 0; i < Projektile.size(); i++) {
                    Projektil projektil = Projektile.get(i);
                    g2d.setColor(projektil.getFarbe());
                    double projektilposx = projektil.getPosition().x * resXMultiplikator;
                    double projektilposy = projektil.getPosition().y * resYMultiplikator;
                    double radius = projektil.getRadius() * resXMultiplikator;
                    Ellipse2D projektilkreis = new Ellipse2D.Double(projektilposx, projektilposy, radius*2, radius*2);
                    g2d.fill(projektilkreis);
                }
            }
            if (!WaffenPickups.isEmpty()){
                for (int i = 0; i < WaffenPickups.size(); i++) {
                    WaffenPickup waffenPickup = WaffenPickups.get(i);
                    g2d.setColor(waffenPickup.getFarbe());
                    double waffenpickupposx = waffenPickup.getPosition().x * resXMultiplikator;
                    double waffenpickupposy = waffenPickup.getPosition().y * resYMultiplikator;
                    double waffenpickupbreite = waffenPickup.getBreite() * resXMultiplikator;
                    double waffenpickuphoehe = waffenPickup.getHoehe() * resYMultiplikator;
                    Rectangle2D rect = new Rectangle2D.Double(waffenpickupposx, waffenpickupposy, waffenpickupbreite, waffenpickuphoehe);
                    g2d.fill(rect);
                    //Waffennamen holen um ihn ins Pickup zu schreiben
                    String waffenname = waffenPickup.getWaffe().getName();
                    //Farbe und Font festlegen und Waffennamen ins Pickup schreiben
                    g2d.setColor(Color.WHITE);
                    g2d.setFont( new Font( "Serif", Font.PLAIN, 13 ) );
                    g2d.drawString( String.format( "%s", waffenname),(int) waffenpickupposx, (int) (waffenpickupposy + waffenpickuphoehe/2));
                }
            }
            // display frames per second...
            g2d.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
            g2d.setColor( Color.GREEN );
            g2d.drawString( String.format( "FPS: %s", fps ), 20, 20 );


            // im Hintergrund gemaltes Bild zeigen
            graphics = buffer.getDrawGraphics();
            graphics.drawImage( bi, 0, 0, null );
            if( !buffer.contentsLost() )
                buffer.show();
            Thread.yield();
        }finally{
            if( graphics != null )
                graphics.dispose();
            if( g2d != null )
                g2d.dispose();
        }
    }
    public void SpielerHinzufuegen(Spieler spieler ){
        zuMalendeSpieler.add(spieler);
        System.out.println(zuMalendeSpieler);
    }
    public void GegnerHinzufuegen(Gegner gegner){
        Gegner.add(gegner);
        System.out.println(Gegner);
    }

    public void WandHinzufuegen(Wand wand ){
        Waende.add(wand);
        System.out.println(Waende);
    }

    public void WaffenPickupHinzufuegen(WaffenPickup waffenPickup){
        WaffenPickups.add(waffenPickup);
        System.out.println(WaffenPickups);
    }

    public void ProjektilHinzufuegen(Projektil projektil){
        Projektile.add(projektil);
        System.out.println(Projektile);
    }

    public ArrayList<Spieler> getZuMalendeSpieler() {
        return zuMalendeSpieler;
    }

    public ArrayList<Gegner> getGegner() {
        return Gegner;
    }

    public ArrayList<Wand> getWaende() {
        return Waende;
    }

    public ArrayList<Projektil> getProjektile() {
        return Projektile;
    }

    public ArrayList<WaffenPickup> getWaffenPickups(){return WaffenPickups;}

    public JFrame getFrame() {
        return frame;
    }
    public Canvas getCanvas(){ return canvas;}

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public int getResX() {
        return resX;
    }

    public int getResY() {
        return resY;
    }
}
