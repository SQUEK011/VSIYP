package com.example.vsiyp.ui.common.view.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.utils.SafeSecureRandom;
import com.example.vsiyp.ui.common.utils.SizeUtils;

import java.util.Timer;
import java.util.TimerTask;

public class AudioColumnView extends View {
    private int random;

    private boolean isStart = true;

    private int mRect_t1;

    private int mRect_t2;

    private int mRect_t3;

    private int mRect_t4;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1234) {
                invalidate();
            }
        }
    };

    private Paint mPaint;

    private int mHeight;

    private RectF r1;

    private RectF r2;

    private RectF r3;

    private RectF r4;

    public AudioColumnView(Context context) {
        this(context, null);
    }

    public AudioColumnView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AudioColumnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AudioColumnView);
        int height = ta.getInteger(R.styleable.AudioColumnView_column_height, 0);
        random = height;
        mHeight = SizeUtils.dp2Px(getContext(), height);
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        r1 = new RectF();
        r2 = new RectF();
        r3 = new RectF();
        r4 = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void start() {
        isStart = true;
        invalidate();
    }

    public void stop() {
        isStart = false;
        invalidate();
    }

    public boolean isStart() {
        return isStart;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isStart) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mRect_t1 = SafeSecureRandom.nextInt(random);
                    mRect_t2 = SafeSecureRandom.nextInt(random);
                    mRect_t3 = SafeSecureRandom.nextInt(random);
                    mRect_t4 = SafeSecureRandom.nextInt(random);
                    mHandler.sendEmptyMessage(0x1234);
                }
            }, 150);
        }
        r1.set((float) (0), mRect_t1, (float) (SizeUtils.dp2Px(getContext(), 2)), (float) (mHeight * 0.9));
        r2.set((float) (SizeUtils.dp2Px(getContext(), 5)), mRect_t2, (float) (SizeUtils.dp2Px(getContext(), 7)),
                (float) (mHeight * 0.9));
        r3.set((float) (SizeUtils.dp2Px(getContext(), 10)), mRect_t3, (float) (SizeUtils.dp2Px(getContext(), 12)),
                (float) (mHeight * 0.9));
        r4.set((float) (SizeUtils.dp2Px(getContext(), 15)), mRect_t4, (float) (SizeUtils.dp2Px(getContext(), 17)),
                (float) (mHeight * 0.9));
        canvas.drawRect(r1, mPaint);
        canvas.drawRect(r2, mPaint);
        canvas.drawRect(r3, mPaint);
        canvas.drawRect(r4, mPaint);
    }
}
