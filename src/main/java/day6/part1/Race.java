package day6.part1;

public class Race {
    private final int numberOfWaysToBeatTheRecord;

    Race(int duration, int distanceRecord) {
        int wayCount = 0;
        for (int holdTime = 0; holdTime < duration; holdTime++) {
            int movementTime = duration - holdTime;
            if (holdTime*movementTime > distanceRecord) wayCount++;
        }
        this.numberOfWaysToBeatTheRecord = wayCount;
    }

    public int getNumberOfWaysToBeatTheRecord() {return numberOfWaysToBeatTheRecord;}
}
