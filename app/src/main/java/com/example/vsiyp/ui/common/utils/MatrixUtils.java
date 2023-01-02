package com.example.vsiyp.ui.common.utils;

import android.graphics.Matrix;

public class MatrixUtils {
    private final static float[] MATRIX_V = new float[9];

    public static float getRotate(Matrix matrix) {
        matrix.getValues(MATRIX_V);
        return Math.round(Math.atan2(MATRIX_V[Matrix.MSKEW_X], MATRIX_V[Matrix.MSCALE_X]) * (180 / Math.PI));
    }
}
