package com.brutalbarbarian.functions.interfaces;

import com.brutalbarbarian.functions.constants.Category;

/**
 * Created by Lu on 3/13/14.
 */
public interface ProjectEulerFunction extends Function {
    @Override
    public default String getCategory() {
        return Category.ProjectEuler;
    }

    @Override
    public default String getURL() {
        return "http://projecteuler.net/problem=" + getExternalID();
    }
}
