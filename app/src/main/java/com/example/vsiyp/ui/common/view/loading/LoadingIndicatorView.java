package com.example.vsiyp.ui.common.view.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.example.vsiyp.R;

public class LoadingIndicatorView extends FrameLayout {

    public LoadingIndicatorView(Context context) {
        this(context, null);
    }

    public LoadingIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.lottie_network_loading, this, true);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }
}
