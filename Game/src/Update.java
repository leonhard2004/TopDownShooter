import java.awt.*;

public class Update extends Thread{
    private GameController gameController;
    private Client client;

    public Update(GameController gameController, Client client) {
        this.gameController = gameController;
        this.client = client;
    }

    @Override
    public void run() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode dm = gs[0].getDisplayMode();

        // Get refresh rate in Hz
        int refreshRate = dm.getRefreshRate();

        long lastTime = System.nanoTime();
        final double updateNs = 1000000000.0 / 60;
        double updateDelta = 0;
        final double frameNs = 1000000000.0 / refreshRate;
        double frameDelta = 0;
        int fps = 0;
        int frames = 0;
        long totalTime = 0;


        while(true){
            long now = System.nanoTime();
            totalTime += now - lastTime;
            updateDelta += (now - lastTime) / updateNs;
            frameDelta += (now - lastTime) / frameNs;
            lastTime = now;
            while(updateDelta >= 1){
                gameController.FixedUpdate();
                try {
                    client.sendPlayerData();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                updateDelta--;
            }
            if (frameDelta >= 1){
                client.getGui().malen();
                frames++;
                frameDelta--;
            }
            if(totalTime >= 1000000000){
                totalTime -= 1000000000;
                fps = frames;
                frames = 0;
                System.out.println("FPS: "+fps);
            }
        }
    }
}
