package day19.part2;

import java.util.ArrayList;

public class Workflow {
    private final Rule[] rules;
    private final String defaultResult;
    private final ArrayList<RatingRangeSet> rangeSetsToProcess = new ArrayList<>();

    Workflow (String inputString) {
        String[] ruleStrings = inputString.split(",");
        rules = new Rule[ruleStrings.length-1];
        for (int i = 0; i < rules.length; i++) rules[i] = new Rule(ruleStrings[i]);
        defaultResult = ruleStrings[ruleStrings.length-1];
    }

    ArrayList<RatingRangeSet> processRangeSet(RatingRangeSet incomingRangeSet) {
        rangeSetsToProcess.add(incomingRangeSet);
        ArrayList<RatingRangeSet> processedRangeSets = new ArrayList<>();

        while (!rangeSetsToProcess.isEmpty()) {
            RatingRangeSet currentRangeSet = rangeSetsToProcess.remove(0);
            if (currentRangeSet.completedRuleCountOnCurrentWorkflow == rules.length) {
                currentRangeSet.resetForNextWorkflow(defaultResult);
                processedRangeSets.add(currentRangeSet);
            } else {
                ArrayList<RatingRangeSet> resultingRangeSets = rules[currentRangeSet.completedRuleCountOnCurrentWorkflow].matchCheck(currentRangeSet);
                for (RatingRangeSet resultingRangeSet : resultingRangeSets) {
                    if (resultingRangeSet.metMostRecentWorkflowCondition) {
                        resultingRangeSet.resetForNextWorkflow();
                        processedRangeSets.add(resultingRangeSet);
                    } else rangeSetsToProcess.add(resultingRangeSet);
                }
            }
        }
        return processedRangeSets;
    }

}
