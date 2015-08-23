package com.brutalbarbarian.functions.functions.xcom;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.XcomFunction;
import com.brutalbarbarian.utils.MathUtils;
import javafx.scene.control.TextArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lu
 * Date: 23/08/15
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterceptorStrategy implements XcomFunction {
    @Override
    public String getName() {
        return "Interceptor Probability Strategy";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        // We care about the following stats
        // for each interception count up to theoretical infinite (10 will do)
        //  for each UFO
        //    probability of # of UFO's destroyed
        //  for each interceptor
        //    probability of # of interceptors surviving
        int ufoCount = Integer.parseInt(parameters.get(Parameter.UFOCount));
        int interceptorCount = Integer.parseInt(parameters.get(Parameter.InterceptorCount));
        int continentSpread = Integer.parseInt(parameters.get(Parameter.ContinentSpread));

        int[] ufos = new int[continentSpread];
        int[] interceptors = new int[continentSpread];
        // allocate ufo and interceptors
        for (int i = 0; i < continentSpread; i++) {
            ufos[i] = ufoCount / (continentSpread - i);
            interceptors[i] = interceptorCount / (continentSpread - i);

            ufoCount -= ufos[i];
            interceptorCount -= interceptors[i];
        }


        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void computeProbability(int ufoCount, int interceptorCount, int threat) {
        double[] destroyedUFOProbability = new double[ufoCount];

        for (int successes = 0; successes <= interceptorCount; successes++) {
            double probability = Math.pow(1f/3f, successes);
            int survivingUFOs = Math.max(ufoCount - successes, 0);
            destroyedUFOProbability[survivingUFOs] += probability;
        }
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.InterceptorCount, Parameter.UFOCount, Parameter.ContinentSpread);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case InterceptorCount: return 3;// How many interceptors are deployed
            case UFOCount: return 3;        // How many UFOs are there
            case ContinentSpread: return 1; // How many continents are the UFOs and Interceptors spread over
            default: return null;
        }
    }
}
