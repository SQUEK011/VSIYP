package com.example.vsiyp.ui.mediaeditor.canvas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
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

public class CanvasStyleStyleAdapter extends RCommandAdapter<CloudMaterialBean> {
    private static final String TAG = "CanvasStyleStyleAdapter";

    private volatile int mSelectPosition = -1;

    private final Map<String, CloudMaterialBean> mDownloadingMap = new LinkedHashMap<>();

    private final int mImageViewWidth;

    private final int mImageViewHeight;

    private OnItemClickListener mOnItemClickListener;

    public CanvasStyleStyleAdapter(Context context, List<CloudMaterialBean> list, int layoutId) {
        super(context, list, layoutId);
        mImageViewWidth = (SizeUtils.screenWidth(mContext) - (SizeUtils.dp2Px(mContext, 72))) / 6;
        mImageViewHeight = mImageViewWidth;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    protected void convert(RViewHolder holder, CloudMaterialBean item, int dataPosition, int position) {
        ConstraintLayout mContentView = holder.getView(R.id.item_content);
        View mSelectView = holder.getView(R.id.item_select_view);
        ImageFilterView imageView = holder.getView(R.id.item_image_view);
        ImageView mDownloadIv = holder.getView(R.id.item_download_view);
        ProgressBar mHwProgressBar = holder.getView(R.id.item_progress);

        holder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewHeight));
        mContentView.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewHeight));
        mSelectView.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewHeight));
        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(mImageViewWidth, mImageViewHeight));
        mSelectView.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);

        Glide.with(mContext)
                .load(item.getPreviewUrl())
                .placeholder(R.drawable.sticker_normal_bg)
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop())))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

        if (!StringUtil.isEmpty(item.getLocalPath())) {
            mDownloadIv.setVisibility(View.GONE);
            mHwProgressBar.setVisibility(View.GONE);
        } else {
            mDownloadIv.setVisibility(mSelectPosition == position ? View.INVISIBLE : View.VISIBLE);
            mHwProgressBar.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);
        }

        if (mDownloadingMap.containsKey(item.getId())) {
            mDownloadIv.setVisibility(View.GONE);
            mHwProgressBar.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new OnClickRepeatedListener((v) -> {
            mDownloadIv.setVisibility(View.INVISIBLE);
            if (mOnItemClickListener != null) {
                if (!StringUtil.isEmpty(item.getLocalPath())) {
                    mOnItemClickListener.onItemClick(position, dataPosition);
                } else {
                    if (!mDownloadingMap.containsKey(item.getId())) {

                        mOnItemClickListener.onDownloadClick(position, dataPosition);
                        mHwProgressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }));

        mDownloadIv.setOnClickListener(new OnClickRepeatedListener(v -> {
            mDownloadIv.setVisibility(View.INVISIBLE);
            mHwProgressBar.setVisibility(View.VISIBLE);
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

