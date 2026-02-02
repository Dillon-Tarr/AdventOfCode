package year2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Day8 {
    static private final int DAY = 8;
    static private final File INPUT_FILE = new File("input-files/2019/"+DAY+".txt");
    static private String inputString;

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            inputString = br.readLine();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void solve() {
        ArrayList<Layer> layers = new ArrayList<>();
        int inputLength = inputString.length();
        for (int c = 150; c < inputLength; c += 150) layers.add(new Layer(inputString.substring(c-150, c).toCharArray()));
        layers.add(new Layer(inputString.substring(inputLength-150).toCharArray()));
        var fewest0sLayer = Layer.layerWithFewest0s;
        int part1Answer = fewest0sLayer.countOf1s*fewest0sLayer.countOf2s;
        System.out.println("\nPart 1 answer: "+part1Answer+"\n\nPart 2 message:\n");
        var sb = new StringBuilder();
        int c = 0;
        for (int i = 0; i < 150; i++) {
            boolean transparent = true;
            layerLoop: for (var l : layers) switch (l.chars[i]) {
                case '0' -> { sb.append(' '); transparent = false; break layerLoop; }
                case '1' -> { sb.append('#'); transparent = false; break layerLoop; }
            }
            if (transparent) sb.append(' ');
            if (++c == 25) {
                c = 0;
                System.out.println(sb);
                sb.setLength(0);
            }
        }
    }

    private static class Layer {
        static Layer layerWithFewest0s = null;

        char[] chars;
        int countOf0s = 0, countOf1s = 0, countOf2s = 0;

        Layer(char[] ca) {
            chars = ca;
            for (int i = 0; i < 150; i++) {
                switch (ca[i]) {
                    case '0' -> countOf0s++;
                    case '1' -> countOf1s++;
                    default -> countOf2s++;
                }
            }
            if (layerWithFewest0s == null || countOf0s < layerWithFewest0s.countOf0s) layerWithFewest0s = this;
        }
    }

}
