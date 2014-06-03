package pilot;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class HexMapView extends Canvas {

    private Color backgroundColor = Color.BLACK;
    private Color foregroundColor = Color.WHITE;

    private HexMapModel model;

    private Dimension mapDimension;
    private int hexSize;
    private int[] hexFractions;
    private int[] offsets;

    public HexMapView(HexMapModel model, Dimension mapDimension, int hexSize) {
        this.model = model;
        this.mapDimension = mapDimension;
        this.hexSize = hexSize;
        this.offsets = this.getOffsets(model.getRange());
        this.hexFractions = this.calculateFractions(this.hexSize);
    }

    @Override
    public Dimension getPreferredSize() {
        return this.mapDimension;
    }

    @Override
    public void paint(Graphics graphics) {
        this.setBackground(this.backgroundColor);
        Graphics2D g = (Graphics2D)graphics;
        g.setColor(this.foregroundColor);
        ArrayList<int[]> coordinateSet = this.model.getCoordinateSet();
        for (int[] coords : coordinateSet) {
            this.drawHex(g, this.translateCoordinates(coords), this.hexSize, this.hexFractions, this.model.getHex(coords));
        }
        // refactor
        this.drawStar(g, this.translateCoordinates(new int[]{-5,2}), 8);
        this.drawStar(g, this.translateCoordinates(new int[]{2,6}), 6);
        this.drawStar(g, this.translateCoordinates(new int[]{3,-2}), 10);
        this.drawStar(g, this.translateCoordinates(new int[]{-5,8}), 16);
        this.drawShip(g, this.translateCoordinates(new int[]{0,0}), this.hexSize);
    }

    private int[] getOffsets(int[] range) {
        return new int[]{range[0], range[3]};
    }

    private int[] calculateFractions(int size) {
        double side = 0.5*size/Math.sqrt(3);
        int[] fractions = new int[]{size/2, (int)side, (int)(side*2), (int)(side*3)};
        return fractions;
    }

    private int[] translateCoordinates(int[] coords) {
        int y = this.hexFractions[3] * (this.offsets[1] - coords[1]) + this.hexSize;
        int x = (this.hexSize * (coords[0] - this.offsets[0])) + (this.hexFractions[0] * (this.offsets[1] - coords[1]))  + this.hexSize;
        return new int[]{x,y};
    }

    private void drawHex(Graphics g, int[] coords, int size, int[] fractions, HexModel hex) {

        int[] x = new int[]{coords[0], coords[0]+fractions[0], coords[0]-fractions[0]};
        int[] xPoints = new int[]{x[0], x[1], x[1], x[0], x[2], x[2]};

        int[] y = new int[]{coords[1]-fractions[2], coords[1]-fractions[1], coords[1]+fractions[1], coords[1]+fractions[2]};
        int[] yPoints = new int[]{y[0], y[1], y[2], y[3], y[2], y[1]};

        g.drawPolygon(xPoints, yPoints, 6);
    }

    private void drawShip(Graphics g, int[] coords, int size) {
        int[] xPoints = new int[]{coords[0], coords[0]+size/8, coords[0], coords[0]-size/8, coords[0]};
        int[] yPoints = new int[]{coords[1]-size/4, coords[1]+size/4, coords[1], coords[1]+size/4, coords[1]-size/4};
        g.fillPolygon(xPoints, yPoints, 5);
    }

    private void drawStar(Graphics g, int[] coords, int radius) {
        int diameter = radius*2;
        g.fillOval(coords[0]-radius, coords[1]-radius, diameter, diameter);
    }
}
