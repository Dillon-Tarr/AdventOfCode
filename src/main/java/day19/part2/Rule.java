package day19.part2;

import java.util.ArrayList;

class Rule {
    private final char category;
    private final boolean comparisonOperatorIsLessThan;
    private final int comparisonValue;
    private final String nextWorkflow;

    Rule (String ruleString) {
        int colonIndex = ruleString.indexOf(":");
        category = ruleString.charAt(0);
        comparisonOperatorIsLessThan = ruleString.charAt(1) == '<';
        comparisonValue = Integer.parseInt(ruleString.substring(2, colonIndex));
        nextWorkflow = ruleString.substring(colonIndex+1);
    }

    ArrayList<RatingRangeSet> matchCheck(RatingRangeSet rangeSet) {
        ArrayList<RatingRangeSet> resultingRangeSets = new ArrayList<>();
        rangeSet.incrementCompletedRuleCount();
        switch (category) {
            case 'x' -> {
                if (comparisonOperatorIsLessThan) {
                    if (rangeSet.xRange.end() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.xRange.start() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.xRange.start(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue, rangeSet.xRange.end())));
                    }
                } else {
                    if (rangeSet.xRange.start() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.xRange.end() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue+1, rangeSet.xRange.end()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.xRange.start(), comparisonValue)));
                    }
                }
            }
            case 'm' -> {
                if (comparisonOperatorIsLessThan) {
                    if (rangeSet.mRange.end() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.mRange.start() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.mRange.start(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue, rangeSet.mRange.end())));
                    }
                } else {
                    if (rangeSet.mRange.start() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.mRange.end() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue+1, rangeSet.mRange.end()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.mRange.start(), comparisonValue)));
                    }
                }
            }
            case 'a' -> {
                if (comparisonOperatorIsLessThan) {
                    if (rangeSet.aRange.end() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.aRange.start() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.aRange.start(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue, rangeSet.aRange.end())));
                    }
                } else {
                    if (rangeSet.aRange.start() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.aRange.end() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue+1, rangeSet.aRange.end()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.aRange.start(), comparisonValue)));
                    }
                }
            }
            case 's' -> {
                if (comparisonOperatorIsLessThan) {
                    if (rangeSet.sRange.end() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.sRange.start() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.sRange.start(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue, rangeSet.sRange.end())));
                    }
                } else {
                    if (rangeSet.sRange.start() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.sRange.end() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(comparisonValue+1, rangeSet.sRange.end()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongRange(rangeSet.sRange.start(), comparisonValue)));
                    }
                }
            }
            default -> throw new RuntimeException("Invalid category: '"+category+"'");
        }
        return resultingRangeSets;
    }

}
