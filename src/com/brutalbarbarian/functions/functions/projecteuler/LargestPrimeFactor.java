package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;
import com.brutalbarbarian.utils.MathUtils;
import javafx.scene.control.TextArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/12/14.
 */
public class LargestPrimeFactor implements ProjectEulerFunction{
    @Override
    public String getName() {
        return "Largest Prime Factor";
    }

    @Override
    public String getDescription() {
        return "What is the largest prime factor of the number 600851475143 ?";
    }

    @Override
    public String getExternalID() {
        return "3";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        long target = Long.valueOf(parameters.get(Parameter.Target));
        long stoppingPoint = target;

        long largestPrime = 0;
        for (long i = 3; i < stoppingPoint; i++) {
            if ((target % i) == 0 && MathUtils.isPrime(i)) {
                largestPrime = i;
            }
            stoppingPoint = target / i;
        }

        return largestPrime;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.Target);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case Target: return "600851475143";
            default: return null;
        }
    }
}
