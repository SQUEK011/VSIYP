/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.example.vsiyp.ui.mediaexport.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.LazyFragment;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.FileUtil;
import com.example.vsiyp.ui.common.utils.ToastWrapper;
import com.example.vsiyp.ui.mediaexport.model.VideoParams;
import com.example.vsiyp.ui.mediaexport.viewmodel.ExportPreviewViewModel;
import com.example.vsiyp.ui.mediaexport.viewmodel.SettingViewModel;
import com.huawei.secure.android.common.intent.SafeIntent;

import java.io.File;

public class ExportSuccessFragment extends LazyFragment {
    private ConstraintLayout mVideoLayout;

    private ConstraintLayout mCoverLayout;

    private FrameLayout mVideoView;

    private ImageView mVideoCover;

    private ImageView mVideoPlayButton;

    private Button mExportDone;

    private Button mExportShare;

    private SettingViewModel mSettingViewModel;

    private ExportPreviewViewModel mPreviewViewModel;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_export_success;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        statusBarColor = R.color.export_bg;
        navigationBarColor = R.color.export_bg;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        initPreviewUIView(view);
        ToastWrapper
            .makeText(mActivity.getApplicationContext(),
                " " + mActivity.getResources().getString(R.string.export_toast_tips) + " ")
            .show();
    }

    private void initPreviewUIView(View view) {
        mVideoLayout = view.findViewById(R.id.video_layout);
        mVideoView = view.findViewById(R.id.video_view);
        mCoverLayout = view.findViewById(R.id.cover_layout);
        mVideoCover = view.findViewById(R.id.video_cover);
        mVideoPlayButton = view.findViewById(R.id.video_play_button);
        mExportDone = view.findViewById(R.id.export_done);
        mExportShare = view.findViewById(R.id.export_share);
    }

    @Override
    protected void initObject() {
        super.initObject();
        initViewModel();
        initObj();
        handleViewShowOrHide();
    }

    private void initViewModel() {
        mSettingViewModel = new ViewModelProvider(mActivity, (ViewModelProvider.Factory) mFactory).get(SettingViewModel.class);
        mPreviewViewModel = new ViewModelProvider(mActivity, (ViewModelProvider.Factory) mFactory).get(ExportPreviewViewModel.class);
    }

    private void initObj() {
        mPreviewViewModel.initPlayEditor(mVideoView);
        String videoPath = mSettingViewModel.getVideoPath();
        if (!TextUtils.isEmpty(videoPath)) {
            MediaScannerConnection.scanFile(mActivity, new String[] {videoPath}, null, null);
            mPreviewViewModel.initAsset(videoPath);
        }
    }

    private void handleViewShowOrHide() {
        resizeVideoAndCover(mVideoLayout, mVideoView, mVideoCover);
        showCover(mVideoCover, mSettingViewModel.getCoverUrl());
    }

    public void resizeVideoAndCover(View parentView, View videoView, View coverView) {
        ViewTreeObserver previewObserver = parentView.getViewTreeObserver();
        previewObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                VideoParams resetParams =
                    mSettingViewModel.getResetParams(parentView.getMeasuredWidth(), parentView.getMeasuredHeight());
                resetViewSize(videoView, resetParams);
                resetViewSize(coverView, resetParams);

                if (previewObserver.isAlive()) {
                    previewObserver.removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    private void resetViewSize(View view, VideoParams resetParams) {
        if (view == null) {
            return;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = resetParams.getLayoutWidth();
        layoutParams.height = resetParams.getLayoutHeight();
        view.setLayoutParams(layoutParams);
    }

    private void showCover(ImageView coverView, String url) {
        if (mActivity == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            coverView.setBackgroundColor(coverView.getResources().getColor(R.color.pure_black));
            return;
        }
        Glide.with(mActivity)
            .load(url)
            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop())))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(coverView);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        initViewModelObserve();
        mVideoLayout.setOnClickListener(new OnClickRepeatedListener(v -> mPreviewViewModel.playOrPauseEditor()));
        mExportDone.setOnClickListener(new OnClickRepeatedListener(v -> exportCompletedEvent()));
        mExportShare.setOnClickListener(new OnClickRepeatedListener(v -> shareSocialMedia()));
    }

    private void shareSocialMedia() {
        if (mActivity == null) {
            return;
        }
        mActivity.setResult(RESULT_OK);
        Log.d("Export File Path",mSettingViewModel.getVideoPath());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri videoUri = FileProvider.getUriForFile(mContext,"com.example.vsiyp.fileprovider",new File(mSettingViewModel.getVideoPath()));
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        shareIntent.setType("video/mp4");
        startActivity(Intent.createChooser(shareIntent, "Share video using"));
    }

    private void initViewModelObserve() {
        mPreviewViewModel.getIsPlaying()
            .observe(this, isPlaying -> mVideoPlayButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE));

        mPreviewViewModel.getIsShowCover().observe(this, isShowCover -> {
            mCoverLayout.setVisibility(isShowCover ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPreviewViewModel != null) {
            mPreviewViewModel.pauseEditor();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPreviewViewModel != null) {
            mPreviewViewModel.release();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resizeVideoAndCover(mVideoLayout, mVideoView, mVideoCover);
        if (mSettingViewModel == null) {
            showCover(mVideoCover, "");
            return;
        }
        showCover(mVideoCover, mSettingViewModel.getCoverUrl());
    }

    private void exportCompletedEvent() {
        if (mActivity == null) {
            return;
        }
        Log.d("Export File Path",mSettingViewModel.getVideoPath());
        Log.d("iniUri",mSettingViewModel.initJumpUri(mActivity).toString());
        mActivity.setResult(RESULT_OK);
        backHomePage();
    }

    private void backHomePage() {
        //judgeGoToPhotoBrowser(new File(mSettingViewModel.getVideoPath()), mSettingViewModel.initJumpUri(mActivity));
        Intent intent = new Intent();
        mActivity.setResult(Constant.RESULT_CODE, intent);
        mActivity.finish();
    }

    public static final String SOURCE = "source";

    public static final String SOURCE_HIMOVIE_LOCALVIDEO = "Himovie_LocalVideo";

    private void judgeGoToPhotoBrowser(File exportFile, Uri uri) {
        SafeIntent safeIntent = new SafeIntent(mActivity.getIntent());
        String source = safeIntent.getStringExtra(SOURCE);
        if (SOURCE_HIMOVIE_LOCALVIDEO.equals(source)) {
            return;
        }
        if (uri != null) {
            FileUtil.goToPhotoBrowser(mActivity, uri);
        } else {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(exportFile));
            mActivity.sendBroadcast(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
