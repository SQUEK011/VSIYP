package com.example.vsiyp.ui.common.adapter.comment;

import androidx.collection.SparseArrayCompat;

public class ItemViewDelegateManager<T> {
    @SuppressWarnings("unchecked")
    private SparseArrayCompat<ItemViewDelegate<T>> delegateSparseArrayCompat = new SparseArrayCompat();

    public int getItemViewDelegateCount() {
        return delegateSparseArrayCompat.size();
    }

    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> delegate) {
        int viewType = delegateSparseArrayCompat.size();
        if (delegate != null) {
            delegateSparseArrayCompat.put(viewType, delegate);
        }
        return this;
    }

    public ItemViewDelegateManager<T> addDelegate(int viewType, ItemViewDelegate<T> delegate) {
        if (delegateSparseArrayCompat.get(viewType) != null) {
            throw new IllegalArgumentException("An ItemViewDelegate is already registered for the viewType = "
                    + viewType + ". Already registered ItemViewDelegate is " + delegateSparseArrayCompat.get(viewType));
        }
        delegateSparseArrayCompat.put(viewType, delegate);
        return this;
    }

    public int getItemViewType(T item, int position) {
        int delegatesCount = delegateSparseArrayCompat.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewDelegate<T> delegate = delegateSparseArrayCompat.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return delegateSparseArrayCompat.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegate added that matches position=" + position + " in data source");
    }

    public void convert(RViewHolder holder, T item, int dataPosition, int position) {
        int delegatesCount = delegateSparseArrayCompat.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemViewDelegate<T> delegate = delegateSparseArrayCompat.valueAt(i);

            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, dataPosition, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }

    public ItemViewDelegate getItemViewDelegate(int viewType) {
        return delegateSparseArrayCompat.get(viewType);
    }
}

