package com.example.vsiyp.ui.mediaeditor.canvas;

import android.content.Context;
import android.view.View;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;

import java.util.List;

public class CanvasColor2Adapter extends RCommandAdapter<Integer> {
    private int mSelectPosition = -1;

    OnBlurSelectedListener blurSelectedListener;

    public CanvasColor2Adapter(Context context, List<Integer> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void convert(RViewHolder holder, Integer integer, int dataPosition, int position) {
        View viewBg = holder.getView(R.id.bg_view_item_color);
        View viewColor = holder.getView(R.id.color_view_item_color);
        viewBg.setVisibility(mSelectPosition == position ? View.VISIBLE : View.GONE);
        viewColor.setBackgroundColor(integer);
        viewColor.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mSelectPosition != position) {
                if (mSelectPosition != -1) {
                    int lastp = mSelectPosition;
                    mSelectPosition = position;
                    notifyItemChanged(lastp);
                } else {
                    mSelectPosition = position;
                }
                notifyItemChanged(mSelectPosition);
                if (blurSelectedListener != null) {
                    blurSelectedListener.onBlurSelected(integer, position);
                }
            }
        }));
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return this.mSelectPosition;
    }

    public void setBlurSelectedListener(OnBlurSelectedListener blurSelectedListener) {
        this.blurSelectedListener = blurSelectedListener;
    }

    public interface OnBlurSelectedListener {
        void onBlurSelected(Integer blur, int position);
    }

}

