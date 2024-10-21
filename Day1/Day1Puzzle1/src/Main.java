import java.io.*;


public class Main {
    private static final String GARBLED_CALIBRATION_VALUES_FILE_PATH = "Day1/Day1Puzzle1/garbled-calibration-values.txt";
    private static final File garbledCalibrationValuesFile = new File(GARBLED_CALIBRATION_VALUES_FILE_PATH);

    private static final String NUMERALS_ONLY_VALUES_FILE_PATH = "Day1/Day1Puzzle1/numerals-only-values.txt";
    private static final File numeralsOnlyValuesFile = new File(NUMERALS_ONLY_VALUES_FILE_PATH);
    private static final String PLAIN_ENGLISH_OF_NUMERALS_ONLY_FILE_NAME = "numerals-only values file";

    private static final String REAL_CALIBRATION_VALUES_FILE_PATH = "Day1/Day1Puzzle1/real-calibration-values.txt";
    private static final File realCalibrationValuesFile = new File(REAL_CALIBRATION_VALUES_FILE_PATH);
    private static final String PLAIN_ENGLISH_OF_REAL_CALIBRATION_VALUES_FILE_PATH = "real calibration values file";

    private static int sumOfRealCalibrationValues;

    public static void main(String[] args) {
        if (!garbledCalibrationValuesFile.exists()) throw new RuntimeException("\""+ GARBLED_CALIBRATION_VALUES_FILE_PATH +"\" could not be found.");
        else readAndPrintGarbledCalibrationValues();
        String numeralsOnlyValues = reduceCalibrationValuesToNumerals();
        System.out.println(numeralsOnlyValues);
        System.out.println(evaluateRealCalibrationValues());
        System.out.println("\nSUM OF REAL CALIBRATION VALUES:\n"+sumOfRealCalibrationValues);
    }

    private static void readAndPrintGarbledCalibrationValues() {
        String garbledCalibrationValues = getStringFromTextFile(garbledCalibrationValuesFile);
        System.out.println("\n\nGarbled calibration values:\n" + garbledCalibrationValues);
    }

    private static String getStringFromTextFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
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
        deleteFileAndCreateEmptyFileAtPath(numeralsOnlyValuesFile, NUMERALS_ONLY_VALUES_FILE_PATH, PLAIN_ENGLISH_OF_NUMERALS_ONLY_FILE_NAME);
        System.out.println("New file created to store numerals-only values. Attempting to remove non-numerals...");
        try (FileWriter fileWriter = new FileWriter(numeralsOnlyValuesFile)) {
            StringBuilder ungarbledStringBuilder = new StringBuilder();
            String regex = "[^0-9]";
            try (BufferedReader br = new BufferedReader(new FileReader(garbledCalibrationValuesFile))){
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
        deleteFileAndCreateEmptyFileAtPath(realCalibrationValuesFile, REAL_CALIBRATION_VALUES_FILE_PATH, PLAIN_ENGLISH_OF_REAL_CALIBRATION_VALUES_FILE_PATH);
        System.out.println("New file created to store real calibration values. Attempting to remove non-numerals...");
        try (FileWriter fileWriter = new FileWriter(realCalibrationValuesFile)) {
            StringBuilder ungarbledStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(numeralsOnlyValuesFile))){
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