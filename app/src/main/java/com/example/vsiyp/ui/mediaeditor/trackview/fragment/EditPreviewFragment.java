package com.example.vsiyp.ui.mediaeditor.trackview.fragment;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.EditorManager;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.ThumbNailMemoryCache;
import com.example.vsiyp.ui.mediaeditor.VideoClipsActivity;
import com.example.vsiyp.ui.mediaeditor.filter.FilterLinearLayoutManager;
import com.example.vsiyp.ui.mediaeditor.menu.EditItemViewModel;
import com.example.vsiyp.ui.mediaeditor.menu.VideoClipsPlayViewModel;
import com.example.vsiyp.ui.mediaeditor.trackview.adapter.ImageTrackRecyclerViewAdapter;
import com.example.vsiyp.ui.mediaeditor.trackview.bean.MainRecyclerData;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.lane.HVEVideoLane;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditPreviewFragment extends BaseFragment implements View.OnTouchListener {
    private static final String TAG = "EditPreviewFragment";

    private RecyclerView imageTrackRecyclerView;

    private ImageView addView;

    private ImageTrackRecyclerViewAdapter imageAdapter;

    private MainRecyclerData mainData;

    private boolean isPlaying;

    private long startTime = 0L;

    private long endTime = 0L;

    private Point touchDown = new Point();

    private EditItemViewModel mEditViewModel;

    private VideoClipsPlayViewModel playViewModel;

    public EditPreviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        navigationBarColor = R.color.home_color_FF181818;
        super.onCreate(savedInstanceState);
        mEditViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(EditItemViewModel.class);
        playViewModel = new ViewModelProvider((ViewModelStoreOwner) mActivity, (ViewModelProvider.Factory) mFactory).get(VideoClipsPlayViewModel.class);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_edit_preview;
    }

    @Override
    protected void initView(View view) {
        initComponent(view);
        init(view);
    }

    @Override
    protected void initObject() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected int setViewLayoutEvent() {
        return NOMERA_HEIGHT;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ThumbNailMemoryCache.getInstance().init();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setViewMargin();
    }

    @Override
    protected void initViewModelObserve() {
        viewModel.getSelectedUUID().observe(this, uuid -> {
            imageTrackRecyclerView.invalidateItemDecorations();
        });

        viewModel.getImageItemList().observe(this, list -> {
            imageAdapter.updateData(list);
        });

        mEditViewModel.getItemsFirstSelected().observe(this, editMenuBean -> {
            if (editMenuBean != null) {
                mainData.setViewState(editMenuBean.getId());
            }
        });

        playViewModel.getPlayState().observe(this, isPlay -> isPlaying = isPlay);
    }

    @Override
    public void onBackPressed() {
    }

    private void initComponent(View view) {
        imageTrackRecyclerView = view.findViewById(R.id.imageTrack_layout);
        addView = view.findViewById(R.id.add_video);
    }

    public List<HVEAsset> getItems() {
        HVEVideoLane videoLane = EditorManager.getInstance().getMainLane();
        if (videoLane == null) {
            return new ArrayList<>();
        }
        return videoLane.getAssets();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(View view) {
        mainData = new MainRecyclerData(mActivity);
        viewModel.setMainData(mainData);
        imageAdapter = new ImageTrackRecyclerViewAdapter(viewModel, mActivity);
        imageAdapter.updateData(getItems());

        RecyclerView.RecycledViewPool pool = imageTrackRecyclerView.getRecycledViewPool();
        pool.setMaxRecycledViews(0, 20);
        imageTrackRecyclerView.setRecycledViewPool(pool);

        FilterLinearLayoutManager layoutManager2 = new FilterLinearLayoutManager(mActivity);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        imageTrackRecyclerView.setLayoutManager(layoutManager2);
        imageTrackRecyclerView.setAdapter(imageAdapter);

        setViewMargin();
        imageTrackRecyclerView.setOnTouchListener(this);

        addView.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mActivity == null) {
                return;
            }

            ((VideoClipsActivity) mActivity).addMediaData();
        }));
    }

    private void setViewMargin() {
    }

    boolean interuptedScrolling = false;

    private void pauseTimeLine() {
        SmartLog.i(TAG, "pauseTimeLine:");
        if (mActivity == null) {
            return;
        }

        ((VideoClipsActivity) mActivity).pauseTimeLine();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mActivity == null) {
            return false;
        }

        if (interuptedScrolling) {
            return true;
        }
        try {
            switch (event.getAction() & event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    touchDown.x = (int) event.getX();
                    touchDown.y = (int) event.getY();
                    startTime = System.currentTimeMillis();
                    if (isPlaying) {
                        pauseTimeLine();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    if (Math.abs(touchDown.x - event.getX()) < 20 && Math.abs(touchDown.y - event.getY()) < 20
                            && (endTime - startTime) <= 500) {
                        viewModel.setSelectedUUID("");
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            SmartLog.i("onTouch ", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

