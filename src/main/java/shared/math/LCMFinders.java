package shared.math;

import java.util.ArrayList;

public class LCMFinders {

    // Java program to find the LCM of n elements by getting the product of the least greater-than-1 integer factors from all elements:
    public static long findLCM(long[] longArray) {
        for (int n = 0; n < longArray.length; n++) {
            if (longArray[n] == 0) return 0;  // LCM is 0 if an element is 0.
            if (longArray[n] < 0) longArray[n] *= -1; // If negative, set element to its absolute value.
        }

        long LCM = 1;
        long divisor = 2;

        while (true) { // Factor the divisor out of each element, if possible, as many times as possible.
            boolean atLeastOneElementIsEvenlyDivisibleByDivisor = false;
            for (int n = 0; n < longArray.length; n++) {
                if (longArray[n] % divisor == 0) {
                    atLeastOneElementIsEvenlyDivisibleByDivisor = true;
                    longArray[n] /= divisor;
                }
            }
            // If none of the elements of the array were evenly divisible by the divisor, increment the divisor and check the elements again:
            if (!atLeastOneElementIsEvenlyDivisibleByDivisor) divisor++;
                // Otherwise, each element that was evenly divisible by the divisor has been reduced by a factor of the divisor. Those elements require that this factor
                // be a factor of the LCM of the group. For that reason, this factor (the divisor) is multiplied into the LCM:
            else LCM *= divisor;

            int fullyFactoredElementCounter = 0;
            for (long n : longArray) if (n == 1) fullyFactoredElementCounter++;
            if (fullyFactoredElementCounter == longArray.length) return LCM;
            /* If each element is fully factored (reduced to 1 by repeatedly finding and dividing by its least greater-than-1 integer factor),
            the least factor of any one element has been filtered from all elements (if possible for each element) until all greater-than-1
            integer factors have been removed from all elements. The product of these factors is the least common multiple of all the elements. */
        }
    }

    // NOW WITH PRIMES for efficiency when dealing with many division checks (many elements):
    public static long findLCMWithPrimes(long[] longArray) {
        for (int n = 0; n < longArray.length; n++) {
            if (longArray[n] == 0) return 0;
            if (longArray[n] < 0) longArray[n] *= -1;
        }

        long LCM = 1;
        int divisor;
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);

        int findPrimesToValue = 1000;
        PrimeFinders.findMorePrimes(findPrimesToValue, primes);
        int primeIndex = 0;

        while (true) {
            if (primeIndex == primes.size()) {
                findPrimesToValue *= 2;
                PrimeFinders.findMorePrimes(findPrimesToValue, primes);
            }
            divisor = primes.get(primeIndex);

            boolean atLeastOneElementIsEvenlyDivisibleByDivisor = false;
            for (int n = 0; n < longArray.length; n++) {
                if (longArray[n] % divisor == 0) {
                    atLeastOneElementIsEvenlyDivisibleByDivisor = true;
                    longArray[n] /= divisor;
                }
            }
            if (!atLeastOneElementIsEvenlyDivisibleByDivisor) primeIndex++;
            else LCM *= divisor;

            int fullyFactoredElementCounter = 0;
            for (long n : longArray) if (n == 1) fullyFactoredElementCounter++;
            if (fullyFactoredElementCounter == longArray.length) return LCM;
        }
    }

    public static long findLCMWithPrimes(ArrayList<Long> arrayList) {
        long[] longArray = new long[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) longArray[i] = arrayList.get(i);
        return findLCMWithPrimes(longArray);
    }

    // With predefined primes (primes must be in order from least to greatest):
    public static long findLCMWithPredefinedPrimes(long[] longArray, ArrayList<Integer> primes) {
        for (int n = 0; n < longArray.length; n++) {
            if (longArray[n] == 0) return 0;
            if (longArray[n] < 0) longArray[n] *= -1;
        }

        long LCM = 1;
        int divisor;

        int findPrimesToValue = primes.get(primes.size()-1);
        PrimeFinders.findMorePrimes(findPrimesToValue, primes);
        int primeIndex = 0;

        while (true) {
            if (primeIndex == primes.size()) {
                findPrimesToValue *= 2;
                PrimeFinders.findMorePrimes(findPrimesToValue, primes);
            }
            divisor = primes.get(primeIndex);

            boolean atLeastOneElementIsEvenlyDivisibleByDivisor = false;
            for (int n = 0; n < longArray.length; n++) {
                if (longArray[n] % divisor == 0) {
                    atLeastOneElementIsEvenlyDivisibleByDivisor = true;
                    longArray[n] /= divisor;
                }
            }
            if (!atLeastOneElementIsEvenlyDivisibleByDivisor) primeIndex++;
            else LCM *= divisor;

            int fullyFactoredElementCounter = 0;
            for (long n : longArray) if (n == 1) fullyFactoredElementCounter++;
            if (fullyFactoredElementCounter == longArray.length) return LCM;
        }
    }


}
