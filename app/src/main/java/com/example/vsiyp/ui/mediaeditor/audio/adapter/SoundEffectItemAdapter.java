package com.example.vsiyp.ui.mediaeditor.audio.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.utils.StringUtil;
import com.example.vsiyp.ui.common.utils.TimeUtils;
import com.example.vsiyp.ui.common.view.audio.AudioColumnView;
import com.example.vsiyp.ui.mediaeditor.audio.view.SoundEffectMarqueTextView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SoundEffectItemAdapter extends RCommandAdapter<CloudMaterialBean> {
    private final Map<String, CloudMaterialBean> aDownloadingMap = new LinkedHashMap<>();

    private volatile int aSelectPosition = -1;

    private OnItemClickListener onClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onClickListener = listener;
    }

    public SoundEffectItemAdapter(Context context, List<CloudMaterialBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void convert(RViewHolder holder, CloudMaterialBean materialsCutContent, int dataPosition, int position) {
        AudioColumnView mColumnView = holder.getView(R.id.audio_column_view);
        ImageView mMusicPictureIv = holder.getView(R.id.music_icon);
        SoundEffectMarqueTextView mNameTv = holder.getView(R.id.music_name_tv);
        TextView mDurationTv = holder.getView(R.id.local_duration_tv);
        ImageView mMusicDownloadIv = holder.getView(R.id.music_download_icon);
        TextView mUseTv = holder.getView(R.id.music_use_tv);
        FrameLayout mProgressLayout = holder.getView(R.id.music_download_progress_layout);
        mNameTv.setMaxWidth(SizeUtils.screenWidth(mContext) - SizeUtils.dp2Px(mContext, 148));
        CloudMaterialBean item = mList.get(dataPosition);
        if (position == aSelectPosition) {
            mMusicPictureIv.setVisibility(View.GONE);
            mColumnView.setVisibility(View.VISIBLE);
            mProgressLayout.setVisibility(View.VISIBLE);
        } else {
            mMusicPictureIv.setVisibility(View.VISIBLE);
            mColumnView.stop();
            mColumnView.setVisibility(View.GONE);
            mProgressLayout.setVisibility(View.GONE);
        }

        mNameTv.setText(item.getName());
        mDurationTv.setText(TimeUtils.makeSoundTimeString(mContext, item.getDuration() * 1000));

        if (!StringUtil.isEmpty(item.getLocalPath())) {
            mProgressLayout.setVisibility(View.GONE);
            mMusicDownloadIv.setVisibility(View.GONE);
            mUseTv.setVisibility(View.VISIBLE);
        } else {
            mMusicDownloadIv.setVisibility(aSelectPosition == position ? View.INVISIBLE : View.VISIBLE);
            mProgressLayout.setVisibility(aSelectPosition == position ? View.VISIBLE : View.INVISIBLE);
            mUseTv.setVisibility(View.GONE);
        }

        if (aDownloadingMap.containsKey(item.getId())) {
            mProgressLayout.setVisibility(View.VISIBLE);
            mMusicDownloadIv.setVisibility(View.GONE);
            mUseTv.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (onClickListener != null) {
                if (!StringUtil.isEmpty(item.getLocalPath())) {
                    onClickListener.onItemClick(position, dataPosition);
                } else {
                    if (!aDownloadingMap.containsKey(item.getId())) {
                        onClickListener.onDownloadClick(position, dataPosition);
                        mProgressLayout.setVisibility(View.VISIBLE);
                        mMusicDownloadIv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }));
        mMusicDownloadIv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (onClickListener != null) {
                if (!aDownloadingMap.containsKey(item.getId())) {
                    onClickListener.onDownloadClick(position, dataPosition);
                    mProgressLayout.setVisibility(View.VISIBLE);
                    mMusicDownloadIv.setVisibility(View.INVISIBLE);
                }
            }
        }));
        mUseTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (onClickListener != null) {
                onClickListener.onUseClick(position, dataPosition);
            }
        }));
    }

    public void setSelectPosition(int selectPosition) {
        this.aSelectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return aSelectPosition;
    }

    public void addDownloadMaterial(CloudMaterialBean item) {
        aDownloadingMap.put(item.getId(), item);
    }

    public void removeDownloadMaterial(String contentId) {
        aDownloadingMap.remove(contentId);
    }

    public interface OnItemClickListener {
        void onItemClick(int aPosition, int aDataPosition);

        void onDownloadClick(int aPosition, int aDataPosition);

        void onUseClick(int aPosition, int aDataPosition);
    }
}

