package shared;

import java.util.Objects;

public class Coordinates {
    public int y;
    public int x;

    public Coordinates(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public void update(int y, int x) {
        this.y = y;
        this.x = x;
    }

    @Override
    public String toString() {
        return "y: "+y+", "+"x: "+x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return y == that.y && x == that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }
}
