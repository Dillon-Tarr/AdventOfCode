package day3.puzzle2;

public class PotentialGear {
    private final int xIndex;
    private final int yIndex;
    private int adjacentNumberHitCount = 0;
    private boolean isAGear = false;
    private final int[] adjacentNumbers = {0, 0};
    private int gearRatio = 0;

    PotentialGear(int xIndex, int yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public int getXIndex() {return xIndex;}
    public int getYIndex() {return yIndex;}
    public boolean isAGear(){return isAGear;}
    public int getGearRatio() {return gearRatio;}

    public void addAdjacentNumberHit(int adjacentNumber) {
        if (adjacentNumberHitCount < 2) adjacentNumbers[adjacentNumberHitCount] = adjacentNumber;
        adjacentNumberHitCount += 1;
        isAGear = adjacentNumberHitCount == 2;
        if (isAGear) gearRatio = adjacentNumbers[0]*adjacentNumbers[1];
        else this.gearRatio = 0;
    }

}

