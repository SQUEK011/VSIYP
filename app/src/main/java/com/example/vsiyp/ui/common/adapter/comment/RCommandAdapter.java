package com.example.vsiyp.ui.common.adapter.comment;

import android.content.Context;

import java.util.List;

public abstract class RCommandAdapter<T> extends RMCommandAdapter<T> {
    protected abstract void convert(RViewHolder holder, T t, int dataPosition, int position);

    public RCommandAdapter(Context context, List<T> list, final int layoutId) {
        super(context, list);

        addItemViewDelegate(new ItemViewDelegate<T>() {

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public void convert(RViewHolder holder, T t, int dataPosition, int position) {
                RCommandAdapter.this.convert(holder, t, dataPosition, position);
            }

        });
    }
}
