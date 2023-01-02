package com.example.vsiyp.ui.common.view;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;

import java.util.List;

public class RotationPopupView extends PopupWindow {
    private LinearLayout mFullView;

    private LinearLayout mPortraitView;

    private LinearLayout mLandView;

    private TextView mFullScreen;

    private TextView mPortraitScreen;

    private TextView mLandScreen;

    private Activity mActivity;

    private List<String> mList;

    private OnActionClickListener mOnActionClickListener;

    private int popupWidth = 0;

    private int popupHeight = 0;

    public RotationPopupView(Activity activity, List<String> list) {
        this.mActivity = activity;
        this.mList = list;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.rotation_popup, null, false);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mFullView = view.findViewById(R.id.rotation_child_full);
        mFullScreen = view.findViewById(R.id.rotation_full_screen);
        mPortraitView = view.findViewById(R.id.rotation_child_portrait);
        mPortraitScreen = view.findViewById(R.id.rotation_portrait_screen);
        mLandView = view.findViewById(R.id.rotation_child_land);
        mLandScreen = view.findViewById(R.id.rotation_landscape);
        mFullScreen.setText(mList.size() > 0 ? mList.get(0) : "");
        mPortraitScreen.setText(mList.size() > 1 ? mList.get(1) : "");
        mLandScreen.setText(mList.size() > 2 ? mList.get(2) : "");

        this.setContentView(view);
        this.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWidth = view.getMeasuredWidth();
        popupHeight = view.getMeasuredHeight();

        this.setOutsideTouchable(true);

        this.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
                return true;
            }
            return false;
        });

        initEvent();
    }

    private void initEvent() {
        mFullView.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mOnActionClickListener != null) {
                mOnActionClickListener.onFullClick();
            }
            dismiss();
        }));

        mPortraitView.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mOnActionClickListener != null) {
                mOnActionClickListener.onPortraitClick();
            }
            dismiss();
        }));

        mLandView.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mOnActionClickListener != null) {
                mOnActionClickListener.onLandClick();
            }
            dismiss();
        }));
    }

    public interface OnActionClickListener {
        void onFullClick();

        void onPortraitClick();

        void onLandClick();
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.mOnActionClickListener = listener;
    }

    public int getPopupWidth() {
        return popupWidth;
    }

    public void setPopupWidth(int popupWidth) {
        this.popupWidth = popupWidth;
    }

    public int getPopupHeight() {
        return popupHeight;
    }

    public void setPopupHeight(int popupHeight) {
        this.popupHeight = popupHeight;
    }
}
