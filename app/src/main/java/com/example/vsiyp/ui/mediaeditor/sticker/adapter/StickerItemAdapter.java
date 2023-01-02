package com.example.vsiyp.ui.mediaeditor.sticker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
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
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StickerItemAdapter extends RCommandAdapter<CloudMaterialBean> {
    private static final String TAG = "StickerItemAdapter";

    private final Map<String, CloudMaterialBean> theDownloadingMap = new LinkedHashMap<>();

    private final Map<String, CloudMaterialBean> theFirstScreenMap = new LinkedHashMap<>();

    private final int mImageViewWidth;

    private final int mImageViewHeight;

    private volatile int mSelectPosition = -1;

    private OnItemClickListener mOnItemClickListener;

    public StickerItemAdapter(Context context, List<CloudMaterialBean> list, int layoutId) {
        super(context, list, layoutId);
        mImageViewWidth = (SizeUtils.screenWidth(mContext) - (SizeUtils.dp2Px(mContext, 62))) / 4;
        mImageViewHeight = mImageViewWidth;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public void addDownloadMaterial(CloudMaterialBean item) {
        if (!theDownloadingMap.containsKey(item.getId())) {
            theDownloadingMap.put(item.getId(), item);
        }
    }

    public void removeFirstScreenMaterial(CloudMaterialBean materialsCutContent) {
        if (materialsCutContent == null || theFirstScreenMap.size() == 0) {
            SmartLog.e(TAG, "input materials is null");
            return;
        }
        theFirstScreenMap.remove(materialsCutContent.getId());
        if (theFirstScreenMap.size() == 0) {
            SmartLog.w(TAG, "HianalyticsEvent10007 postEvent");
        }
    }
    public void addFirstScreenMaterial(CloudMaterialBean item) {
        if (!theFirstScreenMap.containsKey(item.getId())) {
            theFirstScreenMap.put(item.getId(), item);
        }
    }
    @Override
    protected void convert(RViewHolder viewHolder, CloudMaterialBean bean, int dataPosition, int position) {
        ConstraintLayout constraintLayout = viewHolder.getView(R.id.item_content);
        constraintLayout.setTag(position);
        ImageView downloadiv = viewHolder.getView(R.id.item_download_view);
        View downloadpb = viewHolder.getView(R.id.item_progress);
        TextView name = viewHolder.getView(R.id.item_name);
        View mSelectView = viewHolder.getView(R.id.item_select_view);
        ImageView itemImageView = viewHolder.getView(R.id.item_image_view);

        mSelectView.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewWidth));
        itemImageView.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewWidth));
        viewHolder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewHeight));
        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewHeight));

        Glide.with(mContext)
                .load(bean.getPreviewUrl())
                .apply(new RequestOptions()
                        .transform(new MultiTransformation<>(new RoundedCorners(SizeUtils.dp2Px(mContext, 4)))))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        removeFirstScreenMaterial(bean);
                        return false;
                    }
                })
                .into(itemImageView);
        mSelectView.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);
        itemImageView.setVisibility(View.VISIBLE);
        name.setText(bean.getName());
        name.setVisibility(View.GONE);

        if (!StringUtil.isEmpty(bean.getLocalPath()) || bean.getId().equals("-1")) {
            downloadiv.setVisibility(View.GONE);
            downloadpb.setVisibility(View.GONE);
        } else {
            downloadiv.setVisibility(mSelectPosition == position ? View.INVISIBLE : View.VISIBLE);
            downloadpb.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);
        }
        if (theDownloadingMap.containsKey(bean.getId())) {
            downloadiv.setVisibility(View.GONE);
            downloadpb.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new OnClickRepeatedListener((v) -> {
            if (mOnItemClickListener == null) {
                return;
            }
            if (!StringUtil.isEmpty(bean.getLocalPath()) || bean.getId().equals("-1")) {
                mOnItemClickListener.onItemClick(position);
            } else {
                if (!theDownloadingMap.containsKey(bean.getId())) {
                    mOnItemClickListener.onDownloadClick(position);
                }
            }
        }));
    }
    public void removeDownloadMaterial(String contentId) {
        theDownloadingMap.remove(contentId);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDownloadClick(int position);
    }
}

