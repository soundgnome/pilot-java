package pilot;

import java.util.ArrayList;

public class HexMapModel {

    private HexModel[][] hexes;
    private int[] offsets;
    private int[] range;
    private ArrayList<int[]> coordinateSet = new ArrayList<int[]>();

    private ArrayList<StarModel> stars = new ArrayList<StarModel>();

    public HexMapModel(int startX, int startY, int width, int height) {

        this.offsets = new int[]{startX, startY};
        this.range = new int[]{startX, startY, startX+width-1, startY+height-1};

        this.hexes = new HexModel[width][height];
        for (int i=0; i<width; i++) {

            for (int j=0; j<height; j++) {
                this.hexes[i][j] = new HexModel();
                this.coordinateSet.add(new int[]{i+this.offsets[0],j+this.offsets[1]});
            }
        }
    }

    public void addShip(ShipModel ship) {
        int[] coords = ship.getCoords();
        this.getHex(coords).ship = ship;
    }

    public void addStar(StarModel star) {
        int[] coords = star.getCoords();
        HexModel hex = this.getHex(coords);
        hex.star = star;
        int force = star.getMass();
        hex.tidalForce += force;
        for (int i=1; i<force; i++) {
            for (HexModel neighbor : this.getNeighbors(coords, i)) {
                neighbor.tidalForce += force-i;
            }
        }
    }

    public int[] getRange() {
        return this.range;
    }

    public ArrayList<int[]> getCoordinateSet() {
        return this.coordinateSet;
    }

    public HexModel getHex(int[] coords) {
        HexModel hex;
        try {
            hex = this.hexes[coords[0]-this.offsets[0]][coords[1]-this.offsets[1]];
        } catch (ArrayIndexOutOfBoundsException e) {
            hex = null;
        }
        return hex;
    }

    public int[] getNeighborCoords(int[] initialCoords, int direction) {
        int[] coords = initialCoords.clone();
        switch (direction) {
        case 0:
            coords[0] += 1;
            coords[1] += 1;
            break;
        case 1:
            coords[0] += 1;
            break;
        case 2:
            coords[1] -= 1;
            break;
        case 3:
            coords[0] -= 1;
            coords[1] -= 1;
            break;
        case 4:
            coords[0] -= 1;
            break;
        case 5:
            coords[1] += 1;
            break;
        }
        return coords;
    }

    public ArrayList<HexModel> getNeighbors(int[] coords, int distance) {
        ArrayList<HexModel> hexes = new ArrayList<HexModel>();

        for (int i=0; i<distance; i++) {
            coords = this.getNeighborCoords(coords, 4);
        }

        for (int i=0; i<6; i++) {
            for (int j=0; j<distance; j++) {
                coords = this.getNeighborCoords(coords, i);
                HexModel hex = this.getHex(coords);
                if (hex != null)
                    hexes.add(hex);
            }
        }
        return hexes;
    }

}
