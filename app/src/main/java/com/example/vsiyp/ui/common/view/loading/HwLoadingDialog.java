package com.example.vsiyp.ui.common.view.loading;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.vsiyp.R;

public class HwLoadingDialog extends Dialog {

    private ProgressBar hwProgressBar;

    private TextView tvContent;

    public HwLoadingDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hw_loading_dialog);
        Window window = getWindow();
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1.0F;
        lp.dimAmount = 0.0F;
        lp.verticalMargin = 0.415F;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        hwProgressBar = findViewById(R.id.hw_progress_bar);
        tvContent = findViewById(R.id.tv_content);
    }

    public void show(int width) {
        if (!isShowing()) {
            show();
        }
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, width);
        hwProgressBar.setLayoutParams(layoutParams);
    }

    public void setHwCancelable(boolean isCancelable) {
        setCancelable(isCancelable);
    }

    public void setHwCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);
    }

    public void setTvContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
    }

    public void setVisibility(boolean isVisibility) {
        if (!isVisibility) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
        }
    }
}
