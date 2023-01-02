package com.example.vsiyp.ui.mediaeditor.filter.aifilter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;

import java.util.List;

public class ExclusiveFilterItemAdapter extends RCommandAdapter<CloudMaterialBean> {
    private static final String TAG = "FilterItemAdapter";

    private volatile int mSelectPosition = -1;

    private OnItemClickListener mOnItemClickListener;

    public ExclusiveFilterItemAdapter(Context context, List<CloudMaterialBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    protected void convert(RViewHolder holder, CloudMaterialBean item, int dataPosition, int position) {
        View mSelectView = holder.getView(R.id.item_select_view);
        ImageFilterView mItemIv = holder.getView(R.id.item_image_view);
        TextView mNameTv = holder.getView(R.id.item_name);
        View mPopupPosMarkView = holder.getView(R.id.item_popup_pos_mark);
        Glide.with(mContext)
                .load(item.getLocalPath())
                .apply(new RequestOptions().transform(
                        new MultiTransformation<>(new CenterCrop(), new RoundedCorners(SizeUtils.dp2Px(mContext, 4)))))
                .into(mItemIv);

        mNameTv.setText(item.getName());
        mSelectView.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);

        holder.itemView.setOnClickListener(new OnClickRepeatedListener((v) -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, dataPosition);
            }
        }));
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemLongClick(position, dataPosition, mPopupPosMarkView);
            }
            return false;
        });
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int dataPosition);

        void onItemLongClick(int position, int dataPosition, View markPosView);
    }

}

