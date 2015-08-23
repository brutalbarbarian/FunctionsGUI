package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;
import com.brutalbarbarian.utils.MathUtils;
import com.brutalbarbarian.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/12/14.
 */
public class LargestPalindromeProduct implements ProjectEulerFunction{
    @Override
    public String getName() {
        return "Largest palindrome product";
    }

    @Override
    public String getDescription() {
        return "Find the largest palindrome made from the product of two 3-digit numbers.";
    }

    @Override
    public String getExternalID() {
        return "4";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text) {
        int digits = Integer.parseInt(parameters.get(Parameter.Digits));
        int upperLimit = MathUtils.pow(10, digits) - 1; // e.g. 3 will give 999
        int lowerLimit = MathUtils.pow(10, digits - 1); // e.g. 3 will give 100

        int result = 0;

        for (int i = upperLimit; i >= lowerLimit; i--) {
            for (int j = upperLimit; j >= lowerLimit; j--) {
                int num = i * j;
                if (num < result) {
                    break;  // no need to look any lower in j as no result can be better
                } else if (StringUtils.IsPalindrome(num)) {
                    // Result is valid and better then our current.
                    result = num;
                }
            }
        }

        return result;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.Digits);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case Digits : return 3;
            default: return null;
        }
    }
}
