package com.example.vsiyp.ui.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.util.Locale;

public class ScreenBuilderUtil {

    private ScreenBuilderUtil() {
    }

    public static boolean isRTL() {
        boolean isRtl = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;
        return isRtl;
    }

    public static boolean isRtl(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
}

