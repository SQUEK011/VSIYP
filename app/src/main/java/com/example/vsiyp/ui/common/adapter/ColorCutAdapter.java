package com.example.vsiyp.ui.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.mediaeditor.fragment.ColorCutoutFragment;
import com.example.vsiyp.ui.mediaeditor.preview.ColorCutViewModel;

import java.util.List;

public class ColorCutAdapter extends SelectAdapter {
    ColorCutViewModel mColorCutViewModel;

    Context mContext;

    public ColorCutAdapter(Context context, List list, ColorCutViewModel colorCutViewModel) {
        super(context, list, R.layout.adapter_mask_effect, MaskViewHolder.class);
        this.mContext = context;
        this.mColorCutViewModel = colorCutViewModel;
    }

    @Override
    public int getPosition() {
        return super.getPosition();
    }

    private class MaskViewHolder extends ThisViewHolder {

        private TextView tv;

        private ImageView iv;

        public MaskViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void findView(View view) {
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
            mColorCutViewModel.getMove().observe((LifecycleOwner) mContext, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        if (getPosition() == 1) {
                            iv.setImageResource(R.drawable.ico_qiangdu_select);
                        }
                    } else {
                        if (getPosition() == 1) {
                            iv.setImageResource(R.drawable.ico_qiangdu);
                        }
                    }
                }
            });
        }

        @Override
        protected void onSelect(View view) {
            view.setSelected(true);
        }

        @Override
        protected void onUnSelect(View view) {
            view.setSelected(false);

        }

        @Override
        protected void bindView(Object k) {
            iv.setImageResource(((ColorCutoutFragment.Item) k).icoId);
            tv.setText(((ColorCutoutFragment.Item) k).name);
        }
    }
}
