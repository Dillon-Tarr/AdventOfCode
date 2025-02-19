package day23.part2;

import java.util.ArrayList;

class Step {
    int stepCount;
    Node node;
    ArrayList<Node> visitedNodes = new ArrayList<>();

    Step(Node node, int stepCount) {
        this.node = node;
        this.stepCount = stepCount;
        visitedNodes.add(node);
    }

    Step(Node node, int stepCount, ArrayList<Node> pastNodes) {
        this.node = node;
        this.stepCount = stepCount;
        visitedNodes.addAll(pastNodes);
        visitedNodes.add(node);
    }

}
