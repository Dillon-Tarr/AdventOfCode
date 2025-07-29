package year2023.day19.part2;

import shared.LongInclusiveNumberRange;

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
                    if (rangeSet.xRange.inclusiveRangeEnd() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.xRange.rangeStart() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.xRange.rangeStart(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue, rangeSet.xRange.inclusiveRangeEnd())));
                    }
                } else {
                    if (rangeSet.xRange.rangeStart() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.xRange.inclusiveRangeEnd() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue+1, rangeSet.xRange.inclusiveRangeEnd()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.xRange.rangeStart(), comparisonValue)));
                    }
                }
            }
            case 'm' -> {
                if (comparisonOperatorIsLessThan) {
                    if (rangeSet.mRange.inclusiveRangeEnd() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.mRange.rangeStart() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.mRange.rangeStart(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue, rangeSet.mRange.inclusiveRangeEnd())));
                    }
                } else {
                    if (rangeSet.mRange.rangeStart() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.mRange.inclusiveRangeEnd() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue+1, rangeSet.mRange.inclusiveRangeEnd()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.mRange.rangeStart(), comparisonValue)));
                    }
                }
            }
            case 'a' -> {
                if (comparisonOperatorIsLessThan) {
                    if (rangeSet.aRange.inclusiveRangeEnd() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.aRange.rangeStart() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.aRange.rangeStart(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue, rangeSet.aRange.inclusiveRangeEnd())));
                    }
                } else {
                    if (rangeSet.aRange.rangeStart() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.aRange.inclusiveRangeEnd() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue+1, rangeSet.aRange.inclusiveRangeEnd()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.aRange.rangeStart(), comparisonValue)));
                    }
                }
            }
            case 's' -> {
                if (comparisonOperatorIsLessThan) {
                    if (rangeSet.sRange.inclusiveRangeEnd() < comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.sRange.rangeStart() >= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.sRange.rangeStart(), comparisonValue-1), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue, rangeSet.sRange.inclusiveRangeEnd())));
                    }
                } else {
                    if (rangeSet.sRange.rangeStart() > comparisonValue) {
                        rangeSet.metMostRecentWorkflowCondition = true;
                        resultingRangeSets.add(rangeSet);
                    }
                    else if (rangeSet.sRange.inclusiveRangeEnd() <= comparisonValue) resultingRangeSets.add(rangeSet);
                    else {
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(comparisonValue+1, rangeSet.sRange.inclusiveRangeEnd()), nextWorkflow));
                        resultingRangeSets.add(new RatingRangeSet(rangeSet, category, new LongInclusiveNumberRange(rangeSet.sRange.rangeStart(), comparisonValue)));
                    }
                }
            }
            default -> throw new RuntimeException("Invalid category: '"+category+"'");
        }
        return resultingRangeSets;
    }

}
