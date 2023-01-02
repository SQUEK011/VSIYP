package com.example.vsiyp.ui.common.view.dialog;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseDialog;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;

public class HumanTrackingProgressDialog extends BaseDialog {

    private TextView tvName;

    private TextView tvProgress;

    private ProgressBar progress;

    private ImageView ivStop;

    private String title;

    private OnProgressClick onProgressClick;

    public void setOnProgressClick(OnProgressClick onProgressClick) {
        this.onProgressClick = onProgressClick;
    }

    public HumanTrackingProgressDialog(@NonNull Context context, String title) {
        super(context, R.style.DialogProgress);
        this.title = title;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setDimAmount(0.5f);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_downloading_progress);

        tvName = findViewById(R.id.tv_name);
        tvProgress = findViewById(R.id.tv_progress);
        progress = findViewById(R.id.progress);
        ivStop = findViewById(R.id.iv_stop);

        tvName.setText(title);

        ivStop.setOnClickListener(new OnClickRepeatedListener(v -> {
            dismiss();
            if (onProgressClick != null) {
                onProgressClick.onCancel();
            }
        }));
    }

    public void setProgress(int progress) {
        this.progress.setProgress(progress);
        tvProgress.setText(progress + "%");
    }

    public void setStopVisble(boolean visble) {
        ivStop.setVisibility(visble ? View.VISIBLE : View.GONE);
    }

    public void show(WindowManager windowManager) {
        show();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point = new Point();
        defaultDisplay.getSize(point);
        params.width = (int) (point.x * 1);
        getWindow().setAttributes(params);
    }

    public interface OnProgressClick {
        void onCancel();
    }
}
