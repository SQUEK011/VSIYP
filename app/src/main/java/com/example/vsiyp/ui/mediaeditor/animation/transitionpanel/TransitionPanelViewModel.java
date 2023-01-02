package com.example.vsiyp.ui.mediaeditor.animation.transitionpanel;

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

public class TransitionPanelViewModel extends AndroidViewModel {
    private static final String TAG = "TransitionPanelViewModel";

    private final MutableLiveData<List<HVEColumnInfo>> mTransitionColumns = new MutableLiveData<>();

    private final MutableLiveData<String> errorString = new MutableLiveData<>();

    private final MutableLiveData<List<CloudMaterialBean>> mTransitionMaterials = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadSuccess = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadFail = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadProgress = new MutableLiveData<>();

    private final MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    public TransitionPanelViewModel(@NonNull Application application) {
        super(application);
    }

    public void initColumns(String columnId) {
        List<String> fatherColumn = new ArrayList<>();
        fatherColumn.add(columnId);
        HVETopColumnRequest topColumnRequest = new HVETopColumnRequest(fatherColumn);
        HVEMaterialsManager.getTopColumnById(topColumnRequest,
                new HVEMaterialsResponseCallback<HVETopColumnResponse>() {
                    @Override
                    public void onFinish(HVETopColumnResponse response) {
                        initMaterialsCutColumnResp(response, columnId);
                    }

                    @Override
                    public void onUpdate(HVETopColumnResponse response) {
                        initMaterialsCutColumnResp(response, columnId);
                    }

                    @Override
                    public void onError(Exception e) {
                        errorString.postValue(getApplication().getString(R.string.result_illegal));
                        String b = e.getMessage();
                        SmartLog.e(TAG, b);
                    }
                });
    }

    private void initMaterialsCutColumnResp(HVETopColumnResponse transitionResp, String columnId) {
        List<HVETopColumnInfo> transitionCutColumns = transitionResp.getColumnInfos();
        if (transitionCutColumns == null || transitionCutColumns.size() <= 0) {
            return;
        }

        for (HVETopColumnInfo transitionCutColumn : transitionCutColumns) {
            if (!transitionCutColumn.getColumnId().equals(columnId)
                    || transitionCutColumn.getChildInfoList().size() <= 0) {
                return;
            }
            mTransitionColumns.postValue(transitionCutColumn.getChildInfoList());
            break;
        }
    }

    public void loadMaterials(HVEColumnInfo content, Integer page) {
        if (content.getColumnId().equals("-1")) {
            return;
        }

        HVEChildColumnRequest request =
                new HVEChildColumnRequest(content.getColumnId(), page * PAGE_SIZE, PAGE_SIZE, false);
        HVEMaterialsManager.getChildColumnById(request, new HVEMaterialsResponseCallback<HVEChildColumnResponse>() {
            @Override
            public void onFinish(HVEChildColumnResponse response) {
                initMaterialsCutContentResp(response);
            }

            @Override
            public void onUpdate(HVEChildColumnResponse response) {
                initMaterialsCutContentResp(response);
            }

            @Override
            public void onError(Exception e) {
                errorString.postValue(getApplication().getString(R.string.result_illegal));
                String b = e.getMessage();
                SmartLog.e(TAG, b);
            }
        });
    }

    private void initMaterialsCutContentResp(HVEChildColumnResponse transitionResp) {
        List<HVEMaterialInfo> materialsCutContents = transitionResp.getMaterialInfoList();
        boundaryPageData.postValue(transitionResp.isHasMoreItem());
        if (materialsCutContents != null) {
            SmartLog.i(TAG, "onFinish materialsCutContents :" + materialsCutContents.toString());
            queryDownloadStatus(materialsCutContents);
        }
    }

    private void queryDownloadStatus(List<HVEMaterialInfo> transitionCutContents) {
        List<CloudMaterialBean> list = new ArrayList<>();
        for (int i = 0; i < transitionCutContents.size(); i++) {
            CloudMaterialBean materialInfo = new CloudMaterialBean();

            HVEMaterialInfo hveMaterialInfo = transitionCutContents.get(i);

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

        mTransitionMaterials.postValue(list);
    }

    public void downloadColumn(int previousPosition, int dataPosition, CloudMaterialBean cutContent) {
        MaterialsDownloadInfo downloadTransitionInfo = new MaterialsDownloadInfo();
        downloadTransitionInfo.setPreviousPosition(previousPosition);
        downloadTransitionInfo.setDataPosition(dataPosition);
        downloadTransitionInfo.setContentId(cutContent.getId());
        downloadTransitionInfo.setMaterialBean(cutContent);

        HVEDownloadMaterialRequest request = new HVEDownloadMaterialRequest(cutContent.getId());
        HVEMaterialsManager.downloadMaterialById(request, new HVEDownloadMaterialListener() {
            @Override
            public void onSuccess(String file) {
                SmartLog.i(TAG, "onDecompressionSuccess" + file);
                downloadTransitionInfo.setMaterialLocalPath(file);
                mDownloadSuccess.postValue(downloadTransitionInfo);
            }

            @Override
            public void onProgress(int progress) {
                downloadTransitionInfo.setProgress(progress);
                mDownloadProgress.postValue(downloadTransitionInfo);
            }

            @Override
            public void onFailed(Exception exception) {
                SmartLog.i(TAG, exception.getMessage());
                mDownloadFail.postValue(downloadTransitionInfo);
            }

            @Override
            public void onAlreadyDownload(String file) {
                downloadTransitionInfo.setMaterialLocalPath(file);
                mDownloadSuccess.postValue(downloadTransitionInfo);
            }
        });
    }

    public MutableLiveData<String> getErrorString() {
        return errorString;
    }

    public MutableLiveData<List<CloudMaterialBean>> getPageData() {
        return mTransitionMaterials;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadSuccess() {
        return mDownloadSuccess;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadFail() {
        return mDownloadFail;
    }

    public MutableLiveData<List<HVEColumnInfo>> getColumns() {
        return mTransitionColumns;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadProgress() {
        return mDownloadProgress;
    }
}
