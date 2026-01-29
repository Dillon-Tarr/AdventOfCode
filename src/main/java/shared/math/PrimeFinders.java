package shared.math;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PrimeFinders {

    static private final String FOLDER_PATH = "src/main/java/shared/math/";

    public static void main(String[] args) throws IOException {
        System.out.println("""
                \nRunning main method of PrimeFinders.java. To exit at any time, enter only a single zero (0).
                The following methods are available to run:
                1 - savePrimesUntilValue() - Saves new primes to primes.txt.
                2 - transferPrimesToNewFile() - Transfers primes from one text file you select to another file you select, up to a limit.
                3 - confirmPrimeFileFormat() - Confirms that the selected file is formatted properly. Select for more details.
                
                Which method do you want to run? Enter the corresponding number:""");
        switch (new Scanner(System.in).nextInt()) {
            case 1 -> savePrimesUntilValue();
            case 2 -> transferPrimesToNewFile();
            case 3 -> confirmPrimeFileFormat();
            case 0 -> exitViaZeroInput();
            default -> {
                System.out.println("Entered option is invalid. Exiting.");
                System.exit(0);
            }
        }
    }

    private static void exitViaZeroInput() {
        System.out.println("Input \"0\" received. Exiting.");
        System.exit(0);
    }

    /**
     * Collects user input and calls an overload of this method with collected input.
     */
    private static void savePrimesUntilValue() {
        System.out.println("Running \"savePrimesUntilValue()\".");
        System.out.println("Note: this program assumes the supplied \"primes.txt\" file, if it already exists, has ascending ordered primes starting with 2, each alone on a new line, with no empty lines.");
        System.out.println("\"primes.txt\" will be updated with new primes up to the value you enter (in base-10):");
        Scanner scanner = new Scanner(System.in);
        int userIntInput = scanner.nextInt();
        if (userIntInput == 0) exitViaZeroInput();
        long startTime = System.nanoTime();
        savePrimesUntilValue(userIntInput);
        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    /**
     * Reads primes from primes.txt, if the file exists, then finds more primes to add to the same file.
     * @param savePrimesToThisValue the inclusive upper bound for finding new primes
     */
    private static void savePrimesUntilValue(int savePrimesToThisValue) {
        if (savePrimesToThisValue < 2) throw new RuntimeException("The smallest prime number is 2, but you entered "+savePrimesToThisValue+".");
        if (savePrimesToThisValue < 5) {
            System.out.println("You asked for primes up to "+savePrimesToThisValue+". You're getting up to 5 for free.");
            savePrimesToThisValue = 5;
        }
        File file = new File(FOLDER_PATH+"primes.txt");
        ArrayList<Integer> primes;
        if (file.exists()) {
            primes = getAllPrimesFromFile(file);
            if (!primes.isEmpty() && primes.get(primes.size()-1) >= savePrimesToThisValue) throw new RuntimeException(
                        "Your last prime in the file is already greater than or equal to what you asked for.");
        } else primes = new ArrayList<>();
        int newPrimeCount = 0;
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            switch (primes.size()) {
                case 0:
                    fileWriter.append("2");
                    primes.add(2);
                    newPrimeCount++;
                case 1:
                    fileWriter.append(System.lineSeparator()).append("3");
                    primes.add(3);
                    newPrimeCount++;
                    System.out.println();
                    break;
                default: System.out.println("\nPrimes from "+ primes.get(0)+" to "+ primes.get(primes.size()-1)+" read from file. ");
            }
            System.out.print("Calculating primes up to "+savePrimesToThisValue+".\n");

            int i = 2+primes.get(primes.size()-1);
            boolean goodToGo = false;
            while (!goodToGo) {
                switch (i % 6) {
                    case 1, 5 -> goodToGo = true;
                    default -> i++;
                }
            }
            boolean remainderOne = i % 6 == 1;
            while (i <= savePrimesToThisValue) {
                boolean foundThatIHasAPrimeFactor = false;
                for (int prime : primes) {
                    if (i % prime == 0) {
                        foundThatIHasAPrimeFactor = true;
                        break;
                    }
                }
                if (!foundThatIHasAPrimeFactor) {
                    primes.add(i);
                    fileWriter.append(System.lineSeparator()).append(String.valueOf(i));
                    newPrimeCount++;
                    if (newPrimeCount % 1000 == 0) System.out.println(newPrimeCount + " new primes found so far! Current value of i: "+i);
                }
                i += remainderOne ? 4 : 2;
                remainderOne = !remainderOne;
            }
        } catch (IOException e) {throw new RuntimeException(e.toString());}
        if (newPrimeCount == 0) System.out.println("No new primes found. This should mean the limit you entered was above the " +
                "last prime already in the file, but there is no prime number between that number and the supplied limit.");
        else {
            int lastNewPrime = primes.get(primes.size()-1);
            System.out.println("\nPrimes up to "+lastNewPrime+" (the last prime less than "+(lastNewPrime==savePrimesToThisValue?"or equal to ":"")+savePrimesToThisValue+") added to file.");
            System.out.println("Number of new primes found: "+newPrimeCount);
            System.out.println("\nConfirming that the file remains properly formatted, free from errors...\n");
            confirmPrimeFileFormatAndReturnPrimes(file);
            System.out.println("""
                Confirmed that the format of the file remains correct, meaning:
                - the destination file has no empty lines (except possibly the last one) and consists of only numbers,
                - those numbers ascend on each new line, and
                - the last number is not divisible by any of the others.""");
        }
    }

    /**
     * Reads prime numbers from the supplied file and returns them all.
     * @param file the file from which to read primes
     * */
    private static ArrayList<Integer> getAllPrimesFromFile(File file) {
        return getPrimesFromFile(file, true, Integer.MAX_VALUE);
    }

    /**
     * Reads prime numbers from the supplied file and returns those less than or equal to the supplied limit.
     * @param file the file from which to read primes
     * @param getAllMode whether to return all the primes from the supplied file
     * @param numberLimit the value at which to stop reading primes
     * */
    public static ArrayList<Integer> getPrimesFromFile(File file, boolean getAllMode, int numberLimit) {
        ArrayList<Integer> allPrimesFromFile = confirmPrimeFileFormatAndReturnPrimes(file);
        if (getAllMode) return allPrimesFromFile;
        ArrayList<Integer> primesUpToLimit = new ArrayList<>();
        int i = 0; int prime;
        while (i < allPrimesFromFile.size()) {
            prime = allPrimesFromFile.get(i++);
            if (prime > numberLimit) return primesUpToLimit;
            primesUpToLimit.add(prime);
        }
        throw new RuntimeException("End of file was reached before all desired primes were read from file.\n"+
                "The supplied limit was greater than the value of the last prime in the file.");
    }

    /**
     * Collects user input and calls an overload of this method with collected input.
     */
    private static void transferPrimesToNewFile() {
        System.out.println("Running \"transferPrimesToNewFile()\".\n" +
                "This program will write primes from one file to another file within the folder \""+FOLDER_PATH+"\".");
        Scanner scanner = new Scanner(System.in);
        String input;
        String pathname;

        System.out.println("Enter source file name:");
        input = scanner.next();
        if (input.equals("0")) exitViaZeroInput();
        if (!input.endsWith(".txt")) throw new IllegalArgumentException("File name must end with: \".txt\"");
        pathname = FOLDER_PATH+ input;
        File sourceFile = new File(pathname);
        if (!sourceFile.exists()) throw new RuntimeException("There is no file at \""+pathname+"\".");

        System.out.println("Enter destination file name:");
        input = scanner.next();
        if (input.equals("0")) exitViaZeroInput();
        if (!input.endsWith(".txt")) throw new IllegalArgumentException("File name must end with: \".txt\"");
        pathname = FOLDER_PATH+ input;
        File destinationFile = new File(pathname);

        if (destinationFile.exists()) {
            System.out.println("A file already exists at \""+destinationFile.getPath()+"\". Do you want to overwrite it? (y/n):");
            input = scanner.next();
            if (input.contains("n") || input.contains("N")
                    || !(input.contains("y") || input.contains("Y") || input.equals("1") || input.equals("true"))) {
                System.out.println("Operation canceled.");
                return;
            }
        }

        System.out.println("Enter the number at which to stop transferring primes from source to destination file, or 0 to cancel:");
        int userIntInput = scanner.nextInt();
        if (userIntInput == 0) exitViaZeroInput();
        if (userIntInput < 5) return;

        transferPrimesToNewFile(sourceFile, destinationFile, userIntInput);
    }

    /**
     * Reads some of the prime numbers from one file and writes them to another.
     * @param sourceFile the file from which primes will be transferred
     * @param destinationFile the file to which primes will be transferred
     * @param numberLimit the inclusive upper bound of the primes which will be transferred
     * @throws IllegalArgumentException if the supplied limit is less than 5
     */
    private static void transferPrimesToNewFile(File sourceFile, File destinationFile, int numberLimit) {
        ArrayList<Integer> primes = getPrimesFromFile(sourceFile, false, numberLimit);
        if (primes.isEmpty()) throw new RuntimeException("Supplied source file \""+sourceFile.getPath()+"\" is primeless.");
        if (primes.size() == 1) throw new RuntimeException("Supplied source file \""+sourceFile.getPath()+"\" only contains one number.");
        try (FileWriter fileWriter = new FileWriter(destinationFile)) {
            fileWriter.append(String.valueOf(primes.get(0)));
            int prime;
            for (int i = 1; i < primes.size(); i++) {
                prime = primes.get(i);
                if (prime > numberLimit) break;
                fileWriter.append(System.lineSeparator()).append(String.valueOf(prime));
            }
        } catch (IOException e) {throw new RuntimeException(e.toString());}
        confirmPrimeFileFormatAndReturnPrimes(destinationFile);
        System.out.println("Primes from "+primes.get(0)+" to "+primes.get(primes.size()-1)+" read from \""+sourceFile.getPath()+"\" and written to \""+destinationFile.getPath()+"\".");
        System.out.println("""
                It was also confirmed that the format of the new file is correct, meaning:
                - the destination file has no empty lines (except possibly the last one) and consists of only numbers,
                - those numbers ascend on each new line, and
                - the last number is not divisible by any of the others.""");
    }

    /**
     * Collects user input and calls {@code confirmPrimeFileFormatAndReturnPrimes(File)} with collected input.
     * If the file is empty, user is told so. If another problem is found, an error will be thrown.
     */
    private static void confirmPrimeFileFormat() {
        System.out.println("Running \"confirmPrimeFileFormat()\".\n" +
                "This program confirms that the supplied (.txt) file has no empty lines and consists of only numbers.\n" +
                "It also confirms that those numbers ascend on each new line and that the last number is\n" +
                "not divisible by any of the others (is prime if all the other numbers in the file are all the primes below it).\n" +
                "Within the folder \""+FOLDER_PATH+"\", specify the name of the file you want to check:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        String pathname;
        if (input.equals("0")) exitViaZeroInput();
        if (!input.endsWith(".txt")) throw new IllegalArgumentException("File name must end with: \".txt\"");
        pathname = FOLDER_PATH+input;
        File file = new File(pathname);
        if (!file.exists()) throw new RuntimeException("There is no file at \""+pathname+"\".");
        ArrayList<Integer> primes = confirmPrimeFileFormatAndReturnPrimes(file);
        if (primes.isEmpty()) System.out.println("Result: The file is empty.");
        else System.out.println("Result: The file plays by the rules.");
    }

    /**
     * Confirms that the supplied (.txt) file has no empty lines and consists of only numbers.
     * Also confirms that those numbers ascend on each new line and that the last number is
     * not divisible by any of the others (is prime if all the other numbers in the file are all the primes below it).
     *
     * <p>NOTE: This method does not re-check that all numbers in the file are prime.
     * @param file the file to be checked for proper format and seeming primeness
     */
    private static ArrayList<Integer> confirmPrimeFileFormatAndReturnPrimes(File file) { //
        ArrayList<Integer> primes = new ArrayList<>();
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String fileLine = br.readLine();
                int lineNumber = 1, currentNumber;
                while (fileLine != null) {
                    if (fileLine.isEmpty()) throw new RuntimeException("Line number "+lineNumber+" is empty in file \""+file.getPath()+"\"");
                    currentNumber = Integer.parseInt(fileLine);
                    if (!primes.isEmpty() && primes.get(primes.size()-1) > currentNumber)
                        throw new RuntimeException("In file \""+file.getPath()+"\", the number on line "+(lineNumber-1)+" is greater than the number on line "+lineNumber+".");
                    primes.add(currentNumber);
                    fileLine = br.readLine();
                    lineNumber++;
                }
            } catch (IOException e) {throw new RuntimeException(e.toString());}
        }
        if (!primes.isEmpty() && primes.get(0) != 2) throw new RuntimeException("In file \""+file.getPath()+"\", the first line is not 2.");
        if (primes.size() >= 2) confirmLastNumberIsNotDivisibleByTheRest(primes);
        return primes;
    }

    /**
     * Confirms that the last number in the supplied {@code ArrayList} is not divisible by any of the others.
     * @param supposedPrimes the {@code ArrayList} of ascending numbers to check
     * @throws RuntimeException if {@code supposedPrimes} has a size less than 2,
     * or if it is found that the last number is divisible by one of the others
     * */
    private static void confirmLastNumberIsNotDivisibleByTheRest(ArrayList<Integer> supposedPrimes) {
        if (supposedPrimes.size() < 2) throw new RuntimeException("This method is only useful if 2 or more primes are present.");
        int lastNumber = supposedPrimes.get(supposedPrimes.size()-1);
        boolean foundLastNumberIsDivisibleByAnother = false;
        int i = 0, supposedPrime = -1;
        while (i < supposedPrimes.size()-1) { // -1 to not try division by self... Of course primes are divisible by self.
            supposedPrime = supposedPrimes.get(i);
            if (lastNumber % supposedPrime == 0) {
                foundLastNumberIsDivisibleByAnother = true;
                break;
            }
            i++;
        }
        if (foundLastNumberIsDivisibleByAnother) throw new RuntimeException("Last number in file, "+lastNumber+", is divisible by the number on line "+(i+1)+": "+supposedPrime);
    }

    /**
     * Finds and returns prime numbers up to the supplied limit.
     * @param limit the inclusive upper bound to which primes will be found
     * @return an {@code ArrayList<Integer>} containing the found primes in ascending order
     * @throws IllegalArgumentException if the supplied limit is less than 5
     */
    public static ArrayList<Integer> findPrimesUntilValue(int limit) {
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        primes.add(3);
        findMorePrimes(limit, primes);
        return primes;
    }

    /**
     * Uses the supplied ArrayList of prime numbers to find more primes
     * up to the supplied limit and add them to the same ArrayList.
     * @param limit the inclusive upper bound to which primes will be found
     * @param primes the {@code ArrayList<Integer>} of prime numbers which will be used for finding more primes
     *               and to which the new primes will be added
     * @throws IllegalArgumentException if the supplied limit is less than 5,
     * or if the supplied limit is less than or equal to the last prime in the existing list
     */
    public static void findMorePrimes(int limit, ArrayList<Integer> primes) {
        if (limit < 5) throw new IllegalArgumentException("Supplied value to which to find primes is too low: "+ limit);
        int lastPrime = primes.get(primes.size()-1);
        if (limit <= lastPrime) throw new IllegalArgumentException("Supplied value to which to find primes, "+ limit
                    +", is less than or equal to the last prime in the existing list: "+lastPrime);
        int i = 2+primes.get(primes.size()-1);
        boolean goodToGo = false;
        while (!goodToGo) {
            switch (i % 6) {
                case 1, 5 -> goodToGo = true;
                default -> i++;
            }
        }
        boolean remainderOne = i % 6 == 1;
        while (i <= limit) {
            boolean foundThatIHasAPrimeFactor = false;
            for (int prime : primes) {
                if (i % prime == 0) {
                    foundThatIHasAPrimeFactor = true;
                    break;
                }
            }
            if (!foundThatIHasAPrimeFactor) primes.add(i);
            i += remainderOne ? 4 : 2;
            remainderOne = !remainderOne;
        }
    }

}
