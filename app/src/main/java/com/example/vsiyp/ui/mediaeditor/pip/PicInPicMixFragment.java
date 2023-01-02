package com.example.vsiyp.ui.mediaeditor.pip;

import static com.example.vsiyp.ui.common.bean.Constant.LTR_UI;
import static com.example.vsiyp.ui.common.bean.Constant.RTL_UI;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.utils.BigDecimalUtils;
import com.example.vsiyp.ui.common.utils.ScreenUtil;
import com.example.vsiyp.ui.mediaeditor.preview.view.MySeekBar;
import com.example.vsiyp.ui.mediaeditor.trackview.viewmodel.EditPreviewViewModel;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVisibleAsset;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PicInPicMixFragment extends BaseFragment {

    private TextView mTitleTv;

    private ImageView mCertainTv;

    private MySeekBar mSeekBar;

    private RecyclerView blendList;

    private List<BlendAdapter.BlendItem> blendItemList = new ArrayList<>();

    private Map<BlendAdapter.BlendItem, Integer> blendItem = new LinkedHashMap<>();

    private BlendAdapter adapter;

    private PictureInPicViewModel mPictureInPicViewModel;

    private EditPreviewViewModel mEditPreviewViewModel;

    private HVEVisibleAsset hveVisibleAsset;

    private HuaweiVideoEditor mEditor;

    @Override
    protected void initViewModelObserve() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.pip_blend;
    }

    @Override
    protected void initView(View view) {
        mEditPreviewViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(EditPreviewViewModel.class);
        mPictureInPicViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(PictureInPicViewModel.class);
        mTitleTv = view.findViewById(R.id.tv_title);
        mCertainTv = view.findViewById(R.id.iv_certain);
        mSeekBar = view.findViewById(R.id.sb_items);
        if (ScreenUtil.isRTL()) {
            mSeekBar.setScaleX(RTL_UI);
        } else {
            mSeekBar.setScaleX(LTR_UI);
        }
        blendList = view.findViewById(R.id.blend_list);
    }

    @Override
    protected void initObject() {
        HVEAsset asset = mEditPreviewViewModel.getSelectedAsset();
        mEditor = mEditPreviewViewModel.getEditor();
        if (mEditor != null) {
            mPictureInPicViewModel.setEditor(mEditor);
        }
        if (asset instanceof HVEVisibleAsset) {
            hveVisibleAsset = (HVEVisibleAsset) asset;
        }
        mTitleTv.setText(R.string.blend);
        mSeekBar.setMinProgress(0);
        mSeekBar.setMaxProgress(100);
        mSeekBar.setAnchorProgress(0);
    }

    @Override
    protected void initData() {
        blendItem.clear();
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend_normall, getString(R.string.mix_mode1)), 0);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend1, getString(R.string.mix_mode2)), 2);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend2, getString(R.string.mix_mode3)), 4);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend3, getString(R.string.mix_mode4)), 5);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend4, getString(R.string.mix_mode5)), 3);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend5, getString(R.string.mix_mode6)), 1);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend6, getString(R.string.mix_mode7)), 7);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend7, getString(R.string.mix_mode8)), 6);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend8, getString(R.string.mix_mode9)), 10);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend9, getString(R.string.mix_mode10)), 9);
        blendItem.put(new BlendAdapter.BlendItem(R.drawable.icon_blend10, getString(R.string.mix_mode11)), 8);
        blendItemList = new ArrayList<>(blendItem.keySet());
        blendList.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter = new BlendAdapter(blendItemList, mActivity);
        if (hveVisibleAsset != null) {
            adapter.setIsSelect(hveVisibleAsset.getBlendMode());
            int process = (int) (BigDecimalUtils.round(BigDecimalUtils.mul(hveVisibleAsset.getOpacityValue(), 100), 0));
            mSeekBar.setProgress(process);
        }
        blendList.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        mCertainTv.setOnClickListener(view -> {
            mActivity.onBackPressed();
        });
        adapter.setSelectedListener(position -> {
            mPictureInPicViewModel.setBlendMode(hveVisibleAsset, blendItem.get(blendItemList.get(position)));
        });
        mSeekBar.setOnProgressChangedListener(progress -> {
            mPictureInPicViewModel.setOpacityValue(hveVisibleAsset, progress);
        });
    }

    @Override
    protected int setViewLayoutEvent() {
        return FIXED_HEIGHT_210;
    }
}

