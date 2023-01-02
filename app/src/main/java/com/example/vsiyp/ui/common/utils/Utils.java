package com.example.vsiyp.ui.common.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class Utils {
    public static SpannableStringBuilder setNumColor(String string, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(string);
        for (int i = 0; i < string.length(); i++) {
            char a = string.charAt(i);
            if (a >= '0' && a <= '9') {
                style.setSpan(new ForegroundColorSpan(color), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return style;
    }

    public static void setTextAttrs(SpannableString spannableString, String number, int resId) {
        if (spannableString == null || StringUtil.isEmpty(number)) {
            return;
        }
        int color = ResUtils.getColor(resId);
        int start = spannableString.toString().indexOf(number);
        int end = start + number.length();
        setStringSpan(spannableString, new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setStringSpan(SpannableString spannableString, Object what, int start, int end, int flag) {
        if (null == spannableString || null == what) {
            return;
        }

        if (start < 0 || end < 0 || end < start || start > spannableString.length() || end > spannableString.length()) {
            return;
        }

        spannableString.setSpan(what, start, end, flag);
    }
}

