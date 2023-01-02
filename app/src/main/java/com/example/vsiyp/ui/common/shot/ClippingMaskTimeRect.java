package com.example.vsiyp.ui.common.shot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.vsiyp.R;

public class ClippingMaskTimeRect extends View {
    private Paint mPaint;

    private Context mContext;

    private Path mOutPath;

    private Path mInnerPath;

    private Path mResultPath;

    private int mRectWith = dipToPx(192);

    private int mRectHeight = dipToPx(48);

    private Paint mInnerPaint;

    public ClippingMaskTimeRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dipToPx(1.5f));
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.translucent_black_45));

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(dipToPx(2));
        mInnerPaint.setColor(ContextCompat.getColor(mContext, R.color.white));
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mOutPath = new Path();
        mInnerPath = new Path();
        mOutPath = new Path();
        mResultPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mResultPath.reset();
        mOutPath.reset();
        mOutPath.addRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), Path.Direction.CCW);
        int left = mRectWith >= getMeasuredWidth() ? 0 : (getMeasuredWidth() - mRectWith) / 2;

        int top = mRectHeight >= getMeasuredHeight() ? 0 : (getMeasuredHeight() - mRectHeight) / 2;
        mInnerPath.reset();
        mInnerPath.addRect(left, top, left + mRectWith, top + mRectHeight, Path.Direction.CCW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mResultPath.op(mOutPath, mInnerPath, Path.Op.DIFFERENCE);
        }
        canvas.drawPath(mResultPath, mPaint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, left + mRectWith, top + mRectHeight, dipToPx(5), dipToPx(5), mInnerPaint);
        }
    }

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
