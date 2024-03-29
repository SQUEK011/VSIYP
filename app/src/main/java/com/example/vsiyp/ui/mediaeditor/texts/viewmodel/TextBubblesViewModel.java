package com.example.vsiyp.ui.mediaeditor.texts.viewmodel;

import static com.example.vsiyp.ui.common.bean.Constant.PAGE_SIZE;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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
import com.huawei.hms.videoeditor.materials.HVEMaterialConstant;
import com.huawei.hms.videoeditor.materials.HVEMaterialInfo;
import com.huawei.hms.videoeditor.materials.HVEMaterialsManager;
import com.huawei.hms.videoeditor.materials.HVEMaterialsResponseCallback;
import com.huawei.hms.videoeditor.materials.HVETopColumnInfo;
import com.huawei.hms.videoeditor.materials.HVETopColumnRequest;
import com.huawei.hms.videoeditor.materials.HVETopColumnResponse;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.util.ArrayList;
import java.util.List;

public class TextBubblesViewModel extends AndroidViewModel {

    private static final String TAG = "TextBubblesViewModel";

    private final MutableLiveData<String> errorBubblesString = new MutableLiveData<>();

    private final MutableLiveData<String> emptyBubblesString = new MutableLiveData<>();

    private final MutableLiveData<List<CloudMaterialBean>> mBubblesMaterials = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadBubblesSuccess = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadBubblesFail = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadBubblesProgress = new MutableLiveData<>();

    private final MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    private MutableLiveData<String> fontColumn = new MutableLiveData<>();

    public TextBubblesViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadMaterials(Integer page) {
        HVEMaterialsResponseCallback contentListener = new HVEMaterialsResponseCallback<HVEChildColumnResponse>() {
            @Override
            public void onFinish(HVEChildColumnResponse response) {
                initMaterialsCutContentResp(response, R.string.result_empty);
            }

            @Override
            public void onUpdate(HVEChildColumnResponse response) {
                initMaterialsCutContentResp(response, R.string.result_empty);
            }

            @Override
            public void onError(Exception e) {
                errorBubblesString.postValue(getApplication().getString(R.string.result_illegal));
                String b = e.getMessage();
                SmartLog.e(TAG, b);
            }
        };

        HVEMaterialsResponseCallback columnListener = new HVEMaterialsResponseCallback<HVETopColumnResponse>() {
            @Override
            public void onFinish(HVETopColumnResponse response) {
                initMaterialsCutColumnResp(response, page, contentListener);
            }

            @Override
            public void onUpdate(HVETopColumnResponse response) {
                initMaterialsCutColumnResp(response, page, contentListener);
            }

            @Override
            public void onError(Exception e) {
                errorBubblesString.postValue(getApplication().getString(R.string.result_illegal));
                String b = e.getMessage();
                SmartLog.e(TAG, b);
            }
        };

        List<String> fatherColumnFont = new ArrayList<>();
        fatherColumnFont.add(HVEMaterialConstant.TEXT_BUBBLE_FATHER_COLUMN);
        HVETopColumnRequest request = new HVETopColumnRequest(fatherColumnFont);

        HVEMaterialsManager.getTopColumnById(request, columnListener);
    }

    private void initMaterialsCutContentResp(HVEChildColumnResponse response, int p) {
        List<HVEMaterialInfo> bubblesContents = response.getMaterialInfoList();
        if (!bubblesContents.isEmpty()) {
            boundaryPageData.postValue(response.isHasMoreItem());
            queryDownloadStatus(bubblesContents);
        } else {
            errorBubblesString.postValue(getApplication().getString(p));
        }
    }

    private void initMaterialsCutColumnResp(HVETopColumnResponse response, Integer page,
                                            HVEMaterialsResponseCallback<HVEChildColumnResponse> contentListener) {
        List<HVETopColumnInfo> columns = response.getColumnInfos();
        if (columns.isEmpty()) {
            return;
        }

        HVETopColumnInfo topColumnInfo = columns.get(0);
        if (topColumnInfo == null || topColumnInfo.getChildInfoList().isEmpty()) {
            errorBubblesString.postValue(getApplication().getString(R.string.result_empty));
            return;
        }

        List<HVEColumnInfo> fontColumns = topColumnInfo.getChildInfoList();

        if (fontColumns.size() > 0) {
            SmartLog.i(TAG, "return text font content category");
            HVEChildColumnRequest request =
                    new HVEChildColumnRequest(fontColumns.get(0).getColumnId(), page * PAGE_SIZE, PAGE_SIZE, false);

            fontColumn.postValue(fontColumns.get(0).getColumnName());

            HVEMaterialsManager.getChildColumnById(request, contentListener);
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
        mBubblesMaterials.postValue(list);
    }

    public void downloadColumn(int previousPosition, int position, int dataPosition, CloudMaterialBean cutContent) {
        MaterialsDownloadInfo downloadCanvasInfo = new MaterialsDownloadInfo();
        downloadCanvasInfo.setPreviousPosition(previousPosition);
        downloadCanvasInfo.setDataPosition(dataPosition);
        downloadCanvasInfo.setPosition(position);
        downloadCanvasInfo.setContentId(cutContent.getId());
        downloadCanvasInfo.setMaterialBean(cutContent);

        HVEDownloadMaterialRequest request = new HVEDownloadMaterialRequest(cutContent.getId());
        HVEMaterialsManager.downloadMaterialById(request, new HVEDownloadMaterialListener() {
            @Override
            public void onSuccess(String file) {
                downloadCanvasInfo.setMaterialLocalPath(file);
                mDownloadBubblesSuccess.postValue(downloadCanvasInfo);
            }

            @Override
            public void onProgress(int progress) {
                downloadCanvasInfo.setProgress(progress);
                mDownloadBubblesProgress.postValue(downloadCanvasInfo);
            }

            @Override
            public void onFailed(Exception exception) {
                SmartLog.e(TAG, exception.getMessage());
                mDownloadBubblesFail.postValue(downloadCanvasInfo);
            }

            @Override
            public void onAlreadyDownload(String file) {
                downloadCanvasInfo.setMaterialLocalPath(file);
                mDownloadBubblesSuccess.postValue(downloadCanvasInfo);
                SmartLog.i(TAG, "onDownloadExists");
            }
        });
    }

    public MutableLiveData<List<CloudMaterialBean>> getPageData() {
        return mBubblesMaterials;
    }

    public MutableLiveData<String> getErrorString() {
        return errorBubblesString;
    }

    public MutableLiveData<String> getEmptyString() {
        return emptyBubblesString;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadSuccess() {
        return mDownloadBubblesSuccess;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadFail() {
        return mDownloadBubblesFail;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadProgress() {
        return mDownloadBubblesProgress;
    }

    public LiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    public MutableLiveData<String> getFontColumn() {
        return fontColumn;
    }
}

