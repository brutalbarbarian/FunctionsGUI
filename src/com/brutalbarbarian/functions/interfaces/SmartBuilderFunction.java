package com.brutalbarbarian.functions.interfaces;

import com.brutalbarbarian.functions.constants.Category;

/**
 * Created with IntelliJ IDEA.
 * User: Lu
 * Date: 23/08/15
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SmartBuilderFunction extends Function {
    @Override
    public default String getCategory() {
        return Category.SmartBuilder;
    }
}
