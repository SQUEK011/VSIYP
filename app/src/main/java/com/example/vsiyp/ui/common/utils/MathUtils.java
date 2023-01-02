package com.example.vsiyp.ui.common.utils;

public class MathUtils {
    public static boolean isEqual(float f1, float f2) {
        return Math.abs(f1 - f2) < 1.0E-7F;
    }
}
