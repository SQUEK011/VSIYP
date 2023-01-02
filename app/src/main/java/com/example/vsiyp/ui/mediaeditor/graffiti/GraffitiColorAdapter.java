package com.example.vsiyp.ui.mediaeditor.graffiti;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.adapter.comment.RCommandAdapter;
import com.example.vsiyp.ui.common.adapter.comment.RViewHolder;
import com.example.vsiyp.ui.common.utils.SharedPreferencesUtils;
import com.example.vsiyp.ui.mediaeditor.trackview.viewmodel.EditPreviewViewModel;

import java.util.List;

public class GraffitiColorAdapter extends RCommandAdapter<Integer> {
    private int selectPosition = -1;

    private EditPreviewViewModel viewModel;

    private Context context;

    public GraffitiColorAdapter(Context context, List<Integer> list, int layoutId,
                                EditPreviewViewModel textEditViewModel) {
        super(context, list, layoutId);
        this.context = context;
        this.viewModel = textEditViewModel;
    }

    @Override
    protected void convert(RViewHolder holder, Integer integer, int dataPosition, int position) {
        holder.itemView.setBackgroundColor(integer);
        View viewColor = holder.getView(R.id.color_view_item_color);
        View bgView = holder.getView(R.id.bg_view_item_color);
        if (position == 1) {
            bgView.setVisibility(View.VISIBLE);
        }
        viewColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (selectPosition != position) {
                    if (selectPosition != -1) {
                        int lastp = selectPosition;
                        selectPosition = position;
                        notifyItemChanged(lastp);
                    } else {
                        selectPosition = position;
                    }
                    notifyItemChanged(selectPosition);
                    SharedPreferencesUtils.getInstance()
                            .putIntValue(context, SharedPreferencesUtils.COLOR_SELECT_INDEX, selectPosition);
                }
                return false;
            }
        });

        viewModel.getHeadClick().observe((LifecycleOwner) context, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                selectPosition = SharedPreferencesUtils.getInstance()
                        .getIntValue(context, SharedPreferencesUtils.COLOR_SELECT_INDEX);
                if (aBoolean) {
                    bgView.setVisibility(View.GONE);
                } else {
                    bgView.setVisibility(selectPosition == position ? View.VISIBLE : View.GONE);
                }
            }
        });
    }
}

