package com.example.vsiyp.ui.common.view.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.EditorTextView;
import com.example.vsiyp.ui.common.view.tab.ITab;

public class TabBottom extends RelativeLayout implements ITab<TabBottomInfo<?>> {
    private TabBottomInfo<?> mTabInfo;

    private ImageView mTabImageView;

    private EditorTextView mTabNameView;

    public TabBottom(Context context) {
        this(context, null);
    }

    public TabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.tab_bottom_layout, this);
        mTabImageView = findViewById(R.id.iv_image);
        mTabNameView = findViewById(R.id.tv_name);
    }

    @Override
    public void setHiTabInfo(@NonNull TabBottomInfo<?> hiTabBottomInfo) {
        this.mTabInfo = hiTabBottomInfo;
        inflateInfo(false, true);
    }

    public TabBottomInfo<?> getHiTabInfo() {
        return mTabInfo;
    }

    @Override
    public void resetHeight(@Px int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        setLayoutParams(params);
    }

    private void inflateInfo(boolean selected, boolean init) {
        if (init) {
            int mMenuWidth = (int) (SizeUtils.screenWidth(getContext()) / 6.5f);
            ViewGroup.LayoutParams layoutParams =
                    new ViewGroup.LayoutParams(mMenuWidth, SizeUtils.dp2Px(getContext(), 64));
            setLayoutParams(layoutParams);
            mTabNameView.setText(mTabInfo.nameResId);
            mTabImageView.setImageResource(mTabInfo.drawableIcon);
        }
        mTabNameView.setTextColor(getTextColor(selected ? mTabInfo.textSelectColor : mTabInfo.textDefaultColor));
        mTabImageView.setSelected(selected);
    }

    @Override
    public void onTabSelectedChange(int index, @Nullable TabBottomInfo<?> prevInfo,
                                    @NonNull TabBottomInfo<?> nextInfo) {
        if (prevInfo != mTabInfo && nextInfo != mTabInfo || prevInfo == nextInfo) {
            return;
        }
        inflateInfo(prevInfo != mTabInfo, false);
    }

    @ColorInt
    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }
}

