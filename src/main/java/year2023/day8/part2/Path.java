package year2023.day8.part2;

import java.util.ArrayList;
import java.util.Arrays;

class Path {
    final ArrayList<Node> nodes;
    final char[] instructionSet;
    Node currentNode;
    int stepCount = 0;
    ArrayList <KeyFindRecord> zEndingKeyFindRecords = new ArrayList<>();

    Path(ArrayList<Node> nodes, char[] instructionSet, Node startingNode) {
        this.nodes = nodes;
        this.instructionSet = instructionSet;
        currentNode = startingNode;
        findZEndingNodeSteps();
    }

    ArrayList<KeyFindRecord> getZEndingKeyFindRecords() {return zEndingKeyFindRecords;}

    private void setCurrentNode(Node node) {this.currentNode = node;}

    private void findZEndingNodeSteps() {
        while (true) { for (int instructionIndex = 0; instructionIndex < instructionSet.length; instructionIndex++) {
            for (Node node : nodes) {
                if (Arrays.equals(node.getKey(), currentNode.getValue(instructionSet[instructionIndex]))) {
                    setCurrentNode(node);
                    break;
                }
            }
            stepCount++;
            if (currentNode.getKey()[2] == 'Z') {
                if (!zEndingKeyFindRecords.isEmpty()) {
                    for (KeyFindRecord record : zEndingKeyFindRecords)
                        if (instructionIndex == record.instructionIndex() && Arrays.equals(currentNode.getKey(), record.key())) {
                            System.out.println("Found loop point for a path.");
                            return;
                        }
                }
                zEndingKeyFindRecords.add(new KeyFindRecord(currentNode.getKey(), instructionIndex, stepCount));
            }
        }}
    }

}
