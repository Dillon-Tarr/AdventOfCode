package year2015.day13;

import java.util.ArrayList;

class PossibilityChecker {

    static ArrayList<String> getMirrorlessPossibilities(int n) {
        ArrayList<String> mirrorlessPossibilities = new ArrayList<>();
        switch (n) {
            case 2 -> {
                for (int a = 0; a < n; a++) for (int b = 0; b < n; b++)
                    if (a!=b && !mirrorlessPossibilities.contains(""+b+a))
                        mirrorlessPossibilities.add(""+a+b);
            }
            case 3 -> {
                for (int a = 0; a < n; a++) for (int b = 0; b < n; b++) for (int c = 0; c < n; c++)
                    if (a!=b && a!=c && b!=c && !mirrorlessPossibilities.contains(""+c+b+a))
                        mirrorlessPossibilities.add(""+a+b+c);
            }
            case 4 -> {
                for (int a = 0; a < n; a++) for (int b = 0; b < n; b++) for (int c = 0; c < n; c++) for (int d = 0; d < n; d++)
                    if (a!=b && a!=c && a!=d && b!=c && b!=d && c!=d && !mirrorlessPossibilities.contains(""+d+c+b+a))
                        mirrorlessPossibilities.add(""+a+b+c+d);
            }
            case 5 -> {
                for (int a = 0; a < n; a++) for (int b = 0; b < n; b++) for (int c = 0; c < n; c++) for (int d = 0; d < n; d++)
                    for (int e = 0; e < n; e++)
                        if (a!=b && a!=c && a!=d && a!=e && b!=c && b!=d && b!=e && c!=d && c!=e && d!=e
                                && !mirrorlessPossibilities.contains(""+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e);
            }
            case 6 -> {
                for (int a = 0; a < n; a++) for (int b = 0; b < n; b++) for (int c = 0; c < n; c++) for (int d = 0; d < n; d++)
                    for (int e = 0; e < n; e++) for (int f = 0; f < n; f++)
                        if (a!=b && a!=c && a!=d && a!=e && a!=f && b!=c && b!=d && b!=e && b!=f && c!=d && c!=e&& c!=f
                                && d!=e && d!=f && e!=f && !mirrorlessPossibilities.contains(""+f+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e+f);
            }
            case 7 -> {
                for (int a = 0; a < n; a++) for (int b = 0; b < n; b++) for (int c = 0; c < n; c++) for (int d = 0; d < n; d++)
                    for (int e = 0; e < n; e++) for (int f = 0; f < n; f++) for (int g = 0; g < n; g++)
                        if (a!=b && a!=c && a!=d && a!=e && a!=f && a!=g && b!=c && b!=d && b!=e && b!=f && b!=g && c!=d && c!=e && c!=f
                                && c!=g && d!=e && d!=f && d!=g && e!=f && e!=g && f!=g && !mirrorlessPossibilities.contains(""+g+f+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e+f+g);
            }
            case 8 -> {
                for (int a = 0; a < n; a++) for (int b = 0; b < n; b++) for (int c = 0; c < n; c++) for (int d = 0; d < n; d++)
                    for (int e = 0; e < n; e++) for (int f = 0; f < n; f++) for (int g = 0; g < n; g++) for (int h = 0; h < n; h++)
                        if (a!=b && a!=c && a!=d && a!=e && a!=f && a!=g && a!=h && b!=c && b!=d && b!=e && b!=f && b!=g && b!=h && c!=d
                                && c!=e && c!=f && c!=g && c!=h && d!=e && d!=f && d!=g && d!=h && e!=f && e!=g && e!=h && f!=g && f!=h
                                && g!=h && !mirrorlessPossibilities.contains(""+h+g+f+e+d+c+b+a))
                            mirrorlessPossibilities.add(""+a+b+c+d+e+f+g+h);
            }
            default -> throw new RuntimeException(new IllegalArgumentException("Illegal n(umberOfPeople) argument: "+ n));
        }
        return mirrorlessPossibilities;
    }

}
