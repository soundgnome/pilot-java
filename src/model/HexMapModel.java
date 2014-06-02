package pilot;

import java.util.ArrayList;

public class HexMapModel {

    private HexModel[][] hexes;
    private int[] offsets;
    private int[] range;
    private ArrayList<int[]> coordinateSet;

    public HexMapModel(int startX, int startY, int width, int height) {

        this.offsets = new int[]{startX, startY};
        this.range = new int[]{startX, startY, startX+width-1, startY+height-1};

        this.coordinateSet = new ArrayList<int[]>();
        this.hexes = new HexModel[width][height];
        for (int i=0; i<width; i++) {

            for (int j=0; j<height; j++) {
                this.hexes[i][j] = new HexModel();
                this.coordinateSet.add(new int[]{i+this.offsets[0],j+this.offsets[1]});
            }
        }
    }

    public HexModel getHex(int[] coords) {
        return this.hexes[coords[0]-this.offsets[0]][coords[1]-this.offsets[1]];
    }

    public int[] getRange() {
        return this.range;
    }

    public ArrayList<int[]> getCoordinateSet() {
        return this.coordinateSet;
    }
}