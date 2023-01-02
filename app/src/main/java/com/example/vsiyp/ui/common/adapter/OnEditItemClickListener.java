package com.example.vsiyp.ui.common.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface OnEditItemClickListener<T> {
    void onItemClick(RecyclerView.ViewHolder holder, T data, int position);

    boolean onItemLongClick(RecyclerView.ViewHolder holder, T data, int position);
}
