package com.example.vsiyp.ui.mediaeditor.split;

import static com.example.vsiyp.ui.common.bean.Constant.LTR_UI;
import static com.example.vsiyp.ui.common.bean.Constant.RTL_UI;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.ScreenUtil;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.mediaeditor.cover.CoverAdapter;
import com.example.vsiyp.ui.mediaeditor.cover.CoverTrackView;
import com.example.vsiyp.ui.mediaeditor.materialedit.MaterialEditViewModel;
import com.example.vsiyp.ui.mediaeditor.menu.MenuClickManager;
import com.example.vsiyp.ui.mediaeditor.menu.MenuViewModel;
import com.example.vsiyp.ui.mediaeditor.menu.VideoClipsPlayViewModel;
import com.example.vsiyp.ui.mediaeditor.trackview.viewmodel.EditPreviewViewModel;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.ArrayList;
import java.util.List;

public class AssetSplitFragment extends BaseFragment {
    public static final String TAG = "AssetSplitFragment";

    private RecyclerView recyclerView;

    private CoverAdapter coverAdapter;

    private List<HVEAsset> mAssetList;

    private EditPreviewViewModel mEditPreviewViewModel;

    private MaterialEditViewModel mMaterialEditViewModel;

    private VideoClipsPlayViewModel mSdkPlayViewModel;

    private MenuViewModel mMenuViewModel;

    private HVEAsset selectedAsset;

    private TextView tvTitle;

    private ImageView ivCertain;

    private int mRvScrollX = 0;

    private int mItemWidth;

    private long videoCoverTime;

    private long durationTime;

    public static AssetSplitFragment newInstance(int operateId) {
        Bundle args = new Bundle();
        args.putInt("operateId", operateId);
        AssetSplitFragment fragment = new AssetSplitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_asset_split;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_split);
        ivCertain = view.findViewById(R.id.iv_certain);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.cut_second_menu_severing);
        tvTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.clip_color_E6FFFFFF));
    }

    @Override
    protected void initObject() {
        mItemWidth = SizeUtils.dp2Px(mActivity, 64);
        mEditPreviewViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(EditPreviewViewModel.class);
        mMaterialEditViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(MaterialEditViewModel.class);
        mSdkPlayViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(VideoClipsPlayViewModel.class);
        mMenuViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(MenuViewModel.class);
        resetView();
    }

    @Override
    protected void initData() {
        mAssetList = new ArrayList<>();
        coverAdapter = new CoverAdapter(context, mAssetList, R.layout.adapter_cover_item2);
        if (ScreenUtil.isRTL()) {
            recyclerView.setScaleX(RTL_UI);
        } else {
            recyclerView.setScaleX(LTR_UI);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(coverAdapter);
        recyclerView.setItemAnimator(null);
        View header = new View(context);
        View foot = new View(context);
        header.setLayoutParams(
                new ViewGroup.LayoutParams(SizeUtils.screenWidth(context) / 2, SizeUtils.dp2Px(context, 64)));
        foot.setLayoutParams(
                new ViewGroup.LayoutParams(SizeUtils.screenWidth(context) / 2, SizeUtils.dp2Px(context, 64)));
        coverAdapter.addHeaderView(header);
        coverAdapter.addFooterView(foot);

        selectedAsset = mEditPreviewViewModel.getMainLaneAsset();

        if (selectedAsset == null) {
            SmartLog.e(TAG, "SelectedAsset is null!");
            return;
        }
        durationTime = selectedAsset.getDuration();
        getBitmapList(selectedAsset);

        mSdkPlayViewModel.getPlayState().observe(this, aBoolean -> {
            if (aBoolean) {
                mMaterialEditViewModel.clearMaterialEditData();
            }
        });
    }

    private void getBitmapList(HVEAsset asset) {
        if (mAssetList != null) {
            mAssetList.clear();
            mAssetList.add(asset);
        }
        if (coverAdapter != null) {
            coverAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initEvent() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView view, int newState) {
                super.onScrollStateChanged(view, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mMaterialEditViewModel.clearMaterialEditData();
                mRvScrollX += dx;
                updateTimeLine();
            }
        });

        ivCertain.setOnClickListener(new OnClickRepeatedListener(v -> {
            mMenuViewModel.pauseTimeLine();
            mMenuViewModel.splitAsset();

            onBackPressed();
            MenuClickManager.getInstance().popView();
        }));
    }

    private void updateTimeLine() {
        if (mActivity != null && selectedAsset != null) {
            float totalWidth = durationTime / 1000f * mItemWidth;
            float percent = mRvScrollX / totalWidth;
            videoCoverTime = (long) (percent * durationTime);
            if (videoCoverTime > durationTime) {
                videoCoverTime = durationTime;
            }
            if (selectedAsset.getLaneIndex() == 0) {
                viewModel.setCurrentTimeLine(selectedAsset.getStartTime() + videoCoverTime);
            }
            notifyCurrentTimeChange(selectedAsset.getStartTime() + videoCoverTime);
        }
    }

    @Override
    protected int setViewLayoutEvent() {
        return DYNAMIC_HEIGHT;
    }

    private void notifyCurrentTimeChange(long time) {
        if (recyclerView != null) {
            for (int j = 0; j < recyclerView.getChildCount(); j++) {
                if (recyclerView.getChildAt(j) instanceof CoverTrackView) {
                    ((CoverTrackView) recyclerView.getChildAt(j)).handleCurrentTimeChange(time);
                }
            }
        }
    }

    @Override
    protected void initViewModelObserve() {

    }

    private void resetView() {
        if (mEditPreviewViewModel == null) {
            return;
        }
        mEditPreviewViewModel.updateTimeLine();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mEditPreviewViewModel == null) {
            return;
        }
        mMaterialEditViewModel.clearMaterialEditData();
    }
}

