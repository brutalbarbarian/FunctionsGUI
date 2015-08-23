package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/13/14.
 */
public class SmallestMultiple implements ProjectEulerFunction {
    @Override
    public String getName() {
        return "Smallest Multiple";
    }

    @Override
    public String getDescription() {
        return "What is the smallest positive number that is evenly divisible by all of the numbers from 1 to 20?";
    }

    @Override
    public String getExternalID() {
        return "5";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text) {
        int upperLimit = Integer.parseInt(parameters.get(Parameter.UpperLimit));
        int lowerLimit = upperLimit/2 + 1;

        int inc = upperLimit * (upperLimit - 1);

        int iterations = 0;
        int result = inc;
        while (true) {
            boolean valid = true;
            for (int i = lowerLimit; i < upperLimit - 1; i++) {
                if (result % i != 0) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                break;
            }
            iterations ++;
            result += inc;
        }
        return result;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.UpperLimit);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch (parameter) {
            case UpperLimit: return 20;
        }
        return null;
    }
}
