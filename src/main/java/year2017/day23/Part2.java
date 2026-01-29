package year2017.day23;

import shared.math.PrimeFinders;

import java.util.ArrayList;

class Part2 {

    static void main() {
        long startTime = System.nanoTime();

        runWellWrittenVersionOfTheAssembly();

        System.out.println("\nExecution time in ms: "+((double) (System.nanoTime()-startTime)/1000000));
    }

    private static void runWellWrittenVersionOfTheAssembly() {
        ArrayList<Integer> primes = PrimeFinders.findPrimesUntilValue(125400);
        int h = 0;
        for (int b = 108400; b <= 125400; b+=17) if (!primes.contains(b)) h++;
        System.out.println("\nh: "+h);
    }

}
