package com.brutalbarbarian.functions.functions.projecteuler;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.ProjectEulerFunction;
import com.brutalbarbarian.utils.PrimeGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/29/14.
 */
public class SummationOfPrimes implements ProjectEulerFunction {
    @Override
    public String getName() {
        return "Summation of Primes";
    }

    @Override
    public String getDescription() {
        return "Find the sum of all the primes below two million.";
    }

    @Override
    public String getExternalID() {
        return "10";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text) {
        int upperLimit = Integer.parseInt(parameters.get(Parameter.UpperLimit));
        int i = 1;
        int prime = 0;
        int result = 0;
        PrimeGenerator gen = new PrimeGenerator();
        while (prime < upperLimit) {
            System.out.println(prime);
            result += prime;

            prime = gen.getPrime(i++);
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
            case UpperLimit: return 2000000;
        }
        return null;
    }

    @Override
    public boolean requiresParameters() {
        return true;
    }
}
