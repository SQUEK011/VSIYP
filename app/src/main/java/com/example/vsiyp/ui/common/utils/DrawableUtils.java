package com.example.vsiyp.ui.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.concurrent.ExecutionException;

public class DrawableUtils {

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable compressDrawable(Context context, String filePath) {
        Bitmap bpResult = null;
        try {
            Drawable drawable = Glide.with(context).load(filePath).submit().get();
            Bitmap tempBp = DrawableUtils.drawableToBitmap(drawable);
            bpResult = BitmapUtils.compressBitmap(tempBp, tempBp.getWidth(), tempBp.getHeight());
        } catch (ExecutionException | InterruptedException e) {
            SmartLog.e("DrawableUtils", e.toString());
        }
        return new BitmapDrawable(bpResult);
    }
}

