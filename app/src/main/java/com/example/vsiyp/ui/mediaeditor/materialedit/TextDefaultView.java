package com.example.vsiyp.ui.mediaeditor.materialedit;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class TextDefaultView extends TransformView {
    public TextDefaultView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TextDefaultView(Context context) {
        this(context, null);
    }
    public TextDefaultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        if (isDrawDelete) {
            c.drawBitmap(mDeleteBitmap, null, mDeleteRect, mPaint);
        }
        if (isDrawScale) {
            c.drawBitmap(mScaleBitmap, null, mScaleRect, mPaint);
        }
        if (isDrawEdit) {
            c.drawBitmap(mEditBitmap, null, mEditRect, mPaint);
        }
        if (isDrawCopy) {
            c.drawBitmap(mCopyBitmap, null, mCopyRect, mPaint);
        }
    }
}

