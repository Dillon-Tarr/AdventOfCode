package year2016.day4.part2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Main {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<Room> rooms = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        unencryptAndPrintNamesOfPossiblyRelevantRealRooms();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                rooms.add(new Room(inputString));
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void unencryptAndPrintNamesOfPossiblyRelevantRealRooms() {
        for (Room room : rooms) if (room.checkIfReal()) room.printUnencryptedNameIfPossiblyRelevant();
    }

}
