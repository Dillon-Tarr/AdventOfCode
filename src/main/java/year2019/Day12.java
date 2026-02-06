package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day12 {
    static private final int DAY = 12;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final Moon[] moons = new Moon[4];

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            var s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (int i = 0; i < 4; i++) {
            var s = inputStrings.get(i);
            s = s.substring(1, s.length()-1);
            var ss = s.split(", ");
            var xs = ss[0]; var ys = ss[1]; var zs = ss[2];
            moons[i] = new Moon(Integer.parseInt(xs.substring(xs.indexOf("=")+1)),
                    Integer.parseInt(ys.substring(ys.indexOf("=")+1)),
                    Integer.parseInt(zs.substring(zs.indexOf("=")+1)));
        }
    }

    private static void solve() {
        long t = 0; long[] loopIntervals = new long[]{-1, -1, -1};
        boolean xLoopFound = false, yLoopFound = false, zLoopFound = false;
        int[] initialState = new int[24]; int stateIndex = 0;
        for (var moon : moons) {
            initialState[stateIndex++] = moon.xp; initialState[stateIndex++] = moon.yp;
            initialState[stateIndex++] = moon.zp; initialState[stateIndex++] = moon.xv;
            initialState[stateIndex++] = moon.yv; initialState[stateIndex++] = moon.zv;
        }
        while (++t < Long.MAX_VALUE) {
            for (int i = 0; i < 3; i++) {
                var a = moons[i];
                for (int j = i+1; j < 4; j++) a.applyGravityWith(moons[j]);
            }
            for (var moon : moons) moon.applyVelocity();
            if (t == 1000) {
                int totalEnergy = 0;
                for (var m : moons) {
                    int potentialEnergy = Math.abs(m.xp)+Math.abs(m.yp)+Math.abs(m.zp);
                    int kineticEnergy = Math.abs(m.xv)+Math.abs(m.yv)+Math.abs(m.zv);
                    totalEnergy += potentialEnergy*kineticEnergy;
                }
                System.out.println("\nPart 1 answer: "+totalEnergy);
            }
            xLoop: if (!xLoopFound) {
                stateIndex = -3;
                for (var moon : moons) if (initialState[stateIndex +=3] != moon.xp ||
                        initialState[stateIndex +=3] != moon.xv) break xLoop;
                xLoopFound = true; loopIntervals[0] = t; if (yLoopFound && zLoopFound) break;
            }
            yLoop: if (!yLoopFound) {
                stateIndex = -2;
                for (var moon : moons) if (initialState[stateIndex +=3] != moon.yp ||
                        initialState[stateIndex +=3] != moon.yv) break yLoop;
                yLoopFound = true; loopIntervals[1] = t; if (xLoopFound && zLoopFound) break;
            }
            zLoop: if (!zLoopFound) {
                stateIndex = -1;
                for (var moon : moons) if (initialState[stateIndex +=3] != moon.zp ||
                        initialState[stateIndex +=3] != moon.zv) break zLoop;
                zLoopFound = true; loopIntervals[2] = t; if (xLoopFound && yLoopFound) break;
            }
        }
        System.out.println("\nTime when state is repeated (part 2 answer): "+
                shared.math.LCMFinders.findLCM(loopIntervals));
    }

    private static class Moon {
        int xp, yp, zp, xv = 0, yv = 0, zv = 0;

        Moon(int x, int y, int z) {
            xp = x; yp = y; zp = z;
        }

        void applyGravityWith(Moon o) {
            if (xp != o.xp) { if (xp < o.xp) { xv++; o.xv--; } else { o.xv++; xv--; } }
            if (yp != o.yp) { if (yp < o.yp) { yv++; o.yv--; } else { o.yv++; yv--; } }
            if (zp != o.zp) { if (zp < o.zp) { zv++; o.zv--; } else { o.zv++; zv--; } }
        }

        void applyVelocity() {
            xp += xv; yp += yv; zp += zv;
        }

    }

}
