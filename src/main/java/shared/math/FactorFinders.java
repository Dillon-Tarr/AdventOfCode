package shared.math;

import java.util.ArrayList;
import java.util.HashSet;

public class FactorFinders {

    public static ArrayList<Integer> findPrimeFactorization(int product){
        ArrayList<Integer> primes = PrimeFinders.findPrimesUntilValue(product/2);
        return findPrimeFactorizationWithPrimeList(product, primes);
    }

    public static ArrayList<Integer> findPrimeFactorization(int product, ArrayList<Integer> primes){
        if (primes.get(primes.size()-1) < product/2) PrimeFinders.findMorePrimes(product/2, primes);
        return findPrimeFactorizationWithPrimeList(product, primes);
    }

    private static ArrayList<Integer> findPrimeFactorizationWithPrimeList(int product, ArrayList<Integer> primes){
        if (product == 1) return new ArrayList<>();
        if (primes.get(primes.size()-1) < product/2) PrimeFinders.findMorePrimes(product/2, primes);
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
