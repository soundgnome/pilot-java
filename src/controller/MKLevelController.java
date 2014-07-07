package pilot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MKLevelController extends LevelController implements MouseMotionListener {

    private HexMapView view;

    public void mouseMoved(MouseEvent e) {
        this.view.updateHexInfo(e.getX(), e.getY());
    }

    /** required for MouseMotionListener implementation */
    public void mouseDragged(MouseEvent e) {}

    public void setView(HexMapView view) {
        this.view = view;
    }
}
