package year2025;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day12 {
    static private final int DAY = 12;
    static private final File INPUT_FILE = new File("input-files/2025/"+DAY+".txt");
    static private final int[] presentAreas = new int[6];
    static private final ArrayList<String> regionInfoLines = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getAndProcessInputData() {
        var inputStrings = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (int i = 0; i < 6; i++) {
            int area = 0;
            for (int j = 1; j < 4; j++) {
                char[] chars = inputStrings.get((i*5)+j).toCharArray();
                for (int k = 0; k < 3; k++) if (chars[k] == '#') area++;
            }
            presentAreas[i] = area;
        }
        for (int i = 30; i < inputStrings.size(); i++) regionInfoLines.add(inputStrings.get(i));
    }

    private static void solve() {
        int count = 0;
        for (String line : regionInfoLines) if (solveLine(line)) count++;
        System.out.println("\nNumber of regions which can fit their listed presents: "+count);
    }

    private static boolean solveLine(String line) {
        int xIndex = line.indexOf('x'), colonIndex = line.indexOf(':'),
                width = Integer.parseInt(line.substring(0, xIndex)),
                length = Integer.parseInt(line.substring(xIndex+1, colonIndex)),
                regionArea = width*length,
                minimumArea = 0;
        String[] countStrings = line.substring(colonIndex+2).split(" ");
        for (int i = 0; i < 6; i++) {
            int count = Integer.parseInt(countStrings[i]);
            minimumArea += count*presentAreas[i];
        }
        if (regionArea < minimumArea) return false;
        if (regionArea >= (width/3)*(length/3)) return true;
        else throw new RuntimeException("Uh oh -- if you get this with your puzzle input, you'll have to code a complex solution!");
    }

}
