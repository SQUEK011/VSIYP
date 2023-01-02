package com.example.vsiyp.ui.common.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.LanguageUtils;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.Locale;

public class AdvanceExitDialog extends Dialog {
    private static final String TAG = "AdvanceExitDialog";

    public AdvanceExitDialog(Context context, OnClickListener onClickListener) {
        super(context, R.style.DialogTheme);
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.advance_exit_dialog, null, false);
        setContentView(view);
        TextView cancelTv = view.findViewById(R.id.cancel_tv);
        TextView saveTv = view.findViewById(R.id.confirm_tv);

        Resources resources = context.getResources();
        if (resources != null) {
            if (LanguageUtils.isEn()) {
                cancelTv.setText(resources.getString(R.string.is_give_up_no).toUpperCase(Locale.ENGLISH));
                saveTv.setText(resources.getString(R.string.save_wza).toUpperCase(Locale.ENGLISH));
            } else {
                cancelTv.setText(resources.getString(R.string.is_give_up_no));
                saveTv.setText(resources.getString(R.string.save_wza));
            }
        }
        saveTv.setOnClickListener(new OnClickRepeatedListener(View -> {
            cancel();
            if (onClickListener != null) {
                onClickListener.onSave();
            }
        }));

        cancelTv.setOnClickListener(new OnClickRepeatedListener(View -> {
            cancel();
            if (onClickListener != null) {
                onClickListener.onBack();
            }
        }));

        Window window = getWindow();
        if (window == null) {
            SmartLog.d(TAG, "window is null");
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(params);
        window.setLayout(SizeUtils.screenWidth(context) - SizeUtils.dp2Px(context, 32),
                WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getDecorView().setPaddingRelative(0, 0, 0, SizeUtils.dp2Px(context, 16));
    }

    public interface OnClickListener {
        void onSave();

        void onBack();
    }
}
