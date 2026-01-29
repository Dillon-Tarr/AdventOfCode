package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day23 {
    static private final int DAY = 23;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<Nanobot> nanobots = new ArrayList<>();
    static private final int[] stepsPerDirectionByPowerOfTwoStepSize = new int[] // [26] 2pow(0-25)
            { 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 3 };

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                var ss = s.substring(s.indexOf('<')+1, s.indexOf('>')).split(",");
                nanobots.add(new Nanobot(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
                        Integer.parseInt(ss[2]), Integer.parseInt(s.substring(s.lastIndexOf('=')+1))));
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solvePart1() {
        int largestRange = 0; Nanobot strongestBot = null;
        for (var bot : nanobots) {
            int range = bot.range;
            if (range > largestRange) {
                largestRange = range;
                strongestBot = bot;
            }
        }
        if (strongestBot == null) throw new RuntimeException("No strongest bot found!? Check input file?");
        int x = strongestBot.x, y = strongestBot.y, z = strongestBot.z, range = strongestBot.range, count = 1;
        for (var bot : nanobots) {
            if (bot == strongestBot) continue;
            int distance = Math.abs(x - bot.x) + Math.abs(y - bot.y) + Math.abs(z - bot.z);
            if (distance <= range) count++;
        }
        System.out.println("\nNumber of nanobots in range of the strongest nanobot (part 1 answer): "+count);
    }

    /**
     * Part 2 solution inspired by:
     * <a href="https://www.reddit.com/r/adventofcode/comments/a8s17l/comment/echapvu/">wzkx on Reddit</a>
     */
    private static void solvePart2() {
        long xSum = 0, ySum = 0, zSum = 0;
        for (var bot : nanobots) { xSum += bot.x; ySum += bot.y; zSum += bot.z; }
        int botCount = nanobots.size();
        int xMean = (int)(xSum/botCount), yMean = (int)(ySum/botCount),  zMean = (int)(zSum/botCount);
        System.out.println("""
                
                \t\t\t\t\tResults  of  part  2  search:\s
                -----------------------------------------------------------------------------------------
                   Step  | No. of steps  |       Best coordinates        | Best   x how many points found
                   size  | per direction |     x:        y:        z:    | count  x   with that count""");
        int highCountCount;
        for (int stepSizePowerOfTwo = 25; stepSizePowerOfTwo >= 0; stepSizePowerOfTwo--) {
            highCountCount = 0;
            int stepSize = 1 << stepSizePowerOfTwo;
            int perDirSteps = stepsPerDirectionByPowerOfTwoStepSize[stepSizePowerOfTwo];
            int highCount = 0, xOfHighCount = 0, yOfHighCount = 0, zOfHighCount = 0;
            for (int xShift = -perDirSteps; xShift <= perDirSteps; xShift++) {
                int x = xMean + (stepSize * xShift);
                for (int yShift = -perDirSteps; yShift <= perDirSteps; yShift++) {
                    int y = yMean + (stepSize * yShift);
                    for (int zShift = -perDirSteps; zShift <= perDirSteps; zShift++) {
                        int z = zMean + (stepSize * zShift);
                        int count = 0;
                        for (var b : nanobots) if (Math.abs(b.x - x) + Math.abs(b.y - y) + Math.abs(b.z - z) <= b.range) count++;
                        if (count > highCount) {
                            highCount = count;
                            xOfHighCount = x;
                            yOfHighCount = y;
                            zOfHighCount = z;
                            highCountCount = 1;
                        } else if (count == highCount) highCountCount++;
                    }
                }
            }
            System.out.printf("%8s |      %3s      | %9s,%9s,%9s | %3s    x%d\n", stepSize, perDirSteps, xOfHighCount, yOfHighCount, zOfHighCount, highCount, highCountCount);
            xMean = xOfHighCount;
            yMean = yOfHighCount;
            zMean = zOfHighCount;
        }
        int x = Math.abs(xMean), y = Math.abs(yMean), z = Math.abs(zMean);
        System.out.println("\nManhattan distance of the closest-to-0,0,0 point of those closest to the most nodes (part 2 answer): " + (x + y + z));
    }

    private record Nanobot (int x, int y, int z, int range) {}

}
