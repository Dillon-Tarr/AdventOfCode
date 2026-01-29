package year2023.day1.part1;

import java.io.*;


class Main {
    static private final int DAY = 1;
    static private final File INPUT_FILE = new File("input-files/2023/"+DAY+".txt");

    private static final String NUMERALS_ONLY_VALUES_FILE_PATH = "input-files/2023/day1-numerals-only-values.txt";
    private static final File NUMERALS_ONLY_VALUES_FILE = new File(NUMERALS_ONLY_VALUES_FILE_PATH);
    private static final String PLAIN_ENGLISH_OF_NUMERALS_ONLY_FILE_NAME = "numerals-only values file";

    private static final String REAL_CALIBRATION_VALUES_FILE_PATH = "input-files/2023/day1-real-calibration-values.txt";
    private static final File REAL_CALIBRATION_VALUES_FILE = new File(REAL_CALIBRATION_VALUES_FILE_PATH);
    private static final String PLAIN_ENGLISH_OF_REAL_CALIBRATION_VALUES_FILE_PATH = "real calibration values file";

    private static int sumOfRealCalibrationValues;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        readAndPrintGarbledCalibrationValues();
        String numeralsOnlyValues = reduceCalibrationValuesToNumerals();
        System.out.println(numeralsOnlyValues);
        System.out.println(evaluateRealCalibrationValues());
        System.out.println("\nSUM OF REAL CALIBRATION VALUES:\n"+sumOfRealCalibrationValues);

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void readAndPrintGarbledCalibrationValues() {
        String garbledCalibrationValues = getStringFromTextFile();
        System.out.println("\n\nGarbled calibration values:\n" + garbledCalibrationValues);
    }

    private static String getStringFromTextFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(Main.INPUT_FILE))){
            StringBuilder fileString = new StringBuilder();
            String currentLine = br.readLine();

            while (currentLine != null) {
                fileString.append(currentLine);
                fileString.append(System.lineSeparator());
                currentLine = br.readLine();
            }
            return fileString.toString();
        }
        catch (IOException e){
            return e.toString();
        }
    }

    private static String reduceCalibrationValuesToNumerals() {
        deleteFileAndCreateEmptyFileAtPath(NUMERALS_ONLY_VALUES_FILE, NUMERALS_ONLY_VALUES_FILE_PATH, PLAIN_ENGLISH_OF_NUMERALS_ONLY_FILE_NAME);
        System.out.println("New file created to store numerals-only values. Attempting to remove non-numerals...");
        try (FileWriter fileWriter = new FileWriter(NUMERALS_ONLY_VALUES_FILE)) {
            StringBuilder ungarbledStringBuilder = new StringBuilder();
            String regex = "[^0-9]";
            try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))){
                String currentLine = br.readLine();
                while (currentLine != null) {
                    ungarbledStringBuilder.append(currentLine.replaceAll(regex, ""));
                    ungarbledStringBuilder.append(System.lineSeparator());
                    currentLine = br.readLine();
                }
            }
            catch (IOException e){
                return e.toString();
            }
            String ungarbledString = ungarbledStringBuilder.toString();
            fileWriter.append(ungarbledString);
            return ungarbledString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String evaluateRealCalibrationValues(){
        deleteFileAndCreateEmptyFileAtPath(REAL_CALIBRATION_VALUES_FILE, REAL_CALIBRATION_VALUES_FILE_PATH, PLAIN_ENGLISH_OF_REAL_CALIBRATION_VALUES_FILE_PATH);
        System.out.println("New file created to store real calibration values. Attempting to remove non-numerals...");
        try (FileWriter fileWriter = new FileWriter(REAL_CALIBRATION_VALUES_FILE)) {
            StringBuilder ungarbledStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(NUMERALS_ONLY_VALUES_FILE))){
                String currentLine = br.readLine();
                while (currentLine != null) {
                    String stringyRealValueOfCurrentLine = ""+currentLine.charAt(0)+currentLine.charAt(currentLine.length()-1);
                    ungarbledStringBuilder.append(stringyRealValueOfCurrentLine);
                    ungarbledStringBuilder.append(System.lineSeparator());

                    int realValueOfCurrentLine = Integer.parseInt(stringyRealValueOfCurrentLine);
                    sumOfRealCalibrationValues += realValueOfCurrentLine;

                    currentLine = br.readLine();
                }
            }
            catch (IOException e){
                return e.toString();
            }
            String ungarbledString = ungarbledStringBuilder.toString();
            fileWriter.append(ungarbledString);
            return ungarbledString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFileAndCreateEmptyFileAtPath(File file, String path, String plainEnglishOfFileName) {
        if (!file.delete()) System.out.println("No " +plainEnglishOfFileName+" exists. Creating one...");
        else System.out.println("Deleted existing "+plainEnglishOfFileName+". Creating new file...");
        try {
            if (!file.createNewFile()) {
                throw new RuntimeException("Unable to create new file at path \""+path+"\"");
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

}
