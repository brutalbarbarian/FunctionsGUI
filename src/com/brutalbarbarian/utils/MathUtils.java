package com.brutalbarbarian.utils;

/**
 * Created by Lu on 3/11/14.
 */
public class MathUtils {
    public static final int partialSum(final int number) {
        return (number * (number + 1)) / 2;
    };

    public static final long factorial(final int number) {
        int result = 1;
        for (int i = 2; i <= number; i++) {
            result *= i;
        }
        return result;
    }

    public static final boolean isPrime(long number) {
        if (number <= 1) return false;
        for (long i = 2; i*i <= number; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public static final int pow(int number, int power) {
        int result = 1;
        for (int i = 0; i < power; i++) {
            result = result * number;
        }
        return result;
    }

    public static final int partialSquareSum(final int number) {
        // See http://www.trans4mind.com/personal_development/mathematics/series/sumNaturalSquares.htm
        return pow(number, 3) / 3 + pow(number, 2) / 2 + number / 6 + 1;
    }
}
