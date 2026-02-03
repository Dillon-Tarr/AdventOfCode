package shared.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FactorFinders {
    private static final HashMap<Integer, ArrayList<Integer>> factors = new HashMap<>();
    private static final HashMap<Integer, HashMap<Integer, Integer>> greatestCommonFactors = new HashMap<>();
    private static ArrayList<Integer> primes;

    public static int getGreatestCommonFactor(int a, int b) {
        if (primes == null) primes = PrimeFinders.findPrimesUntilValue(999);
        if (a < 0) a = -a; if (b < 0) b = -b;
        int min, max; if (a < b) { min = a; max = b; } else { max = a; min = b; }
        if (min < 2 || min == max) return min;
        var minMap = greatestCommonFactors.get(min);
        Integer gcf = null;
        if (minMap != null) {
            gcf = minMap.get(max);
            if (gcf != null) return gcf;
        } else {
            minMap = new HashMap<>();
            greatestCommonFactors.put(min, minMap);
        }
        ArrayList<Integer> minFactors = factors.get(min), maxFactors = factors.get(max);
        if (minFactors == null) {
            minFactors = findAllNonOneFactors(min);
            factors.put(min, minFactors);
        }
        if (maxFactors == null) {
            maxFactors = findAllNonOneFactors(max);
            factors.put(max, maxFactors);
        }
        ArrayList<Integer> combinedFactors = new ArrayList<>(maxFactors);
        for (int factor : minFactors) if (!combinedFactors.contains(factor)) combinedFactors.add(factor);
        combinedFactors.sort(Integer::compareTo);
        for (int i = combinedFactors.size()-1; i >= 0; i--) {
            int factor = combinedFactors.get(i);
            if (minFactors.contains(factor) && maxFactors.contains(factor)) { gcf = factor; break; }
        }
        if (gcf == null) gcf = 1;
        minMap.put(max, gcf);
        return gcf;
    }

    public static ArrayList<Integer> findPrimeFactorization(int product){
        if (primes.getLast() < product/2) PrimeFinders.findMorePrimes(product/2, primes);
        return findPrimeFactorizationWithPrimeList(product, primes);
    }

    public static ArrayList<Integer> findPrimeFactorization(int product, ArrayList<Integer> primes){
        if (primes.getLast() < product/2) PrimeFinders.findMorePrimes(product/2, primes);
        return findPrimeFactorizationWithPrimeList(product, primes);
    }

    private static ArrayList<Integer> findPrimeFactorizationWithPrimeList(int product, ArrayList<Integer> primes){
        if (product == 1) return new ArrayList<>();
        if (primes.getLast() < product/2) PrimeFinders.findMorePrimes(product/2, primes);
        ArrayList<Integer> primeFactors = new ArrayList<>();
        int multiDivisionTestNumber;
        for (Integer prime : primes) {
            if (prime > product/2) return primeFactors;
            if (product % prime == 0) {
                multiDivisionTestNumber = product;
                do {
                    primeFactors.add(prime);
                    multiDivisionTestNumber /= prime;
                }
                while (multiDivisionTestNumber % prime == 0);
            }
        }
        return primeFactors;
    }

    public static ArrayList<Integer> findAllNonOneFactors(int product) {
        ArrayList<Integer> primeFactors = findPrimeFactorization(product);
        return findAllNonOneFactorsWithPrimeFactors(product, primeFactors);
    }

    public static ArrayList<Integer> findAllNonOneFactors(int product, ArrayList<Integer> primes) {
        ArrayList<Integer> primeFactors = findPrimeFactorization(product, primes);
        return findAllNonOneFactorsWithPrimeFactors(product, primeFactors);
    }

    private static ArrayList<Integer> findAllNonOneFactorsWithPrimeFactors(int product, ArrayList<Integer> primeFactors) {
        if (product == 1) return new ArrayList<>();
        HashSet<Integer> factorSet = new HashSet<>();
        int subProduct;
        for (int intAsBinary = 1; intAsBinary < 1 << primeFactors.size(); intAsBinary++) {
            subProduct = 1;
            for (int i = 0; i < primeFactors.size(); i++) {
                if ((intAsBinary & 1 << i) != 0)
                    subProduct *= primeFactors.get(i);
            }
            factorSet.add(subProduct);
        }
        factorSet.add(product);
        ArrayList<Integer> nonOneFactors = new ArrayList<>(factorSet);
        nonOneFactors.sort(Integer::compareTo);
        return nonOneFactors;
    }

}
