package com.example.vsiyp.ui.mediaeditor.blockface.cropimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.vsiyp.R;

public class CropImageBorderView extends View {
    private int mHorizontalPadding;

    private int mVerticalPadding;

    private int mWidth;

    private int mBorderColor = Color.WHITE;

    private int mBgColor;

    private int mBorderWidth = 2;

    private Paint mPaint;

    public CropImageBorderView(Context context) {
        this(context, null);
    }

    public CropImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth,
                getResources().getDisplayMetrics());
        mBgColor = context.getResources().getColor(R.color.clip_color_99000000);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth() - 2 * mHorizontalPadding;
        mVerticalPadding = (getHeight() - mWidth) / 2;
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(), getHeight(), mPaint);
        canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding, mVerticalPadding, mPaint);
        canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding, getWidth() - mHorizontalPadding,
                getHeight(), mPaint);
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth() - mHorizontalPadding,
                getHeight() - mVerticalPadding, mPaint);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }
}
