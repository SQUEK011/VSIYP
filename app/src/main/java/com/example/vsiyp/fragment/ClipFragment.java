package com.example.vsiyp.fragment;

import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.CLIPS_VIEW_TYPE;
import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.VIEW_HISTORY;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsiyp.CameraActivity;
import com.example.vsiyp.HomeRecordAdapter;
import com.example.vsiyp.R;
import com.example.vsiyp.SettingActivity;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SharedPreferencesUtils;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.view.EditorTextView;
import com.example.vsiyp.ui.common.view.decoration.RecyclerViewDivider;
import com.example.vsiyp.ui.mediaeditor.VideoClipsActivity;
import com.example.vsiyp.ui.mediaeditor.texts.viewmodel.TextEditViewModel;
import com.example.vsiyp.ui.mediapick.activity.MediaPickActivity;
import com.example.vsiyp.view.ClipDeleteDialog;
import com.example.vsiyp.view.ClipRenameDialog;
import com.example.vsiyp.view.HomeClipPopWindow;
import com.example.vsiyp.viewmodel.MainViewModel;
import com.huawei.hms.videoeditor.sdk.HVEProject;
import com.huawei.hms.videoeditor.sdk.HVEProjectManager;
import com.huawei.hms.videoeditor.sdk.bean.HVEWordStyle;
import com.huawei.secure.android.common.intent.SafeIntent;

import java.util.ArrayList;
import java.util.List;

public class ClipFragment extends BaseFragment {
    private static final String TAG = "ClipFragment";

    private EditorTextView mDraftClip;

    private LinearLayout mAddCardView;

    private LinearLayout mAddCameraCardView;

    private RecyclerView mRecyclerView;

    private ConstraintLayout homeSelectLayout;

    private TextView homeSelectDelete;

    private TextView homeSelectAll;

    private TextView homeDraftNoText;

    private MainViewModel mainViewModel;

    private List<HVEProject> mDraftList;

    private HomeRecordAdapter mHomeRecordAdapter;

    private HomeClipPopWindow mActionPopWindow;

    private TextEditViewModel mTextEditViewModel;

    private ImageView mSettings;

