package com.example.vsiyp.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.ToastWrapper;
import com.huawei.hms.videoeditor.sdk.HVEProject;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.text.NumberFormat;
import java.util.Locale;

public class ClipRenameDialog extends Dialog {
    private static final int MAX_TEXT = 50;

    private TextView mRenameConfirmTv;

    private TextView mRenameCancelTv;

    private EditText mNameTv;

    private HVEProject item;

    private Activity mActivity;

    private int mSelectionEnd;

    public ClipRenameDialog(@NonNull Activity activity, HVEProject item) {
        super(activity, R.style.DialogTheme);
        this.mActivity = activity;
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_rename);
        initView();
        initEvent();
    }

    @Override
    public void show() {
        super.show();

        WindowManager.LayoutParams renameParams = getWindow().getAttributes();
        renameParams.gravity = Gravity.BOTTOM;
        renameParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        renameParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPaddingRelative(0, 0, 0, 0);
        getWindow().setAttributes(renameParams);
        new Handler().postDelayed(() -> {
            if (mActivity != null) {
                mNameTv.setFocusable(true);
                mNameTv.setFocusableInTouchMode(true);
                mNameTv.requestFocus();
                showKeyboard(mActivity);

            }
        }, 300);
    }

    private void initView() {
        mRenameConfirmTv = findViewById(R.id.home_clip_rename_dialog_ok);
        mRenameCancelTv = findViewById(R.id.home_clip_rename_dialog_cancel);
        mNameTv = findViewById(R.id.home_clip_rename_dialog_name);
        String nameText = item.getName();
        if (!TextUtils.isEmpty(nameText)) {
            mNameTv.setText(item.getName());

            try {
                mNameTv.setSelection(nameText.length());
            } catch (RuntimeException e) {
                SmartLog.w("ClipRenameDialog", "initView setSelection " + e.getMessage());
            }
        }
    }

    private void initEvent() {
        mNameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("StringFormatMatches")
            @Override
            public void afterTextChanged(Editable s) {
                mSelectionEnd = mNameTv.getSelectionEnd();
                if (s.length() > MAX_TEXT) {
                    int num = s.length() - MAX_TEXT;

                    s.delete(mSelectionEnd - num, mSelectionEnd);
                    mNameTv.setText(s.toString().trim());
                    mNameTv.setSelection(s.toString().trim().length());
                    if (mActivity != null) {
                        mActivity.runOnUiThread(() -> {
                            ToastWrapper.makeText(mActivity, String.format(Locale.ROOT,
                                            mActivity.getString(R.string.most_text), NumberFormat.getInstance().format(MAX_TEXT)))
                                    .show();
                        });
                    }
                }

            }
        });

        mRenameCancelTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mRenameCancelClickListener != null) {
                mRenameCancelClickListener.onCancelClick();
            }
            dismiss();
        }));

        mRenameConfirmTv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mRenamePositiveClickListener != null) {
                if (TextUtils.isEmpty(mNameTv.getText().toString().trim())) {
                    ToastWrapper.makeText(mActivity, mActivity.getString(R.string.name_can_not_be_empty)).show();
                    return;
                } else {
                    mRenamePositiveClickListener.onPositiveClick(mNameTv.getText().toString().trim());
                }
            }
            dismiss();
        }));
    }

    private OnPositiveClickListener mRenamePositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener positiveClickListener) {
        this.mRenamePositiveClickListener = positiveClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick(String updateName);
    }

    private OnCancelClickListener mRenameCancelClickListener;

    public void setOnCancelClickListener(OnCancelClickListener cancelClickListener) {
        this.mRenameCancelClickListener = cancelClickListener;
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }

    private void showKeyboard(Activity context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mNameTv.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
    }
}
