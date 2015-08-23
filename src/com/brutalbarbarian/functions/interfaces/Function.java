package com.brutalbarbarian.functions.interfaces;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.constants.ResultType;
import com.brutalbarbarian.functions.constants.Category;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 3/8/14.
 */
public interface Function {
    public String getName();
    public default String getDisplayName() {
        return getName();
    }
    public default String getCategory() {
        return Category.Unknown;
    }
    public default String getDescription() {
        return "";
    }
    public default String getURL() {
        return "";
    }
    public default String getExternalID() {
        return "";
    }

    public Object computeResult(HashMap<Parameter, String> parameters, String text);

    // Parameter related functions
    public default boolean requiresText() {
        return false;
    }
    public default List<Parameter> getParameters() {
        return null;
    }
    public default Object getDefaultParamValue(Parameter parameter) {
        return null;
    }
    public default String getDefaultText() {
        return null;
    }
    public default boolean requiresParameters() {
        return getParameters() != null;
    }

    // Result flags
    public default ResultType getResultType() {
        return ResultType.String;
    }

    public default void requestStop() {
        // Do nothing... implement to do something
    }
}
