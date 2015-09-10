package pilot;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class MKHexMapView extends HexMapView {

    private static final long serialVersionUID = 1L;

    private MKLevelController level;


    public MKHexMapView(HexMapModel model, Dimension mapDimension, int hexSize, MKLevelController level) {
        super(model, mapDimension, hexSize);
        this.level = level;
    }


    @Override
    public void render() {
        super.render();

        Point pixels = this.level.getMousePosition();
        int[] coords = this.pixelsToCoords(pixels.x, pixels.y);
        this.g2d.setBackground(this.backgroundColor);
        this.g2d.clearRect(this.hexInfoCoords[0], this.hexInfoCoords[1], this.hexInfoCoords[3], this.hexInfoCoords[4]);
        this.g2d.setColor(this.foregroundColor);
        this.g2d.drawString("Cursor Position: "+coords[0]+", "+coords[1], this.hexInfoCoords[0], this.hexInfoCoords[2]);
    }

}
