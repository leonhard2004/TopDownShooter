import java.io.*;
import java.net.Socket;

public class Client {
    GameController main;
    int clientNr;
    Spieler meinSpieler;
    Socket socket;

    public Client(GameController main, int clientnr){
        this.main = main;
        this.clientNr = clientnr;
    }


    public void startClient() {
        try{
            socket =new Socket("localhost",8888);
            DataInputStream inStream=new DataInputStream(socket.getInputStream());
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            /*BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String clientMessage="",serverMessage="";
            while(!clientMessage.equals("bye")){
                System.out.println("Enter number :");
                clientMessage=br.readLine();
                outStream.writeUTF(clientMessage);
                outStream.flush();
                serverMessage=inStream.readUTF();
                System.out.println(serverMessage);
            }

            outStream.close();
            outStream.close();
            socket.close();*/
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void sendPlayerData(){

        try {
            if(socket == null){
                socket =new Socket("localhost",8888);
            }
            this.meinSpieler = main.getPlayer(clientNr);
            InputController input = meinSpieler.getMeinInputController();
            int xAxis = input.getxAxis();
            int yAxis = input.getyAxis();
            boolean isShooting = input.isShooting();
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            outStream.writeUTF(String.valueOf(xAxis));
            outStream.writeUTF(String.valueOf(yAxis));
            outStream.writeUTF(String.valueOf(isShooting));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
