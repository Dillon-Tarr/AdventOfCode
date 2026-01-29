package year2023.day19.part1;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

class Main {
    static private final int DAY = 19;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");
    static private final HashMap<String, Workflow> workflows = new HashMap<>();
    static private final ArrayList<Part> parts = new ArrayList<>();
    static private final ArrayList<Part> acceptedParts = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        processInputData();
        processParts();
        getSumOfAcceptedPartsRatings();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void processInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            boolean workflowMode = true;
            String cl = br.readLine();
            while (cl != null) {
                if (cl.isEmpty()) {
                    workflowMode = false;
                } else if (workflowMode) {
                    int firstBracketIndex = cl.indexOf("{");
                    workflows.put(cl.substring(0, firstBracketIndex), new Workflow(cl.substring(firstBracketIndex+1, cl.length()-1)));
                } else {
                    String[] ratingStrings = cl.substring(1, cl.length()-1).split(",");
                    int[] ratings = new int[ratingStrings.length];
                    for (int i = 0; i < ratingStrings.length; i++) ratings[i] = Integer.parseInt(ratingStrings[i].substring(2));
                    parts.add(new Part(ratings[0], ratings[1], ratings[2], ratings[3]));
                }
                cl = br.readLine();
            }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    private static void processParts() {
        for (Part part : parts) {
            Workflow currentWorkflow = workflows.get("in");
            String resultOrNextWorkflow = currentWorkflow.processPart(part);
            while (!resultOrNextWorkflow.equals("A") && !resultOrNextWorkflow.equals("R")) {
                currentWorkflow = workflows.get(resultOrNextWorkflow);
                resultOrNextWorkflow = currentWorkflow.processPart(part);
            }
            if (resultOrNextWorkflow.equals("A")) acceptedParts.add(part);
        }
    }

    private static void getSumOfAcceptedPartsRatings() {
        int sum = 0;
        for (Part part : acceptedParts) sum += part.getRatingSum();
        System.out.println("\n\nSum of the ratings of all accepted parts: "+ sum);
    }

}
