package com.example.vsiyp.ui.common.view.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect aOutRect, View view, RecyclerView parent, RecyclerView.State state) {
        aOutRect.left = mSpace;
        aOutRect.right = mSpace;
        aOutRect.bottom = 0;
        aOutRect.top = 0;
    }
}