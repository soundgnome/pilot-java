package pilot;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class Pilot {

    public static void main(String[] args) {
        MKLevelController level = new MKLevelController();
        HexMapModel map = level.getMapForLevel(1);
        JFrame frame = new JFrame("Pilot");
        HexMapView view = new HexMapView(map, new Dimension(1200,675), 60);
        view.addMouseMotionListener((MouseMotionListener)level);
        level.setView(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
