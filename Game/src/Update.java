import java.awt.*;

public class Update extends Thread{
    private GameController gameController;
    boolean pause = false;


    public Update(GameController gameController) {
        this.gameController = gameController;

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
                while (updateDelta >= 1) {
                    if(!pause){
                        gameController.FixedUpdate();
                    }
                    updateDelta--;

                }
                if (frameDelta >= 1) {
                    if(!pause) {
                        gameController.getGUI().malen();
                    }
                    frames++;
                    frameDelta--;
                }
                if (totalTime >= 1000000000) {
                    totalTime -= 1000000000;
                    fps = frames;
                    frames = 0;
                    //System.out.println("FPS: " + fps);
                }


        }
    }
    public synchronized void pausieren(){
        pause = true;
    }
    public synchronized void weiter(){
        pause = false;
        System.out.println("update weiter");
    }

    public boolean isPaused() {
        return pause;
    }
}
