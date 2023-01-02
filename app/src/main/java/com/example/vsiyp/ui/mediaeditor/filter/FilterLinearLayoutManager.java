package com.example.vsiyp.ui.mediaeditor.filter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.videoeditor.sdk.util.SmartLog;

public class FilterLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = "FilterLinearLayoutManager";

    public FilterLinearLayoutManager(Context context) {
        super(context);
    }

    public FilterLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FilterLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            SmartLog.e(TAG, e.getMessage());
        }
    }
}

