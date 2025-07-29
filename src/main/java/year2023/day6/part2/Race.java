package year2023.day6.part2;

class Race {
    private final long numberOfWaysToBeatTheRecord;

    Race(long duration, long distanceRecord) {
        long wayCount = 0;
        for (long holdTime = 0; holdTime < duration; holdTime++) {
            long movementTime = duration - holdTime;
            if (holdTime*movementTime > distanceRecord) wayCount++;
        }
        this.numberOfWaysToBeatTheRecord = wayCount;
    }

    public long getNumberOfWaysToBeatTheRecord() {return numberOfWaysToBeatTheRecord;}
}
