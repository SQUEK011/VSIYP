package com.example.vsiyp.ui.common.tools;



import com.example.vsiyp.ui.common.utils.ReflectionUtils;

import java.lang.reflect.Method;

public class SystemPropertiesInvoke {
    public SystemPropertiesInvoke() {
    }

    public static boolean getBoolean(String key, boolean def) {
        Method getMethod = ReflectionUtils.getMethod("android.os.SystemProperties", "getBoolean",
                new Class[] {String.class, Boolean.TYPE});
        Object object = ReflectionUtils.invoke(getMethod, (Object) null, new Object[] {key, def});
        return object instanceof Boolean ? (Boolean) object : def;
    }

    public static int getInt(String key, int def) {
        Method getIntMethod = ReflectionUtils.getMethod("android.os.SystemProperties", "getInt",
                new Class[] {String.class, Integer.TYPE});
        Object object = ReflectionUtils.invoke(getIntMethod, (Object) null, new Object[] {key, def});
        return object instanceof Integer ? (Integer) object : def;
    }

    public static String getString(String key, String def) {
        Method getMethod =
                ReflectionUtils.getMethod("android.os.SystemProperties", "get", new Class[] {String.class, String.class});
        Object object = ReflectionUtils.invoke(getMethod, (Object) null, new Object[] {key, def});
        return object instanceof String ? (String) object : def;
    }
}
