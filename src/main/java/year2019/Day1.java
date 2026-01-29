package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day1 {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private final ArrayList<Integer> moduleValues = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        ArrayList<String> inputStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        inputStrings.forEach(s -> moduleValues.add(Integer.parseInt(s)));
    }

    private static void solve() {
        int fuelRequired = 0;
        moduleValues.replaceAll(m -> (m/3)-2);
        for (var m : moduleValues) fuelRequired += m;
        System.out.println("\nRequired fuel (part 1 answer): "+fuelRequired);
        for (var m : moduleValues) {
            while (true) {
                m = (m/3)-2;
                if (m < 1) break;
                fuelRequired += m;
            }
        }
        System.out.println("\nRequired fuel including the fuel's fuel (part 2 answer): "+fuelRequired);
    }

}
