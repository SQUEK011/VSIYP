package com.example.vsiyp.ui.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.vsiyp.R;

import java.util.Objects;

public abstract class LazyFragment extends Fragment {

    protected FragmentActivity mActivity;

    protected Context mContext;

    protected ViewModelProvider.AndroidViewModelFactory mFactory;

    private boolean isFirstLoad = true;

    protected int statusBarColor = R.color.app_statusBarColor;

    protected int navigationBarColor = R.color.app_navigationBarColor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = mActivity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity != null) {
            setStatusBarColor(Objects.requireNonNull(mActivity));
            Application application = mActivity.getApplication();
            if (application != null) {
                mFactory = new ViewModelProvider.AndroidViewModelFactory(application);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(getContentViewId(), null);
        initView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            reSetStatusBarColor(Objects.requireNonNull(mActivity));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            initObject();
            initData();
            initEvent();
            isFirstLoad = false;
        }
    }

    protected abstract int getContentViewId();

    protected void initView(View view) {

    }

    protected void initObject() {

    }

    protected void initData() {

    }

    protected void initEvent() {

    }

    protected void setStatusBarColor(Activity activity) {
        Window activityWindow = activity.getWindow();
        activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activityWindow.setStatusBarColor(ContextCompat.getColor(activity, statusBarColor));
        activityWindow.setNavigationBarColor(ContextCompat.getColor(activity, navigationBarColor));
    }

    protected void reSetStatusBarColor(Activity activity) {
        Window activityWindow = activity.getWindow();
        activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activityWindow.setStatusBarColor(ContextCompat.getColor(activity, R.color.video_clips_color));
        activityWindow.setNavigationBarColor(ContextCompat.getColor(activity, R.color.home_color_FF181818));
    }
}
