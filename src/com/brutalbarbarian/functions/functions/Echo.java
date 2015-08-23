package com.brutalbarbarian.functions.functions;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.Function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/8/14.
 */
public class Echo implements Function {
    @Override
    public String getName() {
        return "Echo";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text) {
        for (Object o : parameters.entrySet()) {
            System.out.println(o.toString());
        }
        return text;
    }

    @Override
    public boolean requiresParameters() {
        return true;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.FirstNumber, Parameter.SecondNumber, Parameter.ThirdNumber);
    }

    @Override
    public boolean requiresText() {
        return true;
    }
}
