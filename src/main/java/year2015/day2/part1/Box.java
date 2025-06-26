package year2015.day2.part1;

public class Box {
    private final int[] dimensions = new int[3];
    private final int[] surfaceAreas = new int[3];
    int wrappingPaperNeeded;

    Box(String inputString) {
        parseDimensions(inputString);
        calculateSurfaceAreas();
        calculateWrappingPaperNeeded();
        System.out.println(inputString+", needed: "+wrappingPaperNeeded);
    }

    private void parseDimensions(String inputString) {
        String[] dimensionStrings = inputString.split("x");
        for (int i = 0; i < 3; i++) {
            dimensions[i] = Integer.parseInt(dimensionStrings[i]);
        }
    }

    private void calculateSurfaceAreas() {
        surfaceAreas[0] = dimensions[0]*dimensions[1];
        surfaceAreas[1] = dimensions[1]*dimensions[2];
        surfaceAreas[2] = dimensions[2]*dimensions[0];
    }

    private void calculateWrappingPaperNeeded() {
        int areaOfSmallestSurface = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            if (surfaceAreas[i] < areaOfSmallestSurface) areaOfSmallestSurface = surfaceAreas[i];
        }
        wrappingPaperNeeded = (2*surfaceAreas[0])+(2*surfaceAreas[1])+(2*surfaceAreas[2])+areaOfSmallestSurface;
    }

}
