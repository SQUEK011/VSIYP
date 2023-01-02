package com.example.vsiyp.ui.common.view.decoration;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vsiyp.ui.common.utils.SizeUtils;
public class DividerLine {
    public static RecyclerViewDivider getLine(Context context, float height, int colorRes) {
        return new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, SizeUtils.dp2Px(context, height),
                ContextCompat.getColor(context, colorRes));
    }
}