    @Override
    protected void initViewModelObserve() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_clip;
    }

    @Override
    protected void initView(View view) {
        mDraftClip = view.findViewById(R.id.home_draft_clip);
        mRecyclerView = view.findViewById(R.id.content_list);
        mAddCardView = view.findViewById(R.id.card_upload);
        homeSelectLayout = view.findViewById(R.id.home_select_layout);
        homeSelectDelete = view.findViewById(R.id.home_select_delete);
        homeSelectAll = view.findViewById(R.id.home_select_all);
        homeDraftNoText = view.findViewById(R.id.home_draft_no_text);
        mAddCameraCardView = view.findViewById(R.id.card_upload_2);
        mSettings = view.findViewById(R.id.setting);
        Activity activity = getActivity();
        if (activity != null) {
            boolean isFromHome = activity.getIntent().getBooleanExtra("fromHome", false);
        }
        mTextEditViewModel = new ViewModelProvider(mActivity, (ViewModelProvider.Factory) mFactory).get(TextEditViewModel.class);
    }

    @Override
    protected void initObject() {
        mainViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) mFactory).get(MainViewModel.class);
        mDraftList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(itemAnimator);

        mHomeRecordAdapter = new HomeRecordAdapter(context, mDraftList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL,
                SizeUtils.dp2Px(context, 8), ContextCompat.getColor(context, R.color.edit_background)));
        mRecyclerView.setAdapter(mHomeRecordAdapter);

        boolean refresh = viewModel.getRefresh();
        if (refresh) {
            refresh();
        }
    }

    @Override
    protected void initData() {
        mainViewModel.getDraftProjects().observe(getViewLifecycleOwner(), draftProjects -> {
            if (draftProjects.size() > 0) {
                mDraftList.clear();
                mDraftList.addAll(draftProjects);
                mHomeRecordAdapter.notifyDataSetChanged();
                homeDraftNoText.setVisibility(View.GONE);
                mDraftClip.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                homeDraftNoText.setVisibility(View.VISIBLE);
                mDraftClip.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            hideEditStatus();
        });
    }

    @Override
    protected void initEvent() {
        mSettings.setOnClickListener(new OnClickRepeatedListener((v -> this.startActivity(new Intent(this.mActivity, SettingActivity.class)))));

        mDraftClip.setOnClickListener(new OnClickRepeatedListener(v -> {
            mDraftClip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mDraftClip.setTextColor(context.getColor(R.color.white));
            initData();
        }));

        homeSelectDelete.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (homeSelectDelete.isSelected()) {
                if (mHomeRecordAdapter.getSelectList().isEmpty()) {
                    Toast.makeText(context, getText(R.string.home_select_num4), Toast.LENGTH_LONG).show();
                } else {
                    showDeleteDialog(mHomeRecordAdapter.getSelectList());
                }
            }
        }));

        homeSelectAll.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (homeSelectAll.isSelected()) {
                homeSelectAll.setText(R.string.home_select_all);
                homeSelectAll.setSelected(false);
                homeSelectDelete.setSelected(false);
                mHomeRecordAdapter.setSelectList(new ArrayList<>());
            } else {
                homeSelectAll.setText(R.string.home_select_all_deselect);
                homeSelectAll.setSelected(true);
                homeSelectDelete.setSelected(true);
                mHomeRecordAdapter.setSelectList(mDraftList);
            }
            mHomeRecordAdapter.notifyDataSetChanged();
        }));

        mAddCardView.setOnClickListener(new OnClickRepeatedListener(v -> {
            mTextEditViewModel.setLastWordStyle(new HVEWordStyle());
            mTextEditViewModel.refreshAsset();
            hideEditStatus();
            startActivity(new Intent(this.mActivity, MediaPickActivity.class));
            this.mActivity.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_silent);
        }));

        mHomeRecordAdapter.setOnItemClickListener(new HomeRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HVEProject hveProject = mDraftList.get(position);
                if (context == null || hveProject == null) {
                    return;
                }

                SafeIntent intent = new SafeIntent(new Intent());
                intent.setClass(mActivity, VideoClipsActivity.class);
                intent.putExtra(CLIPS_VIEW_TYPE, VIEW_HISTORY);
                intent.putExtra(VideoClipsActivity.EXTRA_FROM_SELF_MODE, true);
                intent.putExtra("projectId", hveProject.getProjectId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick() {
                showEditStatus();
            }

            @Override
            public void onSelectClick(List<HVEProject> selectList, int position) {
                homeSelectAll.setSelected(selectList.size() == mDraftList.size());
                if (homeSelectAll.isSelected()) {
                    homeSelectAll.setText(R.string.home_select_all_deselect);
                } else {
                    homeSelectAll.setText(R.string.home_select_all);
                }
                homeSelectDelete.setSelected(selectList.size() != 0);
                mHomeRecordAdapter.notifyItemChanged(position);
            }

            @Override
            public void onActionClick(View view, HVEProject item, int pos) {
                showActionPopWindow(view, item, pos);
            }

        });

        mAddCameraCardView.setOnClickListener(new OnClickRepeatedListener(v -> {
            Log.d(TAG, "Camera Selected");
            //create new Intent
            Intent intent = new Intent(mActivity,CameraActivity.class);
            startActivity(intent);
        }));
    }

    @Override
    protected int setViewLayoutEvent() {
        return NOMERA_HEIGHT;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showActionPopWindow(View view, HVEProject item, int pos) {
        int width;
        int height;
        if (mActionPopWindow == null) {
            mActionPopWindow = new HomeClipPopWindow(mActivity);
            width = mActionPopWindow.getPopWidth() + 40;
            height = mActionPopWindow.getPopHeight() + 10;
        } else {
            width = mActionPopWindow.getContentView().getWidth();
            height = mActionPopWindow.getContentView().getHeight();
        }
        mActionPopWindow.setPosition(item);
        mActionPopWindow.setOnActionClickListener(new HomeClipPopWindow.ActionOnClickListener() {
            @Override
            public void onRenameClick(HVEProject item) {
                showRenameDialog(item);
            }

            @Override
            public void onCopyClick() {
                if (context == null) {
                    return;
                }
                if (mDraftList == null || mDraftList.isEmpty() || pos >= mDraftList.size() || pos < 0) {
                    return;
                }
                int copyTimes =
                        SharedPreferencesUtils.getInstance().getCopyDraftTimes(context, mDraftList.get(pos).getProjectId());
                StringBuilder copyName = new StringBuilder(mDraftList.get(pos).getName());
                copyTimes++;
                copyName.append(context.getString(R.string.home_select_delete_copy_name)).append(copyTimes);
                boolean isCopySuccess =
                        HVEProjectManager.copyDraft(mDraftList.get(pos).getProjectId(), copyName.toString());
                if (isCopySuccess) {
                    SharedPreferencesUtils.getInstance()
                            .putCopyDraftTimes(context, mDraftList.get(pos).getProjectId(), copyTimes);
                    refresh();
                    Toast.makeText(context, context.getString(R.string.home_select_delete_copy_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.home_select_delete_copy_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick() {
                List<HVEProject> temps = new ArrayList<>();
                if (mDraftList == null || mDraftList.isEmpty() || pos >= mDraftList.size() || pos < 0) {
                    return;
                }
                temps.add(mDraftList.get(pos));
                showDeleteDialog(temps);
            }
        });

        int off = 10;
        if (pos == 0 || pos == 1) {
            mActionPopWindow.showAsDropDown(view, -width + view.getWidth(), -height / 3);
        } else {
            mActionPopWindow.showAsDropDown(view, -width + view.getWidth(), -height - view.getHeight() - off);
        }

        mActionPopWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));
    }

    private void showDeleteDialog(List<HVEProject> mSelectProjectId) {
        if (context == null || mSelectProjectId == null || mDraftList == null || mDraftList.isEmpty()) {
            return;
        }
        ClipDeleteDialog dialog = new ClipDeleteDialog(context);
        dialog.show();
        dialog.setOnPositiveClickListener(() -> {
            for (HVEProject projectId : mSelectProjectId) {
                HVEProjectManager.deleteProject(projectId.getProjectId());
                int indexOfDraftList = getIndexOfDraftList(projectId.getProjectId());
                if (indexOfDraftList != -1) {
                    mDraftList.remove(projectId);
                }
            }
            hideEditStatus();
            refresh();
        });
    }

    private void showRenameDialog(HVEProject item) {
        if (mActivity == null) {
            return;
        }

        ClipRenameDialog dialog = new ClipRenameDialog(mActivity, item);
        dialog.show();
        dialog.setOnPositiveClickListener((updateName) -> {
            HVEProjectManager.updateProjectName(item.getProjectId(), updateName);
            hideEditStatus();
            refresh();
            initData();
        });
    }

    private int getIndexOfDraftList(String projectId) {
        int index = -1;
        for (int i = 0; i < mDraftList.size(); i++) {
            if (mDraftList.get(i).getProjectId().equals(projectId)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void refresh() {
        mainViewModel.initDraftProjects();
        mHomeRecordAdapter.notifyDataSetChanged();
    }

    private void hideEditStatus() {
        homeSelectAll.setText(R.string.home_select_all);
        homeSelectAll.setSelected(false);
        homeSelectDelete.setSelected(false);
        mHomeRecordAdapter.setSelectList(new ArrayList<>());
        homeSelectLayout.setVisibility(View.INVISIBLE);
        mHomeRecordAdapter.setIsEditStatus(false);
    }

    private void showEditStatus() {
        homeSelectAll.setText(R.string.home_select_all);
        homeSelectAll.setSelected(false);
        homeSelectDelete.setSelected(false);
        homeSelectLayout.setVisibility(View.VISIBLE);
        mHomeRecordAdapter.setIsEditStatus(true);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }
}
