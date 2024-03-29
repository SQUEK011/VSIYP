package com.example.vsiyp.ui.mediapick.fragment;

import static com.example.vsiyp.ui.mediapick.fragment.GalleryFragment.SHOW_MEDIA_TYPE;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.paging.DataSource;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.LazyFragment;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.decoration.GridItemDividerDecoration;
import com.example.vsiyp.ui.mediapick.activity.MediaPickActivity;
import com.example.vsiyp.ui.mediapick.adapter.MediaPickAdapter;
import com.example.vsiyp.ui.mediapick.manager.MediaPickManager;
import com.example.vsiyp.ui.mediapick.viewmodel.MediaFolderViewModel;
import com.example.vsiyp.ui.mediapick.viewmodel.PickPictureViewModel;
import com.huawei.secure.android.common.intent.SafeBundle;

import java.util.List;

public class PickPictureFragment extends LazyFragment implements MediaPickManager.OnSelectItemChangeListener {

    private RecyclerView mPictureRecyclerView;

    private MediaPickAdapter mMediaAdapter;

    private PickPictureViewModel mMediaPictureViewModel;

    private MediaFolderViewModel mMediaFolderViewModel;

    private String mFolderPath = "";

    private int mShowMediaType = 2; // 0 video 1 photo 2 both

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        navigationBarColor = R.color.home_color_FF181818;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_pick_video;
    }

    @Override
    protected void initView(View view) {
        mPictureRecyclerView = view.findViewById(R.id.choice_recyclerview);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMediaAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initObject() {
        mMediaPictureViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) mFactory).get(PickPictureViewModel.class);
        mMediaFolderViewModel = new ViewModelProvider(mActivity, (ViewModelProvider.Factory) mFactory).get(MediaFolderViewModel.class);
        mPictureRecyclerView.setHasFixedSize(true);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setSupportsChangeAnimations(false);
        mPictureRecyclerView.setItemAnimator(defaultItemAnimator);

        SafeBundle safeBundle = new SafeBundle(getArguments());
        mShowMediaType = safeBundle.getInt(SHOW_MEDIA_TYPE, mShowMediaType);
        List<MediaData> mInitMediaList = safeBundle.getParcelableArrayList(Constant.EXTRA_SELECT_RESULT);
        int actionType = safeBundle.getInt(MediaPickActivity.ACTION_TYPE);
        mMediaAdapter = new MediaPickAdapter(mActivity, actionType);
        if (mInitMediaList != null) {
            mMediaAdapter.setInitMediaList(mInitMediaList);
        }
        mMediaAdapter.setShowMediaType(mShowMediaType);
        mPictureRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        if (mPictureRecyclerView.getItemDecorationCount() == 0) {
            mPictureRecyclerView.addItemDecoration(new GridItemDividerDecoration(SizeUtils.dp2Px(mActivity, 8),
                SizeUtils.dp2Px(mActivity, 8), ContextCompat.getColor(mActivity, R.color.black)));
        }
        mPictureRecyclerView.setAdapter(mMediaAdapter);
    }

    @Override
    protected void initData() {
        mMediaPictureViewModel.getPageData().observe(this, pagedList -> {
            mMediaAdapter.submitList(pagedList);
        });

        mMediaFolderViewModel.getFolderSelect().observe(this, mediaFolder -> {
            if (mFolderPath.equals(mediaFolder.getDirPath())) {
                return;
            }
            mFolderPath = mediaFolder.getDirPath();
            MutableLiveData<Boolean> mutableLiveData = mMediaFolderViewModel.getGalleryVideoSelect();
            Boolean mutableLiveDataValue = mutableLiveData.getValue();
            if (mutableLiveDataValue != null && mMediaPictureViewModel.getDataSource() != null
                && !mutableLiveDataValue) {
                mMediaPictureViewModel.setDirPathName(mFolderPath);
                mMediaPictureViewModel.getDataSource().invalidate();
            }
        });

        mMediaFolderViewModel.getRotationState().observe(this, state -> {
            if (state == mMediaPictureViewModel.getRotationState()) {
                return;
            }
            mMediaPictureViewModel.setRotationState(state);
            DataSource dataSource = mMediaPictureViewModel.getDataSource();
            if (dataSource != null) {
                dataSource.invalidate();
            }

        });
    }

    @Override
    protected void initEvent() {
        MediaPickManager.getInstance().addOnSelectItemChangeListener(this);
    }

    @Override
    public void onSelectItemChange(MediaData item) {
        PagedList<MediaData> mediaDataList = mMediaAdapter.getCurrentList();
        if (mediaDataList == null || item == null) {
            return;
        }
        int position = mediaDataList.indexOf(item);
        mMediaAdapter.notifyItemChanged(position);
    }
}
