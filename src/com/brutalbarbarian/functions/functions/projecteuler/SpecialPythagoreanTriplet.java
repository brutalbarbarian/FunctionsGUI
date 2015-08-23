package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/26/14.
 */
public class SpecialPythagoreanTriplet implements ProjectEulerFunction {
    @Override
    public String getName() {
        return "Special Pythagorean triplet";
    }

    @Override
    public String getExternalID() {
        return "9";
    }

    @Override
    public String getDescription() {
        return "There exists exactly one Pythagorean triplet for which a + b + c = 1000. " +
                "Find the product abc.";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text) {
        int target = Integer.parseInt(parameters.get(Parameter.Target));

        for (int i = 1; i < target; i++) {
            for (int j = i + 1; j < target - i; j++) {
                int k = target - i - j;
                if (i*i + j*j == k*k) {
                    System.out.println("i:" + i);
                    System.out.println("j:" + j);
                    System.out.println("k:" + k);
                    return i * j * k;
                }
            }
        }

        // Sequence: 1, 3, 5, 7, 9, 11 ...
        // k is the nth member of the sequence
        // n is the position
        //
        // n = (k + 1) / 2

        // By picking any odd square number k
        // k = a^2  i.e. k = 9 = 3^2 = a^2
        //               Thus n = 5 (9 is 5th term in sequence)
        // b^2 = sum(n-1 terms) i.e. (n-1) ^ 2 = 4^2 = b^2
        // c^2 = sum(n terms) i.e. (n)^2 = 5^2 = c^2


        return null;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.Target);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case Target: return 1000;
        }
        return null;
    }

    @Override
    public boolean requiresParameters() {
        return true;
    }
}
