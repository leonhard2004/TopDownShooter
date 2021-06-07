import java.io.*;
import java.net.Socket;

public class Client {
    GUI gui;
    int clientNr;
    Socket socket;

    public void startClient() {
        gui = new GUI();
        try{
            socket =new Socket("localhost",8888);
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            DataInputStream inStream = new DataInputStream(socket.getInputStream());
            outStream.writeUTF("getClientID");
            outStream.flush();
            clientNr = inStream.readInt();
            System.out.println("clientNr: "+clientNr);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void sendPlayerData(){

        try {
            if(socket == null){
                socket =new Socket("localhost",8888);
            }
            InputController input = new InputController(gui);
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
}
