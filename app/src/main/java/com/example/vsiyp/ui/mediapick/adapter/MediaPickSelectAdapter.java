
package com.example.vsiyp.ui.mediapick.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.TimeUtils;
import com.example.vsiyp.ui.mediapick.manager.MediaPickManager;
import com.example.vsiyp.ui.mediapick.manager.MediaSelectDragCallback;


import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

public class MediaPickSelectAdapter extends RecyclerView.Adapter<MediaPickSelectAdapter.ViewHolder>
    implements MediaSelectDragCallback.OnItemMoveCallback {
    private final Activity mContext;

    private final List<MediaData> mAllMediaList;

    private final MediaPickManager mManager;

    private final int mActionType;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public MediaPickSelectAdapter(Activity context, List<MediaData> list, int actionType) {
        mContext = context;
        mAllMediaList = list;
        this.mActionType = actionType;
        mManager = MediaPickManager.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pick_select_video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaData item = mAllMediaList.get(holder.getAdapterPosition());
        Glide.with(mContext)
            .load(!TextUtils.isEmpty(item.getCoverUrl()) ? item.getCoverUrl() : item.getPath())
            .apply(new RequestOptions().transform(new MultiTransformation(new CenterCrop())))
            .into(holder.mMediaIv);
        if (item.isVideo()) {
            holder.mDurationTv.setVisibility(View.VISIBLE);
            if (item.getCutTrimOut() <= 0 && item.getCutTrimIn() <= 0) {
                holder.mDurationTv.setText(TimeUtils.makeTimeString(mContext, item.getDuration()));
            } else {
                holder.mDurationTv.setText(TimeUtils.makeTimeString(mContext,
                    item.getDuration() - item.getCutTrimIn() - item.getCutTrimOut()));
            }
        } else {
            holder.mDurationTv.setVisibility(View.GONE);
        }

        holder.mDeleteIv.setOnClickListener(new OnClickRepeatedListener((v) -> {
            if (holder.getAdapterPosition() >= 0 && holder.getAdapterPosition() < mAllMediaList.size()) {
                int selectMediaIndex = mAllMediaList.get(holder.getAdapterPosition()).getIndex();
                MediaData entity;
                for (int i = selectMediaIndex; i < mManager.getSelectItemList().size(); i++) {
                    entity = mManager.getSelectItemList().get(i);
                    mManager.setNewIndexForSelectItem(entity, i);
                }
                mManager.removeSelectItemAndSetIndex(item);
            }
        }));
        holder.itemView.setOnClickListener(new OnClickRepeatedListener((v) -> {
            if (mOnItemClickListener != null) {
                mManager.setPreviewMediaData(item);
                mOnItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        }));

        holder.itemView.setOnLongClickListener(view -> true);
    }

    @Override
    public int getItemCount() {
        return mAllMediaList == null ? 0 : mAllMediaList.size();
    }

    public List<MediaData> getDataList() {
        return mAllMediaList;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemMove(fromPosition, toPosition);
        }
    }

    @Override
    public void onFinish() {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onFinish();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemMove(int fromPosition, int toPosition);

        void onFinish();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageFilterView mMediaIv;

        TextView mDurationTv;

        ImageView mDeleteIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMediaIv = itemView.findViewById(R.id.iv_media);
            mDurationTv = itemView.findViewById(R.id.tv_duration);
            mDeleteIv = itemView.findViewById(R.id.iv_delete);
        }
    }
}
