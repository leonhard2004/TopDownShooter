import java.io.IOException;

public class Start {
    public static void main(String[] args) throws IOException {
        //Die Windows Einstellungen zum GUI Scaling ignorieren
        System.setProperty("sun.java2d.uiScale", "1.0");
        GameController gamecontroller = new GameController();
        Client client = new Client();
        Update update = new Update(gamecontroller, client);
        Server server = new Server(gamecontroller);
        server.start();
        gamecontroller.start();
        update.start();
        client.startClient();

    }
}
