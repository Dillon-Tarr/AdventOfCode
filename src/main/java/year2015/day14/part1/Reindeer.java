package year2015.day14.part1;

class Reindeer {
    int speed, goTimeLimit, requiredRestTime;

    Reindeer(String inputString) {
        int firstSpaceIndex = inputString.indexOf(' '), slashIndex = inputString.indexOf('/'),
                secIndex = inputString.indexOf("sec"), lastSpaceIndex = inputString.lastIndexOf(' '),
                secondToLastSpaceIndex = inputString.lastIndexOf(' ', lastSpaceIndex-1);
        speed = Integer.parseInt(inputString.substring(firstSpaceIndex +9, slashIndex-3));
        goTimeLimit = Integer.parseInt(inputString.substring(slashIndex+7, secIndex-1));
        requiredRestTime = Integer.parseInt(inputString.substring(secondToLastSpaceIndex+1, lastSpaceIndex));
    }

    int race () {
        int distanceTravelled = 0, timeElapsed = 0, timeInState = 0;
        boolean goMode = true;
        while (timeElapsed < 2503) {
            timeElapsed++;
            timeInState++;
            if (goMode) {
                distanceTravelled += speed;
                if (timeInState == goTimeLimit) {
                    goMode = false;
                    timeInState = 0;
                }
            } else if (timeInState == requiredRestTime) {
                goMode = true;
                timeInState = 0;
            }
        }
        return distanceTravelled;
    }

}
