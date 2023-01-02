package com.example.vsiyp.ui.mediaeditor.aihair;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.videoeditor.sdk.util.SmartLog;

public class FilterLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = "FilterLinearLayoutManager";

    public FilterLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler parent, RecyclerView.State state) {
        try {
            super.onLayoutChildren(parent, state);
        } catch (IndexOutOfBoundsException e) {
            SmartLog.e(TAG, e.getMessage());
        }
    }
}
