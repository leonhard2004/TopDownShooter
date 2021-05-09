import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    ArrayList<Spieler> zuMalendeSpieler = new ArrayList<Spieler>();
    ArrayList<Wand> Waende = new ArrayList<Wand>();
    ArrayList<Projektil> Projektile = new ArrayList<Projektil>();

    //Auflösung der Leinwand
    int resX = 0;
    int resY = 0;
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
            g2d.fillRect( 0, 0, resX, resY );
            //sachen malen
            if (!zuMalendeSpieler.isEmpty()) {
                for (int i = 0; i < zuMalendeSpieler.size(); i++) {
                    Spieler spieler = zuMalendeSpieler.get(i);
                    g2d.setColor(spieler.getFarbe());
                    double spielerposx = spieler.getPosition().x;
                    double spielerposy = spieler.getPosition().y;
                    double spielerbreite = spieler.getBreite();
                    double spielerhöhe = spieler.getHoehe();
                    Rectangle2D rect = new Rectangle2D.Double(spielerposx, spielerposy, spielerbreite, spielerhöhe);
                    g2d.fill(rect);
                    g2d.setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
                    g2d.setColor( Color.WHITE );
                    g2d.drawString( String.format( "HP: %s", spieler.getLeben() ), (int) spielerposx, (int) spielerposy);
                    //System.out.println("spieler gemalt"+i);
                }
            }
            if (!Waende.isEmpty()){
                for (int i = 0; i < Waende.size(); i++) {
                    Wand wand = Waende.get(i);
                    g2d.setColor(wand.getFarbe());
                    double wandposx = wand.getPosition().x;
                    double wandposy = wand.getPosition().y;
                    double wandbreite = wand.getBreite();
                    double wandhoehe = wand.getHoehe();
                    Rectangle2D wandrect = new Rectangle2D.Double(wandposx, wandposy, wandbreite, wandhoehe);
                    g2d.fill(wandrect);
                }
            }
            if (!Projektile.isEmpty()){
                for (int i = 0; i < Projektile.size(); i++) {
                    Projektil projektil = Projektile.get(i);
                    g2d.setColor(projektil.getFarbe());
                    double projektilposx = projektil.getPosition().x;
                    double projektilposy = projektil.getPosition().y;
                    double radius = projektil.getRadius();
                    Ellipse2D projektilkreis = new Ellipse2D.Double(projektilposx, projektilposy, radius*2, radius*2);
                    g2d.fill(projektilkreis);
                }
            }
            // display frames per second...
            g2d.setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
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

    public void WandHinzufuegen(Wand wand ){
        Waende.add(wand);
        System.out.println(Waende);
    }

    public void ProjektiilHinzufügen(Projektil projektil){
        Projektile.add(projektil);
        System.out.println(Projektile);
    }
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
