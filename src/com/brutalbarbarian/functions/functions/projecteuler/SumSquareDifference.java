package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;
import com.brutalbarbarian.utils.MathUtils;
import javafx.scene.control.TextArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/14/14.
 */
public class SumSquareDifference implements ProjectEulerFunction{

    @Override
    public String getName() {
        return "Sum Square Difference";
    }

    @Override
    public String getDescription() {
        return "Find the difference between the sum of the squares of the first one hundred natural numbers and the square of the sum.";
    }

    @Override
    public String getExternalID() {
        return "6";
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.Digits);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch (parameter) {
            case Digits: return 3;
        }
        return null;
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        int digits = Integer.parseInt(parameters.get(Parameter.Digits));
        int n = MathUtils.pow(10, digits - 1);

        return MathUtils.pow(MathUtils.partialSum(n), 2) - MathUtils.partialSquareSum(n);
    }
}
