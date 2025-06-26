package year2015;

import java.io.*;

public class Part1 {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");

    static private String inputString;
    static private int ascentCount = 0;
    static private int descentCount = 0;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        countAscentsAndDescents();

        System.out.println("\nAscent count: "+ascentCount+"\nDescent count: "+ descentCount+"\n\nResulting floor: "+(ascentCount-descentCount));

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            StringBuilder sb = new StringBuilder();
            String currentLine = br.readLine();
            while(!(currentLine == null)) {
                sb.append(currentLine);
                currentLine = br.readLine();
            }
            inputString = sb.toString();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void countAscentsAndDescents(){
        for (int i = 0; i < inputString.length(); i++) {
            switch (inputString.charAt(i)) {
                case '(' -> ascentCount++;
                case ')' -> descentCount++;
            }
        }
    }

}
