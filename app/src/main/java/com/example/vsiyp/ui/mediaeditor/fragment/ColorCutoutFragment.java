package com.example.vsiyp.ui.mediaeditor.fragment;

import static com.example.vsiyp.ui.common.bean.Constant.LTR_UI;
import static com.example.vsiyp.ui.common.bean.Constant.RTL_UI;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.adapter.ColorCutAdapter;
import com.example.vsiyp.ui.common.adapter.SelectAdapter;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.ScreenUtil;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.decoration.HorizontalDividerDecoration;
import com.example.vsiyp.ui.mediaeditor.materialedit.MaterialEditViewModel;
import com.example.vsiyp.ui.mediaeditor.preview.ColorCutViewModel;
import com.example.vsiyp.ui.mediaeditor.preview.view.MySeekBar;
import com.example.vsiyp.ui.mediaeditor.trackview.viewmodel.EditPreviewViewModel;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;

import java.util.ArrayList;
import java.util.List;

public class ColorCutoutFragment extends BaseFragment {
    private View mRootView;

    private RecyclerView mRecyclerView;

    private TextView mTvReversing;

    private TextView mTvIntensity;

    private ColorCutAdapter mColorCutAdapter;

    private ColorCutViewModel mColorCutViewModel;

    private MaterialEditViewModel mMaterialEditViewModel;

    protected EditPreviewViewModel mEditPreviewViewModel;

    private MySeekBar mMySeekBar;

    private HVEAsset mHVEAsset;

    private static final int FIXED_HEIGHT_210 = 1;

    @Override
    protected void initViewModelObserve() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        navigationBarColor = R.color.color_20;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_color_cutout;
    }

    @Override
    protected void initView(View view) {
        this.mRootView = view;
        mRecyclerView = mRootView.findViewById(R.id.recycler_view_color_cut);
        mMySeekBar = mRootView.findViewById(R.id.myseekbar);
        if (ScreenUtil.isRTL()) {
            mMySeekBar.setScaleX(RTL_UI);
        } else {
            mMySeekBar.setScaleX(LTR_UI);
        }
        mTvReversing = mRootView.findViewById(R.id.tv_reset);
        mTvIntensity = mRootView.findViewById(R.id.tv_intensity);
        TextView tvTitle = mRootView.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.edit_item2_17);
    }

    @Override
    protected void initObject() {
        mEditPreviewViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(EditPreviewViewModel.class);
        mColorCutViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(ColorCutViewModel.class);
        mMaterialEditViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(MaterialEditViewModel.class);
    }

    @Override
    protected void initData() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(R.drawable.ico_quse, getString(R.string.color_picker)));
        itemList.add(new Item(R.drawable.ico_qiangdu_select, getString(R.string.strength)));
        mColorCutAdapter = new ColorCutAdapter(mActivity, itemList, mColorCutViewModel);
        mRecyclerView
                .addItemDecoration(new HorizontalDividerDecoration(ContextCompat.getColor(mActivity, R.color.color_fff_10),
                        SizeUtils.dp2Px(mActivity, 56), SizeUtils.dp2Px(mActivity, 1), true));
        mRecyclerView.setAdapter(mColorCutAdapter);
    }

    @Override
    protected void initEvent() {
        mColorCutAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object k, int position) {
                switch (position) {
                    case 0:
                        mColorCutViewModel.setIsShow(true);
                        mTvIntensity.setVisibility(View.INVISIBLE);
                        mMySeekBar.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        mColorCutViewModel.setIsShow(false);
                        mTvIntensity.setVisibility(View.VISIBLE);
                        mMySeekBar.setVisibility(View.VISIBLE);
                        mMySeekBar.setProgress(mColorCutViewModel.getStrength());
                        break;
                    case 2:
                        mColorCutViewModel.setIsShow(false);
                        mTvIntensity.setVisibility(View.VISIBLE);
                        mMySeekBar.setVisibility(View.VISIBLE);
                        mMySeekBar.setProgress(mColorCutViewModel.getShadow());
                        break;
                    default:
                        break;
                }
            }
        });

        mTvReversing.setOnClickListener(new OnClickRepeatedListener(v -> {
            mMySeekBar.setProgress(0);
            mColorCutViewModel.setEffectStrength(0);
            mColorCutViewModel.setShadow(0);
            mColorCutViewModel.setMove(false);
            mColorCutViewModel.setAsset(mHVEAsset);
        }));

        mRootView.findViewById(R.id.iv_certain)
                .setOnClickListener(new OnClickRepeatedListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.onBackPressed();
                        }
                    }
                }));
        mMySeekBar.setOnProgressChangedListener(progress -> {
            mEditPreviewViewModel.setToastTime(String.valueOf(progress));
            if (mColorCutAdapter.getPosition() == 1) {
                mColorCutViewModel.setEffectStrength(progress);
            } else if (mColorCutAdapter.getPosition() == 2) {
                mColorCutViewModel.setShadow(progress);
            }
        });
        mMySeekBar.setcTouchListener(
                isTouch -> mEditPreviewViewModel.setToastTime(isTouch ? String.valueOf(mMySeekBar.getProgress()) : ""));
    }

    @Override
    protected int setViewLayoutEvent() {
        return FIXED_HEIGHT_210;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMaterialEditViewModel.setMaterialEditShow(false);
        mHVEAsset = mEditPreviewViewModel.getSelectedAsset();
        if (mHVEAsset == null) {
            mHVEAsset = mEditPreviewViewModel.getMainLaneAsset();
        }
        if (mHVEAsset != null) {
            mColorCutViewModel.setAsset(mHVEAsset);
            mColorCutViewModel.setIsShow(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mColorCutViewModel.setIsShow(false);
        mMaterialEditViewModel.setMaterialEditShow(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class Item {
        @DrawableRes
        public int icoId;

        public String name;

        public Item(int icoId, String name) {
            this.icoId = icoId;
            this.name = name;
        }
    }
}

