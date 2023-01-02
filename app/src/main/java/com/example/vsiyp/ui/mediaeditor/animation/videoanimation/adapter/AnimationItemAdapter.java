package com.example.vsiyp.ui.mediaeditor.animation.videoanimation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.utils.StringUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnimationItemAdapter extends RecyclerView.Adapter<AnimationItemAdapter.ViewHolder> {
    private Context mContext;

    private List<CloudMaterialBean> mCloudMaterialBeanList;

    private final Map<String, CloudMaterialBean> mCloudMaterialBeanMap = new LinkedHashMap<>();

    private int mSelectPosition = 0;

    private OnItemClickListener mItemClickListener;

    public AnimationItemAdapter(Context context, List<CloudMaterialBean> list) {
        mContext = context;
        mCloudMaterialBeanList = list;
    }

    public void setData(List<CloudMaterialBean> list) {
        this.mCloudMaterialBeanList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_animation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CloudMaterialBean materialBean = mCloudMaterialBeanList.get(position);

        Glide.with(mContext)
                .load(!StringUtil.isEmpty(materialBean.getPreviewUrl()) ? materialBean.getPreviewUrl()
                        : materialBean.getLocalDrawableId())
                .apply(new RequestOptions().transform(
                        new MultiTransformation(new CenterInside(), new RoundedCorners(SizeUtils.dp2Px(mContext, 4)))))
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
                .into(viewHolder.mItemIv);
        viewHolder.mSelectView.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);
        viewHolder.mNameTv.setText(materialBean.getName());

        if (!StringUtil.isEmpty(materialBean.getLocalPath()) || position == 0) {
            viewHolder.mDownloadIv.setVisibility(View.GONE);
            viewHolder.mDownloadPb.setVisibility(View.GONE);
        } else {
            viewHolder.mDownloadIv.setVisibility(mSelectPosition == position ? View.INVISIBLE : View.VISIBLE);
            viewHolder.mDownloadPb.setVisibility(mSelectPosition == position ? View.VISIBLE : View.INVISIBLE);
        }

        if (mCloudMaterialBeanMap.containsKey(materialBean.getId())) {
            viewHolder.mDownloadIv.setVisibility(View.GONE);
            viewHolder.mDownloadPb.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new OnClickRepeatedListener((v) -> {
            if (mItemClickListener == null) {
                return;
            }
            if (position == 0) {
                mItemClickListener.onItemClick(position);
                return;
            }
            if (!StringUtil.isEmpty(materialBean.getLocalPath())) {
                mItemClickListener.onItemClick(position);
            } else {
                if (!mCloudMaterialBeanMap.containsKey(materialBean.getId())) {
                    mItemClickListener.onDownloadClick(position);
                }
            }
        }));
    }

    @Override
    public int getItemCount() {
        return mCloudMaterialBeanList == null ? 0 : mCloudMaterialBeanList.size();
    }

    public void addDownloadMaterial(CloudMaterialBean item) {
        if (!mCloudMaterialBeanMap.containsKey(item.getId())) {
            mCloudMaterialBeanMap.put(item.getId(), item);
        }
    }

    public void removeDownloadMaterial(String contentId) {
        mCloudMaterialBeanMap.remove(contentId);
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mSelectView;

        ImageFilterView mItemIv;

        TextView mNameTv;

        ImageView mDownloadIv;

        ProgressBar mDownloadPb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSelectView = itemView.findViewById(R.id.item_select_view);
            mItemIv = itemView.findViewById(R.id.item_image_view);
            mNameTv = itemView.findViewById(R.id.item_name);
            mDownloadIv = itemView.findViewById(R.id.item_download_view);
            mDownloadPb = itemView.findViewById(R.id.item_progress);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDownloadClick(int position);
    }
}

