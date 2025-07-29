package year2015.day2.part2;

class Box {
    private final int[] dimensions = new int[3];
    int ribbonNeeded;

    Box(String inputString) {
        parseDimensions(inputString);
        calculateRibbonNeeded();
        System.out.println(inputString+", needed: "+ribbonNeeded);
    }

    private void parseDimensions(String inputString) {
        String[] dimensionStrings = inputString.split("x");
        for (int i = 0; i < 3; i++) {
            dimensions[i] = Integer.parseInt(dimensionStrings[i]);
        }
    }

    private void calculateRibbonNeeded() {
        int largestDimensionSize = Integer.MIN_VALUE;
        int largestDimensionIndex = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            if (dimensions[i] > largestDimensionSize) {
                largestDimensionSize = dimensions[i];
                largestDimensionIndex = i;
            }
        }
        int shortestPerimeter = 0;
        for (int i = 0; i < 3; i++) {
            if (i != largestDimensionIndex) shortestPerimeter += 2*dimensions[i];
        }

        ribbonNeeded = (dimensions[0]*dimensions[1]*dimensions[2])+shortestPerimeter;
    }

}
