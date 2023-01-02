package com.example.vsiyp.ui.mediaeditor.animation;

import static com.example.vsiyp.ui.common.bean.Constant.PAGE_SIZE;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.bean.MaterialsDownloadInfo;
import com.example.vsiyp.ui.common.utils.StringUtil;
import com.huawei.hms.videoeditor.materials.HVEChildColumnRequest;
import com.huawei.hms.videoeditor.materials.HVEChildColumnResponse;
import com.huawei.hms.videoeditor.materials.HVEColumnInfo;
import com.huawei.hms.videoeditor.materials.HVEDownloadMaterialListener;
import com.huawei.hms.videoeditor.materials.HVEDownloadMaterialRequest;
import com.huawei.hms.videoeditor.materials.HVELocalMaterialInfo;
import com.huawei.hms.videoeditor.materials.HVEMaterialInfo;
import com.huawei.hms.videoeditor.materials.HVEMaterialsManager;
import com.huawei.hms.videoeditor.materials.HVEMaterialsResponseCallback;
import com.huawei.hms.videoeditor.materials.HVETopColumnInfo;
import com.huawei.hms.videoeditor.materials.HVETopColumnRequest;
import com.huawei.hms.videoeditor.materials.HVETopColumnResponse;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.ArrayList;
import java.util.List;

public class AnimationPanelViewModel extends AndroidViewModel {
    private static final String TAG = "AnimationPanelViewModel";

    private final MutableLiveData<List<HVEColumnInfo>> mAnimationColumns = new MutableLiveData<>();

    private final MutableLiveData<String> errorString = new MutableLiveData<>();

    private final MutableLiveData<List<CloudMaterialBean>> mAnimationMaterials = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadSuccess = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadFail = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadProgress = new MutableLiveData<>();

    private final MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    public AnimationPanelViewModel(@NonNull Application application) {
        super(application);
    }

    public void initColumns(String animationColumnId) {
        List<String> fatherColumn = new ArrayList<>();
        fatherColumn.add(animationColumnId);
        HVETopColumnRequest request = new HVETopColumnRequest(fatherColumn);

        HVEMaterialsManager.getTopColumnById(request, new HVEMaterialsResponseCallback<HVETopColumnResponse>() {
            @Override
            public void onFinish(HVETopColumnResponse response) {
                initMaterialsCutColumnResp(response, animationColumnId);
            }

            @Override
            public void onUpdate(HVETopColumnResponse response) {
                initMaterialsCutColumnResp(response, animationColumnId);
            }

            @Override
            public void onError(Exception e) {
                errorString.postValue(getApplication().getString(R.string.result_illegal));
                String b = e.getMessage();
                SmartLog.e(TAG, b);
            }
        });
    }

    private void initMaterialsCutColumnResp(HVETopColumnResponse animationResp, String animationColumnId) {
        List<HVETopColumnInfo> animationCutContents = animationResp.getColumnInfos();
        if (animationCutContents == null || animationCutContents.size() <= 0) {
            return;
        }
        for (HVETopColumnInfo animationCutColumn : animationCutContents) {
            if (!animationCutColumn.getColumnId().equals(animationColumnId)
                    || animationCutColumn.getChildInfoList().size() <= 0) {
                return;
            }
            mAnimationColumns.postValue(animationCutColumn.getChildInfoList());
            break;
        }
    }

    public void loadMaterials(HVEColumnInfo cutContent, Integer page) {
        if (cutContent.getColumnId().equals("-1")) {
            return;
        }

        HVEChildColumnRequest request =
                new HVEChildColumnRequest(cutContent.getColumnId(), page * PAGE_SIZE, PAGE_SIZE, false);

        HVEMaterialsManager.getChildColumnById(request, new HVEMaterialsResponseCallback<HVEChildColumnResponse>() {
            @Override
            public void onFinish(HVEChildColumnResponse response) {
                initAnimationResp(response);
            }

            @Override
            public void onUpdate(HVEChildColumnResponse response) {
                initAnimationResp(response);
            }

            @Override
            public void onError(Exception e) {
                errorString.postValue(getApplication().getString(R.string.result_illegal));
                String b = e.getMessage();
                SmartLog.e(TAG, b);
            }
        });
    }

    private void initAnimationResp(HVEChildColumnResponse animationCutContentResp) {
        List<HVEMaterialInfo> materialsCutContents = animationCutContentResp.getMaterialInfoList();
        boundaryPageData.postValue(animationCutContentResp.isHasMoreItem());

        if (materialsCutContents != null) {
            queryDownloadStatus(materialsCutContents);
        }
    }

    private void queryDownloadStatus(List<HVEMaterialInfo> materialInfos) {
        List<CloudMaterialBean> list = new ArrayList<>();
        for (int i = 0; i < materialInfos.size(); i++) {
            CloudMaterialBean materialInfo = new CloudMaterialBean();
            HVEMaterialInfo hveMaterialInfo = materialInfos.get(i);
            HVELocalMaterialInfo localMaterialInfo =
                    HVEMaterialsManager.queryLocalMaterialById(hveMaterialInfo.getMaterialId());
            if (!StringUtil.isEmpty(localMaterialInfo.getMaterialPath())) {
                materialInfo.setLocalPath(localMaterialInfo.getMaterialPath());
            }

            materialInfo.setPreviewUrl(hveMaterialInfo.getPreviewUrl());
            materialInfo.setId(hveMaterialInfo.getMaterialId());
            materialInfo.setName(hveMaterialInfo.getMaterialName());

            list.add(materialInfo);
        }
        mAnimationMaterials.postValue(list);
    }

    public void downloadColumn(int previousPosition, int dataPosition, CloudMaterialBean cutContent) {
        MaterialsDownloadInfo downloadAnimationInfo = new MaterialsDownloadInfo();
        downloadAnimationInfo.setPreviousPosition(previousPosition);
        downloadAnimationInfo.setDataPosition(dataPosition);
        downloadAnimationInfo.setContentId(cutContent.getId());
        downloadAnimationInfo.setMaterialBean(cutContent);

        HVEDownloadMaterialRequest request = new HVEDownloadMaterialRequest(cutContent.getId());
        HVEMaterialsManager.downloadMaterialById(request, new HVEDownloadMaterialListener() {
            @Override
            public void onSuccess(String file) {
                SmartLog.i(TAG, "onDecompressionSuccess" + file);
                downloadAnimationInfo.setMaterialLocalPath(file);
                mDownloadSuccess.postValue(downloadAnimationInfo);
            }

            @Override
            public void onProgress(int progress) {
                downloadAnimationInfo.setProgress(progress);
                mDownloadProgress.postValue(downloadAnimationInfo);
            }

            @Override
            public void onFailed(Exception exception) {
                SmartLog.i(TAG, exception.getMessage());
                mDownloadFail.postValue(downloadAnimationInfo);
            }

            @Override
            public void onAlreadyDownload(String file) {
                SmartLog.i(TAG, "onDecompressionSuccess" + file);
                downloadAnimationInfo.setMaterialLocalPath(file);
                mDownloadSuccess.postValue(downloadAnimationInfo);
            }
        });
    }

    public MutableLiveData<String> getErrorString() {
        return errorString;
    }

    public MutableLiveData<List<CloudMaterialBean>> getPageData() {
        return mAnimationMaterials;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadSuccess() {
        return mDownloadSuccess;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadFail() {
        return mDownloadFail;
    }

    public MutableLiveData<List<HVEColumnInfo>> getColumns() {
        return mAnimationColumns;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadProgress() {
        return mDownloadProgress;
    }
}

