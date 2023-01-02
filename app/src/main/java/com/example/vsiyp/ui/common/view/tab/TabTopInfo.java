package com.example.vsiyp.ui.common.view.tab;

public class TabTopInfo<Color> {

    public String mFamilyName = "Helvetica";

    public String mName;

    public Color mDefaultColor;

    public Color mTintColor;

    public int mPaddingLeft;

    public int mPaddingRight;

    public int mDefaultTextSize = 0;

    public int mSelectTextSize = 0;

    public boolean mShowIndicator;

    public TabTopInfo(String mName, boolean showIndicator, Color defaultColor, Color tintColor, int paddingLeft,
                      int paddingRight) {
        this.mName = mName;
        this.mShowIndicator = showIndicator;
        this.mDefaultColor = defaultColor;
        this.mTintColor = tintColor;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
    }

    public TabTopInfo(String mName, boolean showIndicator, Color defaultColor, Color tintColor, int defaultTextSize,
                      int selectTextSize, int paddingLeft, int paddingRight) {
        this.mName = mName;
        this.mShowIndicator = showIndicator;
        this.mDefaultColor = defaultColor;
        this.mTintColor = tintColor;
        this.mDefaultTextSize = defaultTextSize;
        this.mSelectTextSize = selectTextSize;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
    }

    public void setFamilyName(String familyName) {
        this.mFamilyName = familyName;
    }
}

