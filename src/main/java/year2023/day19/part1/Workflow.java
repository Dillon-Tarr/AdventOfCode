package year2023.day19.part1;

class Workflow {
    private final Rule[] rules;
    private final String defaultResult;

    Workflow (String inputString) {
        String[] ruleStrings = inputString.split(",");
        rules = new Rule[ruleStrings.length-1];
        for (int i = 0; i < rules.length; i++) rules[i] = new Rule(ruleStrings[i]);
        defaultResult = ruleStrings[ruleStrings.length-1];
    }

    String processPart(Part part) {
        for (Rule rule : rules) if (rule.matchCheck(part)) return rule.nextWorkflow;
        return defaultResult;
    }

    private class Rule {
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

        public boolean matchCheck(Part part) {
            return switch (category) {
                case 'x' -> comparisonOperatorIsLessThan ?  part.x < comparisonValue : part.x > comparisonValue;
                case 'm' -> comparisonOperatorIsLessThan ?  part.m < comparisonValue : part.m > comparisonValue;
                case 'a' -> comparisonOperatorIsLessThan ?  part.a < comparisonValue : part.a > comparisonValue;
                case 's' -> comparisonOperatorIsLessThan ?  part.s < comparisonValue : part.s > comparisonValue;
                default -> throw new RuntimeException("Invalid category: '"+category+"'");
            };
        }

    }

}
