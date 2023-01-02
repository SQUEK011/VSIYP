package com.example.vsiyp.ui.common.view.tab.bottom;

public class TabBottomInfo<Color> {

    public int nameId;

    public int nameResId;

    public int drawableIcon;

    public Color textDefaultColor;

    public Color textSelectColor;

    public boolean enable;

    public boolean responseEnable = true;

    public TabBottomInfo(int nameRes, int nameId, int drawableIcon, Color textDefaultColor, Color textSelectColor,
                         boolean enable) {
        this.nameResId = nameRes;
        this.nameId = nameId;
        this.drawableIcon = drawableIcon;
        this.textDefaultColor = textDefaultColor;
        this.textSelectColor = textSelectColor;
        this.enable = enable;
    }

    public void setResponseEnable(boolean responseEnable) {
        this.responseEnable = responseEnable;
    }

}
