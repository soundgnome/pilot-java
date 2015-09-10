package pilot;

import java.awt.MouseInfo;
import java.awt.Point;

public class MKLevelController extends LevelController {

    public Point getMousePosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }

}
