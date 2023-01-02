package com.example.vsiyp.ui.mediaeditor.materialedit;

import android.view.MotionEvent;

import com.huawei.hms.videoeditor.sdk.bean.HVEPosition2D;

public interface OnMaterialEditListener {

    void onTap(HVEPosition2D position2D);

    void onFingerDown();

    void onFling(float dx, float dy);

    void onVibrate();

    void onShowReferenceLine(boolean isShow, boolean isHorizontal);

    void onFingerUp();

    void onScaleRotate(float scale, float angle);

    void onDelete();

    void onEdit();

    void onDoubleFingerTap();

    void onCopy();

    void onKeyDown(MotionEvent event);

    void onKeyMove(MotionEvent event);

    void onKeyUp(MotionEvent event);
}

