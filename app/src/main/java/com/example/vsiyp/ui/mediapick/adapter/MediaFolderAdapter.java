package com.example.vsiyp.ui.mediapick.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.view.image.RoundImage;
import com.example.vsiyp.ui.mediapick.bean.MediaFolder;


public class MediaFolderAdapter extends RCommandAdapter<MediaFolder> {

    public MediaFolderAdapter(Context context, List<MediaFolder> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void convert(RViewHolder holder, MediaFolder mediaFolder, int dataPosition, int position) {
        RoundImage roundImage = holder.getView(R.id.iv_media);
        View bottomLine = holder.getView(R.id.bottom_line);
        bottomLine.setVisibility(dataPosition == (mList.size() - 1) ? View.INVISIBLE : View.VISIBLE);
        Glide.with(mContext).load(mediaFolder.getFirstMediaPath()).into(roundImage);
        holder.setText(R.id.tv_folder_name, String.valueOf(mediaFolder.getDirName()));
    }
}
