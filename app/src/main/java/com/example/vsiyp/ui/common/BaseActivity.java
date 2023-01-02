package com.example.vsiyp.ui.common;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.utils.ActivityContainer;

public class BaseActivity extends AppCompatActivity {
    protected ViewModelProvider.AndroidViewModelFactory factory;

    protected int statusBarColor = R.color.app_statusBarColor;

    protected int navigationBarColor = R.color.app_navigationBarColor;

    protected boolean isOpenStatusBarInner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (isOpenStatusBarInner) {
            fitSystemBar(this);
        } else {
            setStatusBarColor(this);
        }
        factory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ActivityContainer.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reSetStatusBarColor(this);
        ActivityContainer.getInstance().removeActivity(this);
    }

    /**
     * Set status bar's color.
     *
     * @param activity Activity of page.
     */
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

    protected void fitSystemBar(Activity activity) {
        Window activityWindow = activity.getWindow();
        activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activityWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activityWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        activityWindow.getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        activityWindow.setStatusBarColor(ContextCompat.getColor(activity, statusBarColor));
        activityWindow.setNavigationBarColor(ContextCompat.getColor(activity, navigationBarColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams layoutParams = activityWindow.getAttributes();
            layoutParams.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activityWindow.setAttributes(layoutParams);
        }
    }

    protected boolean isValidActivity() {
        return !isFinishing() && !isDestroyed();
    }
}
