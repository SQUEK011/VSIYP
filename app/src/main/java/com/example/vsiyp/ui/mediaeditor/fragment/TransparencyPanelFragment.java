package com.example.vsiyp.ui.mediaeditor.fragment;

import static com.example.vsiyp.ui.common.bean.Constant.LTR_UI;
import static com.example.vsiyp.ui.common.bean.Constant.RTL_UI;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.utils.BigDecimalUtils;
import com.example.vsiyp.ui.common.utils.ScreenUtil;
import com.example.vsiyp.ui.mediaeditor.preview.view.MySeekBar;
import com.example.vsiyp.ui.mediaeditor.trackview.viewmodel.EditPreviewViewModel;
import com.huawei.hms.videoeditor.sdk.HVETimeLine;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEImageAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVideoAsset;

public class TransparencyPanelFragment extends BaseFragment {
    private TextView mTitleTv;

    private ImageView mCertainTv;

    private CheckBox other;

    private MySeekBar mSeekBar;

    private HuaweiVideoEditor mEditor;

    protected EditPreviewViewModel mEditPreviewViewModel;

    private int mProgress = 0;

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
    protected int getContentViewId() {
        return R.layout.fragment_panel_transparency;
    }

    @Override
    public void initView(View view) {
        mTitleTv = view.findViewById(R.id.tv_title);
        mCertainTv = view.findViewById(R.id.iv_certain);
        mSeekBar = view.findViewById(R.id.sb_items);
        if (ScreenUtil.isRTL()) {
            mSeekBar.setScaleX(RTL_UI);
        } else {
            mSeekBar.setScaleX(LTR_UI);
        }
        other = view.findViewById(R.id.cb_apply);
    }

    @Override
    protected void initObject() {
        mTitleTv.setText(R.string.cut_second_menu_opaqueness);
        mEditPreviewViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(EditPreviewViewModel.class);
        mEditor = mEditPreviewViewModel.getEditor();
        other.setVisibility(View.GONE);
        mSeekBar.setMinProgress(0);
        mSeekBar.setMaxProgress(100);
        mSeekBar.setAnchorProgress(0);
    }

    @Override
    protected void initData() {
        mProgress = (int) (getTransparency() * 100);
        mSeekBar.setProgress(mProgress);
    }

    @Override
    protected void initEvent() {
        HuaweiVideoEditor editor = mEditPreviewViewModel.getEditor();
        if (editor == null) {
            return;
        }

        mSeekBar.setOnProgressChangedListener(progress -> {
            mProgress = progress;
            mEditPreviewViewModel.setToastTime(String.valueOf(mProgress));
            setTransparency((float) BigDecimalUtils.round(BigDecimalUtils.div(mProgress, 100f), 2));
        });

        mSeekBar.setcTouchListener(isTouch -> {
            mEditPreviewViewModel.setToastTime(isTouch ? (int) mSeekBar.getProgress() + "" : "");
        });

        mCertainTv.setOnClickListener(view -> {
            mActivity.onBackPressed();
        });
    }

    @Override
    protected int setViewLayoutEvent() {
        return FIXED_HEIGHT_210;
    }

    private float getTransparency() {
        HVEAsset asset = mEditPreviewViewModel.getSelectedAsset();
        if (asset == null) {
            asset = mEditPreviewViewModel.getMainLaneAsset();
            if (asset == null) {
                return 1f;
            }
        }
        if (asset instanceof HVEVideoAsset) {
            return ((HVEVideoAsset) asset).getOpacityValue();
        } else if (asset instanceof HVEImageAsset) {
            return ((HVEImageAsset) asset).getOpacityValue();
        }
        return 1f;
    }

    private void setTransparency(float value) {
        HVEAsset asset = mEditPreviewViewModel.getSelectedAsset();
        if (asset == null || mEditor == null) {
            asset = mEditPreviewViewModel.getMainLaneAsset();
            if (asset == null) {
                return;
            }
        }
        if (asset instanceof HVEVideoAsset) {
            ((HVEVideoAsset) asset).setOpacityValue(value);
        } else if (asset instanceof HVEImageAsset) {
            ((HVEImageAsset) asset).setOpacityValue(value);
        }
        if (mEditor != null) {
            HVETimeLine timeLine = mEditor.getTimeLine();
            if (timeLine != null) {
                mEditor.seekTimeLine(timeLine.getCurrentTime());
            }
        }
    }

}

