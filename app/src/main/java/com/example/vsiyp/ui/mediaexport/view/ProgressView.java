
package com.example.vsiyp.ui.mediaexport.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.vsiyp.R;
import com.example.vsiyp.VideoEditorApplication;
import com.example.vsiyp.ui.common.utils.BigDecimalUtil;
import com.example.vsiyp.ui.common.utils.SizeUtils;


public class ProgressView extends View {
    private Paint firstPaint;

    private Paint secondPaint;

    private int firstColor;

    private int secondColor;

    private int maxProgress = 100;

    private int currentProgress = 0;

    private int w;

    private int h;

    private float calibration;

    private Paint linePaint;

    private int lineColor;

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        firstColor = typedArray.getColor(R.styleable.ProgressView_firstColor,
            getResources().getColor(R.color.clip_color_E6000000));
        secondColor = typedArray.getColor(R.styleable.ProgressView_secondColor, getResources().getColor(R.color.white));
        lineColor = typedArray.getColor(R.styleable.ProgressView_lineColor, getResources().getColor(R.color.white));
        typedArray.recycle();
        init();
    }

    private void init() {
        firstPaint = new Paint();
        firstPaint.setColor(firstColor);
        firstPaint.setStrokeWidth(5);
        firstPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        firstPaint.setAntiAlias(true);

        secondPaint = new Paint();
        secondPaint.setColor(secondColor);
        secondPaint.setStrokeWidth(5);
        secondPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        secondPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setAntiAlias(true);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(0, SizeUtils.dp2Px(VideoEditorApplication.getInstance().getContext(), 8), getWidth(),
            getHeight() - SizeUtils.dp2Px(VideoEditorApplication.getInstance().getContext(), 8));
        canvas.drawRect(rectF, firstPaint);

        RectF rectF2 = new RectF(calibration * currentProgress,
            SizeUtils.dp2Px(VideoEditorApplication.getInstance().getContext(), 8), getWidth(),
            getHeight() - SizeUtils.dp2Px(VideoEditorApplication.getInstance().getContext(), 8));
        canvas.drawRect(rectF2, secondPaint);

        canvas.drawLine(calibration * currentProgress, 0, calibration * currentProgress, getHeight(), linePaint);
    }

    public int getProgress() {
        return currentProgress;
    }

    public void setProgress(int maxProgress, int currentProgress) {
        this.maxProgress = maxProgress;
        this.currentProgress = currentProgress;
        calibration = (float) BigDecimalUtil.div(getWidth(), maxProgress, 5);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    public void setHeightByVideoHeight() {
        requestLayout();
        postInvalidate();
    }
}
