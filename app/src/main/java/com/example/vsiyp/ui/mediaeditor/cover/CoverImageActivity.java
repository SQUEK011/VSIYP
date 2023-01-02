package com.example.vsiyp.ui.mediaeditor.cover;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseActivity;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.view.navigator.FixFragmentNavigator;
import com.huawei.secure.android.common.intent.SafeIntent;

public class CoverImageActivity extends BaseActivity {

    public static final String WIDTH = "width";

    public static final String HEIGHT = "height";

    public static final String PROJECT_ID = "projectId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_image);
        initView();
    }

    private void initView() {
        SafeIntent intent = new SafeIntent(getIntent());
        float videoWidth = intent.getFloatExtra(WIDTH, 720);
        float videoHeight = intent.getFloatExtra(HEIGHT, 1080);
        String projectId = intent.getStringExtra(PROJECT_ID);
        Bundle bundle = new Bundle();
        bundle.putFloat(WIDTH, videoWidth);
        bundle.putFloat(HEIGHT, videoHeight);
        bundle.putString(PROJECT_ID, projectId);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_cover_image);
        if (fragment != null) {
            NavController navController = NavHostFragment.findNavController(fragment);
            NavigatorProvider provider = navController.getNavigatorProvider();
            FixFragmentNavigator fragmentNavigator =
                    new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
            provider.addNavigator(fragmentNavigator);
            navController.setGraph(R.navigation.nav_graph_cover_image, bundle);
        }
    }

    public void onResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_SELECT_RESULT, path);
        setResult(Constant.RESULT_CODE, intent);
        finish();
    }
}

