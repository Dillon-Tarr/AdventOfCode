package year2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

class Day20 {
    static private final int DAY = 20;
    static private final File INPUT_FILE = new File("input-files/2017/"+DAY+".txt");
    static private final ArrayList<Particle> particles = new ArrayList<>();
    static private final ArrayList<Particle> part2cles = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine(), substring;
            String[] substrings, subSubs;
            int pX, pY, pZ, vX, vY, vZ, aX, aY, aZ;
            while (s != null) {
                substrings = s.split(", ");
                substring = substrings[0]; // Position info:
                substring = substring.substring(3, substring.length()-1);
                subSubs = substring.split(","); pX = Integer.parseInt(subSubs[0]);
                pY = Integer.parseInt(subSubs[1]); pZ = Integer.parseInt(subSubs[2]);
                substring = substrings[1]; // Velocity info:
                substring = substring.substring(3, substring.length()-1);
                subSubs = substring.split(","); vX = Integer.parseInt(subSubs[0]);
                vY = Integer.parseInt(subSubs[1]); vZ = Integer.parseInt(subSubs[2]);
                substring = substrings[2]; // Acceleration info:
                substring = substring.substring(3, substring.length()-1);
                subSubs = substring.split(","); aX = Integer.parseInt(subSubs[0]);
                aY = Integer.parseInt(subSubs[1]); aZ = Integer.parseInt(subSubs[2]);
                particles.add(new Particle(pX, pY, pZ, vX, vY, vZ, aX, aY, aZ));
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (Particle particle : particles) part2cles.add(new Particle(particle));
    }

    private static void solvePart1() {
        boolean atLeastOneParticleIsGettingCloser = true;
        while (atLeastOneParticleIsGettingCloser) {
            atLeastOneParticleIsGettingCloser = false;
            for (Particle particle : particles) {
                particle.update();
                if (particle.getWhetherLastDistanceIncreaseIsNegative()) atLeastOneParticleIsGettingCloser = true;
            }
        }
        int lowestAccel = Integer.MAX_VALUE, accel;
        Particle p, nearest = null;
        for (int i = 0; i < particles.size(); i++) {
            p = particles.get(i);
            accel = p.getManhattanAcceleration();
            if (accel < lowestAccel) {
                lowestAccel = accel;
                nearest = p;
            } else if (accel == lowestAccel && (nearest == null || p.distanceFrom000 < nearest.distanceFrom000)) nearest = p;
        }
        System.out.println("\nIndex of the long-term closest particle to 0,0,0 (part 1 answer): "+particles.indexOf(nearest));
    }

    private static void solvePart2() {
        Particle p1, p2;
        HashSet<Particle> part2clesToRemove = new HashSet<>();
        int iterationsWithoutCollisions = 0;
        while (iterationsWithoutCollisions++ < 50) { // This value is arbitrary. It just has to be high enough.
            for (Particle part2cle : part2cles) part2cle.update();
            for (int i = 0; i < part2cles.size()-1; i++) {
                p1 = part2cles.get(i);
                for (int j = i+1; j < part2cles.size(); j++) {
                    p2 = part2cles.get(j);
                    if (p1.checkIfColliding(p2)) {
                        part2clesToRemove.add(p1);
                        part2clesToRemove.add(p2);
                    }
                }
                if (!part2clesToRemove.isEmpty()) {
                    part2cles.removeAll(part2clesToRemove);
                    part2clesToRemove.clear();
                    i--;
                    iterationsWithoutCollisions = 0;
                }
            }
        }
        System.out.println("\nNumber of remaining particles after collisions are resolved (part 2 answer): "+part2cles.size());
    }

    static class Particle {
        private int pX;
        private int pY;
        private int pZ;
        private int vX;
        private int vY;
        private int vZ;
        private final int aX;
        private final int aY;
        private final int aZ;
        private int distanceFrom000;
        private int lastDistanceIncrease = 0;

        Particle(int p1, int p2, int p3, int v1, int v2, int v3, int a1, int a2, int a3) {
            pX = p1; pY = p2; pZ = p3;
            vX = v1; vY = v2; vZ = v3;
            aX = a1; aY = a2; aZ = a3;
            distanceFrom000 = calculateDistanceFrom000();
        }

        Particle(Particle p) {
            pX = p.pX; pY = p.pY; pZ = p.pZ;
            vX = p.vX; vY = p.vY; vZ = p.vZ;
            aX = p.aX; aY = p.aY; aZ = p.aZ;
            distanceFrom000 = p.distanceFrom000;
            lastDistanceIncrease = p.lastDistanceIncrease;
        }

        private int calculateDistanceFrom000() {
            return Math.abs(pX)+Math.abs(pY)+Math.abs(pZ);
        }

        void update() {
            updateSpeedAndPosition();
            updateDistanceInformation();
        }

        boolean getWhetherLastDistanceIncreaseIsNegative() {
            return lastDistanceIncrease < 0;
        }

        private void updateSpeedAndPosition() {
            vX += aX; pX += vX;
            vY += aY; pY += vY;
            vZ += aZ; pZ += vZ;
        }

        private void updateDistanceInformation() {
            int previousDistance = distanceFrom000;
            distanceFrom000 = calculateDistanceFrom000();
            lastDistanceIncrease = distanceFrom000 - previousDistance;
        }

        int getManhattanAcceleration() {
            return Math.abs(aX)+Math.abs(aY)+Math.abs(aZ);
        }

        boolean checkIfColliding(Particle o) {
            return pX == o.pX && pY == o.pY && pZ == o.pZ;
        }

    }

}
