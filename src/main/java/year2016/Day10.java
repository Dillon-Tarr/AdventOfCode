package year2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Day10 {
    static private final int DAY = 10;
    static private final File INPUT_FILE = new File("input-files/2016/"+DAY+".txt");
    static private final ArrayList<String> instructions = new ArrayList<>();
    static private final HashMap<Integer, Bot> numberToBotMap = new HashMap<>();
    static private final ArrayList<Bot> allBots = new ArrayList<>();
    static private final int lowChipNumber = 17, highChipNumber = 61;
    static private Bot output0SourceBot, output1SourceBot, output2SourceBot;
    static private boolean output0ReceivesLow, output1ReceivesLow, output2ReceivesLow;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getInputData();
        createBots();
        solveBots();
        findSpecialBot(); // Part 1 answer
        findSpecialProduct(); // Part 2 answer

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String inputString = br.readLine();
            while (inputString != null) {
                instructions.add(inputString);
                inputString = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void createBots() {
        int inputBinNumber, destinationBotNumber, sourceBotNumber, lowBotNumber, highBotNumber, lIndex, hIndex;
        Bot sourceBot, destinationBot, lowBot, highBot;
        for (String s : instructions) {
            switch (s.charAt(0)) {
                case 'v' -> {
                    inputBinNumber = Integer.parseInt(s.substring(6, s.indexOf('g')-1));
                    destinationBotNumber = Integer.parseInt(s.substring(s.lastIndexOf(' ')+1));
                    destinationBot = getOrCreateBot(destinationBotNumber);
                    destinationBot.addValue(inputBinNumber);
                }
                case 'b' -> {
                    sourceBotNumber = Integer.parseInt(s.substring(4, s.indexOf('g')-1));
                    sourceBot = getOrCreateBot(sourceBotNumber);
                    lIndex = s.indexOf('l');
                    hIndex = s.indexOf('h');
                    if (s.charAt(lIndex+7) == 'b') {
                        lowBotNumber = Integer.parseInt(s.substring(lIndex+11, hIndex-5));
                        lowBot = getOrCreateBot(lowBotNumber);
                        sourceBot.lowDestination = lowBot;
                    } else if (s.contains("ut 0 ")) { output0SourceBot = sourceBot; output0ReceivesLow = true; }
                    else if (s.contains("ut 1 ")) { output1SourceBot = sourceBot; output1ReceivesLow = true; }
                    else if (s.contains("ut 2 ")) { output2SourceBot = sourceBot; output2ReceivesLow = true; }
                    if (s.charAt(hIndex+8) == 'b') {
                        highBotNumber = Integer.parseInt(s.substring(hIndex+12));
                        highBot = getOrCreateBot(highBotNumber);
                        sourceBot.highDestination = highBot;
                    } else if (s.endsWith("ut 0")) output0SourceBot = sourceBot;
                    else if (s.endsWith("ut 1")) output1SourceBot = sourceBot;
                    else if (s.endsWith("ut 2")) output2SourceBot = sourceBot;
                }
                default -> throw new RuntimeException("Did you supply a proper text file?");
            }
        }
    }

    private static void solveBots() {
        ArrayList<Bot> unsolvedBots = new ArrayList<>(allBots);
        int i = 0;
        while (!unsolvedBots.isEmpty()) {
            Bot bot = unsolvedBots.get(i);
            if (bot.attemptSolve()) unsolvedBots.remove(bot);
            else i++;
            if (i >= unsolvedBots.size()) i = 0;
        }
    }

    private static void findSpecialBot() {
        int specialBotNumber = -1;
        for (Bot bot : allBots) {
            if (bot.lowValue == lowChipNumber && bot.highValue == highChipNumber) {
                specialBotNumber = bot.number;
                break;
            }
        }
        System.out.println("\nNumber of bot responsible for comparing value-"+lowChipNumber+" microchips and value-"+highChipNumber+" microchips: "+specialBotNumber);
    }

    private static void findSpecialProduct() {
        int output0 = output0ReceivesLow ? output0SourceBot.lowValue : output0SourceBot.highValue;
        int output1 = output1ReceivesLow ? output1SourceBot.lowValue : output1SourceBot.highValue;
        int output2 = output2ReceivesLow ? output2SourceBot.lowValue : output2SourceBot.highValue;
        System.out.println("\nProduct of multiplying together the chip values of outputs 0 ("+output0+"), 1 ("+output1+"), and 2 ("+output2+"): "+(output0*output1*output2));
    }

    private static Bot getOrCreateBot(int botNumber) {
        Bot bot;
        if (numberToBotMap.containsKey(botNumber)) bot = numberToBotMap.get(botNumber);
        else {
            bot = new Bot(botNumber);
            numberToBotMap.put(botNumber, bot);
            allBots.add(bot);
        }
        return bot;
    }

    private static class Bot {
        private final int number;
        private Bot lowDestination, highDestination;
        private Integer value1, value2, lowValue, highValue;

        Bot(int number) {
            this.number = number;
        }

        void addValue(int value) {
            if (value1 == null) value1 = value;
            else if (value2 == null) value2 = value;
            else throw new RuntimeException("Each bot should have only two value sources.");
        }

        boolean attemptSolve() {
            if (value2 == null || value1 == null) return false;
            lowValue = Math.min(value1, value2);
            highValue = Math.max(value1, value2);
            if (lowDestination != null) lowDestination.addValue(lowValue);
            if (highDestination != null) highDestination.addValue(highValue);
            return true;
        }

    }

}
