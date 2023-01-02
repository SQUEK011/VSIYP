package com.example.vsiyp.ui.common.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils {
    public static Bitmap compressBitmap(Bitmap bitmap, double newWidth, double newHeight) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
        return newBitmap;
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
            int xTopLeft = (widthOrg - edgeLength) / 2;
            int yTopLeft = (heightOrg - edgeLength) / 2;
            try {
                result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }
}
