package com.example.vsiyp.ui.common.listener;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public abstract class SafeClickListener implements View.OnClickListener {
    private static final String TAG = "SafeClickListener";

    private static final long INTERVAL = 1000;

    private long mLastClickTime;

    private long mInterval = INTERVAL;
    public SafeClickListener() {
    }

    public SafeClickListener(long interval) {
        mInterval = interval;
    }

    public abstract void onSafeClick(View view);

    @Override
    public void onClick(View view) {
        if (isInInterval()) {
            Log.d(TAG, "onClick too quickly!");
            return;
        }

        onSafeClick(view);
    }

    private boolean isInInterval() {
        long nowTime = SystemClock.elapsedRealtime();
        if (nowTime - mLastClickTime <= mInterval) {
            return true;
        }
        mLastClickTime = nowTime;
        return false;
    }
}
