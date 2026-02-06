package shared.math;

import java.util.ArrayList;

public class LCMFinders {

    // Find the least common multiple of n elements by getting the product of the least greater-than-1 integer factors from all elements:
    public static long findLCM(long[] longArray) {
        final int arrayLength = longArray.length;
        if (arrayLength > 33) return findLCMWithPrimes(longArray); // Using primes is likely to be faster with many or
        int usePrimesThreshold = 999999/arrayLength;                                 // <- sufficiently large elements
        for (int i = 0; i < arrayLength; i++) {
            long n = longArray[i];
            if (n < 0) longArray[i] *= -1; // If negative, set element to its absolute value.
            else if (n == 0) return 0;  // LCM is 0 if an element is 0.
            else if (n > usePrimesThreshold) return findLCMWithPrimes(longArray);
        }
        long LCM = 1;
        int divisor = 2;
        boolean remainderOne = false; // <- referring to when doing a division by 6; set to false initially because this is first used with 5.
        mainLoop: while (true) { // Factor the divisor out of each element, if possible, as many times as possible.
            boolean atLeastOneEvenDivision = false;
            // Try to divide each element by the divisor and update the element's value if successful:
            for (int i = 0; i < arrayLength; i++) { if (longArray[i] % divisor == 0) { atLeastOneEvenDivision = true; longArray[i] /= divisor; } }
            // If at least one division had no remainder, each element that was evenly divisible by the divisor has been reduced
            // by a factor of the divisor. Those elements require that this factor be a factor of the LCM of the group.
            if (atLeastOneEvenDivision) LCM *= divisor; // <- For that reason, this factor (the divisor) is multiplied into the LCM:
            else switch (divisor) { // Otherwise, increase the divisor and check the elements again:
                case 2 -> divisor = 3;
                case 3 -> divisor = 5;
                default -> { // Skips multiples of 2 and 3; in some cases faster than calculating primes; much faster than simple incrementing.
                    divisor += remainderOne ? 4 : 2;
                    remainderOne = !remainderOne;
                }
            }
            for (long n : longArray) if (n != 1) continue mainLoop;
            return LCM;
            /* If each element is fully factored (reduced to 1 by repeatedly finding and dividing by its least greater-than-1 integer factor),
            the least factor of any one element has been filtered from all elements (if possible for each element) until all greater-than-1
            integer factors have been removed from all elements. The product of these factors is the least common multiple of all the elements. */
        }
    }

    // NOW WITH PRIMES for efficiency when dealing with many division checks (many elements):
    public static long findLCMWithPrimes(long[] longArray) {
        return findLCMWithPredefinedPrimes(longArray, PrimeFinders.findPrimesUntilValue(99));
    }

    public static long findLCMWithPrimes(ArrayList<Long> arrayList) {
        long[] longArray = new long[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) longArray[i] = arrayList.get(i);
        return findLCMWithPrimes(longArray);
    }

    // With predefined primes (primes must be in order from least to greatest):
    public static long findLCMWithPredefinedPrimes(long[] longArray, ArrayList<Integer> primes) {
        final int arrayLength = longArray.length;
        for (int i = 0; i < arrayLength; i++) {
            if (longArray[i] < 0) longArray[i] *= -1;
            else if (longArray[i] == 0) return 0;
        }
        long LCM = 1;
        int divisor, primeIndex = 0, findPrimesToValue = primes.getLast();
        mainLoop: while (true) {
            if (primeIndex == primes.size()) {
                findPrimesToValue <<= 1;
                PrimeFinders.findMorePrimes(findPrimesToValue, primes);
            }
            divisor = primes.get(primeIndex);
            boolean atLeastOneEvenDivision = false;
            for (int n = 0; n < arrayLength; n++) if (longArray[n] % divisor == 0) { atLeastOneEvenDivision = true; longArray[n] /= divisor; }
            if (atLeastOneEvenDivision) LCM *= divisor; else primeIndex++;
            for (long n : longArray) if (n != 1) continue mainLoop;
            return LCM;
        }
    }

}
