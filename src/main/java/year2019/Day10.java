package year2019;

import shared.math.FactorFinders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

class Day10 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private int height, width;
    static private final ArrayList<Asteroid> list = new ArrayList<>();
    static private Asteroid[][] grid;
    static private Asteroid monitoringStation;

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        checkWhichAsteroidsSeeEachOther();
        pickMonitoringStation();
        determine200thAsteroid();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputRows = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var s = br.readLine();
            while (s != null) {
                inputRows.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        height = inputRows.size(); width = inputRows.getFirst().length(); grid = new Asteroid[height][width];
        for (int y = 0; y < height; y++) {
            var inputRow = inputRows.get(y).toCharArray();
            var asteRow = grid[y];
            for (int x = 0; x < width; x++) if (inputRow[x] == '#') {
                var asteroid = new Asteroid(y, x);
                asteRow[x] = asteroid;
                list.add(asteroid);
            }
        }
    }

    private static void checkWhichAsteroidsSeeEachOther() {
        for (int i = 0; i < list.size(); i++) {
            var a = list.get(i); int ax = a.x, ay = a.y;
            for (int y = ay-1; y >= 0; y--) if (grid[y][ax] != null) { a.seeEachOther(grid[y][ax]); break; } // look up
            for (int y = ay+1; y < height; y++) if (grid[y][ax] != null) { a.seeEachOther(grid[y][ax]); break; } // look down
            for (int x = ax-1; x >= 0; x--) if (grid[ay][x] != null) { a.seeEachOther(grid[ay][x]); break; } // look left
            for (int x = ax+1; x < width; x++) if (grid[ay][x] != null) { a.seeEachOther(grid[ay][x]); break; } // look right
            for (int j = i+1; j < list.size(); j++) { // for every pair of asteroids, a and b, look toward each other:
                var b = list.get(j); int by = b.y, bx = b.x;
                if (ax == bx || ay == by) continue; // <- because we already checked up, down, left, and right
                var slope = new Slope(by-ay, bx-ax); int dy = slope.dy, dx = slope.dx;
                Asteroid l, r; int y, x, ry, rx; // ly and lx unnecessary; starting from left asteroid's values only once.
                if (bx > ax) { l = a; y = ay; x = ax; r = b; ry = by; rx = bx; } // a is left of b
                else { l = b; y = by; x = bx; r = a; ry = ay; rx = ax; } // b is left of a
                if (slope.negative) { // left asteroid looking right and negative (UP):
                    while ((x += dx) < width && (y -= dy) >= 0) if (grid[y][x]!= null) { l.seeEachOther(grid[y][x]); break; }
                    y = ry; x = rx; // right asteroid looking left and positive (DOWN):
                    while ((x -= dx) >= 0 && (y += dy) < height) if (grid[y][x] != null) { r.seeEachOther(grid[y][x]); break; }
                } else { // left asteroid looking right and positive (DOWN):
                    while ((x += dx) < width && (y += dy) < height) if (grid[y][x]!= null) { l.seeEachOther(grid[y][x]); break; }
                    y = ry; x = rx; // right asteroid looking left and negative (UP)
                    while ((x -= dx) >= 0 && (y -= dy) >= 0) if (grid[y][x] != null) { r.seeEachOther(grid[y][x]); break; }
                }
            }
        }
    }

    private static void pickMonitoringStation() {
        int maxVisible = 0;
        for (var asteroid : list) {
            int count =  asteroid.visibles.size();
            if (count > maxVisible) { maxVisible = count; monitoringStation = asteroid; }
        }
        System.out.println("\nVisible asteroid count from the best asteroid: (part 1 answer): "+ maxVisible);
    }

    private static void determine200thAsteroid() {
        if (list.size() < 201) throw new RuntimeException(
                "200th destroyed asteroid can't be found, because there are only "+list.size()+"including the monitoring station!");
        ArrayList<Asteroid> visibles = new ArrayList<>(monitoringStation.visibles);
        int msy = monitoringStation.y, msx = monitoringStation.x;
        Asteroid firstToDestroy = null;
        for (var o : visibles) if (o.x == msx && o.y < msy) { firstToDestroy = o; break; }
        for (var o : visibles) {
            int oy = o.y, ox = o.x;
            // msy - oy rather than oy - msy because y directionality is reversed from standard Cartesian coordinates:
            double angle = Math.toDegrees(Math.atan2(msy - oy, ox - msx));
            if (angle < 0) angle += 360;
            o.angleFromLaserStation = angle;
        }
        visibles.sort(Comparator.comparing((Asteroid asteroid) -> asteroid.angleFromLaserStation).reversed());
        Asteroid twoHundredth;
        int startI = 0;
        if (firstToDestroy == null) { // due to no asteroid being directly up from the monitoring station
            for (int i = visibles.size()-1; i >= 0; i--) if (visibles.get(i).angleFromLaserStation < 90) { startI = i; break; }
        } else for (int i = 0; i < visibles.size(); i++) if (visibles.get(i) == firstToDestroy) { startI = i; break; }
        int allTimeVisibleCount = visibles.size();
        if (allTimeVisibleCount >= 200) {
            int trueI = startI + 199; if (trueI >= allTimeVisibleCount) trueI -= allTimeVisibleCount;
            twoHundredth = visibles.get(trueI);
        } else {
            var queue = new ArrayDeque<Asteroid>();
            for (int i = startI; i < visibles.size(); i++) queue.add(visibles.get(i));
            for (int i = 0; i < startI; i++) queue.add(visibles.get(i));
            while (allTimeVisibleCount < 200) {
                var o = queue.remove(); int y = o.y, x = o.x;
                if (x == msx) {
                    if (y < msy) { while (--y >= 0) if (grid[y][x] != null) { queue.add(grid[y][x]); allTimeVisibleCount++; break; } }
                    else while (++y < height) { if (grid[y][x] != null) { queue.add(grid[y][x]); allTimeVisibleCount++; break; } }
                } else {
                    Slope slope = new Slope(y-msy, x-msx); int dy = slope.dy, dx = slope.dx;
                    if (x > msx) {
                        if (slope.negative) while ((x += dx) < width && (y -= dy) >= 0) {
                            if (grid[y][x]!= null) { queue.add(grid[y][x]); allTimeVisibleCount++; break; }
                        } else while ((x += dx) < width && (y += dy) < height) {
                            if (grid[y][x]!= null) { queue.add(grid[y][x]); allTimeVisibleCount++; break; }
                        }
                    } else if (slope.negative) while ((x -= dx) >= 0 && (y += dy) < height) {
                        if (grid[y][x] != null) { queue.add(grid[y][x]); allTimeVisibleCount++; break; }
                    } else while ((x -= dx) >= 0 && (y -= dy) >= 0) {
                        if (grid[y][x] != null) { queue.add(grid[y][x]); allTimeVisibleCount++; break; }
                    }
                }
            }
            twoHundredth = queue.removeLast();
        }
        int part2Answer = (100*twoHundredth.x)+twoHundredth.y;
        System.out.println("\nPart 2 answer: "+part2Answer);
    }

    private static class Asteroid {
        int y, x;
        HashSet<Asteroid> visibles = new HashSet<>();
        double angleFromLaserStation;

        Asteroid(int y, int x) {
            this.y = y;
            this.x = x;
        }

        void seeEachOther(Asteroid o) { if (visibles.add(o)) o.visibles.add(this); }

    }

    private static class Slope {
        int dy, dx;
        boolean negative;

        Slope(int yChange, int xChange) {
            negative = yChange < 0 ^ xChange < 0;
            dy = Math.abs(yChange);
            dx = Math.abs(xChange);
            int gcf = FactorFinders.getGreatestCommonFactor(dy, dx);
            if (gcf > 1) { dy /= gcf; dx /= gcf; }
        }

    }

}
