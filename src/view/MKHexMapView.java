package pilot;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public class MKHexMapView extends HexMapView {

    private MKLevelController level;


    public MKHexMapView(HexMapModel model, Dimension mapDimension, ConfigController config, MKLevelController level) {
        super(model, mapDimension, config);
        this.level = level;
    }

    @Override
    protected Graphics2D updateComposite() {
        g2d = super.updateComposite();

        Point pixels = this.level.getMousePosition();
        int[] coords = this.pixelsToCoords(pixels.x, pixels.y);
        if (coords != null) {
            g2d.setColor(this.foregroundColor);
            g2d.drawString("sector coordinates: "+coords[0]+", "+coords[1], this.hexInfoCoords[0], this.hexInfoCoords[2]);
        }

        return g2d;
    }

}
