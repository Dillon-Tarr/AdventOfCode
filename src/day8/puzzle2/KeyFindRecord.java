package day8.puzzle2;

public class KeyFindRecord {
    private final char[] key;
    private final int instructionIndex;
    private final int stepCount;

    KeyFindRecord(char[] key, int instructionIndex, int stepCount) {
        this.key = key;
        this.instructionIndex = instructionIndex;
        this.stepCount = stepCount;
    }

    public char[] getKey() {return key;}
    public int getInstructionIndex() {return instructionIndex;}
    public int getStepCount() {return stepCount;}
}
