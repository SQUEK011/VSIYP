package com.example.vsiyp.ui.mediaeditor.audio.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vsiyp.R;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.bean.Constant;
import com.example.vsiyp.ui.common.bean.MaterialsDownloadInfo;
import com.example.vsiyp.ui.common.utils.StringUtil;
import com.huawei.hms.videoeditor.materials.HVEChildColumnRequest;
import com.huawei.hms.videoeditor.materials.HVEChildColumnResponse;
import com.huawei.hms.videoeditor.materials.HVEDownloadMaterialListener;
import com.huawei.hms.videoeditor.materials.HVEDownloadMaterialRequest;
import com.huawei.hms.videoeditor.materials.HVELocalMaterialInfo;
import com.huawei.hms.videoeditor.materials.HVEMaterialInfo;
import com.huawei.hms.videoeditor.materials.HVEMaterialsManager;
import com.huawei.hms.videoeditor.materials.HVEMaterialsResponseCallback;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;
import com.huawei.hms.videoeditor.sdk.v1.AssetBeanAnalyer;

import java.util.ArrayList;
import java.util.List;

public class SoundEffectItemViewModel extends AndroidViewModel {

    private static final String TAG = "SoundEffectItemViewModel";

    private final MutableLiveData<String> errorString = new MutableLiveData<>();

    private final MutableLiveData<String> emptyString = new MutableLiveData<>();

    private final MutableLiveData<List<CloudMaterialBean>> mSoundEffectMaterials = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadSuccess = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadFail = new MutableLiveData<>();

    private final MutableLiveData<MaterialsDownloadInfo> mDownloadProgress = new MutableLiveData<>();

    private final MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    public SoundEffectItemViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getErrorString() {
        return errorString;
    }

    public MutableLiveData<String> getEmptyString() {
        return emptyString;
    }

    public void loadMaterials(String cloumnId, Integer page) {
        HVEChildColumnRequest request =
                new HVEChildColumnRequest(cloumnId, page * Constant.PAGE_SIZE, Constant.PAGE_SIZE, false);

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

    private void initMaterialsCutContentResp(HVEChildColumnResponse response) {
        List<HVEMaterialInfo> soundEffectCutContents = response.getMaterialInfoList();

        boundaryPageData.postValue(response.isHasMoreItem());
        if (soundEffectCutContents.size() > 0) {
            queryDownloadStatus(soundEffectCutContents);
        } else {
            emptyString.postValue(getApplication().getString(R.string.result_empty));
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
                AssetBeanAnalyer assetBeanAnalyer = new AssetBeanAnalyer(localMaterialInfo.getMaterialPath());
                materialInfo.setLocalPath(assetBeanAnalyer.getAssetPath());
            }

            materialInfo.setPreviewUrl(hveMaterialInfo.getPreviewUrl());
            materialInfo.setId(hveMaterialInfo.getMaterialId());
            materialInfo.setName(hveMaterialInfo.getMaterialName());

            list.add(materialInfo);
        }

        mSoundEffectMaterials.postValue(list);
    }

    public void downloadColumn(int previousPosition, int position, CloudMaterialBean cutContent) {
        MaterialsDownloadInfo downloadSoundEffectInfo = new MaterialsDownloadInfo();
        downloadSoundEffectInfo.setPreviousPosition(previousPosition);
        downloadSoundEffectInfo.setDataPosition(position);
        downloadSoundEffectInfo.setPosition(position);
        downloadSoundEffectInfo.setContentId(cutContent.getId());
        downloadSoundEffectInfo.setMaterialBean(cutContent);

        HVEDownloadMaterialRequest request = new HVEDownloadMaterialRequest(cutContent.getId());
        HVEMaterialsManager.downloadMaterialById(request, new HVEDownloadMaterialListener() {
            @Override
            public void onSuccess(String file) {
                AssetBeanAnalyer analyer = AssetBeanAnalyer.create(file);
                if (analyer != null) {
                    downloadSoundEffectInfo.setMaterialLocalPath(analyer.getAssetPath());
                }

                mDownloadSuccess.postValue(downloadSoundEffectInfo);
            }

            @Override
            public void onProgress(int progress) {
                SmartLog.d(TAG, "onDownloading" + progress + "---" + cutContent.getId());
                downloadSoundEffectInfo.setProgress(progress);
                mDownloadProgress.postValue(downloadSoundEffectInfo);
            }

            @Override
            public void onFailed(Exception exception) {
                SmartLog.e(TAG, exception.getMessage());
                mDownloadFail.postValue(downloadSoundEffectInfo);
            }

            @Override
            public void onAlreadyDownload(String file) {
                downloadSoundEffectInfo.setMaterialLocalPath(file);
                mDownloadSuccess.postValue(downloadSoundEffectInfo);
                SmartLog.i(TAG, "onDownloadExists");
            }
        });
    }

    public MutableLiveData<List<CloudMaterialBean>> getPageData() {
        return mSoundEffectMaterials;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadSuccess() {
        return mDownloadSuccess;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadFail() {
        return mDownloadFail;
    }

    public MutableLiveData<MaterialsDownloadInfo> getDownloadProgress() {
        return mDownloadProgress;
    }

    public LiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

}
