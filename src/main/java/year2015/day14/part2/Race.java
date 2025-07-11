package year2015.day14.part2;

import java.util.ArrayList;

class Race {
    Reindeer[] racers;
    int leadDistance;
    int timeElapsed;

    Race (ArrayList<Reindeer> reindeer) {
        racers = new Reindeer[reindeer.size()];
        for (int i = 0; i < racers.length; i++) racers[i] = reindeer.get(i);
        leadDistance = 0;
        timeElapsed = 0;
    }

    void race () {
        while(timeElapsed < 2503) {
            raceForOneSecond();
            awardPointToLeaders();
        }
    }

    private void raceForOneSecond() {
        for (Reindeer racer: racers) racer.raceForOneSecond();
        timeElapsed++;
    }

    private void awardPointToLeaders() {
        int distance;
        for (Reindeer racer : racers) {
            distance = racer.getDistanceTravelled();
            if (distance > leadDistance) leadDistance = distance;
        }
        for (Reindeer racer : racers) {
            distance = racer.getDistanceTravelled();
            if (distance == leadDistance) racer.awardPoint();
        }
    }

    int getHighestPointCount() {
        int highestPointCount = Integer.MIN_VALUE, points;
        for (Reindeer racer : racers) {
            points = racer.getPoints();
            if (points > highestPointCount) highestPointCount = points;
        }
        return highestPointCount;
    }

}
