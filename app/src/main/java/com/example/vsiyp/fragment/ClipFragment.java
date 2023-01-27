package com.example.vsiyp.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.CLIPS_VIEW_TYPE;
import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.VIEW_HISTORY;
import static com.example.vsiyp.ui.mediaeditor.VideoClipsActivity.VIEW_NORMAL;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.vsiyp.HomeActivity;
import com.example.vsiyp.CameraActivity;
import com.example.vsiyp.HomeRecordAdapter;
import com.example.vsiyp.MainActivity;
import com.example.vsiyp.R;
import com.example.vsiyp.SettingActivity;
import com.example.vsiyp.ui.common.BaseFragment;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.listener.OnClickRepeatedListener;
import com.example.vsiyp.ui.common.utils.SharedPreferencesUtils;
import com.example.vsiyp.ui.common.utils.SizeUtils;
import com.example.vsiyp.ui.common.utils.ToastWrapper;
import com.example.vsiyp.ui.common.view.EditorTextView;
import com.example.vsiyp.ui.common.view.decoration.RecyclerViewDivider;
import com.example.vsiyp.ui.mediaeditor.VideoClipsActivity;
import com.example.vsiyp.ui.mediaeditor.texts.viewmodel.TextEditViewModel;
import com.example.vsiyp.ui.mediapick.activity.MediaPickActivity;
import com.example.vsiyp.utils.SmartLog;
import com.example.vsiyp.view.ClipDeleteDialog;
import com.example.vsiyp.view.ClipRenameDialog;
import com.example.vsiyp.view.HomeClipPopWindow;
import com.example.vsiyp.viewmodel.MainViewModel;
import com.huawei.hms.videoeditor.ai.p.M;
import com.huawei.hms.videoeditor.sdk.HVEProject;
import com.huawei.hms.videoeditor.sdk.HVEProjectManager;
import com.huawei.hms.videoeditor.sdk.bean.HVEWordStyle;
import com.huawei.secure.android.common.intent.SafeIntent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClipFragment extends BaseFragment {
    private static final String TAG = "ClipFragment";
    //private TextView homeSelectNum;

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

    //private ImageView back;

    private ImageView mSettings;

    private boolean isFromHome = false;

    //Variables for Camera Usage
    private Uri videoUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int VIEW_CAMERA = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    String imageFilePath;

    @Override
    protected void initViewModelObserve() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_clip;
    }

    @Override
    protected void initView(View view) {
        //homeSelectNum = view.findViewById(R.id.home_select_num);
        mDraftClip = view.findViewById(R.id.home_draft_clip);
        mRecyclerView = view.findViewById(R.id.content_list);
        mAddCardView = view.findViewById(R.id.card_upload);
        homeSelectLayout = view.findViewById(R.id.home_select_layout);
        homeSelectDelete = view.findViewById(R.id.home_select_delete);
        homeSelectAll = view.findViewById(R.id.home_select_all);
        homeDraftNoText = view.findViewById(R.id.home_draft_no_text);
        //homeSelectNum.setText(getResources().getQuantityString(R.plurals.home_select_num3, 0, 0));
        //back = view.findViewById(R.id.iv_back);
        mAddCameraCardView = view.findViewById(R.id.card_upload_2);
        mSettings = view.findViewById(R.id.setting);
        Activity activity = getActivity();
        if (activity != null) {
            isFromHome = activity.getIntent().getBooleanExtra("fromHome", false);
            //back.setVisibility(isFromHome ? View.VISIBLE : View.GONE);
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
                for (HVEProject project : draftProjects) {
                    mDraftList.add(project);
                }
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
        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.onBackPressed();
                }
            }
        });*/
        mSettings.setOnClickListener(new OnClickRepeatedListener((v -> {
            this.startActivity(new Intent(this.mActivity, SettingActivity.class));
        })));

        mDraftClip.setOnClickListener(new OnClickRepeatedListener(v -> {
            mDraftClip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mDraftClip.setTextColor(context.getColor(R.color.white));
            initData();
        }));

        /*homeSelectNum.setOnClickListener(new OnClickRepeatedListener(v -> {
            if (mHomeRecordAdapter.getIsEditStatus()) {
                hideEditStatus();
                homeSelectAll.setSelected(false);
                homeSelectDelete.setSelected(false);
            } else {
                showEditStatus();
            }

        }));*/

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
                //homeSelectNum.setText(getResources().getQuantityString(R.plurals.home_select_num3,new ArrayList<>().size(), new ArrayList<>().size()));
            } else {
                homeSelectAll.setText(R.string.home_select_all_deselect);
                homeSelectAll.setSelected(true);
                homeSelectDelete.setSelected(true);
                mHomeRecordAdapter.setSelectList(mDraftList);
                //homeSelectNum.setText(getResources().getQuantityString(R.plurals.home_select_num3, mDraftList.size(), mDraftList.size()));

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
                //homeSelectNum.setText(getResources().getQuantityString(R.plurals.home_select_num3, selectList.size(), selectList.size()));
            }

            @Override
            public void onActionClick(View view, HVEProject item, int pos) {
                showActionPopWindow(view, item, pos);
            }

        });

        mAddCameraCardView.setOnClickListener(new OnClickRepeatedListener(v -> {
            Log.d("ClipFragment", "Camera Selected");
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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this.context, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                //Intent returnIntent = new Intent(this.mActivity, HomeActivity.class);
                //startActivity(returnIntent);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this.context, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                Log.d("Video Saved to:", data.getDataString());

                Intent returnIntent = new Intent(this.context, VideoClipsActivity.class);
                ArrayList<MediaData> mSelectList = new ArrayList<>();
                MediaData mediaDataFromCam = new MediaData();
                mediaDataFromCam.setUri(data.getData());

                mSelectList.add(mediaDataFromCam);

                File videoFilePath = new File(videoUri.getPath());
                String outputUri = videoFilePath.getPath();

                returnIntent.putParcelableArrayListExtra(Constant.EXTRA_SELECT_RESULT, mSelectList);
                returnIntent.putExtra(CLIPS_VIEW_TYPE, VIEW_CAMERA);
                returnIntent.putExtra("videoFileUri",outputUri);
                returnIntent.putExtra(VideoClipsActivity.EXTRA_FROM_SELF_MODE, true);
                startActivity(returnIntent);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
                Toast.makeText(this.context, "Video Capture Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Video capture failed, advise user
            }
        }
    }*/

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

        mActionPopWindow.setOnDismissListener(() -> {
            backgroundAlpha(1.0f);
        });
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
        //homeSelectNum.setText(getResources().getQuantityString(R.plurals.home_select_num3, new ArrayList<>().size(),new ArrayList<>().size()));
        //homeSelectNum.setVisibility(View.INVISIBLE);
        homeSelectLayout.setVisibility(View.INVISIBLE);
        //back.setVisibility(isFromHome ? View.VISIBLE : View.GONE);
        mHomeRecordAdapter.setIsEditStatus(false);
    }

    private void showEditStatus() {
        homeSelectAll.setText(R.string.home_select_all);
        homeSelectAll.setSelected(false);
        homeSelectDelete.setSelected(false);
        //homeSelectNum.setVisibility(View.VISIBLE);
        homeSelectLayout.setVisibility(View.VISIBLE);
        //back.setVisibility(View.GONE);
        mHomeRecordAdapter.setIsEditStatus(true);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return FileProvider.getUriForFile(this.context,"com.example.vsiyp.fileprovider", getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "VSIYP");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("VSIYP", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private MediaData getVideoMediaData(Uri fileUri) {
        List<MediaData> mediaDataList = new ArrayList<>();
        MediaData returnData = new MediaData();

        //1. Find the captured video from the uri
        final String[] videoProjection =
                {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATE_MODIFIED};

        try {
            Cursor cursor = this.getActivity().getApplication().getContentResolver()
                    .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoProjection, null, null,
                            MediaStore.Video.Media.DATE_MODIFIED + " DESC ");
            if (cursor != null) {
                cursor.moveToFirst();
                try {
                    String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(videoProjection[0]));
                    long videoDuration = cursor.getInt(cursor.getColumnIndexOrThrow(videoProjection[1]));
                    long videoAddTime = cursor.getLong(cursor.getColumnIndexOrThrow(videoProjection[2]));
                    int videoHeight = cursor.getInt(cursor.getColumnIndexOrThrow(videoProjection[3]));
                    long mimeType = cursor.getLong(cursor.getColumnIndexOrThrow(videoProjection[4]));
                    long displayName = cursor.getLong(cursor.getColumnIndexOrThrow(videoProjection[5]));
                    long videoSize = cursor.getLong(cursor.getColumnIndexOrThrow(videoProjection[6]));
                    long videoWidth = cursor.getLong(cursor.getColumnIndexOrThrow(videoProjection[7]));

                    if (videoDuration < 500) {
                        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) {
                            if (!TextUtils.isEmpty(videoPath)) {
                                File file = new File(videoPath);
                                if (!file.exists() || file.length() <= 0) {
                                    MediaData tempStore = new MediaData();
                                    tempStore.setAddTime(videoAddTime);
                                    tempStore.setDuration(videoDuration);
                                    tempStore.setHeight(videoHeight);
                                    tempStore.setMimeType(String.valueOf(mimeType));
                                    tempStore.setName(String.valueOf(displayName));
                                    tempStore.setPath(file.getPath());
                                    tempStore.setSize(videoSize);
                                    tempStore.setUri(fileUri);
                                    tempStore.setWidth((int) videoWidth);
                                }
                            }
                        }
                    }

                } catch (SecurityException e) {
                    SmartLog.e(TAG, e.getMessage());
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }catch (SecurityException e){
            SmartLog.e(TAG, e.getMessage());
        }
        //2. Store Data into the temp MediaData Variable
        for (MediaData data: mediaDataList){
            returnData.setAddTime(data.getAddTime());
            returnData.setDuration(data.getDuration());
            returnData.setHeight(data.getHeight());
            returnData.setMimeType(data.getMimeType());
            returnData.setName(data.getName());
            returnData.setPath(data.getPath());
            returnData.setSize(data.getSize());
            returnData.setUri(data.getUri());
            returnData.setWidth(data.getWidth());
        }

        //3. Return the MediaData Item
        return returnData;
    }
}
