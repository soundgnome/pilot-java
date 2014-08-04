package pilot;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

public class HexMapView extends Canvas {

    private static final long serialVersionUID = 1L;

    private final Color backgroundColor = Color.BLACK;
    private final Color foregroundColor = Color.WHITE;
    private final Color[] gravityColors = new Color[]{new Color(128, 0, 0),
                                                new Color(255, 0, 0),
                                                new Color(255, 96, 0),
                                                new Color(255, 192, 0),
                                                new Color(255, 255, 128)};

    private final BasicStroke basicStroke = new BasicStroke(1);
    private final BasicStroke gravityStroke = new BasicStroke(5);

    private final int[] hexInfoCoords = new int[]{0, 50, 80, 200, 50};

    private HexMapModel model;

    private Dimension mapDimension;
    private HashMap<String, Integer> hexDimensions;
    private int[] offsets;

    private HashMap<Integer, HashMap<Integer, int[]>> pixelLookup;
    private int[] previewHexCoords = new int[]{0,0};


    public HexMapView(HexMapModel model, Dimension mapDimension, int hexSize) {
        this.model = model;
        this.mapDimension = mapDimension;
        this.offsets = this.getOffsets(model.getRange());
        this.hexDimensions = this.calculateDimensions(hexSize);
        this.hexInfoCoords[0] = mapDimension.width-hexInfoCoords[3];

        this.pixelLookup = new HashMap<Integer, HashMap<Integer, int[]>>();
        ArrayList<int[]> coordinateSet = this.model.getCoordinateSet();
        for (int[] coords : coordinateSet) {
            int[] pixels = this.coordsToPixels(coords);
            HashMap<Integer, int[]> row = this.pixelLookup.get(pixels[1]);
            if (row == null) {
                row = new HashMap<Integer, int[]>();
                this.pixelLookup.put(pixels[1], row);
            }
            row.put(pixels[0], coords);
        }
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
            this.drawHex(g, this.coordsToPixels(coords), this.model.getHex(coords));
        }
    }

    public void updateHexInfo(int x, int y) {
        int[] coords = this.pixelsToCoords(x, y);
        if (coords[0] != this.previewHexCoords[0] || coords[1] != this.previewHexCoords[1]) {
            this.previewHexCoords = coords;
            Graphics2D g = (Graphics2D)this.getGraphics();
            g.setBackground(this.backgroundColor);
            g.clearRect(this.hexInfoCoords[0], this.hexInfoCoords[1], this.hexInfoCoords[3], this.hexInfoCoords[4]);
            g.setColor(this.foregroundColor);
            g.drawString("Cursor Position: "+coords[0]+", "+coords[1], this.hexInfoCoords[0], this.hexInfoCoords[2]);
        }
    }

    private int[] getOffsets(int[] range) {
        return new int[]{range[0], range[3]};
    }

    private HashMap<String, Integer> calculateDimensions(int size) {
        double side = 0.5*size/Math.sqrt(3);
        HashMap<String, Integer> dimensions = new HashMap<String, Integer>();
        dimensions.put("WIDTH", size);
        dimensions.put("HALF_WIDTH", size/2);
        dimensions.put("HALF_HEIGHT", (int)(side*2));
        dimensions.put("SIDE_HEIGHT", (int)side);
        dimensions.put("ROW_HEIGHT", (int)(side*3));
        return dimensions;
    }

    private int[] coordsToPixels(int[] coords) {
        int y = this.hexDimensions.get("ROW_HEIGHT") * (this.offsets[1] - coords[1]) + this.hexDimensions.get("WIDTH");
        int x = (this.hexDimensions.get("WIDTH") * (coords[0] - this.offsets[0])) + (this.hexDimensions.get("HALF_WIDTH") * (this.offsets[1] - coords[1]))  + this.hexDimensions.get("WIDTH");
        return new int[]{x,y};
    }

    private int[] pixelsToCoords(int x, int y) {
        y = y - (y % this.hexDimensions.get("ROW_HEIGHT"));
        x = x - (x % this.hexDimensions.get("WIDTH"));
        return new int[]{x, y};
    }

    private void drawHex(Graphics2D g, int[] coords, HexModel hex) {

        int[] x = new int[]{coords[0], coords[0]+this.hexDimensions.get("HALF_WIDTH"), coords[0]-this.hexDimensions.get("HALF_WIDTH")};
        int[] xPoints = new int[]{x[0], x[1], x[1], x[0], x[2], x[2]};

        int[] y = new int[]{coords[1]-this.hexDimensions.get("HALF_HEIGHT"), coords[1]-this.hexDimensions.get("SIDE_HEIGHT"), coords[1]+this.hexDimensions.get("SIDE_HEIGHT"), coords[1]+this.hexDimensions.get("HALF_HEIGHT")};
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
            this.drawShip(g, hex.ship);
        }

        g.drawPolygon(xPoints, yPoints, 6);
    }

    private void drawShip(Graphics g, ShipModel ship) {
        if (ship != null) {
            int size = this.hexDimensions.get("WIDTH");
            int[] coords = this.coordsToPixels(ship.getCoords());
            int[] xPoints = new int[]{coords[0], coords[0]+size/8, coords[0], coords[0]-size/8, coords[0]};
            int[] yPoints = new int[]{coords[1]-size/4, coords[1]+size/4, coords[1], coords[1]+size/4, coords[1]-size/4};
            g.fillPolygon(xPoints, yPoints, 5);
        }
    }

    private void drawStar(Graphics g, StarModel star) {
        int radius = star.getVolume();
        int diameter = radius*2;
        int[] coords = this.coordsToPixels(star.getCoords());
        g.fillOval(coords[0]-radius, coords[1]-radius, diameter, diameter);
    }
}
