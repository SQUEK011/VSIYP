package com.example.vsiyp.ui.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FoldScreenUtil {
    public static final int FOLDABLE_STATE_EXPAND = 1;

    private static final String TAG = "FoldScreenUtil";

    private static final String FOLDING_SCREEN_MANAGER_CLASS_NAME = "com.huawei.android.fsm.HwFoldScreenManagerEx";

    private static final String IS_FOLDABLE_METHOD_NAME = "isFoldable";

    private static final String GET_FOLDABLE_STATE_METHOD_NAME = "getFoldableState";

    private static int sOnlyOrientation = -1;

    private static volatile Method IS_FOLDABLE_METHOD;

    private static volatile Method GET_FOLDABLE_STATE_METHOD;

    private static volatile boolean sIsHwFoldScreenManagerExClassNotFound;

    public static boolean isFoldable() {
        if (IS_FOLDABLE_METHOD == null) {
            IS_FOLDABLE_METHOD = getMethod(IS_FOLDABLE_METHOD_NAME);
        }
        if (IS_FOLDABLE_METHOD != null) {
            Object result = invokeMethod(IS_FOLDABLE_METHOD);
            SmartLog.d(TAG, IS_FOLDABLE_METHOD_NAME + " result = " + result);
            return (result != null) && (boolean) result;
        }
        return false;
    }

    private static Object getFoldableState() {
        if (GET_FOLDABLE_STATE_METHOD == null) {
            GET_FOLDABLE_STATE_METHOD = getMethod(GET_FOLDABLE_STATE_METHOD_NAME);
        }
        if (GET_FOLDABLE_STATE_METHOD != null) {
            return invokeMethod(GET_FOLDABLE_STATE_METHOD);
        }
        return null;
    }

    public static boolean isFoldableScreenExpand(@NonNull Context context) {
        Object result = getFoldableState();
        if (result instanceof Integer) {
            return (int) result == FOLDABLE_STATE_EXPAND;
        }
        return isFoldableScreenExpandByScreenRadio(context);
    }

    private static boolean isFoldableScreenExpandByScreenRadio(@NonNull Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        if (metrics != null && metrics.widthPixels != 0 && metrics.heightPixels != 0) {
            float ratioOfHeightToWidth = (float) metrics.heightPixels / (float) metrics.widthPixels;
            return ratioOfHeightToWidth >= 0.667F && ratioOfHeightToWidth <= 1.5F;
        }
        return false;
    }

    private static Object invokeMethod(@NonNull Method method) {
        Object object = null;
        try {
            object = method.invoke(null);
        } catch (IllegalAccessException e) {
            SmartLog.e(TAG, "invokeMethod failed, first exception : " + e.getMessage());
        } catch (InvocationTargetException e) {
            SmartLog.e(TAG, "invokeMethod failed, second exception :　" + e.getMessage());
        }
        return object;
    }

    private static Class getHwFoldScreenManagerExClass() {
        if (sIsHwFoldScreenManagerExClassNotFound) {
            return null;
        }
        Class clazz = null;
        try {
            clazz = Class.forName(FOLDING_SCREEN_MANAGER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            SmartLog.e(TAG, FOLDING_SCREEN_MANAGER_CLASS_NAME + " not found");
        }
        if (clazz == null) {
            sIsHwFoldScreenManagerExClassNotFound = true;
        }
        return clazz;
    }

    private static Method getMethod(@NonNull String methodName) {
        Class<?> clazz = getHwFoldScreenManagerExClass();
        if (clazz == null) {
            return null;
        }
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            SmartLog.e(TAG, methodName + " no such method");
        }
        return null;
    }
}
