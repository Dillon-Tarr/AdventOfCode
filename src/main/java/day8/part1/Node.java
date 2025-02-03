package day8.part1;

public class Node {
    private final String key;
    private final String leftValue;
    private final String rightValue;

    public Node(String inputString) {
        key = inputString.substring(0, 3);
        leftValue = inputString.substring(7, 10);
        rightValue = inputString.substring(12, 15);
    }

    public String getKey() {return key;}
    public String getLeftValue() {return leftValue;}
    public String getRightValue() {return rightValue;}
}
