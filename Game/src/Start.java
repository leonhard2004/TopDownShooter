public class Start {
    public static void main(String[] args) {
        Update update = new Update(new GameController()){};
        update.start();
    }
}
