package com.example.vsiyp.ui.common.utils;

import android.text.TextUtils;

import java.util.List;

public class StringUtil {
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean match(List<String> list, String value) {
        if (list == null || TextUtils.isEmpty(value)) {
            return false;
        }
        for (String item : list) {
            if (item == null) {
                continue;
            }
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
