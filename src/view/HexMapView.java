package pilot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class HexMapView {

    protected BufferedImage background;
    protected BufferedImage composite;

    protected Color backgroundColor;
    protected Color foregroundColor;
    protected Color starColor;
    protected Color shipColor;
    protected Color[] gravityColors;

    protected final BasicStroke basicStroke = new BasicStroke(1);

    protected final int[] hexInfoCoords = new int[]{0, 50, 80, 200, 50};

    protected HexMapModel model;
    protected Graphics2D g2d;

    protected Dimension mapDimension;
    protected HashMap<String, Integer> hexDimensions;
    protected int[] range;
    protected int[] offsets;
    protected float slope;

    protected HashMap<Integer, HashMap<Integer, int[]>> pixelLookup;


    public HexMapView(HexMapModel model, Dimension mapDimension, ConfigController config) {
        this.model = model;
        this.mapDimension = mapDimension;

        int[] coordRange = model.getRange();
        this.offsets = this.getOffsets(coordRange);
        this.hexDimensions = this.calculateDimensions(config.getInt("hexSize"));
        this.hexInfoCoords[0] = mapDimension.width-hexInfoCoords[3];
        this.range = this.getRange(coordRange);
        this.slope = (float)this.hexDimensions.get("SIDE_HEIGHT")/this.hexDimensions.get("HALF_WIDTH");

        this.background = new BufferedImage(mapDimension.width, mapDimension.height, BufferedImage.TYPE_INT_RGB);
        this.composite = new BufferedImage(mapDimension.width, mapDimension.height, BufferedImage.TYPE_INT_RGB);

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

        this.backgroundColor = config.getColor("bgColor");
        this.foregroundColor = config.getColor("fgColor");
        this.shipColor = config.getColor("shipColor");
        this.starColor = config.getColor("starColor");
        this.gravityColors = new Color[]{config.getColor("gravColor1"),
                                         config.getColor("gravColor2"),
                                         config.getColor("gravColor3"),
                                         config.getColor("gravColor4"),
                                         config.getColor("gravColor5")};

        this.updateBackground();
    }

    public BufferedImage getRenderedFrame() {
        this.updateComposite();
        return this.composite;
    }

    protected void updateBackground() {
        Graphics2D g2d = this.background.createGraphics();

        g2d.setBackground(this.backgroundColor);
        g2d.setColor(this.foregroundColor);

        ArrayList<int[]> coordinateSet = this.model.getCoordinateSet();
        for (int[] coords : coordinateSet) {
            this.drawHex(g2d, this.coordsToPixels(coords), this.model.getHex(coords));
        }
    }

    protected Graphics2D updateComposite() {
        Graphics2D g2d = this.composite.createGraphics();
        g2d.drawImage(this.background, 0, 0, null);
        return g2d;
    }

    protected int[] getOffsets(int[] range) {
        return new int[]{range[0], range[3]};
    }

    protected int[] getRange(int[] range) {
        int[] topLeft = this.coordsToPixels(new int[]{range[0], range[3]});
        int[] bottomRight = this.coordsToPixels(new int[]{range[2], range[1]});
        return new int[]{
            topLeft[0] - this.hexDimensions.get("HALF_WIDTH"),
            topLeft[1] - this.hexDimensions.get("HALF_HEIGHT"),
            (range[2] - range[1] +1) * this.hexDimensions.get("WIDTH"),
            bottomRight[1] + this.hexDimensions.get("HALF_HEIGHT")
        };
    }

    protected HashMap<String, Integer> calculateDimensions(int size) {
        double side = 0.5*size/Math.sqrt(3);
        HashMap<String, Integer> dimensions = new HashMap<String, Integer>();
        dimensions.put("WIDTH", size);
        dimensions.put("HALF_WIDTH", size/2);
        dimensions.put("HALF_HEIGHT", (int)(side*2));
        dimensions.put("SIDE_HEIGHT", (int)side);
        dimensions.put("ROW_HEIGHT", (int)(side*3));
        return dimensions;
    }

    protected int[] coordsToPixels(int[] coords) {
        int y = this.hexDimensions.get("ROW_HEIGHT") * (this.offsets[1] - coords[1]) + this.hexDimensions.get("WIDTH");
        int x = (this.hexDimensions.get("WIDTH") * (coords[0] - this.offsets[0])) + (this.hexDimensions.get("HALF_WIDTH") * (this.offsets[1] - coords[1]))  + this.hexDimensions.get("WIDTH");
        return new int[]{x,y};
    }

    protected int[] pixelsToCoords(int x, int y) {
        if (y < this.range[1])
            return null;

        int[] coords = null;
        int height = this.hexDimensions.get("ROW_HEIGHT");

        int row = 0;
        for (int i=this.range[1]+height; i<this.range[3]; i+=height) {
            if (y < i) {

                if (y < i-this.hexDimensions.get("HALF_HEIGHT")) {

                    // We're in a diagonal row, need to consider X when determining Y
                    int columnOffset = (row -1) * this.hexDimensions.get("HALF_WIDTH");
                    int firstX = this.range[0] + columnOffset;
                    if (x < firstX)
                        return null;

                    int width = this.hexDimensions.get("HALF_WIDTH");
                    int lastX = firstX + this.range[2] + (width * 3);
                    int column = 0;
                    for (int j=firstX+width; j<lastX; j+=width) {
                        if (x < j) {
                            if (column % 2 == 0) {
                                float slope = ((float)(i-this.hexDimensions.get("HALF_HEIGHT")-y))/(j-x);
                                if (slope < this.slope) {
                                    coords = new int[]{ this.offsets[0] + (column/2) - 1,
                                                        this.offsets[1] - row};
                                } else {
                                    coords = new int[]{ this.offsets[0] + (column/2),
                                                        this.offsets[1] - row + 1};
                                }
                            }
                            break;
                        }
                        column++;
                    }

                } else {

                    // We know Y for sure
                    int columnOffset = row * this.hexDimensions.get("HALF_WIDTH");
                    int firstX = this.range[0] + columnOffset;
                    if (x < firstX)
                        return null;

                    int width = this.hexDimensions.get("WIDTH");
                    int lastX = firstX + this.range[2] + width;
                    int column = 0;
                    for (int j=firstX+width; j<lastX; j+=width) {
                        if (x < j) {
                            coords = new int[]{this.offsets[0] + column, this.offsets[1] - row};
                            break;
                        }
                        column++;
                    }
                }
                break;
            }
            row++;
        }

        return coords;
    }

    protected void drawHex(Graphics2D g, int[] coords, HexModel hex) {

        int[] x = new int[]{coords[0], coords[0]+this.hexDimensions.get("HALF_WIDTH"), coords[0]-this.hexDimensions.get("HALF_WIDTH")};
        int[] xPoints = new int[]{x[0], x[1], x[1], x[0], x[2], x[2]};

        int[] y = new int[]{coords[1]-this.hexDimensions.get("HALF_HEIGHT"), coords[1]-this.hexDimensions.get("SIDE_HEIGHT"), coords[1]+this.hexDimensions.get("SIDE_HEIGHT"), coords[1]+this.hexDimensions.get("HALF_HEIGHT")};
        int[] yPoints = new int[]{y[0], y[1], y[2], y[3], y[2], y[1]};

        if (hex.tidalForce > 0) {
            try {
                g.setColor(this.gravityColors[hex.tidalForce-1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                g.setColor(Color.WHITE);
            }
            g.fillPolygon(xPoints, yPoints, 6);
        }

        if (hex.ship != null) {
            this.drawShip(g, hex.ship);
        }

        if (hex.star != null) {
            this.drawStar(g, hex.star);
        }

        g.setColor(this.foregroundColor);
        g.drawPolygon(xPoints, yPoints, 6);
    }

    protected void drawShip(Graphics g, ShipModel ship) {
        if (ship != null) {
            g.setColor(this.shipColor);
            int size = this.hexDimensions.get("WIDTH");
            int[] coords = this.coordsToPixels(ship.getCoords());
            int[] xPoints = new int[]{coords[0], coords[0]+size/8, coords[0], coords[0]-size/8, coords[0]};
            int[] yPoints = new int[]{coords[1]-size/4, coords[1]+size/4, coords[1], coords[1]+size/4, coords[1]-size/4};
            g.fillPolygon(xPoints, yPoints, 5);
        }
    }

    protected void drawStar(Graphics g, StarModel star) {
        int radius = star.getVolume();
        int diameter = radius*2;
        int[] coords = this.coordsToPixels(star.getCoords());
        g.setColor(this.starColor);
        g.fillOval(coords[0]-radius, coords[1]-radius, diameter, diameter);
    }
}
