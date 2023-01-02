package com.example.vsiyp.ui.mediaeditor.repository;

import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.bean.MaterialsDownloadInfo;

import java.util.List;

public interface MaterialsListener {
    void pageData(List<CloudMaterialBean> materialsCutContentList);

    void errorType(int type);

    void boundaryPageData(boolean isBoundaryPageData);

    void downloadInfo(MaterialsDownloadInfo materialsDownloadInfo);

    void loadUrlEvent(LoadUrlEvent mLoadUrlEvent);
}
