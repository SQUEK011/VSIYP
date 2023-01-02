package com.example.vsiyp.ui.mediaeditor.blockface;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.view.image.RoundImage;

import java.util.List;

public class FaceBlockingListAdapter extends RCommandAdapter<FaceBlockingInfo> {
    private Context mContext;

    public OnFaceSelectedListener blurSelectedListener;

    public FaceBlockingListAdapter(Context context, List<FaceBlockingInfo> list, int layoutId) {
        super(context, list, layoutId);
        mContext = context;
    }

    @Override
    protected void convert(RViewHolder holder, FaceBlockingInfo faceBlockingInfo, int dataPosition, int position) {
        ConstraintLayout rootLayout = holder.getView(R.id.rootLayout);
        CheckBox checkBox = holder.getView(R.id.checkBox);
        RoundImage sticker = holder.getView(R.id.iv_sticker);
        RoundImage imageView = holder.getView(R.id.item_image_view);
        RoundImage ivBlur = holder.getView(R.id.iv_blur);
        View viewSelectFace = holder.getView(R.id.view_select_face);
        if (faceBlockingInfo.isGetFocus()) {
            viewSelectFace.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.GONE);
        } else {
            viewSelectFace.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
        }
        checkBox.setChecked(faceBlockingInfo.isSelected());
        Glide.with(mContext).load(faceBlockingInfo.getBitmap()).into(imageView);
        if (faceBlockingInfo.isMosaic()) {
            ivBlur.setVisibility(View.VISIBLE);
            sticker.setVisibility(View.GONE);
        } else {
            ivBlur.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(faceBlockingInfo.getLocalSticker())) {
                sticker.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(faceBlockingInfo.getLocalSticker()).into(sticker);
            } else {
                sticker.setVisibility(View.GONE);
            }
        }
        rootLayout.setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceBlockingInfo.setSelected(!checkBox.isChecked());
                if (faceBlockingInfo.isSelected()) {
                    faceBlockingInfo.setGetFocus(false);
                }
                notifyItemChanged(position);
                if (blurSelectedListener != null) {
                    blurSelectedListener.onFaceSelected(faceBlockingInfo, position);
                }
            }
        }));
    }

    public void setFaceSelectedListener(OnFaceSelectedListener faceSelectedListener) {
        blurSelectedListener = faceSelectedListener;
    }

    public interface OnFaceSelectedListener {
        void onFaceSelected(FaceBlockingInfo faceBlockingInfo, int position);
    }

}

