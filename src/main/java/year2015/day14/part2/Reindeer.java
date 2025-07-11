package year2015.day14.part2;

class Reindeer {
    private final int speed;
    private final int goTimeLimit;
    private final int requiredRestTime;
    private int distanceTravelled, points, timeInState;
    private boolean isInGoMode;

    Reindeer(String inputString) {
        int firstSpaceIndex = inputString.indexOf(' '), slashIndex = inputString.indexOf('/'),
                secIndex = inputString.indexOf("sec"), lastSpaceIndex = inputString.lastIndexOf(' '),
                secondToLastSpaceIndex = inputString.lastIndexOf(' ', lastSpaceIndex-1);
        speed = Integer.parseInt(inputString.substring(firstSpaceIndex +9, slashIndex-3));
        goTimeLimit = Integer.parseInt(inputString.substring(slashIndex+7, secIndex-1));
        requiredRestTime = Integer.parseInt(inputString.substring(secondToLastSpaceIndex+1, lastSpaceIndex));
        distanceTravelled = 0;
        points = 0;
        timeInState = 0;
        isInGoMode = true;
    }

    void raceForOneSecond() {
        timeInState++;
        if (isInGoMode) {
            distanceTravelled += speed;
            if (timeInState == goTimeLimit) {
                isInGoMode = false;
                timeInState = 0;
            }
        } else if (timeInState == requiredRestTime) {
            isInGoMode = true;
            timeInState = 0;
        }
    }

    int getDistanceTravelled() {
        return distanceTravelled;
    }

    public int getPoints() {
        return points;
    }

    void awardPoint() {
        points++;
    }


}
