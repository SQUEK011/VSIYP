package com.example.vsiyp.ui.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

public class SizeUtils {
    private static final float GB = (float) BigDecimalUtils.mul2(BigDecimalUtils.mul(1024f, 1024f), 1024f, 0);

    private static final float MB = (float) BigDecimalUtils.mul2(1024f, 1024f, 0);

    private static final float KB = 1024f;

    public static int dp2Px(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        Resources resources = context.getResources();
        if (resources == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return 0;
        }
        float scale = displayMetrics.density;
        return (int) (dp * scale + 0.5f);
    }

    public static String bytes2kb(long bytes) {
        DecimalFormat format = new DecimalFormat("###.02");
        if (bytes / GB >= 1) {
            return format.format((float) (bytes / GB)) + "GB";
        } else if (bytes / MB >= 1) {
            return format.format((float) (bytes / MB)) + "MB";
        } else if (bytes / KB >= 1) {
            return format.format((float) (bytes / KB)) + "KB";
        } else {
            return bytes + "B";
        }
    }

    public static int screenWidth(Context context) {
        Resources resources = context.getResources();
        if (resources == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return 0;
        }
        return displayMetrics.widthPixels;
    }

    /**
     * convert sp to its equivalent px
     */
    public static int sp2px(Context context, float spValue) {
        Resources resources = context.getResources();
        if (resources == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return 0;
        }
        final float fontScale = displayMetrics.scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int screenHeight(Context context) {
        Resources resources = context.getResources();
        if (resources == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return 0;
        }
        return displayMetrics.heightPixels;
    }
}
