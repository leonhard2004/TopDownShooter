import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static void handleConnection( Socket client ) throws IOException
    {
        Scanner in  = new Scanner( client.getInputStream() );
        PrintWriter out = new PrintWriter( client.getOutputStream(), true );

        String factor1 = in.nextLine();
        String factor2 = in.nextLine();

        out.println( new BigInteger(factor1).multiply( new BigInteger(factor2) ) );
    }

    public static void main( String[] args ) throws IOException{
    try{
        ServerSocket server=new ServerSocket(8888);
        int counter=0;
        System.out.println("Server Started ....");
        while(true){
            counter++;
            Socket serverClient=server.accept();  //server accept the client connection request
            System.out.println(" >> " + "Client No:" + counter + " started!");
            ClientHandler sct = new ClientHandler(serverClient,counter); //send  the request to a separate thread
            sct.start();
        }
    }catch(Exception e){
        System.out.println(e);
    }
    }
    private static class ClientHandler extends Thread{
        Socket serverClient;
        int clientNo;
        int squre;
        ClientHandler(Socket inSocket,int counter){
            serverClient = inSocket;
            clientNo=counter;
        }
        public void run(){
            try{
                DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
                DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
                String clientMessage="", serverMessage="";
                while(!clientMessage.equals("bye")){
                    clientMessage=inStream.readUTF();
                    System.out.println("From Client-" +clientNo+ ": Number is :"+clientMessage);
                    squre = Integer.parseInt(clientMessage) * Integer.parseInt(clientMessage);
                    serverMessage="From Server to Client-" + clientNo + " Square of " + clientMessage + " is " +squre;
                    outStream.writeUTF(serverMessage);
                    outStream.flush();
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
