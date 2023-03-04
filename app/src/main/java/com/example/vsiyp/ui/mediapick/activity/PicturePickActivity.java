package com.example.vsiyp.ui.mediapick.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseActivity;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.decoration.GridItemDividerDecoration;
import com.example.vsiyp.ui.mediapick.adapter.PicturePickAdapter;
import com.example.vsiyp.ui.mediapick.viewmodel.PickPictureViewModel;
import com.huawei.secure.android.common.intent.SafeIntent;

public class PicturePickActivity extends BaseActivity {

    private ImageView mCloseIcon;

    private RecyclerView mRecyclerView;

    private PicturePickAdapter mMediaAdapter;

    private PickPictureViewModel mMediaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_pick);

        SafeIntent safeIntent = new SafeIntent(getIntent());
        initView();
        initObject();
        initData();
        initEvent();
    }

    private void initView() {
        mCloseIcon = findViewById(R.id.iv_close);
        mRecyclerView = findViewById(R.id.choice_recyclerview);
    }

    private void initObject() {
        mMediaViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) factory).get(PickPictureViewModel.class);
        mRecyclerView.setHasFixedSize(true);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(itemAnimator);

        mMediaAdapter = new PicturePickAdapter(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        if (mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new GridItemDividerDecoration(SizeUtils.dp2Px(this, 14.5f),
                SizeUtils.dp2Px(this, 14.5f), ContextCompat.getColor(this, R.color.black)));
        }
        mRecyclerView.setAdapter(mMediaAdapter);
    }

    private void initData() {
        mMediaViewModel.getPageData().observe(this, pagedList -> {
            if (pagedList.size() > 0) {
                mMediaAdapter.submitList(pagedList);
            }
        });
    }

    private void initEvent() {
        mCloseIcon.setOnClickListener(new OnClickRepeatedListener(v -> finish()));

        mMediaAdapter.setOnItemClickListener(position -> {
            PagedList<MediaData> mediaDataList = mMediaAdapter.getCurrentList();
            if (mediaDataList != null && mediaDataList.size() > position) {
                MediaData mediaData = mediaDataList.get(position);

                    Intent intent = new Intent();
                    if (mediaData != null && mediaData.getPath() != null) {
                        intent.putExtra(Constant.EXTRA_SELECT_RESULT, mediaData.getPath());
                        setResult(Constant.RESULT_CODE, intent);
                        finish();

                }
            }
        });
    }
}
