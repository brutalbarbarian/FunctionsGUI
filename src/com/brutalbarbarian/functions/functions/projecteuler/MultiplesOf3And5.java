package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;
import com.brutalbarbarian.utils.MathUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/11/14.
 *
 * TODO... Ask someone why does is not the correct answer
 */
public class MultiplesOf3And5 implements ProjectEulerFunction {
    @Override
    public String getName() {
        return "Multiples of 3 and 5";
    }

    @Override
    public String getDescription() {
        return "Find the sum of all the multiples of 3 or 5 below 1000.";
    }

    @Override
    public String getExternalID() {
        return "1";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text) {
        int upperLimit = Integer.parseInt(parameters.get(Parameter.UpperLimit));
        int firstNumber = Integer.parseInt(parameters.get(Parameter.FirstNumber));
        int secondNumber = Integer.parseInt(parameters.get(Parameter.SecondNumber));

        int sharedNumber = firstNumber * secondNumber;

        int result = MathUtils.partialSum(Math.floorDiv(upperLimit - 1, firstNumber)) * firstNumber +
                MathUtils.partialSum(Math.floorDiv(upperLimit - 1, secondNumber)) * secondNumber -
                MathUtils.partialSum(Math.floorDiv(upperLimit - 1, sharedNumber)) * sharedNumber;


        return result;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.FirstNumber, Parameter.SecondNumber, Parameter.UpperLimit);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case FirstNumber: return 3;
            case SecondNumber: return 5;
            case UpperLimit: return 1000;
            default: return null;
        }
    }
}
