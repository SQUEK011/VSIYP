package com.example.vsiyp.ui.mediaeditor.repository;

import android.app.Application;

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

import java.util.ArrayList;
import java.util.List;

public class MaterialsRespository {

    private static final String TAG = "MaterialsRespository";

    public static final int RESULT_ILLEGAL = 0;

    public static final int RESULT_EMPTY = 1;

    public static final int DOWNLOAD_SUCCESS = 2;

    public static final int DOWNLOAD_FAIL = 3;

    public static final int DOWNLOAD_LOADING = 4;

    private MaterialsListener materialsListener;

    public MaterialsRespository(Application application) {
    }

    public void loadMaterials(String columnId, Integer page) {
        if (materialsListener == null) {
            return;
        }

        HVEChildColumnRequest request =
                new HVEChildColumnRequest(columnId, page * Constant.PAGE_SIZE, Constant.PAGE_SIZE, false);

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
                materialsListener.errorType(RESULT_ILLEGAL);
                String b = e.getMessage();
                SmartLog.e(TAG, b);
            }
        });
    }

    private void initMaterialsCutContentResp(HVEChildColumnResponse contentResp) {
        if (materialsListener == null || contentResp == null) {
            return;
        }
        List<HVEMaterialInfo> materialsCutContentList = contentResp.getMaterialInfoList();

        materialsListener.boundaryPageData(contentResp.isHasMoreItem());
        if (materialsCutContentList.size() > 0) {
            queryDownloadStatus(materialsCutContentList);
        } else {
            materialsListener.errorType(RESULT_EMPTY);
        }
    }

    private void queryDownloadStatus(List<HVEMaterialInfo> materialInfos) {
        if (materialsListener == null || materialInfos == null) {
            return;
        }
        if (materialInfos.isEmpty()) {
            return;
        }

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
        materialsListener.pageData(list);
    }

    public void downloadMaterials(int previousPosition, int position, CloudMaterialBean cutContent) {
        if (materialsListener == null || cutContent == null) {
            return;
        }
        MaterialsDownloadInfo downloadInfo = new MaterialsDownloadInfo();
        downloadInfo.setPreviousPosition(previousPosition);
        downloadInfo.setPosition(position);
        downloadInfo.setDataPosition(position);
        downloadInfo.setContentId(cutContent.getId());
        downloadInfo.setMaterialBean(cutContent);

        HVEDownloadMaterialRequest request = new HVEDownloadMaterialRequest(cutContent.getId());
        HVEMaterialsManager.downloadMaterialById(request, new HVEDownloadMaterialListener() {
            @Override
            public void onSuccess(String file) {
                downloadInfo.setMaterialLocalPath(file);
                downloadInfo.setState(DOWNLOAD_SUCCESS);
                materialsListener.downloadInfo(downloadInfo);
            }

            @Override
            public void onProgress(int progress) {
                downloadInfo.setProgress(progress);
                downloadInfo.setState(DOWNLOAD_LOADING);
                materialsListener.downloadInfo(downloadInfo);
            }

            @Override
            public void onFailed(Exception exception) {
                SmartLog.e(TAG, exception.getMessage());
                downloadInfo.setMaterialLocalPath("");
                downloadInfo.setState(DOWNLOAD_FAIL);
                materialsListener.downloadInfo(downloadInfo);
            }

            @Override
            public void onAlreadyDownload(String file) {
                downloadInfo.setMaterialLocalPath(file);
                downloadInfo.setState(DOWNLOAD_SUCCESS);
                materialsListener.downloadInfo(downloadInfo);
                SmartLog.i(TAG, "onDownloadExists");
            }
        });
    }

    public void setMaterialsListener(MaterialsListener materialsListener) {
        this.materialsListener = materialsListener;
    }
}

