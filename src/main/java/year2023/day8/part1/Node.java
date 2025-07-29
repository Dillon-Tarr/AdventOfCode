package year2023.day8.part1;

class Node {
    private final String key;
    private final String leftValue;
    private final String rightValue;

    public Node(String inputString) {
        key = inputString.substring(0, 3);
        leftValue = inputString.substring(7, 10);
        rightValue = inputString.substring(12, 15);
    }

    String getKey() {return key;}
    String getLeftValue() {return leftValue;}
    String getRightValue() {return rightValue;}
}
