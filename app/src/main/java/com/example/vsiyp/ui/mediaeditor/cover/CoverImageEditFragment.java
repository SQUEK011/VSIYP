package com.example.vsiyp.ui.mediaeditor.cover;

import static com.example.vsiyp.ui.mediaeditor.cover.CoverImageViewModel.SOURCE_COVER_IMAGE_NAME_SUFFIX;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.DrawableUtils;
import com.example.vsiyp.ui.common.utils.FileUtil;
import com.example.vsiyp.ui.common.view.image.crop.ClipImageView;
import com.example.vsiyp.utils.SmartLog;
import com.huawei.secure.android.common.intent.SafeBundle;

import java.lang.ref.WeakReference;

public class CoverImageEditFragment extends BaseFragment {

    private static final String TAG = "CoverImageEditFragment";

    private ImageView mBackIv;

    private ImageView mConfirmIv;

    private ClipImageView clipImageView;

    private MediaData mMediaData;

    private int mVideoWidth;

    private int mVideoHeight;

    private String mProjectId;

    private MHandler handler;

    @Override
    protected void initViewModelObserve() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_cover_image_edit;
    }

    @Override
    protected void initView(View view) {
        mBackIv = view.findViewById(R.id.iv_back);
        mConfirmIv = view.findViewById(R.id.iv_confirm);
        clipImageView = view.findViewById(R.id.zoom_layout);
    }

    @Override
    protected void initObject() {
        SafeBundle bundle = new SafeBundle(getArguments());
        mMediaData = bundle.getParcelable(Constant.EXTRA_SELECT_RESULT);
        mVideoWidth = (int) bundle.getFloat(CoverImageActivity.WIDTH, 720f);
        mVideoHeight = (int) bundle.getFloat(CoverImageActivity.HEIGHT, 1080f);
        mProjectId = bundle.getString(CoverImageActivity.PROJECT_ID);

        handler = new MHandler((CoverImageActivity) mActivity);
    }

    @Override
    protected void initData() {
        clipImageView.setAspect(mVideoWidth, mVideoHeight);
        new Thread(() -> {
            Thread.currentThread().setName("CoverImageEditFragment-Thread-init");
            Drawable drawable = DrawableUtils.compressDrawable(mActivity, mMediaData.getPath());
            if (mActivity != null) {
                mActivity.runOnUiThread(() -> clipImageView.setImageDrawable(drawable));
            }
        }).start();
    }

    @Override
    protected void initEvent() {
        mActivity.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mActivity.finish();
            }
        });

        mBackIv.setOnClickListener(new OnClickRepeatedListener(v -> {
            mActivity.finish();
        }));

        mConfirmIv.setOnClickListener(new OnClickRepeatedListener(v -> clipBitmap()));
    }

    @Override
    protected int setViewLayoutEvent() {
        return NOMERA_HEIGHT;
    }

    private void clipBitmap() {
        new Thread("CoverImageEditFragment-Thread-1") {
            @Override
            public void run() {
                super.run();
                try {
                    Bitmap bitmap = clipImageView.clip();
                    String path = FileUtil.saveBitmap(mActivity, mProjectId, bitmap,
                            System.currentTimeMillis() + SOURCE_COVER_IMAGE_NAME_SUFFIX);
                    Message message = handler.obtainMessage();
                    message.obj = path;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = handler.obtainMessage();
                    message.obj = "";
                    handler.sendMessage(message);
                    SmartLog.e(TAG, e.getMessage());
                }
            }
        }.start();
    }

    static class MHandler extends Handler {
        WeakReference<CoverImageActivity> mActivity;

        MHandler(CoverImageActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            CoverImageActivity activity = mActivity.get();
            if (activity != null) {
                String path = (String) msg.obj;
                activity.onResult(path);
            }
        }
    }

}

