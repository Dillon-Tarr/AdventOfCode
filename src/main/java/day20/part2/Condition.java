package day20.part2;

import java.util.ArrayList;

class Condition {
    static ArrayList<Condition> incompleteConditions = new ArrayList<>();
    static ArrayList<Condition> completeConditions = new ArrayList<>();
    String moduleName;
    PulseType requiredPulseType;
    long pushCountAtFirstOccurrence = -1;

    Condition(String moduleName, PulseType requiredPulseType){
        this.moduleName = moduleName;
        this.requiredPulseType = requiredPulseType;
        incompleteConditions.add(this);
    }

    static boolean checkConditionsAndReturnWhetherIncompleteConditionsRemain() {
        return !incompleteConditions.isEmpty();
    }

    static long[] getFirstOccurrenceOfEachCondition() {
        long[] longs = new long[completeConditions.size()];
        for (int i = 0; i < completeConditions.size(); i++) {
            longs[i] = completeConditions.get(i).pushCountAtFirstOccurrence;
        }
        return longs;
    }

    void logCompletion(long pushCountAtFirstOccurrence) {
        this.pushCountAtFirstOccurrence = pushCountAtFirstOccurrence;
        completeConditions.add(this);
        incompleteConditions.remove(this);
    }

}
