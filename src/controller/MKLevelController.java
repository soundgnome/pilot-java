package pilot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MKLevelController extends LevelController implements MouseMotionListener {

    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
    }

    /** required for MouseMotionListener implementation */
    public void mouseDragged(MouseEvent e) {}
}
