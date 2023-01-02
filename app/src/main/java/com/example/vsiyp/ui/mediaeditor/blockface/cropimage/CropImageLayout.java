package com.example.vsiyp.ui.mediaeditor.blockface.cropimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.example.vsiyp.utils.SmartLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropImageLayout extends RelativeLayout {
    private CropZoomImageView zoomImageView;

    private CropImageBorderView clipImageView;

    private int horizontalPadding = 0;

    public CropImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        zoomImageView = new CropZoomImageView(context);
        clipImageView = new CropImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(zoomImageView, lp);
        this.addView(clipImageView, lp);
        horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalPadding,
                getResources().getDisplayMetrics());
        zoomImageView.setHorizontalPadding(horizontalPadding);
        clipImageView.setHorizontalPadding(horizontalPadding);
    }

    public void setImageDrawable(Drawable drawable) {
        zoomImageView.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        zoomImageView.setImageBitmap(bitmap);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.horizontalPadding = mHorizontalPadding;
    }

    public String clip() {
        String sdPath = getContext().getFilesDir().getAbsolutePath();
        File appDir = new File(sdPath);
        if (!appDir.exists()) {
            if (!appDir.mkdir()) {
                SmartLog.e("CropImageLayout", "fail to make dir app");
            }
        }
        String fileName = "face_sticker_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        Bitmap bitmap = zoomImageView.clip();
        boolean isSuccess;
        String filePath = "";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();

            if (isSuccess) {
                filePath = file.getCanonicalPath();
            }
        } catch (IOException e) {
            SmartLog.w("CropImageLayout", "clip:" + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }
}

