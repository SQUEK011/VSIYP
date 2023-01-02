package com.example.vsiyp.ui.mediaeditor.blockface;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseDialog;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;

public class FaceStickerDeleteDialog extends BaseDialog {

    private static final int PADDING = 0;

    private TextView mFaceDeleteConfirmTv;

    private TextView mFaceDeleteCancelTv;

    public FaceStickerDeleteDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_face_sticker_delete);
        initFaceStickerView();
        initFaceStickerEvent();
    }

    @Override
    public void show() {
        super.show();

        if (getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams faceDeleteParams = getWindow().getAttributes();
        faceDeleteParams.gravity = Gravity.BOTTOM;
        faceDeleteParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        faceDeleteParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPaddingRelative(PADDING, PADDING, PADDING, PADDING);
        getWindow().setAttributes(faceDeleteParams);
    }

    private void initFaceStickerView() {
        mFaceDeleteConfirmTv = findViewById(R.id.home_clip_delete_dialog_ok);
        mFaceDeleteCancelTv = findViewById(R.id.home_clip_delete_dialog_cancel);
    }

    private void initFaceStickerEvent() {
        mFaceDeleteConfirmTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mFaceDeletePositiveClickListener != null) {
                mFaceDeletePositiveClickListener.onPositiveClick();
            }
            dismiss();
        }));
        mFaceDeleteCancelTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mFaceDeleteCancelClickListener != null) {
                mFaceDeleteCancelClickListener.onCancelClick();
            }
            dismiss();
        }));
    }

    private OnPositiveClickListener mFaceDeletePositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener positiveClickListener) {
        this.mFaceDeletePositiveClickListener = positiveClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    private OnCancelClickListener mFaceDeleteCancelClickListener;

    public void setOnCancelClickListener(OnCancelClickListener cancelClickListener) {
        this.mFaceDeleteCancelClickListener = cancelClickListener;
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }
}

