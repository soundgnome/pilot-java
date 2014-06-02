package pilot;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class HexMapView extends Canvas {

    // magic numbers (refactor)
    private Dimension windowSize = new Dimension(1200, 675);
    private int hexSize;
    private int[] hexFractions;

    private HexMapModel model;
    private int[] offsets;

    public HexMapView(HexMapModel model, int hexSize) {
        this.model = model;
        this.hexSize = hexSize;
        this.offsets = this.getOffsets(model.getRange());
        this.hexFractions = this.calculateFractions(this.hexSize);
    }

    @Override
    public Dimension getPreferredSize() {
        return this.windowSize;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1200,675);
        g.setColor(Color.WHITE);
        ArrayList<int[]> coordinateSet = this.model.getCoordinateSet();
        for (int[] coords : coordinateSet) {
            this.drawHex(g, this.translateCoordinates(coords), this.hexSize, this.hexFractions, this.model.getHex(coords));
        }
    }

    private int[] getOffsets(int[] range) {
        return new int[]{range[0], range[3]};
    }

    private int[] calculateFractions(int size) {
        double side = 0.5*size/Math.sqrt(3);
        int[] fractions = new int[]{size/2, (int)side, (int)(side*3), (int)(side*4)};
        return fractions;
    }

    private int[] translateCoordinates(int[] coords) {
        int y = this.hexFractions[2] * (this.offsets[1] - coords[1]);
        int x = (this.hexSize * (coords[0] - this.offsets[0])) + y/2;
        return new int[]{x,y};
    }

    private void drawHex(Graphics g, int[] coords, int size, int[] fractions, HexModel hex) {

        int[] x = new int[]{coords[0]+fractions[0], coords[0]+size, coords[0]};
        int[] xPoints = new int[]{x[0], x[1], x[1], x[0], x[2], x[2]};

        int[] y = new int[]{coords[1], coords[1]+fractions[1], coords[1]+fractions[2], coords[1]+fractions[3]};
        int[] yPoints = new int[]{y[0], y[1], y[2], y[3], y[2], y[1]};

        g.drawOval(coords[0], coords[1], this.hexSize, this.hexSize);
    }
}
