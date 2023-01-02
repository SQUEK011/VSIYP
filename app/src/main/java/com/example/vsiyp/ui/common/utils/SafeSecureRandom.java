package com.example.vsiyp.ui.common.utils;

import android.os.Build;

import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SafeSecureRandom {
    private static final String TAG = "SafeRandom";

    public static final String SHA1PRNG = "SHA1PRNG";

    private static SecureRandom secureRandom = null;

    private SafeSecureRandom() {
    }

    private static SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
        if (secureRandom != null) {
            return secureRandom;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            secureRandom = SecureRandom.getInstanceStrong();
        } else {
            secureRandom = SecureRandom.getInstance(SHA1PRNG);
        }

        return secureRandom;
    }

    public static int nextInt(int bound) {
        try {
            return getSecureRandom().nextInt(bound);
        } catch (IllegalArgumentException e) {
            SmartLog.e(TAG, "nextInt IllegalArgumentException bound=" + bound);
            return 0;
        } catch (NoSuchAlgorithmException e) {
            SmartLog.e(TAG, "nextInt NoSuchAlgorithmException");
            return 0;
        }
    }
}
