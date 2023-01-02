package com.example.vsiyp.ui.mediaeditor.cover;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;

public class CoverImageAdapter extends PagedListAdapter<MediaData, CoverImageAdapter.ViewHolder> {

    private Context mContext;

    private int mImageViewWidth;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public CoverImageAdapter(Context context) {
        super(new DiffUtil.ItemCallback<MediaData>() {
            @Override
            public boolean areItemsTheSame(@NonNull MediaData oldItem, @NonNull MediaData newMediaData) {
                return oldItem.getPath().equals(newMediaData.getPath());
            }

            @Override
            public boolean areContentsTheSame(@NonNull MediaData oldItem, @NonNull MediaData newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mImageViewWidth = (SizeUtils.screenWidth(mContext) - (SizeUtils.dp2Px(mContext, 48))) / 3;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cover_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mContentLayout.setLayoutParams(new RelativeLayout.LayoutParams(mImageViewWidth, mImageViewWidth));
        MediaData item = getItem(position);
        if (item != null && !TextUtils.isEmpty(item.getPath())) {
            Glide.with(mContext).asBitmap().load(item.getPath()).into(holder.mMediaIv);
        }
        holder.itemView
                .setOnClickListener(new OnClickRepeatedListener((v) -> mOnItemClickListener.onItemClick(position)));
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mContentLayout;

        ImageView mMediaIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mContentLayout = itemView.findViewById(R.id.content_layout);
            mMediaIv = itemView.findViewById(R.id.iv_media);
        }
    }
}

