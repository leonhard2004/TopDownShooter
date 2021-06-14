import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread{
    private GameController main;
    public Server(GameController main){
        this.main = main;
    }

    @Override
    public  void run(){
    try{
        ServerSocket server=new ServerSocket(8888);
        int counter=0;
        System.out.println("Server Started ....");
        while(true){
            Socket serverClient=server.accept();  //server accept the client connection request
            System.out.println(" >> " + "Client No:" + counter + " started!");
            main.SpielerErstellen();
            Spieler clientSpieler = main.getPlayer(counter);
            ClientHandler sct = new ClientHandler(serverClient,counter, main); //send  the request to a separate thread
            sct.start();
            counter++;
        }
    }catch(Exception e){
        System.out.println(e);
    }
    }
    private static class ClientHandler extends Thread{
        Socket serverClient;
        int clientNo;
        Spieler spieler;
        GameController main;
        ClientHandler(Socket inSocket,int counter, GameController main){
            serverClient = inSocket;
            clientNo=counter;
            this.main = main;
            spieler = main.getPlayer(clientNo);
        }
        public void run(){
            try{
                DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
                DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
                String clientMessage="", serverMessage="";
                while(!clientMessage.equals("ende")){
                    clientMessage=inStream.readUTF();
                    if( !clientMessage.equals("")) {
                        System.out.println("From Client-" + clientNo + ": " + clientMessage);

                        if (clientMessage.equals("getClientID")) {
                            outStream.writeInt(clientNo);
                        }
                        if(clientMessage.equals("getWaende")) {
                            outStream.writeInt(main.getWaende().size());
                            for (int i = 0; i < main.getWaende().size(); i++) {
                                int breite = main.getWaende().get(i).getBreite();
                                int hoehe = main.getWaende().get(i).getHoehe();
                                int r = main.getWaende().get(i).getFarbe().getRed();
                                int g = main.getWaende().get(i).getFarbe().getGreen();
                                int b = main.getWaende().get(i).getFarbe().getBlue();
                                double x = main.getWaende().get(i).getPosition().getX();
                                double y = main.getWaende().get(i).getPosition().getY();
                                outStream.writeInt(breite);
                                outStream.writeInt(hoehe);
                                outStream.writeInt(r);
                                outStream.writeInt(g);
                                outStream.writeInt(b);
                                outStream.writeDouble(x);
                                outStream.writeDouble(y);
                                outStream.flush();
                            }
                        }
                        if(clientMessage.equals("getGegner")) {
                            outStream.writeInt(main.getGegner().size());
                            for (int i = 0; i < main.getGegner().size(); i++) {
                                int breite = main.getGegner().get(i).getBreite();
                                int hoehe = main.getGegner().get(i).getHoehe();
                                int r = main.getGegner().get(i).getFarbe().getRed();
                                int g = main.getGegner().get(i).getFarbe().getGreen();
                                int b = main.getGegner().get(i).getFarbe().getBlue();
                                int maxleben = main.getGegner().get(i).getMaxLeben();
                                double x = main.getGegner().get(i).getPosition().getX();
                                double y = main.getGegner().get(i).getPosition().getY();
                                outStream.writeInt(breite);
                                outStream.writeInt(hoehe);
                                outStream.writeInt(r);
                                outStream.writeInt(g);
                                outStream.writeInt(b);
                                outStream.writeInt(maxleben);
                                outStream.writeDouble(x);
                                outStream.writeDouble(y);
                                outStream.flush();
                            }
                        }
                        if(clientMessage.equals("getPickups")) {
                            outStream.writeInt(main.getWaffenPickups().size());
                            for (int i = 0; i < main.getWaffenPickups().size(); i++) {
                                int breite = main.getWaffenPickups().get(i).getBreite();
                                int hoehe = main.getWaffenPickups().get(i).getHoehe();
                                int r = main.getWaffenPickups().get(i).getFarbe().getRed();
                                int g = main.getWaffenPickups().get(i).getFarbe().getGreen();
                                int b = main.getWaffenPickups().get(i).getFarbe().getBlue();
                                double x = main.getWaffenPickups().get(i).getPosition().getX();
                                double y = main.getWaffenPickups().get(i).getPosition().getY();
                                String waffenname = main.getWaffenPickups().get(i).getWaffe().getName();
                                outStream.writeInt(breite);
                                outStream.writeInt(hoehe);
                                outStream.writeInt(r);
                                outStream.writeInt(g);
                                outStream.writeInt(b);
                                outStream.writeDouble(x);
                                outStream.writeDouble(y);
                                outStream.writeUTF(waffenname);
                                outStream.flush();
                            }
                        }
                        if (clientMessage.equals("spielerdaten")) {
                            int xAxis = inStream.read();
                            int yAxis = inStream.read();
                            boolean isShooting = inStream.readBoolean();
                            System.out.println("x:"+xAxis+" y:"+yAxis+" Shooting: "+isShooting);
                        }
                    }

                }
                inStream.close();
                outStream.close();
                serverClient.close();
            }catch(Exception ex){
                System.out.println(ex);
            }finally{
                System.out.println("Client -" + clientNo + " exit!! ");
            }
        }
    }
}
