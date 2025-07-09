package year2015.day9.part1;

import java.util.HashSet;

class PossibilityChecker {

    static HashSet<String> getMirrorlessPossibilities(int numberOfLocations) {
        HashSet<String> mirrorlessPossibilities = new HashSet<>();
        switch (numberOfLocations) {
            case 2 -> {
                for (int a = 0; a < 2; a++) for (int b = 0; b < 2 ; b++)
                    if (a!=b && !mirrorlessPossibilities.contains(""+b+a))
                        mirrorlessPossibilities.add(""+a+b);
            }
            case 3 -> {
                for (int a = 0; a < 3; a++) for (int b = 0; b < 3 ; b++) for (int c = 0; c < 3; c++)
                    if (a!=b && a!=c && b!=c && !mirrorlessPossibilities.contains(""+c+b+a))
                        mirrorlessPossibilities.add(""+a+b+c);
            }
            case 4 -> {
                for (int a = 0; a < 4; a++) for (int b = 0; b < 4 ; b++) for (int c = 0; c < 4; c++) for (int d = 0; d < 4; d++)
                    if (a!=b && a!=c && a!=d && b!=c && b!=d && c!=d && !mirrorlessPossibilities.contains(""+d+c+b+a))
                        mirrorlessPossibilities.add(""+a+b+c+d);
            }
            case 5 -> {
                for (int a = 0; a < 5; a++) for (int b = 0; b < 5 ; b++) for (int c = 0; c < 5; c++) for (int d = 0; d < 5; d++)
                    for (int e = 0; e < 5; e++)
                        if (a!=b && a!=c && a!=d && a!=e && b!=c && b!=d && b!=e && c!=d && c!=e && d!=e
                                && !mirrorlessPossibilities.contains(""+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e);
            }
            case 6 -> {
                for (int a = 0; a < 6; a++) for (int b = 0; b < 6 ; b++) for (int c = 0; c < 6; c++) for (int d = 0; d < 6; d++)
                    for (int e = 0; e < 6; e++) for (int f = 0; f < 6; f++)
                        if (a!=b && a!=c && a!=d && a!=e && a!=f && b!=c && b!=d && b!=e && b!=f && c!=d && c!=e&& c!=f
                                && d!=e && d!=f && e!=f && !mirrorlessPossibilities.contains(""+f+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e+f);
            }
            case 7 -> {
                for (int a = 0; a < 7; a++) for (int b = 0; b < 7 ; b++) for (int c = 0; c < 7; c++) for (int d = 0; d < 7; d++)
                    for (int e = 0; e < 7; e++) for (int f = 0; f < 7; f++) for (int g = 0; g < 7; g++)
                        if (a!=b && a!=c && a!=d && a!=e && a!=f && a!=g && b!=c && b!=d && b!=e && b!=f && b!=g && c!=d && c!=e && c!=f
                                && c!=g && d!=e && d!=f && d!=g && e!=f && e!=g && f!=g && !mirrorlessPossibilities.contains(""+g+f+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e+f+g);
            }
            case 8 -> {
                for (int a = 0; a < 8; a++) for (int b = 0; b < 8 ; b++) for (int c = 0; c < 8; c++) for (int d = 0; d < 8; d++)
                    for (int e = 0; e < 8; e++) for (int f = 0; f < 8; f++) for (int g = 0; g < 8; g++) for (int h = 0; h < 8; h++)
                        if (a!=b && a!=c && a!=d && a!=e && a!=f && a!=g && a!=h && b!=c && b!=d && b!=e && b!=f && b!=g && b!=h && c!=d
                                && c!=e && c!=f && c!=g && c!=h && d!=e && d!=f && d!=g && d!=h && e!=f && e!=g && e!=h && f!=g && f!=h
                                && g!=h && !mirrorlessPossibilities.contains(""+h+g+f+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e+f+g+h);
            }
            default -> throw new RuntimeException(new IllegalArgumentException("Illegal numberOfLocations argument: "+numberOfLocations));
        }
        return mirrorlessPossibilities;
    }

}
