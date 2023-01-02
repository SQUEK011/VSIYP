package com.example.vsiyp.ui.common.adapter.comment;

public interface ItemViewDelegate<T> {
    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(RViewHolder holder, T t, int dataPosition, int position);
}
