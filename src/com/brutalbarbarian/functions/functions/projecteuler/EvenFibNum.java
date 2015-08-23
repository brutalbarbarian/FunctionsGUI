package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;
import javafx.scene.control.TextArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/12/14.
 */
public class EvenFibNum implements ProjectEulerFunction{
    @Override
    public String getName() {
        return "Even Fibonacci Numbers";
    }

    @Override
    public String getDescription() {
        return "By considering the terms in the Fibonacci sequence whose values do not exceed four million, " +
                "find the sum of the even-valued terms.";
    }

    @Override
    public String getExternalID() {
        return "2";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        int upperLimit = Integer.parseInt(parameters.get(Parameter.UpperLimit));

        int result = 0;
        int prevValue2 = 0, prevValue = 0, value = 1;
        while (value < upperLimit) {
            if ((value % 2) == 0) {
                // is even
                result += value;
            }
            prevValue2 = prevValue;
            prevValue = value;
            value = prevValue2 + prevValue;
        }

        return result;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.UpperLimit);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case UpperLimit: return 4000000;
            default: return null;
        }
    }
}
