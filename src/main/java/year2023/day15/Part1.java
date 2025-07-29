package year2023.day15;

import java.io.*;

class Part1 {
    static private final int DAY = 15;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private String[] steps;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        getValues();

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
            steps = sb.toString().split(",");
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void getValues() {
        int sumOfValues = 0;
        for (String step : steps) {
            int value = 0;
            for (int i = 0; i < step.length(); i++) {
                value = (value+step.charAt(i))*17%256;
            }
            sumOfValues += value;
        }
        System.out.println("\nSum of values:\n\n"+sumOfValues);
    }

}
