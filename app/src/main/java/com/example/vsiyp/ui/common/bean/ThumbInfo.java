package com.example.vsiyp.ui.common.bean;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class ThumbInfo {

    private Bitmap bitmap;

    private boolean isloading = false;

    private long startDownTime;

    private int mInt = 0;

    public ThumbInfo(Bitmap bmp) {
        this.bitmap = bmp;
    }

    @Override
    @NonNull
    public String toString() {
        return "ThumbInfo [bmp=" + ((null != bitmap) ? bitmap.getByteCount() : "null") + ", isloading=" + isloading
                + "]";
    }

    public void recycle() {
        if (null != bitmap) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bmp) {
        this.bitmap = bmp;
    }
}

