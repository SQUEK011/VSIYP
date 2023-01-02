package com.example.vsiyp.ui.mediaeditor.persontrack;

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

public class HumanTrackingConfirmDialog extends Dialog {
    private TextView mDeleteConfirmTv;

    private TextView mDeleteCancelTv;

    private OnPositiveClickListener mDeletePositiveClickListener;

    private OnCancelClickListener mDeleteCancelClickListener;

    public HumanTrackingConfirmDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_human_tracking_confirm);
        initView();
        initEvent();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPaddingRelative(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    private void initView() {
        mDeleteCancelTv = findViewById(R.id.home_clip_delete_dialog_cancel);
        mDeleteConfirmTv = findViewById(R.id.home_clip_delete_dialog_ok);
    }

    private void initEvent() {
        mDeleteConfirmTv.setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeletePositiveClickListener != null) {
                    mDeletePositiveClickListener.onPositiveClick();
                }
                dismiss();
            }
        }));
        mDeleteCancelTv.setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteCancelClickListener != null) {
                    mDeleteCancelClickListener.onCancelClick();
                }
                dismiss();
            }
        }));
    }

    public void setOnCancelClickListener(OnCancelClickListener cancelClickListener) {
        this.mDeleteCancelClickListener = cancelClickListener;
    }

    public void setOnPositiveClickListener(OnPositiveClickListener positiveClickListener) {
        this.mDeletePositiveClickListener = positiveClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }
}

