package com.example.vsiyp.ui.common.listener;

import android.view.View;

public class OnClickRepeatedListener implements View.OnClickListener {
    private final View.OnClickListener onClickListener;

    private long lastClickTime = 0;

    private long spaceTimes = 500;

    public OnClickRepeatedListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickRepeatedListener(View.OnClickListener onClickListener, long spaceTimes) {
        this.onClickListener = onClickListener;
        this.spaceTimes = spaceTimes;
    }

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastClickTime >= spaceTimes) {
            onClickListener.onClick(v);
            lastClickTime = System.currentTimeMillis();
        }
    }
}
