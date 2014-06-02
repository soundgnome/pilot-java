package pilot;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Pilot {

    public static void main(String[] args) {
        HexMapModel map = new HexMapModel(-7,-3,12,12);
        JFrame frame = new JFrame("Pilot");
        HexMapView view = new HexMapView(map, 60);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
