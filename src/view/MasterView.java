package pilot;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseMotionListener;


public class MasterView {

    private MKLevelController level;
    private HexMapModel map;
    private JFrame frame;
    private HexMapView view;

    public MasterView() {
        this.level = new MKLevelController();
        this.map = level.getMapForLevel(1);
        this.frame = new JFrame("Pilot");
    }

    public void activate() {
        this.view = new HexMapView(map, new Dimension(1200,675), 60);
        this.view.addMouseMotionListener((MouseMotionListener)this.level);
        this.level.setView(this.view);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.view);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void render() {}
}
