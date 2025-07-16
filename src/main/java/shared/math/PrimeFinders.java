package shared.math;

import java.util.ArrayList;

public class PrimeFinders {

    public static void main(String[] args) {
        printPrimesUntilValue(1000);
    }

    public static void findMorePrimes(int findPrimesToThisValue, ArrayList<Integer> primes) {
        int lastPrime = primes.get(primes.size()-1);
        for (int i = lastPrime+1; i <= findPrimesToThisValue; i++) {
            boolean foundThatIHasAPrimeFactor = false;
            for (int prime : primes) {
                if (i % prime == 0) {
                    foundThatIHasAPrimeFactor = true;
                    break;
                }
            }
            if (!foundThatIHasAPrimeFactor) primes.add(i);
        }
    }

    public static ArrayList<Integer> findPrimesUntilValue(int findPrimesToThisValue) {
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        for (int i = 3; i <= findPrimesToThisValue; i++) {
            boolean foundThatIHasAPrimeFactor = false;
            for (int prime : primes) {
                if (i % prime == 0) {
                    foundThatIHasAPrimeFactor = true;
                    break;
                }
            }
            if (!foundThatIHasAPrimeFactor) primes.add(i);
        }
        return primes;
    }

    public static void printPrimesUntilValue(int findPrimesToThisValue) {
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        for (int i = 3; i <= findPrimesToThisValue; i++) {
            boolean foundThatIHasAPrimeFactor = false;
            for (int prime : primes) {
                if (i % prime == 0) {
                    foundThatIHasAPrimeFactor = true;
                    break;
                }
            }
            if (!foundThatIHasAPrimeFactor) {
                primes.add(i);
                System.out.println(i);
            }
        }
    }

}
