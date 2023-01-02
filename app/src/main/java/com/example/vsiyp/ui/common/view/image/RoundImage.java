package com.example.vsiyp.ui.common.view.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.utils.SizeUtils;

public class RoundImage extends AppCompatImageView {
    private Matrix mMatrix;

    private int mBorderRadius = 0;

    private Paint paint;

    private BitmapShader mBitmapShader;

    public RoundImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImage);
        mBorderRadius = (int) ta.getDimension(R.styleable.RoundImage_radius, SizeUtils.dp2Px(context, 0));
        ta.recycle();
        mMatrix = new Matrix();
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    public RoundImage(Context context) {
        this(context, null);
    }

    public RoundImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        Bitmap bitmap = drawableToBitamp(getDrawable());
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        paint.setShader(mBitmapShader);
        canvas.drawRoundRect(new RectF(getPaddingStart(), getPaddingTop(), getWidth() - getPaddingEnd(),
                getHeight() - getPaddingBottom()), mBorderRadius, mBorderRadius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Bitmap drawableToBitamp(Drawable theDrawable) {
        if (theDrawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) theDrawable;
            return bd.getBitmap();
        }
        int w = theDrawable.getIntrinsicWidth() <= 0 ? getWidth() : theDrawable.getIntrinsicWidth();
        int height = theDrawable.getIntrinsicHeight() <= 0 ? getHeight() : theDrawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        theDrawable.setBounds(getPaddingStart(), getPaddingTop(), w + getPaddingStart(), height + getPaddingTop());
        theDrawable.draw(canvas);
        return bitmap;
    }

    public void setBorderRadius(int borderRadius) {
        this.mBorderRadius = borderRadius;
        invalidate();
    }

}
