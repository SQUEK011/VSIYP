package com.example.vsiyp.ui.mediaeditor.cover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.decoration.GridItemDividerDecoration;
import com.huawei.secure.android.common.intent.SafeBundle;
import com.huawei.secure.android.common.intent.SafeIntent;

public class CoverImageHomeFragment extends BaseFragment {

    private ImageView mBackIv;

    private RecyclerView mRecyclerView;

    private CoverImageAdapter mCoverImageAdapter;

    private CoverPickPictureViewModel mMediaViewModel;

    private NavController mNavController;

    private float mVideoWidth;

    private float mVideoHeight;

    private String mProjectId;

    @Override
    protected void initViewModelObserve() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_cover_image_home;
    }

    @Override
    protected void initView(View view) {
        mBackIv = view.findViewById(R.id.iv_back);
        mRecyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    protected void initObject() {
        SafeBundle bundle = new SafeBundle(getArguments());
        mVideoWidth = bundle.getFloat(CoverImageActivity.WIDTH, 720);
        mVideoHeight = bundle.getFloat(CoverImageActivity.HEIGHT, 1080);
        mProjectId = bundle.getString(CoverImageActivity.PROJECT_ID);
        mNavController = Navigation.findNavController(mActivity, R.id.nav_host_fragment_cover_image);
        mMediaViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(CoverPickPictureViewModel.class);
        mRecyclerView.setHasFixedSize(true);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(itemAnimator);
        mCoverImageAdapter = new CoverImageAdapter(mActivity);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        if (mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new GridItemDividerDecoration(SizeUtils.dp2Px(mActivity, 8),
                    SizeUtils.dp2Px(mActivity, 8), ContextCompat.getColor(mActivity, R.color.transparent)));
        }
        mRecyclerView.setAdapter(mCoverImageAdapter);
    }

    @Override
    protected void initData() {
        mMediaViewModel.getPageData().observe(this, pagedList -> {
            if (pagedList.size() > 0) {
                mCoverImageAdapter.submitList(pagedList);
            }
        });
    }

    @Override
    protected void initEvent() {
        mActivity.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mNavController == null) {
                    return;
                }
                NavDestination currentDestination = mNavController.getCurrentDestination();
                if (currentDestination == null) {
                    return;
                }
                if (currentDestination.getId() == R.id.coverImageHomeFragment) {
                    mActivity.finish();
                }
            }
        });

        mBackIv.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mNavController == null) {
                return;
            }
            NavDestination currentDestination = mNavController.getCurrentDestination();
            if (currentDestination == null) {
                return;
            }
            if (currentDestination.getId() == R.id.coverImageHomeFragment) {
                mActivity.finish();
            }
        }));

        mCoverImageAdapter.setOnItemClickListener(position -> {
            if (mCoverImageAdapter == null) {
                return;
            }
            PagedList<MediaData> currentList = mCoverImageAdapter.getCurrentList();
            if (currentList == null) {
                return;
            }
            if (position >= 0 && position < currentList.size()) {
                PagedList<MediaData> pagedList = mCoverImageAdapter.getCurrentList();
                MediaData mediaData = null;
                if (pagedList != null) {
                    mediaData = pagedList.get(position);
                }
                SafeIntent safeIntent = new SafeIntent(mActivity.getIntent());
                int sticker = safeIntent.getIntExtra("sticker", 0);
                if (sticker == 1009) {
                    if (mediaData != null) {
                        Intent intent = new Intent();
                        intent.putExtra(Constant.EXTRA_SELECT_RESULT, mediaData.getPath());
                        mActivity.setResult(Constant.RESULT_CODE, intent);
                        mActivity.finish();
                    }
                } else {
                    if (mediaData != null && mNavController != null) {
                        NavDestination currentDestination = mNavController.getCurrentDestination();
                        if (currentDestination == null) {
                            return;
                        }
                        if (currentDestination.getId() == R.id.coverImageHomeFragment) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constant.EXTRA_SELECT_RESULT, mediaData);
                            bundle.putFloat(CoverImageActivity.WIDTH, mVideoWidth);
                            bundle.putFloat(CoverImageActivity.HEIGHT, mVideoHeight);
                            bundle.putString(CoverImageActivity.PROJECT_ID, mProjectId);
                            mNavController.navigate(R.id.action_HomeToEditFragment, bundle);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected int setViewLayoutEvent() {
        return NOMERA_HEIGHT;
    }
}

