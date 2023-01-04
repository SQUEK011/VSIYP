
/*
 *   Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package com.example.vsiyp.ui.mediapick.activity;

import static com.example.vsiyp.ui.mediaeditor.blockface.FaceBlockingFragment.IS_FROM_FACE_BLOCKING;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseActivity;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.decoration.GridItemDividerDecoration;
import com.example.vsiyp.ui.mediaeditor.blockface.cropimage.CropImageActivity;
import com.example.vsiyp.ui.mediapick.adapter.PicturePickAdapter;
import com.example.vsiyp.ui.mediapick.viewmodel.PickPictureViewModel;
import com.huawei.secure.android.common.intent.SafeIntent;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class PicturePickActivity extends BaseActivity {

    private ImageView mCloseIcon;

    private RecyclerView mRecyclerView;

    private PicturePickAdapter mMediaAdapter;

    private PickPictureViewModel mMediaViewModel;

    private boolean isFromFaceBlocking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_pick);

        SafeIntent safeIntent = new SafeIntent(getIntent());
        isFromFaceBlocking = safeIntent.getBooleanExtra(IS_FROM_FACE_BLOCKING, false);
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
        mMediaViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) factory).get(PickPictureViewModel.class);
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
                if (isFromFaceBlocking) {
                    CropImageActivity.startActivityForResult(this, mediaData.getPath());
                } else {
                    Intent intent = new Intent();
                    if (mediaData != null && mediaData.getPath() != null) {
                        intent.putExtra(Constant.EXTRA_SELECT_RESULT, mediaData.getPath());
                        setResult(Constant.RESULT_CODE, intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == CropImageActivity.REQUEST_CODE_OFF_CROP) {
            if (resultCode == RESULT_OK) {
                String imgPath = data.getStringExtra(CropImageActivity.CROP_IMAGE_RESULT);
                Intent intent = new Intent();
                intent.putExtra(Constant.EXTRA_SELECT_RESULT, imgPath);
                setResult(Constant.RESULT_CODE, intent);
                finish();
            }
        }
    }
}