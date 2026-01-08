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

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solvePart1();
        solvePart2();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
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

    private static void solvePart2() {
        System.out.println("\n... (part 2 answer): ?");
    }

    private record Nanobot (int x, int y, int z, int range) {}

}
