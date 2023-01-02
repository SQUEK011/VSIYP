package com.example.vsiyp.ui.common.view.loading;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

@SuppressWarnings("ALL")
public class LoadingDialogUtils {

    private volatile static LoadingDialogUtils utils = null;

    private HwLoadingDialog loadingDialog = null;

    private LifecycleEventObserver observer = null;

    private Lifecycle mLifecycle = null;

    public static LoadingDialogUtils getInstance() {
        if (utils == null) {
            synchronized (LoadingDialogUtils.class) {
                if (utils == null) {
                    utils = new LoadingDialogUtils();
                }
            }
        }
        return utils;
    }

    public Builder builder(Context context, Lifecycle lifecycle) {
        if (mLifecycle != null) {
            mLifecycle.removeObserver(observer);
            observer = null;
        }
        if (observer == null) {
            observer = new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == event.ON_DESTROY) {
                        dismiss();
                    }
                }
            };
            lifecycle.addObserver(observer);
            mLifecycle = lifecycle;
        }

        return new Builder(context);
    }

    public class Builder {
        public Builder(Context context) {
            dismiss();
            loadingDialog = new HwLoadingDialog(context);
        }

        public Builder show(String content, boolean isVisibiltyText) {
            if (loadingDialog != null && !loadingDialog.isShowing()) {
                loadingDialog.show();
                loadingDialog.setTvContent(content);
                loadingDialog.setVisibility(isVisibiltyText);
            }
            return this;
        }

        public Builder show(int width, String content, boolean isVisibiltyText) {
            if (loadingDialog != null && !loadingDialog.isShowing()) {
                loadingDialog.show(width);
                loadingDialog.setTvContent(content);
                loadingDialog.setVisibility(isVisibiltyText);
            }
            return this;
        }

        public Builder setCancelable(boolean isCancelable) {
            if (loadingDialog != null) {
                loadingDialog.setHwCancelable(isCancelable);
            }
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
            if (loadingDialog != null) {
                loadingDialog.setHwCanceledOnTouchOutside(isCanceledOnTouchOutside);
            }
            return this;
        }
    }

    public void dismiss() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
