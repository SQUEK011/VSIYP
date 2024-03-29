package com.example.vsiyp.ui.mediaeditor.aifun.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.utils.StringUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AiFunAdapter extends RCommandAdapter<CloudMaterialBean> {
    private volatile int currentSelectedPosition = -1;

    private final Map<String, CloudMaterialBean> mFirstDataMap = new LinkedHashMap<>();

    private OnAiFunAdapterItemClickListener onAiFunAdapterItemClickListener;

    public AiFunAdapter(Context context, List<CloudMaterialBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    public void setOnItemClickListener(OnAiFunAdapterItemClickListener listener) {
        onAiFunAdapterItemClickListener = listener;
    }

    @Override
    protected void convert(RViewHolder holder, CloudMaterialBean materialItem, int dataPosition, int position) {
        View holderView = holder.getView(R.id.item_select_view_ai_fun);
        ImageView mItemIv = holder.getView(R.id.item_image_view_ai_fun);
        TextView mTitleTv = holder.getView(R.id.item_name_ai_fun);

        Glide.with(mContext)
                .load(!StringUtil.isEmpty(materialItem.getPreviewUrl()) ? materialItem.getPreviewUrl()
                        : materialItem.getLocalDrawableId())
                .apply(new RequestOptions().transform(
                        new MultiTransformation<>(new CenterCrop(), new RoundedCorners(SizeUtils.dp2Px(mContext, 4)))))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        removeFirstScreenData(materialItem);
                        return false;
                    }
                })
                .into(mItemIv);

        mTitleTv.setText(materialItem.getName());
        holderView.setSelected(currentSelectedPosition == position);

        holder.itemView.setOnClickListener(new OnClickRepeatedListener((v) -> {
            if (onAiFunAdapterItemClickListener != null) {
                onAiFunAdapterItemClickListener.onAdapterItemClick(position, dataPosition);
            }
        }));
    }

    public int getSelectPosition() {
        return currentSelectedPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.currentSelectedPosition = selectPosition;
    }

    public void removeFirstScreenData(CloudMaterialBean materialsData) {
        if (materialsData == null || mFirstDataMap.size() == 0) {
            return;
        }
        mFirstDataMap.remove(materialsData.getId());
    }

    public interface OnAiFunAdapterItemClickListener {
        void onAdapterItemClick(int position, int dataPosition);
    }
}
