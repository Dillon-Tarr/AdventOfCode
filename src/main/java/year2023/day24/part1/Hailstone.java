package year2023.day24.part1;

public class Hailstone {
    long xPosition, yPosition, xVelocity, yVelocity;
    boolean hasPositiveXVelocity, hasPositiveYVelocity;
    float slope, yIntercept;

    Hailstone(long xPos, long yPos, long xVel, long yVel) {
        xPosition = xPos;
        yPosition = yPos;
        xVelocity = xVel;
        hasPositiveXVelocity = xVel > 0;
        yVelocity = yVel;
        hasPositiveYVelocity = yVel > 0;
        slope = (float) yVel/xVel;
        yIntercept = yPos-(xPos*slope);
    }

    @Override
    public String toString() {
        return "x, y positions: "+xPosition+", "+yPosition+"\nx, y velocities: "+xVelocity+", "+yVelocity+"\nslope: "+slope+"\ny-intercept: "+yIntercept;
    }
}
