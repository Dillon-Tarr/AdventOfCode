package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class Day25 {
    static private final int DAY = 25;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<Point> points = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                points.add(new Point(s.trim().split(",")));
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        for (int i = 0; i < points.size()-1; i++) {
            Point p1 = points.get(i); var p1Constellation = p1.constellation;
            for (int j = i+1; j < points.size(); j++) {
                Point p2 = points.get(j); var p2Constellation = p2.constellation;
                if (p1.constellation == p2Constellation) continue;
                int sum = Math.abs(p1.a-p2.a); if (sum > 3 || ((sum += Math.abs(p1.b-p2.b)) > 3)
                        || ((sum += Math.abs(p1.c-p2.c)) > 3)|| ((sum + Math.abs(p1.d-p2.d)) > 3)) continue;
                ArrayList<Point> smallerConstellation, biggerConstellation;
                if (p1Constellation.size() > p2Constellation.size()) {
                    biggerConstellation = p1Constellation; smallerConstellation = p2Constellation;
                } else {
                    biggerConstellation = p2Constellation; smallerConstellation = p1Constellation;
                    p1Constellation = p2Constellation;
                }
                biggerConstellation.addAll(smallerConstellation);
                for (var p : smallerConstellation) p.constellation = biggerConstellation;
            }
        }
        HashSet<ArrayList<Point>> constellations = new HashSet<>();
        for (Point p : points) constellations.add(p.constellation);
        System.out.println("\nConstellation count: "+constellations.size());
    }

    private static class Point {
        int a, b, c, d;
        ArrayList<Point> constellation = new ArrayList<>();

        Point(String[] coordinateStrings) {
            a = Integer.parseInt(coordinateStrings[0].trim()); b = Integer.parseInt(coordinateStrings[1].trim());
            c = Integer.parseInt(coordinateStrings[2].trim()); d = Integer.parseInt(coordinateStrings[3].trim());
            constellation.add(this);
        }

    }

}
