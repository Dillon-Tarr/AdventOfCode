package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Day10 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<Point> points = new ArrayList<>();
    static private int secondsPassed = 0, lowY, highY, yDiff, lowX, highX;
    static private final Scanner scanner = new Scanner(System.in);

    static void main() {
        getAndProcessInputData();
        solve();
    }

    private static void getAndProcessInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (String s : inputStrings) {
            String[] positionStrings = s.substring(s.indexOf('<')+1, s.indexOf('>')).split(",");
            int x = Integer.parseInt(positionStrings[0].trim()), y = Integer.parseInt(positionStrings[1].trim());
            String[] velocityStrings = s.substring(s.lastIndexOf('<')+1, s.lastIndexOf('>')).split(",");
            int xV = Integer.parseInt(velocityStrings[0].trim()), yV = Integer.parseInt(velocityStrings[1].trim());
            points.add(new Point(y, x, yV, xV));
        }
    }

    private static void solve() {
        int initialYDiff = yDiff;
        while (true) {
            if (yDiff <= 50) {
                for (int y = lowY; y <= highY; y++) {
                    System.out.println();
                    xLoop: for (int x = lowX; x < highX; x++) {
                        for (var point : points) if (point.x == x && point.y == y) {
                            System.out.print('#'); continue xLoop;
                        } // Otherwise, if no point matches this ^ position:
                        System.out.print(' ');
                    }
                }
                System.out.println("\nSeconds passed: "+secondsPassed+"; Check above for message. yDiff: "+yDiff);
                System.out.println("Press Enter to let another second pass. Type anything and press Enter to exit.");
                if (!scanner.nextLine().isEmpty()) break;
                else updatePoints();
            } else {
                updatePoints();
                if (yDiff > initialYDiff) {
                    System.out.println("You may have missed the message. Scroll up to find it.");
                    System.exit(0);
                }
            }
        }
    }

    private static void updatePoints() {
        highY = Integer.MIN_VALUE; highX = Integer.MIN_VALUE;
        lowY = Integer.MAX_VALUE; lowX = Integer.MAX_VALUE;
        for (var point : points) {
            point.update();
            if (point.y > highY) highY = point.y;
            if (point.y < lowY) lowY = point.y;
            if (point.x > highX) highX = point.x;
            if (point.x < lowX) lowX = point.x;
        }
        yDiff = highY-lowY;
        secondsPassed++;
    }

    private static class Point {
        int y, x, yVelocity, xVelocity;

        Point(int y, int x, int yVelocity, int xVelocity) {
            this.y = y; this.x = x; this.yVelocity = yVelocity; this.xVelocity = xVelocity;
            if (y > highY) { highY = y; yDiff = highY-lowY; } if (y < lowY) { lowY = y; yDiff = highY-lowY; }
            if (x > highX) { highX = x; } if (x < lowX) { lowX = x; }
        }

        void update() {
            y += yVelocity;
            x += xVelocity;
        }

    }

}
