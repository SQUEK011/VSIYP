package com.example.vsiyp.ui.mediapick.fragment;

import static com.example.vsiyp.ui.mediapick.activity.MediaPickActivity.DURATION;
import static com.example.vsiyp.ui.mediapick.fragment.GalleryFragment.SHOW_MEDIA_TYPE;

import java.util.List;

import android.os.Bundle;
import android.view.View;


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
import com.example.vsiyp.ui.mediapick.viewmodel.PickVideoViewModel;
import com.huawei.secure.android.common.intent.SafeBundle;


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

public class PickVideoFragment extends LazyFragment implements MediaPickManager.OnSelectItemChangeListener {

    private RecyclerView mRecyclerView;

    private MediaPickAdapter mMediaAdapter;

    private PickVideoViewModel mPickVideoViewModel;

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
        mRecyclerView = view.findViewById(R.id.choice_recyclerview);
    }

    @Override
    protected void initObject() {
        mPickVideoViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) mFactory).get(PickVideoViewModel.class);
        mMediaFolderViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(MediaFolderViewModel.class);
        mRecyclerView.setHasFixedSize(true);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(itemAnimator);
        SafeBundle safeBundle = new SafeBundle(getArguments());
        long mCheckDuration = safeBundle.getLong(DURATION, 0);
        int actionType = safeBundle.getInt(MediaPickActivity.ACTION_TYPE);
        mShowMediaType = safeBundle.getInt(SHOW_MEDIA_TYPE, mShowMediaType);
        mMediaAdapter = new MediaPickAdapter(mActivity, actionType);
        mMediaAdapter.setReplaceValidDuration(mCheckDuration);
        List<MediaData> mInitMediaList = safeBundle.getParcelableArrayList(Constant.EXTRA_SELECT_RESULT);
        if (mInitMediaList != null) {
            mMediaAdapter.setInitMediaList(mInitMediaList);
        }
        mMediaAdapter.setShowMediaType(mShowMediaType);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        if (mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new GridItemDividerDecoration(SizeUtils.dp2Px(mActivity, 8f),
                SizeUtils.dp2Px(mActivity, 8f), ContextCompat.getColor(mActivity, R.color.transparent)));
        }
        mRecyclerView.setAdapter(mMediaAdapter);
    }

    @Override
    protected void initData() {

        mPickVideoViewModel.getaPageData().observe(this, pagedList -> {
            mMediaAdapter.submitList(pagedList);
        });

        mMediaFolderViewModel.getFolderSelect().observe(this, mediaFolder -> {
            if (mFolderPath.equals(mediaFolder.getDirPath())) {
                return;
            }
            mFolderPath = mediaFolder.getDirPath();
            MutableLiveData<Boolean> mutableLiveData = mMediaFolderViewModel.getGalleryVideoSelect();
            Boolean mutableLiveDataValue = mutableLiveData.getValue();
            if (mutableLiveDataValue != null && mutableLiveDataValue) {
                mPickVideoViewModel.setaDirName(mediaFolder.getDirPath());
                DataSource dataSource = mPickVideoViewModel.getDataSource();
                if (dataSource != null) {
                    dataSource.invalidate();
                }
            }
        });

        mMediaFolderViewModel.getRotationState().observe(this, state -> {
            if (state == mPickVideoViewModel.getRotationState()) {
                return;
            }
            mPickVideoViewModel.setRotationState(state);
            DataSource dataSource = mPickVideoViewModel.getDataSource();
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
        for (int i = 0; i < mediaDataList.size(); i++) {
            MediaData media = mediaDataList.get(i);
            if (media == null) {
                continue;
            }
            if (media.getName().equals(item.getName())) {
                mMediaAdapter.notifyItemChanged(i);
                break;
            }
        }
    }
}
