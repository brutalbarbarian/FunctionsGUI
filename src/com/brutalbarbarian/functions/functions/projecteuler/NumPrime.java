package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;
import com.brutalbarbarian.utils.PrimeGenerator;
import javafx.scene.control.TextArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/17/14.
 */
public class NumPrime implements ProjectEulerFunction{
    @Override
    public String getName() {
        return "10001st Prime";
    }

    @Override
    public String getExternalID() {
        return "7";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        int n = Integer.parseInt(parameters.get(Parameter.Target));
        return new PrimeGenerator().getPrime(n);
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.Target);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch (parameter) {
            case Target: return 10001;
        }
        return null;
    }

    @Override
    public boolean requiresParameters() {
        return true;
    }
}
