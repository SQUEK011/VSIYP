package com.example.vsiyp.ui.mediaeditor.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.GlideBlurTransformer;
import com.example.vsiyp.ui.common.view.GlideRoundTransform;

import java.util.List;

public class CanvasBlurAdapter extends RCommandAdapter<Float> {
    private Context context;

    private Bitmap bitmap;

    private int mSelectPosition = -1;

    OnBlurSelectedListener blurSelectedListener;

    public CanvasBlurAdapter(Context context, List<Float> list, int layoutId, Bitmap bitmap) {
        super(context, list, layoutId);
        this.bitmap = bitmap;
        this.context = context;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return this.mSelectPosition;
    }

    @Override
    protected void convert(RViewHolder holder, Float aFloat, int dataPosition, int position) {

        View mSelectView = holder.getView(R.id.item_add_image_select_view);
        ImageFilterView mImageView = holder.getView(R.id.item_image);
        mSelectView.setSelected(mSelectPosition == position);
        Glide.with(context)
                .load(bitmap)
                .apply(new RequestOptions().error(R.drawable.blur_menu)
                        .transform(new MultiTransformation<>(new CenterCrop(),
                                new GlideBlurTransformer(context, (int) Math.min(aFloat * 10, 25)),
                                new GlideRoundTransform(SizeUtils.dp2Px(context, 4)))))
                .into(mImageView);

        mImageView.setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPosition != position) {
                    if (mSelectPosition != -1) {
                        int lastp = mSelectPosition;
                        mSelectPosition = position;
                        notifyItemChanged(lastp);
                        notifyItemChanged(mSelectPosition);
                    } else {
                        mSelectPosition = position;
                        notifyItemChanged(mSelectPosition);
                    }
                    if (blurSelectedListener != null) {
                        blurSelectedListener.onBlurSelected(aFloat);
                    }
                }
            }
        }));
    }

    public void setBlurSelectedListener(OnBlurSelectedListener blurSelectedListener) {
        this.blurSelectedListener = blurSelectedListener;
    }

    public interface OnBlurSelectedListener {
        void onBlurSelected(float blur);
    }
}

