public class Start {
    public static void main(String[] args) {
        //Die Windows Einstellungen zum GUI Scaling ignorieren
        System.setProperty("sun.java2d.uiScale", "1.0");
        Update update = new Update(new GameController()){};
        update.start();
    }
}
