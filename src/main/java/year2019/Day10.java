package year2019;

import shared.math.FactorFinders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class Day10 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final ArrayList<char[]> inputRows = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var s = br.readLine();
            while (s != null) {
                inputRows.add(s.toCharArray());
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        int height = inputRows.size(), width = inputRows.getFirst().length;
        ArrayList<Asteroid> list = new ArrayList<>();
        Asteroid[][] grid = new Asteroid[height][width];
        for (int y = 0; y < height; y++) {
            var inputRow = inputRows.get(y);
            var asteRow = grid[y];
            for (int x = 0; x < width; x++) if (inputRow[x] == '#') {
                var asteroid = new Asteroid(y, x);
                asteRow[x] = asteroid;
                list.add(asteroid);
            }
        }
        for (int i = 0; i < list.size()-1; i++) {
            var a = list.get(i);
            int ax = a.x, ay = a.y;
            boolean blocked = false;
            for (int y = ay-1; y >= 0; y--) {
                var o = grid[y][ax];
                if (o == null) continue;
                if (blocked) { if (a.nonVisibles.add(o)) o.nonVisibles.add(a); }
                else { blocked = true; if (a.visibles.add(o)) o.visibles.add(a); }
            }
            blocked = false;
            for (int y = ay+1; y < height; y++) {
                var o = grid[y][ax];
                if (o == null) continue;
                if (blocked) { if (a.nonVisibles.add(o)) o.nonVisibles.add(a); }
                else { blocked = true; if (a.visibles.add(o)) o.visibles.add(a); }
            }
            blocked = false;
            for (int x = ax-1; x >= 0; x--) {
                var o = grid[ay][x];
                if (o == null) continue;
                if (blocked) { if (a.nonVisibles.add(o)) o.nonVisibles.add(a); }
                else { blocked = true; if (a.visibles.add(o)) o.visibles.add(a); }
            }
            blocked = false;
            for (int x = ax+1; x < width; x++) {
                var o = grid[ay][x];
                if (o == null) continue;
                if (blocked) { if (a.nonVisibles.add(o)) o.nonVisibles.add(a); }
                else { blocked = true; if (a.visibles.add(o)) o.visibles.add(a); }
            }
            for (int j = i+1; j < list.size(); j++) {
                var b = list.get(j); int by = b.y, bx = b.x;
                if (ax == bx || ay == by) continue;
                var slope = new Slope(by-ay, bx-ax);
                boolean negative = slope.negative;
                int y = ay, x = ax, rise = slope.numerator, run = slope.denominator;
                blocked = false;
                if (bx > ax) {
                    while ((x += run) < width && (negative ? (y -= rise) >= 0 : (y += rise) < height)) {
                        var o = grid[y][x];
                        if (o == null) continue;
                        if (blocked) { if (a.nonVisibles.add(o)) o.nonVisibles.add(a); }
                        else { blocked = true; if (a.visibles.add(o)) o.visibles.add(a); }
                    }
                    blocked = false;
                    y = by; x = bx;
                    while ((x -= run) >= 0 && (negative ? (y += rise) < height : (y -= rise) >= 0)) {
                        var o = grid[y][x];
                        if (o == null) continue;
                        if (blocked) { if (b.nonVisibles.add(o)) o.nonVisibles.add(b); }
                        else { blocked = true; if (b.visibles.add(o)) o.visibles.add(b); }
                    }
                } else {
                    while ((x -= run) >= 0 && (negative ? (y += rise) < height : (y -= rise) >= 0)) {
                        var o = grid[y][x];
                        if (o == null) continue;
                        if (blocked) { if (a.nonVisibles.add(o)) o.nonVisibles.add(a); }
                        else { blocked = true; if (a.visibles.add(o)) o.visibles.add(a); }
                    }
                    blocked = false;
                    y = by; x = bx;
                    while ((x += run) < width && (negative ? (y -= rise) >= 0 : (y += rise) < height)) {
                        var o = grid[y][x];
                        if (o == null) continue;
                        if (blocked) { if (b.nonVisibles.add(o)) o.nonVisibles.add(b); }
                        else { blocked = true; if (b.visibles.add(o)) o.visibles.add(b); }
                    }
                }
            }
        }
        int maxVisible = 0;
        for (var asteroid : list) {
            int count =  asteroid.visibles.size();
            if (count > maxVisible) maxVisible = count;
        }
        System.out.println("\nVisible asteroid count from the best asteroid: (part 1 answer): " + maxVisible);
    }

    private static class Asteroid {
        int y, x;
        HashSet<Asteroid> visibles = new HashSet<>(), nonVisibles = new HashSet<>();

        Asteroid(int y, int x) {
            this.y = y;
            this.x = x;
        }

    }

    private static class Slope {
        int numerator, denominator;
        boolean negative;

        Slope(int num, int den) {
            negative = num < 0 ^ den < 0;
            numerator = Math.abs(num);
            denominator = Math.abs(den);
            int gcf = FactorFinders.getGreatestCommonFactor(numerator, denominator);
            if (gcf > 1) {
                numerator /= gcf;
                denominator /= gcf;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof Slope o)
                return numerator == o.numerator && denominator == o.denominator && negative == o.negative;
            return false;
        }
    }

}
