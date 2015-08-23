package com.brutalbarbarian.functions.functions.smartbuilder;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.SmartBuilderFunction;
import javafx.scene.control.TextArea;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Lu
 * Date: 23/08/15
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeCreater implements SmartBuilderFunction {
    @Override
    public String getName() {
        return "BOAttribute Creator";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        // TODO
        return null;
    }

    @Override
    public boolean requiresText() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
