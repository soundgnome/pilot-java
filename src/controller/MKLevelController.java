package pilot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MKLevelController extends LevelController implements MouseMotionListener {

    private HexMapView view;

    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.view.updateHexInfo(x, y);
    }

    /** required for MouseMotionListener implementation */
    public void mouseDragged(MouseEvent e) {}

    public void setView(HexMapView view) {
        this.view = view;
    }
}
