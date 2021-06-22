import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

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

    BufferedImage cursor = null;
    public void start(int resX, int resY){

        //Fenster konfigurieren
        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        //Cursor zu Ziel ändern

        try {
            cursor = ImageIO.read(new File("Cursor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new Point(cursor.getTileWidth()/2, cursor.getTileHeight()/2));
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(cursor.getTileWidth()/2, cursor.getTileHeight()/2), "customCursor");

        frame.setCursor( customCursor );

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

                    //Waffe rendern
                    BufferedImage waffenimage = null;
                    try {
                        waffenimage = ImageIO.read(new File(spieler.getMeineWaffe().getName()+".png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AffineTransform ti = AffineTransform.getScaleInstance((double) 25 / waffenimage.getHeight(), (double) 25 / waffenimage.getHeight() );
                    ti.translate(0,0);
                    AffineTransformOp transformOp = new AffineTransformOp(ti, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    waffenimage = transformOp.filter(waffenimage, null);
                    double waffenposx = spielerposx + spielerbreite/2;
                    double waffenposy = spielerposy + spielerhöhe/2 - waffenimage.getHeight()/2;
                    double deltaX = (MouseInfo.getPointerInfo().getLocation().x + getCurserWidth()/2 ) - waffenposx;
                    double deltaY = (MouseInfo.getPointerInfo().getLocation().y + getCurserHeight()/2 ) - waffenposy;
                    double alpha = Math.atan2(deltaY, deltaX);
                    //Make a backup so that we can reset our graphics object after using it.
                    AffineTransform backup = g2d.getTransform();
                    //rx is the x coordinate for rotation, ry is the y coordinate for rotation, and angle
                    //is the angle to rotate the image. If you want to rotate around the center of an image,
                    //use the image's center x and y coordinates for rx and ry.
                    AffineTransform rotateInstance = AffineTransform.getRotateInstance(alpha, waffenposx, (waffenposy + waffenimage.getHeight()/2));

                    if(alpha > 2 || alpha < - 2 ){
                         BufferedImage waffenimageflipped = new BufferedImage(waffenimage.getWidth(null), waffenimage.getHeight(null), BufferedImage.TRANSLUCENT);
                         waffenimageflipped.getGraphics().drawImage(waffenimage, 0, 0, null);
                         waffenimageflipped.getGraphics().dispose();
                         AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
                         tx.translate(0, -waffenimage.getHeight());
                         AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                         waffenimageflipped = op.filter(waffenimageflipped, null);
                         //Set our Graphics2D object to the transform
                         g2d.setTransform(rotateInstance);
                         g2d.drawImage(waffenimageflipped, (int) waffenposx, (int) (waffenposy), null);


                    }
                    else {
                        //Set our Graphics2D object to the transform
                        g2d.setTransform(rotateInstance);
                        //Draw our image like normal
                        g2d.drawImage(waffenimage, (int) waffenposx, (int) (waffenposy), null);
                    }




                    //Reset our graphics object so we can draw with it again.
                    g2d.setTransform(backup);
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
                    int magazingroeße = 0;
                    int geschosseneKugeln = 0;
                    if(spieler.getMeineWaffe() != null){
                        magazingroeße = spieler.getMeineWaffe().getMagazingröße();
                        geschosseneKugeln = spieler.getMeineWaffe().getGeschosseneKugeln();
                    }
                    if(spieler.getMeineWaffe().getLaednach() == true){
                        g2d.drawString( String.format("nachladen"), (int) (1700 * resXMultiplikator), (int) (900 * resYMultiplikator));
                    }
                    if(spieler.getMeineWaffe().getLaednach() == false){
                        g2d.drawString( String.format( "%d/%d", magazingroeße - geschosseneKugeln, magazingroeße), (int) (1700 * resXMultiplikator), (int) (900 * resYMultiplikator));
                    }
                    g2d.drawString(String.format("Punkte: %d", spieler.getPunkte()), (int) (1700 * resXMultiplikator), (int) (100 * resYMultiplikator));
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
    }
    public void GegnerHinzufuegen(Gegner gegner){
        Gegner.add(gegner);
    }

    public void WandHinzufuegen(Wand wand ){
        Waende.add(wand);
    }

    public void WaffenPickupHinzufuegen(WaffenPickup waffenPickup){
        WaffenPickups.add(waffenPickup);
    }

    public void ProjektilHinzufuegen(Projektil projektil){
        Projektile.add(projektil);
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
    public int getCurserWidth(){
        return cursor.getWidth();
    }
    public int getCurserHeight(){
        return cursor.getHeight();
    }
}
