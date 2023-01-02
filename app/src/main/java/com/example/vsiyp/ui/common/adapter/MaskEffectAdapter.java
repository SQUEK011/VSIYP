package com.example.vsiyp.ui.common.adapter;

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
import com.huawei.hms.videoeditor.sdk.util.SmartLog;
import com.huawei.hms.videoeditor.sdk.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MaskEffectAdapter extends RCommandAdapter<CloudMaterialBean> {
    private static final String TAG = "MaskEffectAdapter";

    private volatile int mSelectPosition = -1;

    private final Map<String, CloudMaterialBean> mDownloadingMap = new LinkedHashMap<>();

    private final Map<String, CloudMaterialBean> mFirstScreenMap = new LinkedHashMap<>();

    private MaskEffectAdapter.OnItemClickListener mOnItemClickListener;

    public void addFirstScreenMaterial(CloudMaterialBean item) {
        if (!mFirstScreenMap.containsKey(item.getId())) {
            mFirstScreenMap.put(item.getId(), item);
        }
    }

    public void removeFirstScreenMaterial(CloudMaterialBean cloudMaterialBean) {
        if (cloudMaterialBean == null || mFirstScreenMap.size() == 0) {
            SmartLog.e(TAG, "input materials is null");
            return;
        }
        mFirstScreenMap.remove(cloudMaterialBean.getId());
        if (mFirstScreenMap.size() == 0) {
            SmartLog.w(TAG, "HianalyticsEvent10007 postEvent");
        }
    }

    public MaskEffectAdapter(Context context, List<CloudMaterialBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    public void setOnItemClickListener(MaskEffectAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    protected void convert(RViewHolder holder, CloudMaterialBean item, int dataPosition, int position) {
        View mSelectView = holder.getView(R.id.item_select_view_mask_effect);
        ImageView mItemIv = holder.getView(R.id.item_image_view_mask_effect);
        TextView mNameTv = holder.getView(R.id.item_name_mask_effect);
        ImageView mDownloadIv = holder.getView(R.id.item_download_view_mask_effect);
        View mDownloadPb = holder.getView(R.id.item_progress_mask_effect);

        Glide.with(mContext)
                .load(item.getPreviewUrl())
                .placeholder(R.drawable.icon_cancel_wu)
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
                        removeFirstScreenMaterial(item);
                        return false;
                    }
                })
                .into(mItemIv);

        mNameTv.setText(item.getName());
        mSelectView.setSelected(mSelectPosition == position);
        if (!StringUtil.isEmpty(item.getLocalPath()) || position == 0) {
            mDownloadIv.setVisibility(View.GONE);
            mDownloadPb.setVisibility(View.GONE);
        } else {
            mDownloadIv.setVisibility(mSelectPosition == position ? View.INVISIBLE : View.VISIBLE);
            mDownloadPb.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);
        }

        if (mDownloadingMap.containsKey(item.getId())) {
            mDownloadIv.setVisibility(View.GONE);
            mDownloadPb.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new OnClickRepeatedListener((v) -> {
            mDownloadIv.setVisibility(View.INVISIBLE);
            if (mOnItemClickListener != null) {
                if (!StringUtil.isEmpty(item.getLocalPath())) {
                    mOnItemClickListener.onItemClick(position, dataPosition);
                } else {
                    if (!mDownloadingMap.containsKey(item.getId())) {
                        mOnItemClickListener.onDownloadClick(position, dataPosition);
                        mDownloadPb.setVisibility(View.VISIBLE);
                    }
                }
            }
        }));

        mDownloadIv.setOnClickListener(new OnClickRepeatedListener(v -> {
            mDownloadIv.setVisibility(View.INVISIBLE);
            mDownloadPb.setVisibility(View.VISIBLE);
            if (mOnItemClickListener != null && !mDownloadingMap.containsKey(item.getId())) {
                mOnItemClickListener.onDownloadClick(position, dataPosition);
            }
        }));
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public void addDownloadMaterial(CloudMaterialBean item) {
        mDownloadingMap.put(item.getId(), item);
    }

    public void removeDownloadMaterial(String contentId) {
        mDownloadingMap.remove(contentId);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int dataPosition);
        void onDownloadClick(int position, int dataPosition);
    }
}

