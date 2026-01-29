package year2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Day4 {
    static private final int DAY = 4;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<String> inputStrings = new ArrayList<>();
    static private final ArrayList<Event> events = new ArrayList<>();
    static private final HashMap<Integer, GuardSleepRecord> guardNumberToSleepRecordMap = new HashMap<>();

    static void main() {
        long startTime = System.nanoTime();

        getInputData();
        processAndSortInputData();
        createSleepRecords();
        solve();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void getInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputStrings.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void processAndSortInputData() {
        int month, day, hour, minute, bIndex, guardNumber;
        for (String s : inputStrings) {
            month = Integer.parseInt(s.substring(6, 8));
            day = Integer.parseInt((s.substring(9, 11)));
            hour = Integer.parseInt((s.substring(12, 14)));
            minute = Integer.parseInt((s.substring(15, 17)));
            bIndex = s.indexOf('b');
            guardNumber = bIndex == -1 ? -1 : Integer.parseInt(s.substring(s.indexOf('#')+1, bIndex-1));
            events.add(new Event(month, day, hour, minute, guardNumber));
        }
        events.sort(Event::compareTo);
    }

    private static void createSleepRecords() {
        int actualGuardNumber, guardNumber, sleepTime = Integer.MIN_VALUE, wakeTime;
        boolean asleep = false;
        GuardSleepRecord sleepRecord = null;
        for (Event event : events) {
            guardNumber = event.guardNumber;
            if (guardNumber != -1) {
                actualGuardNumber = guardNumber;
                asleep = false;
                if (guardNumberToSleepRecordMap.containsKey(actualGuardNumber)) sleepRecord = guardNumberToSleepRecordMap.get(actualGuardNumber);
                else { sleepRecord = new GuardSleepRecord(); guardNumberToSleepRecordMap.put(actualGuardNumber, sleepRecord);}
            } else {
                asleep = !asleep;
                if (asleep) sleepTime = event.minute;
                else {
                    wakeTime = event.minute;
                    assert sleepRecord != null;
                    sleepRecord.update(sleepTime, wakeTime);
                }
            }
        }
    }

    private static void solve() {
        int sleepiestGuardNumber = -1, mostMinutesSleeping = -1, guardNumber; // prep
        int sameMinuteRecordGuardNumber = -1, sameMinuteRecordMinuteNumber = -1, sameMinuteRecordMinuteCount = -1;
        GuardSleepRecord sleepRecord;
        for (var entry: guardNumberToSleepRecordMap.entrySet()) { // solving
            guardNumber = entry.getKey(); sleepRecord = entry.getValue();
            int[] minuteRecordsOfCurrentGuard = sleepRecord.timesSleepingDuringMinute; // part 2 answer info:
            for (int i = 0; i < 60; i++) {
                if (minuteRecordsOfCurrentGuard[i] > sameMinuteRecordMinuteCount) {
                    sameMinuteRecordGuardNumber = guardNumber;
                    sameMinuteRecordMinuteNumber = i;
                    sameMinuteRecordMinuteCount = minuteRecordsOfCurrentGuard[i];
                }
            }
            if (sleepRecord.totalMinutesSleeping > mostMinutesSleeping) { // part 1 step 1:
                mostMinutesSleeping = sleepRecord.totalMinutesSleeping;
                sleepiestGuardNumber = guardNumber;
            }
        }
        int minuteDuringWhichGuardHasSleptMost = -1, individualMinuteRecord = -1; // prep
        int[] timesSleepingDuringMinute = guardNumberToSleepRecordMap.get(sleepiestGuardNumber).timesSleepingDuringMinute; // part 1 answer info:
        for (int i = 0; i < 60; i++) {
            if (timesSleepingDuringMinute[i] > individualMinuteRecord) {
                individualMinuteRecord = timesSleepingDuringMinute[i];
                minuteDuringWhichGuardHasSleptMost = i;
            }
        }
        int part1Answer = sleepiestGuardNumber * minuteDuringWhichGuardHasSleptMost, part2Answer = sameMinuteRecordGuardNumber * sameMinuteRecordMinuteNumber;
        System.out.println("\nID of the sleepiest guard multiplied by the number of the most slept-during minute (part 1 answer): "+part1Answer);
        System.out.println("\nID of the guard who slept the most during the same minute multiplied by the number of that minute (part 2 answer): "+part2Answer);
    }

    private record Event(int month, int day, int hour, int minute, int guardNumber) implements Comparable<Event> {
        @Override
        public int compareTo(Event o) {
            if (month != o.month) return Integer.compare(month, o.month);
            if (day != o.day) return Integer.compare(day, o.day);
            if (hour != o.hour) return Integer.compare(hour, o.hour);
            if (minute != o.minute) return Integer.compare(minute, o.minute);
            return Integer.compare(o.guardNumber, guardNumber);
        }

    }

    private static class GuardSleepRecord {
        int totalMinutesSleeping = 0;
        int[] timesSleepingDuringMinute = new int[60];

        void update(int sleepTime, int wakeTime) {
            totalMinutesSleeping += wakeTime-sleepTime;
            for (int i = sleepTime; i < wakeTime; i++) timesSleepingDuringMinute[i]++;
        }
    }

}
