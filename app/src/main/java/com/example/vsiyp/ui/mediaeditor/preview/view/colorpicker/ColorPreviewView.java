package com.example.vsiyp.ui.mediaeditor.preview.view.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.vsiyp.ui.common.utils.BigDecimalUtils;

public class ColorPreviewView extends View {

    private final PorterDuffXfermode porterDuffXfermode;

    private Paint paint;

    private Paint circlePaint;

    private RectF rectF;

    private int width;

    private int height;

    private Bitmap dstBitmap;

    public ColorPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        circlePaint.setColor(Color.argb(255, 255, 0, 0));
        circlePaint.setStyle(Paint.Style.FILL);
    }

    private void drawBottom() {
        dstBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(dstBitmap);
        int layerAlpha = canvas.saveLayerAlpha(rectF, 255, Canvas.ALL_SAVE_FLAG);
        Bitmap srcBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvasSrc = new Canvas(srcBitmap);
        canvasSrc.drawCircle(BigDecimalUtils.div(width, 2F), BigDecimalUtils.div(height, 2F),
                BigDecimalUtils.div(width, 2F), circlePaint);
        circlePaint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(srcBitmap, 0, 0, circlePaint);
        canvas.restoreToCount(layerAlpha);
        circlePaint.setXfermode(null);
    }

    public void setColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF(0, 0, width, height);
        drawBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(dstBitmap, 0, 0, paint);
        canvas.drawCircle(BigDecimalUtils.div(width, 2F), BigDecimalUtils.div(height, 2F),
                BigDecimalUtils.div(width, 2F), circlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}

