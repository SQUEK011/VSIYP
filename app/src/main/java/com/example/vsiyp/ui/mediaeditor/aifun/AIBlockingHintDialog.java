package com.example.vsiyp.ui.mediaeditor.aifun;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;

public class AIBlockingHintDialog extends Dialog {

    private TextView mDeleteAgreeTv;

    private TextView mDeleteCancelTv;

    private TextView mTitleTv;

    private TextView mContentTv;

    private String title;

    private String content;

    public AIBlockingHintDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    public AIBlockingHintDialog(@NonNull Context context, String titleStr, String contentStr) {
        super(context, R.style.DialogTheme);
        this.title = titleStr;
        this.content = contentStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ai_block_tip);
        initView();
        initClick();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams deleteParams = getWindow().getAttributes();
        deleteParams.gravity = Gravity.BOTTOM;
        deleteParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        deleteParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView()
                .setPaddingRelative(SizeUtils.dp2Px(getContext(), 16), 0, SizeUtils.dp2Px(getContext(), 16),
                        SizeUtils.dp2Px(getContext(), 16));
        getWindow().setAttributes(deleteParams);
    }

    private void initView() {
        mDeleteAgreeTv = findViewById(R.id.home_clip_delete_dialog_ok);
        mDeleteCancelTv = findViewById(R.id.home_clip_delete_dialog_cancel);
        mTitleTv = findViewById(R.id.tv_title);
        mContentTv = findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(title)) {
            mTitleTv.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            mContentTv.setText(content);
        }
    }

    private void initClick() {
        mDeleteCancelTv.setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelClickListener != null) {
                    mCancelClickListener.onCancelClick();
                }
                dismiss();
            }
        }));
        mDeleteAgreeTv.setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPositiveClickListener != null) {
                    mPositiveClickListener.onPositiveClick();
                }
                dismiss();
            }
        }));
    }

    private OnPositiveClickListener mPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener positiveClickListener) {
        this.mPositiveClickListener = positiveClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    private OnCancelClickListener mCancelClickListener;

    public void setOnCancelClickListener(OnCancelClickListener cancelClickListener) {
        this.mCancelClickListener = cancelClickListener;
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }
}

