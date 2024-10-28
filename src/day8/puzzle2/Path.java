package day8.puzzle2;

import java.util.ArrayList;

public class Path {
    final ArrayList<Node> nodes;
    final char[] instructionSet;
    Node currentNode;
    int stepCount = -1;
    ArrayList <KeyFindRecord> zEndingKeyFindRecords = new ArrayList<>();

    Path(ArrayList<Node> nodes, char[] instructionSet, Node startingNode) {
        this.nodes = nodes;
        this.instructionSet = instructionSet;
        currentNode = startingNode;
        findZEndingNodeSteps();
    }

    public ArrayList<KeyFindRecord> getZEndingKeyFindRecords() {return zEndingKeyFindRecords;}

    void findZEndingNodeSteps() {
        while (true) { for (int instructionIndex = 0; instructionIndex < instructionSet.length; instructionIndex++) {
            for (Node node : nodes) { if (node.getKey() == currentNode.getValue(instructionSet[instructionIndex])) {
                currentNode = node;
                break;}}
            stepCount++;
            if (currentNode.getKey()[2] == 'Z') {
                if (!zEndingKeyFindRecords.isEmpty()) {
                    for (KeyFindRecord record : zEndingKeyFindRecords)
                        if (instructionIndex == record.getInstructionIndex() && currentNode.getKey() == record.getKey())
                            return;
                }
                zEndingKeyFindRecords.add(new KeyFindRecord(currentNode.getKey(), instructionIndex, stepCount));
            }
        }}
    }



}
