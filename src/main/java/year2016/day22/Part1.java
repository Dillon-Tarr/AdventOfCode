package year2016.day22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part1 {
    static private final int DAY = 22;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<Node> nodes = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        instantiateNodes();
        countViablePairs();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            br.readLine(); br.readLine(); // Skip first 2 lines
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void instantiateNodes() {
        int used, avail, preSpace;
        for (String s : inputStrings) {
            preSpace = s.lastIndexOf(' ', 32);
            used = Integer.parseInt(s.substring(preSpace+1, 33));
            preSpace = s.lastIndexOf(' ', 39);
            avail = Integer.parseInt(s.substring(preSpace+1, 40));
            nodes.add(new Node(used, avail));
        }
    }

    private static void countViablePairs() {
        Node a, b;
        int count = 0;
        for (int i = 0; i < nodes.size()-1; i++) {
            a = nodes.get(i);
            for (int j = i+1; j < nodes.size(); j++) {
                b = nodes.get(j);
                if ((a.used != 0 && a.used < b.avail) || (b.used != 0 && b.used < a.avail)) count++;
            }
        }
        System.out.println("\nNumber of viable pairs: "+count);
    }

    private static class Node {
        int used, avail;

        Node(int used, int avail) {
            this.used = used;
            this.avail = avail;
        }
    }

}
