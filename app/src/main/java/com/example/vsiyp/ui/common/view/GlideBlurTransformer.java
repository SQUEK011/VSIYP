package com.example.vsiyp.ui.common.view;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.vsiyp.ui.common.utils.GlideBitmapUtil;

import java.security.MessageDigest;

public class GlideBlurTransformer extends BitmapTransformation {

    private Context context;

    private int mLever = 25;

    public GlideBlurTransformer(Context context, int lever) {
        this.context = context;
        this.mLever = lever;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return GlideBitmapUtil.instance().blurBitmap(context, toTransform, mLever, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}
