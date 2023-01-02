package com.example.vsiyp.ui.common.view.tab;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public interface ITab<D> extends ITabLayout.OnTabSelectedListener<D> {
    void setHiTabInfo(@NonNull D data);

    void resetHeight(@Px int height);
}
