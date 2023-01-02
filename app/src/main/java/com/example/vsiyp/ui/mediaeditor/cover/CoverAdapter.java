package com.example.vsiyp.ui.mediaeditor.cover;

import android.content.Context;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;

import java.util.List;

public class CoverAdapter extends RCommandAdapter<HVEAsset> {

    public CoverAdapter(Context context, List<HVEAsset> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void convert(RViewHolder holder, HVEAsset data, int dataPosition, int position) {
        CoverTrackView mImage = holder.getView(R.id.cove_track);
        mImage.setAsset(data);
    }
}
