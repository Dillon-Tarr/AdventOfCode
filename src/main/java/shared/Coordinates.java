package shared;

public class Coordinates {
    public int y;
    public int x;

    public Coordinates(int y, int x) {
        this.y = y;
        this.x = x;
    }

    @Override
    public String toString() {
        return "y: "+y+", "+"x: "+x;
    }
}
