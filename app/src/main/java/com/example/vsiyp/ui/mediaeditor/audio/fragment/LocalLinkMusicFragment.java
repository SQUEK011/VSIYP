package com.example.vsiyp.ui.mediaeditor.audio.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.LazyFragment;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.StringUtil;

public class LocalLinkMusicFragment extends LazyFragment {

    private EditText mLinkEdit;

    private ImageView mDeleteIv;

    private CardView mDownloadCv;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_local_link_music;
    }

    @Override
    protected void initView(View view) {
        mLinkEdit = view.findViewById(R.id.ed_search);
        mDeleteIv = view.findViewById(R.id.delete_iv);
        mDownloadCv = view.findViewById(R.id.download_cv);
    }

    @Override
    protected void initObject() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        if (mDeleteIv != null) {
            mDeleteIv.setOnClickListener(new OnClickRepeatedListener(v -> {
                if (mLinkEdit == null) {
                    return;
                }
                mLinkEdit.setText("");
            }));
        }

        if (mLinkEdit == null) {
            return;
        }
        mLinkEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDeleteIv.setVisibility(StringUtil.isEmpty(s.toString()) ? View.GONE : View.VISIBLE);
                resetCardView(!StringUtil.isEmpty(s.toString()));
            }
        });
    }

    private void resetCardView(boolean isSelect) {
        if (mDownloadCv == null) {
            return;
        }
        mDownloadCv.setCardBackgroundColor(isSelect ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_fff_10));
    }
}

