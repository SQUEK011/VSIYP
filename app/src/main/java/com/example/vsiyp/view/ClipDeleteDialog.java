package com.example.vsiyp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;

public class ClipDeleteDialog extends Dialog {

    private TextView mDeleteConfirmTv;

    private TextView mDeleteCancelTv;

    public ClipDeleteDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_delete);
        initView();
        initEvent();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams deleteParams = getWindow().getAttributes();
        deleteParams.gravity = Gravity.BOTTOM;
        deleteParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        deleteParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPaddingRelative(0, 0, 0, 0);
        getWindow().setAttributes(deleteParams);
    }

    private void initView() {
        mDeleteConfirmTv = findViewById(R.id.home_clip_delete_dialog_ok);
        mDeleteCancelTv = findViewById(R.id.home_clip_delete_dialog_cancel);
    }

    private void initEvent() {
        mDeleteCancelTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mDeleteCancelClickListener != null) {
                mDeleteCancelClickListener.onCancelClick();
            }
            dismiss();
        }));
        mDeleteConfirmTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mDeletePositiveClickListener != null) {
                mDeletePositiveClickListener.onPositiveClick();
            }
            dismiss();
        }));
    }

    private OnPositiveClickListener mDeletePositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener positiveClickListener) {
        this.mDeletePositiveClickListener = positiveClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    private OnCancelClickListener mDeleteCancelClickListener;

    public void setOnCancelClickListener(OnCancelClickListener cancelClickListener) {
        this.mDeleteCancelClickListener = cancelClickListener;
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }
}
