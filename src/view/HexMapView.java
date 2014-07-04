package pilot;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class HexMapView extends Canvas {

    private Color backgroundColor = Color.BLACK;
    private Color foregroundColor = Color.WHITE;
    private BasicStroke basicStroke = new BasicStroke(1);
    private Color[] gravityColors = new Color[]{new Color(128, 0, 0),
                                                new Color(255, 0, 0),
                                                new Color(255, 96, 0),
                                                new Color(255, 192, 0),
                                                new Color(255, 255, 128)};
    private BasicStroke gravityStroke = new BasicStroke(5);

    private HexMapModel model;

    private Dimension mapDimension;
    private int hexSize;
    private int[] hexFractions;
    private int[] offsets;

    private int[] hexInfoCoords = new int[]{0, 50, 80, 200, 50};

    public HexMapView(HexMapModel model, Dimension mapDimension, int hexSize) {
        this.model = model;
        this.mapDimension = mapDimension;
        this.hexSize = hexSize;
        this.offsets = this.getOffsets(model.getRange());
        this.hexFractions = this.calculateFractions(this.hexSize);
        this.hexInfoCoords[0] = mapDimension.width-hexInfoCoords[3];
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
    }

    public void updateHexInfo(int x, int y) {
        Graphics2D g = (Graphics2D)this.getGraphics();
        g.setBackground(this.backgroundColor);
        g.clearRect(this.hexInfoCoords[0], this.hexInfoCoords[1], this.hexInfoCoords[3], this.hexInfoCoords[4]);
        g.setColor(this.foregroundColor);
        g.drawString("Cursor Position: "+x+", "+y, this.hexInfoCoords[0], this.hexInfoCoords[2]);
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

    private void drawHex(Graphics2D g, int[] coords, int size, int[] fractions, HexModel hex) {

        int[] x = new int[]{coords[0], coords[0]+fractions[0], coords[0]-fractions[0]};
        int[] xPoints = new int[]{x[0], x[1], x[1], x[0], x[2], x[2]};

        int[] y = new int[]{coords[1]-fractions[2], coords[1]-fractions[1], coords[1]+fractions[1], coords[1]+fractions[2]};
        int[] yPoints = new int[]{y[0], y[1], y[2], y[3], y[2], y[1]};

        if (hex.star != null) {
            this.drawStar(g, hex.star);
        }

        if (hex.tidalForce > 0) {
            int[] gravX = new int[]{x[0], x[1]-3, x[1]-3, x[0], x[2]+3, x[2]+3};
            int[] gravY = new int[]{y[0]+3, y[1]+2, y[2]-2, y[3]-3, y[2]-2, y[1]+2};
            try {
                g.setColor(this.gravityColors[hex.tidalForce-1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                g.setColor(Color.WHITE);
            }
            g.setStroke(this.gravityStroke);
            g.drawPolygon(gravX, gravY, 6);
            g.setColor(this.foregroundColor);
            g.setStroke(this.basicStroke);
        }

        if (hex.ship != null) {
            this.drawShip(g, hex.ship, size);
        }

        g.drawPolygon(xPoints, yPoints, 6);
    }

    private void drawShip(Graphics g, ShipModel ship, int size) {
        if (ship != null) {
            int[] coords = this.translateCoordinates(ship.getCoords());
            int[] xPoints = new int[]{coords[0], coords[0]+size/8, coords[0], coords[0]-size/8, coords[0]};
            int[] yPoints = new int[]{coords[1]-size/4, coords[1]+size/4, coords[1], coords[1]+size/4, coords[1]-size/4};
            g.fillPolygon(xPoints, yPoints, 5);
        }
    }

    private void drawStar(Graphics g, StarModel star) {
        int radius = star.getVolume();
        int diameter = radius*2;
        int[] coords = this.translateCoordinates(star.getCoords());
        g.fillOval(coords[0]-radius, coords[1]-radius, diameter, diameter);
    }
}
