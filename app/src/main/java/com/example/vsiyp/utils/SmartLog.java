package com.example.vsiyp.utils;

import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.NonNull;

import com.huawei.videoeditor.BuildConfig;

import java.util.regex.Pattern;

public class SmartLog {
    private static final Pattern M_PATTERN = Pattern.compile("[0-9]*[a-z|A-Z]*[一-龥]*");

    public SmartLog() {
    }

    private static String getLogMsg(String msg, boolean isNeedProguard) {
        StringBuilder retStr = new StringBuilder(512);
        if (!TextUtils.isEmpty(msg)) {
            if (isNeedProguard) {
                retStr.append(formatLogWithStar(msg));
            } else {
                retStr.append(msg);
            }
        }

        return retStr.toString();
    }

    private static String getLogMsg(String noProguardMsg, String msg) {
        StringBuilder retStr = new StringBuilder(512);
        if (!TextUtils.isEmpty(noProguardMsg)) {
            retStr.append(noProguardMsg);
        }

        if (!TextUtils.isEmpty(msg)) {
            retStr.append(formatLogWithStar(msg));
        }

        return retStr.toString();
    }

    public static void d(String tag, String msg, boolean isNeedProguard) {
        if ("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            if (!TextUtils.isEmpty(msg)) {
                Log.d(tag, getLogMsg(msg, isNeedProguard));
            }
        }
    }

    public static void d(String tag, String noProguardMsg, String msg) {
        if ("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
                Log.d(tag, getLogMsg(noProguardMsg, msg));
            }
        }
    }

    public static void d(String tag, String noProguardMsg, String msg, Throwable e) {
        if ("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
                Log.d(tag, getLogMsg(noProguardMsg, msg), getNewThrowable(e));
            }
        }
    }

    public static void d(String tag, String msg) {
        if ("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            if (!TextUtils.isEmpty(msg)) {
                Log.d(tag, getLogMsg(msg, false));
            }
        }
    }

    public static void d(String tag, String msg, Throwable e, boolean isNeedProguard) {
        if ("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            if (!TextUtils.isEmpty(msg)) {
                Log.d(tag, getLogMsg(msg, isNeedProguard), getNewThrowable(e));
            }
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if ("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            if (!TextUtils.isEmpty(msg) || null != e) {
                Log.d(tag, getLogMsg(msg, false), getNewThrowable(e));
            }
        }
    }

    public static void i(String tag, String msg, boolean isNeedProguard) {
        if (!TextUtils.isEmpty(msg)) {
            Log.i(tag, getLogMsg(msg, isNeedProguard));
        }
    }

    public static void i(String tag, String noProguardMsg, String msg) {
        if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
            Log.i(tag, getLogMsg(noProguardMsg, msg));
        }
    }

    public static void i(String tag, String noProguardMsg, String msg, Throwable e) {
        if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
            Log.i(tag, getLogMsg(noProguardMsg, msg), getNewThrowable(e));
        }
    }

    public static void i(String tag, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.i(tag, getLogMsg(msg, false));
        }
    }

    public static void i(String tag, String msg, Throwable e, boolean isNeedProguard) {
        if (!TextUtils.isEmpty(msg) || null != e) {
            Log.i(tag, getLogMsg(msg, isNeedProguard), getNewThrowable(e));
        }
    }

    public static void i(String tag, String msg, Throwable e) {
        if (!TextUtils.isEmpty(msg) || null != e) {
            Log.i(tag, getLogMsg(msg, false), getNewThrowable(e));
        }
    }

    public static void w(String tag, String msg, boolean isNeedProguard) {
        if (!TextUtils.isEmpty(msg)) {
            Log.w(tag, getLogMsg(msg, isNeedProguard));
        }
    }

    public static void w(String tag, String noProguardMsg, String msg) {
        if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
            Log.w(tag, getLogMsg(noProguardMsg, msg));
        }
    }

    public static void w(String tag, String noProguardMsg, String msg, Throwable e) {
        if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
            Log.w(tag, getLogMsg(noProguardMsg, msg), getNewThrowable(e));
        }
    }

    public static void w(String tag, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.w(tag, getLogMsg(msg, false));
        }
    }

    public static void w(String tag, String msg, Throwable e, boolean isNeedProguard) {
        if (!TextUtils.isEmpty(msg) || null != e) {
            Log.w(tag, getLogMsg(msg, isNeedProguard), getNewThrowable(e));
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (!TextUtils.isEmpty(msg) || null != e) {
            Log.w(tag, getLogMsg(msg, false), getNewThrowable(e));
        }
    }

    public static void e(String tag, String msg, boolean isNeedProguard) {
        if (!TextUtils.isEmpty(msg)) {
            Log.e(tag, getLogMsg(msg, isNeedProguard));
        }
    }

    public static void e(String tag, String noProguardMsg, String msg) {
        if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
            Log.e(tag, getLogMsg(noProguardMsg, msg));
        }
    }

    public static void e(String tag, String noProguardMsg, String msg, Throwable e) {
        if (!TextUtils.isEmpty(noProguardMsg) || !TextUtils.isEmpty(msg)) {
            Log.e(tag, getLogMsg(noProguardMsg, msg), getNewThrowable(e));
        }
    }

    public static void e(String tag, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.e(tag, getLogMsg(msg, false));
        }
    }

    public static void e(String tag, String msg, Throwable e, boolean isNeedProguard) {
        if (!TextUtils.isEmpty(msg) || null != e) {
            Log.e(tag, getLogMsg(msg, isNeedProguard), getNewThrowable(e));
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (!TextUtils.isEmpty(msg) || null != e) {
            Log.e(tag, getLogMsg(msg, false), getNewThrowable(e));
        }
    }

    private static String formatLogWithStar(String logStr) {
        if (TextUtils.isEmpty(logStr)) {
            return logStr;
        } else {
            int len = logStr.length();
            if (1 == len) {
                return String.valueOf('*');
            } else {
                StringBuilder retStr = new StringBuilder(len);
                int i = 0;

                for (int k = 1; i < len; ++i) {
                    char charAt = logStr.charAt(i);
                    if (M_PATTERN.matcher(String.valueOf(charAt)).matches()) {
                        if (k % 2 == 0) {
                            charAt = '*';
                        }

                        ++k;
                    }

                    retStr.append(charAt);
                }

                return retStr.toString();
            }
        }
    }

    private static Throwable getNewThrowable(Throwable e) {
        if (e == null) {
            return null;
        } else {
            ThrowableWrapper retWrapper = new ThrowableWrapper(e);
            retWrapper.setStackTrace(e.getStackTrace());
            retWrapper.setMessage(modifyExceptionMessage(e.getMessage()));
            ThrowableWrapper preWrapper = retWrapper;

            for (Throwable currThrowable = e.getCause(); currThrowable != null;
                 currThrowable = currThrowable.getCause()) {
                ThrowableWrapper currWrapper = new ThrowableWrapper(currThrowable);
                currWrapper.setStackTrace(currThrowable.getStackTrace());
                currWrapper.setMessage(modifyExceptionMessage(currThrowable.getMessage()));
                preWrapper.setCause(currWrapper);
                preWrapper = currWrapper;
            }

            return retWrapper;
        }
    }

    private static String modifyExceptionMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return message;
        } else {
            char[] messageChars = message.toCharArray();

            for (int i = 0; i < messageChars.length; ++i) {
                if (i % 2 == 0) {
                    messageChars[i] = '*';
                }
            }

            return new String(messageChars);
        }
    }

    private static class ThrowableWrapper extends Throwable {
        private static final long serialVersionUID = 7129050843360571879L;

        private String message;

        private Throwable thisCause;

        private Throwable ownerThrowable;

        public ThrowableWrapper(Throwable t) {
            this.ownerThrowable = t;
        }

        public Throwable getCause() {
            return this.thisCause == this ? null : this.thisCause;
        }

        public void setCause(Throwable cause) {
            this.thisCause = cause;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @NonNull
        public String toString() {
            if (this.ownerThrowable == null) {
                return "";
            } else {
                String throwableClzName = this.ownerThrowable.getClass().getName();
                if (this.message != null) {
                    String prefix = throwableClzName + ": ";
                    return this.message.startsWith(prefix) ? this.message : prefix + this.message;
                } else {
                    return throwableClzName;
                }
            }
        }
    }
}
