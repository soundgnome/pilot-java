package pilot;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.Dimension;


public class MasterView {

    private JFrame frame;

    private HexMapModel map;
    private HexMapView view;
    private MKLevelController level;

    public MasterView() {
        this.level = new MKLevelController();
        this.map = level.getMapForLevel(1);
        this.frame = new JFrame("Pilot");
    }

    public void activate() {
        this.view = new MKHexMapView(map, new Dimension(1200,675), 60, this.level);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.view);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void render() {
        this.view.render();
    }
}
