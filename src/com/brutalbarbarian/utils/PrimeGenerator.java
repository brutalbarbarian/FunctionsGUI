package com.brutalbarbarian.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lu on 3/17/14.
 */
public class PrimeGenerator {
    private List<Integer> primes;
    public PrimeGenerator () {
        primes = new ArrayList<>();
        primes.add(2);  // Add the first prime
        primes.add(3);  // Add the first odd prime.
    }

    // Returns the nth prime, where
    // n=1=>2,
    // n=2=>3,
    // etc
    public int getPrime(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        if (n > primes.size()) {
            for (int i = primes.get(primes.size() - 1) + 2; primes.size() < n; i+= 2) {
                boolean isPrime = true;
                for (int j : primes) {
                    if (i % j == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if (isPrime) {
                    primes.add(i);
                }
            }
        }

        return primes.get(n - 1);
    }
}
