package com.example.vsiyp.ui.common.utils;

import com.example.vsiyp.utils.SmartLog;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public final class GsonUtils {
    private static final String TAG = "GsonUtils";

    private static ExclusionStrategy serializeExclusionStrategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            Expose expose = fieldAttributes.getAnnotation(Expose.class);
            if (expose != null && !expose.serialize()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    private static final Gson GSON =
            new GsonBuilder().disableHtmlEscaping().addSerializationExclusionStrategy(serializeExclusionStrategy).create();

    private GsonUtils() {
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return GSON.fromJson(json, clazz);
        } catch (Exception e) {
            SmartLog.e(TAG, "GsonUtils.fromJson(String, clazz) exception", e);
            SmartLog.d(TAG, "json=" + json + ", exception=" + e);
            return null;
        }
    }
}
