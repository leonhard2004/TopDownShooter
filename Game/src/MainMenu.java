import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainMenu {
    //Fenster erstellen
    JFrame frame = new JFrame();

    //Knöpfe
    JButton startButton = new JButton();
    JButton infoButton = new JButton();
    JButton exitButton = new JButton();
    //Label
    JLabel highscoreLabel;
    //highscorw
    int highscore;
    // Graphics konfigurieren
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gd.getDefaultConfiguration();

    //Auflösung des Fensters
    int resX;
    int resY;
    //Multiplikatoren für andere Auflösung
    double resXMultiplikator;
    double resYMultiplikator;


    public void start(int resX, int resY){
        this.resX = resX;
        this.resY = resY;
        //Fenster konfigurieren

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        gd.setFullScreenWindow(this.frame);
        frame.setLayout(null);
        //Größe mit mitgegeben Variablen einstellen
        frame.setPreferredSize(new Dimension(resX, resY));

        //Buttons definieren
        Insets insets = frame.getInsets();
        ButtonListener listener = new ButtonListener();
        listener.setMainMenu(this);
        //Start Button
        startButton.setText("Start");
        startButton.setBounds(900+insets.left, 400+insets.top, 200, 70);
        startButton.addActionListener(listener);
        //Info Button
        infoButton.setText("Info");
        infoButton.setBounds(900+insets.left, 500+insets.top, 200, 70);
        infoButton.addActionListener(listener);
        //Exit Button
        exitButton.setText("Schließen");
        exitButton.setBounds(900+insets.left, 600+insets.top, 200, 70);
        exitButton.addActionListener(listener);

        //Buttons hinzufügen
        frame.add(startButton);
        frame.add(infoButton);
        frame.add(exitButton);
        System.out.println("knüpfe hinzugefügt");

        //Highscore einlesen
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\OneDrive - Landrat-Gruber-Schule\\00_TopDownShooter\\data.txt"));

            String highscoreString = bufferedReader.readLine();
            bufferedReader.close();
            if(highscoreString == null)highscoreString = "0";
            highscore = Integer.parseInt(highscoreString);
            highscoreLabel = new JLabel("Highscore: "+highscoreString);
            highscoreLabel.setBounds(1700+insets.left, 100+insets.top, 200, 70);
            frame.add(highscoreLabel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Fenster anzeigen
        frame.pack();
        frame.repaint();
        frame.setVisible(true);

        //Positionen an Bildschirmauflösung anpassen
        this.resXMultiplikator = 1920 / resX;
        this.resYMultiplikator = 1080 / resY;

    }
    Update update;
    public void startGame(){
        GameController gameController = new GameController(this);
        update = new Update(gameController);
        gameController.start(update);
        update.start();
        this.frame.setVisible(false);
    }
    public void showMenu(){
        //Highscore aktualisieren
        highscoreLabel.setText("Highscore: "+highscore);

        frame.pack();
        frame.repaint();
        frame.setVisible(true);
    }

    public void showInfo(){
        JOptionPane.showMessageDialog(frame, "Mit dem Start Knopf wird ein neues Spiel gestartet.\n" +
                "Im Spiel kann das blaue Rechteck(Spielcharakter) mit den Tasten WASD bewegt werden.\n" +
                "Mit einem linksklick kann die ausgrüstete Waffe abgefeuert werden.\n" +
                "Unten rechts steht der aktuell ausgerüstete Waffentyp, das grüne Feld gibt einem eine andere Waffe\n" +
                "mit anderem Schussverhalten. Darüber ist die Munitionsanzeige, wenn diese auf 0 fällt wird automatisch nachgeladen.\n" +
                "Das Ziel ist es alle Gegner im Level zu besiegen. Jeder besiegte Gegner gibt einem 10 Punkte.\n" +
                "Wenn alle Gegner besiegt sind startet das Level von vorne. Die Leben und Punkte bleiben bis zum Tod\n" +
                "oder bis das Spiel beendet wird. Die höchste Punktzahl wird immer als Highscore gespeichert und im Menü angezeigt.\n" +
                "Mit Escape kann das Pause-Menü aufgerufen werden.\n", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    public void stop(){

        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
    }
    public class ButtonListener implements ActionListener {
        MainMenu mainMenu;
        public void setMainMenu(MainMenu mainMenu){
            this.mainMenu = mainMenu;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(startButton) ){
                mainMenu.startGame();
            }
            if (e.getSource().equals(infoButton)){
                mainMenu.showInfo();
            }
            if(e.getSource().equals(exitButton)){
                mainMenu.stop();
            }
        }
    }

    public JLabel getHighscoreLabel() {
        return highscoreLabel;
    }
    public int getHighscore(){return highscore;}
    public void setHighscore(int highscore){this.highscore = highscore;}
}
