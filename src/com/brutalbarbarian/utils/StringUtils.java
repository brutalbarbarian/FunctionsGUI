package com.brutalbarbarian.utils;

/**
 * Created by Lu on 3/12/14.
 */
public class StringUtils {
    public static boolean IsPalindrome(final Object o) {
        if (o == null) {
            return false;
        } else {
            char[] chars = o.toString().toCharArray();
            int high = chars.length - 1;
            int mid = (high + 1) >> 1;    // floor(high/2)
            for (int i = 0; i < mid; i++) {
                if (chars[i] != chars[high - i]) {
                    // At least one character isn't the same
                    return false;
                }
            }
            return true;
        }
    }
}
