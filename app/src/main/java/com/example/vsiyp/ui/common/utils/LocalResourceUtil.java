package com.example.vsiyp.ui.common.utils;

import android.content.Context;

public class LocalResourceUtil {
    public static int getDrawableId(Context context, String var) {
        try {
            return context.getResources().getIdentifier(var, "drawable", context.getPackageName());
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getStringId(Context context, String var) {
        try {
            return context.getResources().getIdentifier(var, "string", context.getPackageName());
        } catch (Exception e) {
            return 0;
        }
    }
}

