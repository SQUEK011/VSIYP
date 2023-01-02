package com.example.vsiyp.ui.mediaeditor.repository;

import com.huawei.hms.videoeditor.materials.HVEColumnInfo;

import java.util.List;

public interface ColumnsListener {
    void columsData(List<HVEColumnInfo> materialsCutContentList);

    void errorType(int type);
}
