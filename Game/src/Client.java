import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.net.Socket;

public class Client {
    GUI gui;
    int clientNr;
    Socket socket;
    InputController input;
    public void startClient() {
        gui = new GUI();
        //GUI initialisieren
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        //Bildschirmgröße holen
        gui.start(dim.width, dim.height);
        input = new InputController(gui);
        try{
            socket =new Socket("localhost",8888);
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            //ID abfragen
            outStream.writeUTF("getClientID");
            outStream.flush();
            clientNr = inStream.readInt();
            System.out.println("clientNr: "+clientNr);
            initGUIData();

        }catch(Exception e){
            System.out.println(e);
        }


    }

    public void sendPlayerData(){

        try {
            if(socket == null){
                socket =new Socket("localhost",8888);
            }

            int xAxis = input.getxAxis();
            int yAxis = input.getyAxis();
            boolean isShooting = input.isShooting();
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            outStream.writeUTF("spielerdaten");
            outStream.write(xAxis);
            outStream.write(yAxis);
            outStream.writeBoolean(isShooting);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initGUIData(){
        try{
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            //Wände abfragen
            outStream.writeUTF("getWaende");
            outStream.flush();
            int wandanzahl = inStream.readInt();
            for (int i = 0; i < wandanzahl; i++) {
                int breite = inStream.readInt();
                int hoehe = inStream.readInt();
                int r = inStream.readInt();
                int g = inStream.readInt();
                int b = inStream.readInt();
                double x = inStream.readDouble();
                double y = inStream.readDouble();
                gui.WandHinzufuegen(new Wand(breite, hoehe, new Color(r,g,b), new Point2D.Double(x,y)));
            }
            //Gegner abfragen
            outStream.writeUTF("getGegner");
            outStream.flush();
            int gegneranzahl = inStream.readInt();
            for (int i = 0; i < gegneranzahl; i++) {
                int breite = inStream.readInt();
                int hoehe = inStream.readInt();
                int r = inStream.readInt();
                int g = inStream.readInt();
                int b = inStream.readInt();
                int maxleben = inStream.readInt();
                double x = inStream.readDouble();
                double y = inStream.readDouble();
                Gegner gegner = new Gegner( new Point2D.Double(x,y), breite, hoehe, new Color(r,g,b),maxleben);
                gui.GegnerHinzufuegen(gegner);

            }
            //Waffenpickups abfragen
            outStream.writeUTF("getPickups");
            outStream.flush();
            int pickupanzahl = inStream.readInt();
            for (int i = 0; i < pickupanzahl; i++) {
                int breite = inStream.readInt();
                int hoehe = inStream.readInt();
                int r = inStream.readInt();
                int g = inStream.readInt();
                int b = inStream.readInt();
                double x = inStream.readDouble();
                double y = inStream.readDouble();
                String waffenname = inStream.readUTF();
                WaffenPickup pickup = new WaffenPickup(breite, hoehe, new Color(r,g,b), new Point2D.Double(x,y));
                if(waffenname.equals("Pistole"))pickup.setWaffe(new Pistole());
                else if(waffenname.equals("Shotgun"))pickup.setWaffe(new Shotgun());
                gui.WaffenPickupHinzufuegen(pickup);
            }
            //Gegner abfragen
            outStream.writeUTF("getSpieler");
            outStream.flush();
            int spieleranzahl = inStream.readInt();
            for (int i = 0; i < spieleranzahl; i++) {
                int breite = inStream.readInt();
                int hoehe = inStream.readInt();
                int r = inStream.readInt();
                int g = inStream.readInt();
                int b = inStream.readInt();
                double x = inStream.readDouble();
                double y = inStream.readDouble();
                Spieler spieler = new Spieler( new Point2D.Double(x,y), breite, hoehe, new Color(r,g,b));
                gui.SpielerHinzufuegen(spieler);

            }


        }catch(Exception e){
            System.out.println(e);
        }
    }

    public GUI getGui() {
        return gui;
    }
}
