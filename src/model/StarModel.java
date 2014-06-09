package pilot;

public class StarModel {

    private int[] coords;
    private int volume;
    private int mass;

    public StarModel(int[] coords, int volume, int mass) {
        this.coords = coords;
        this.volume = volume;
        this.mass = mass;
    }

    public int[] getCoords() {
        return this.coords;
    }

    public int getVolume() {
        return this.volume;
    }

    public int getMass() {
        return this.mass;
    }

}
