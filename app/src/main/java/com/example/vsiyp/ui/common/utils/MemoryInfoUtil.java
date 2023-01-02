package com.example.vsiyp.ui.common.utils;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;

import com.example.vsiyp.VideoEditorApplication;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

public class MemoryInfoUtil {
    private static final String TAG = "MemoryInfoUtil";

    public static final int MEMORY_THRESHOLD_4G = 4 * 1024;

    public static final int MEMORY_THRESHOLD_6G = 6 * 1024;

    private MemoryInfoUtil() {
    }

    public static boolean isLowMemoryDevice() {
        return isLowMemoryDevice(MEMORY_THRESHOLD_4G, VideoEditorApplication.getInstance().getContext());
    }

    public static boolean isLowMemoryDevice(Context context) {
        return isLowMemoryDevice(MEMORY_THRESHOLD_4G, context);
    }

    public static boolean isLowMemoryDevice(int size, Context context) {
        if (context == null) {
            return false;
        }

        long totalMem = getMemorySizeM(context);
        SmartLog.d(TAG, "isLowMemoryDevice: " + totalMem + " MB");
        return (int) totalMem <= size;
    }

    public static long getMemorySizeM(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info.totalMem >> 20;
    }

    public static long getMemorySizeM() {
        Context context = VideoEditorApplication.getInstance().getContext();
        if (context == null) {
            return 0;
        }
        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info.totalMem >> 20;
    }
}
