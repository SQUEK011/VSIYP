package com.example.vsiyp.ui.mediaeditor.preview.view.colorpicker;

import android.widget.RelativeLayout;

public interface OnColorChangeListener {
    void colorChanged(int color, RelativeLayout.LayoutParams layoutParams, RelativeLayout.LayoutParams seekbarParams,
                      float xParams);
}
