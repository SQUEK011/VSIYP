package com.example.vsiyp.ui.common.adapter.comment;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;

    private View convertView;

    public RViewHolder(Context context, View itemView) {
        super(itemView);
        convertView = itemView;
        views = new SparseArray<>();
    }

    public static RViewHolder get(Context context, View view) {
        return new RViewHolder(context, view);
    }

    public static RViewHolder get(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RViewHolder(context, itemView);
    }

    public View getCovertView() {
        return convertView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public RViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public RViewHolder setText(int viewId, int resId) {
        TextView textView = getView(viewId);
        textView.setText(resId);
        return this;
    }

    public RViewHolder setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
        return this;
    }

    public RViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public RViewHolder setViewAlpha(int viewId, float alpha) {
        View view = getView(viewId);
        view.setAlpha(alpha);
        return this;
    }
}

