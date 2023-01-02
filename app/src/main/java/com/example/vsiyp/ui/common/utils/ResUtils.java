package com.example.vsiyp.ui.common.utils;

import android.content.Context;
import android.content.res.Resources;

import com.example.vsiyp.VideoEditorApplication;
import com.example.vsiyp.utils.SmartLog;

public final class ResUtils {
    private static final String TAG = "ResUtils";

    private ResUtils() {
    }

    public static Resources getResources() {
        Context context = VideoEditorApplication.getInstance().getContext();
        return context == null ? null : context.getResources();
    }

    public static int getColor(int resId) {
        if (getResources() == null) {
            return 0;
        }
        try {
            return getResources().getColor(resId);
        } catch (Resources.NotFoundException e) {
            SmartLog.e(TAG, e.getMessage());
        }
        return 0;
    }
}

