package com.example.vsiyp.ui.mediaeditor.materialedit;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class StickerView extends TransformView {
    public StickerView(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override
    protected void onDraw(Canvas theCanvas) {
        super.onDraw(theCanvas);

        if (isDrawDelete) {
            theCanvas.drawBitmap(mDeleteBitmap, null, mDeleteRect, mPaint);
        }
        if (isDrawScale) {
            theCanvas.drawBitmap(mScaleBitmap, null, mScaleRect, mPaint);
        }
        if (isDrawEdit) {
            theCanvas.drawBitmap(mEditBitmap, null, mEditRect, mPaint);
        }
        if (isDrawCopy) {
            theCanvas.drawBitmap(mCopyBitmap, null, mCopyRect, mPaint);
        }
    }
}

