package com.example.vsiyp.ui.mediaeditor.texts.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class CustomNestedScrollView extends NestedScrollView {
    private boolean isScrollable = true;

    private int mLastX;

    private int mLastY;

    public CustomNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastX;
                int dy = y - mLastY;
                if (Math.abs(dy) > Math.abs(dx)) {
                    isScrollable = true;
                } else {
                    isScrollable = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

    public void setScrollEnabled(boolean enabled) {
        isScrollable = enabled;
    }
}
