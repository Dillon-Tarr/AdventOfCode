package day8.part2;

public class Node {
    private final char[] key;
    private final char[] leftValue;
    private final char[] rightValue;

    public Node(String inputString) {
        key = inputString.substring(0, 3).toCharArray();
        leftValue = inputString.substring(7, 10).toCharArray();
        rightValue = inputString.substring(12, 15).toCharArray();
    }

    public char[] getKey() {return key;}
    public char[] getValue(char character) {
        if (character == 'L') return leftValue;
        else return rightValue;
    }
}
