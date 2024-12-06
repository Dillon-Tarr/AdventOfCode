package shared;

public class LongCoordinates {
    public long y;
    public long x;

    public LongCoordinates(long y, long x) {
        this.y = y;
        this.x = x;
    }

    @Override
    public String toString() {
        return "y: "+y+", "+"x: "+x;
    }
}
