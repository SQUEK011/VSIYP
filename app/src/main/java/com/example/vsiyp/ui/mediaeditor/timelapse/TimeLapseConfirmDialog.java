package com.example.vsiyp.ui.mediaeditor.timelapse;

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

public class TimeLapseConfirmDialog extends Dialog {

    private TextView mDeleteConfirmTv;

    private TextView mDeleteCancelTv;

    public TimeLapseConfirmDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_lapse_confirm);
        initView();
        initEvent();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams humanDeleteParams = getWindow().getAttributes();
        humanDeleteParams.gravity = Gravity.BOTTOM;
        humanDeleteParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        humanDeleteParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPaddingRelative(0, 0, 0, 0);
        getWindow().setAttributes(humanDeleteParams);
    }

    private void initView() {
        mDeleteConfirmTv = findViewById(R.id.home_clip_delete_dialog_ok);
        mDeleteCancelTv = findViewById(R.id.home_clip_delete_dialog_cancel);
    }

    private void initEvent() {
        mDeleteConfirmTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mDeletePositiveClickListener != null) {
                mDeletePositiveClickListener.onPositiveClick();
            }
            dismiss();
        }));
        mDeleteCancelTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mDeleteCancelClickListener != null) {
                mDeleteCancelClickListener.onCancelClick();
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

