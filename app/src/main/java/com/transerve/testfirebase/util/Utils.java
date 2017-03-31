package com.transerve.testfirebase.util;

/**
 * Created by Sujay on 21-03-2017.
 */

public class Utils {

    /**
     * current-3, other-6 => 6-3
     * current-6, other-3 => 6-3
     */
    public static String generate(int current, int other) {
        String result;

        if (current > other) {
            result = current + "-" + other;

        } else {
            result = other + "-" + current;
        }
        return result;
    }
}
