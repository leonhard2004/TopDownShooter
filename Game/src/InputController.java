import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.event.*;

public class InputController {
    private GUI gui = null;
    private Spieler meinSpieler = null;
    public boolean rechts = false;
    public boolean links = false;
    public boolean oben = false;
    public boolean unten = false;
    private final String RECHTS = "rechts";
    private final String LINKS = "links";
    private final String OBEN = "oben";
    private final String UNTEN = "unten";


    public InputController(GUI gui) {
        this.gui = gui;
        JComponent inputManager = new JComponent(){};
        //Wenn Tasten gedrückt werden wird das ActionEvent ausgelöst
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed W"), "obenP");
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed A"), "linksP");
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed S"), "untenP");
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed D"), "rechtsP");
        //Wenn Taste lossgelassen wird wird das ActionEvent ausgelöst
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"), "obenR");
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), "linksR");
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"), "untenR");
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"), "rechtsR");
        //ActionEvents werden der MoveAction zugewiesen und geben richtungsparameter sowie information ob gedrückt oder lossgelassen wurde mit
        //Taste wird gedrückt
        inputManager.getActionMap().put("obenP", new MoveAction(OBEN, true));
        inputManager.getActionMap().put("linksP", new MoveAction(LINKS, true));
        inputManager.getActionMap().put("untenP", new MoveAction(UNTEN, true));
        inputManager.getActionMap().put("rechtsP", new MoveAction(RECHTS, true));
        //Taste wird losgelassen
        inputManager.getActionMap().put("obenR", new MoveAction(OBEN, false));
        inputManager.getActionMap().put("linksR", new MoveAction(LINKS, false));
        inputManager.getActionMap().put("untenR", new MoveAction(UNTEN, false));
        inputManager.getActionMap().put("rechtsR", new MoveAction(RECHTS, false));
        //Escapetaste
        inputManager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed ESCAPE"), "escape");
        inputManager.getActionMap().put("escape", new EscapeAction());
        //Mausklick
        gui.getCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("mouseclick");
                    meinSpieler.shoot();
                }
            }
        });

        //der Inputmanager wird zum JFrame hinzugefügt
        gui.getFrame().add(inputManager);
    }

    public int getyAxis() {
        int yAxis = 0;

        if(oben && !unten){
            yAxis = -1;
        }
        if(!oben && unten){
            yAxis = 1;
        }
        return yAxis;
    }

    public int getxAxis() {
        int xAxis = 0;

        if(links && !rechts){
            xAxis = -1;
        }
        if(!links && rechts){
            xAxis = 1;
        }
        return xAxis;
    }

    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public Spieler getMeinSpieler() {
        return meinSpieler;
    }

    public void setMeinSpieler(Spieler meinSpieler) {
        this.meinSpieler = meinSpieler;
    }

    private class MoveAction extends AbstractAction{
        String richtung = "";
        boolean pressed = false;
        MoveAction(String richtung, boolean pressed){
            this.richtung = richtung;
            this.pressed = pressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(richtung+" "+ pressed);
                if (richtung.equals(OBEN)){
                        oben = pressed;
                }
                if(richtung.equals(LINKS)) {
                        links = pressed;
                }
                if(richtung.equals(UNTEN)){
                        unten = pressed;
                }
                if (richtung.equals(RECHTS)){
                        rechts = pressed;
                }
            /*System.out.println("oben: "+ oben);
            System.out.println("unten: "+ unten);
            System.out.println("rechts: "+ rechts);
            System.out.println("links: "+ links);
            System.out.println("x: "+getxAxis());
            System.out.println("y: "+getyAxis());
            */
        }
    }
    private class EscapeAction extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            gui.getFrame().dispatchEvent(new WindowEvent(gui.getFrame(), WindowEvent.WINDOW_CLOSING));
        }
    }

}


