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
            ClientHandler sct = new ClientHandler(serverClient,counter); //send  the request to a separate thread
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
        ClientHandler(Socket inSocket,int counter){
            serverClient = inSocket;
            clientNo=counter;
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
                        if (clientMessage.equals("spielerdaten")) {
                            int xAxis = inStream.read();
                            int yAxis = inStream.read();
                            boolean isShooting = inStream.readBoolean();
                            System.out.println("x: "+xAxis+" y: "+yAxis+" isShooting: "+isShooting);
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
