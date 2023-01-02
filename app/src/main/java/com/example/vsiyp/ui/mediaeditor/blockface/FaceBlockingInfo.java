package com.example.vsiyp.ui.mediaeditor.blockface;

import android.graphics.Bitmap;

import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.huawei.hms.videoeditor.sdk.bean.HVEAIFaceTemplate;

public class FaceBlockingInfo {
    private int id;

    private String type;

    private Bitmap bitmap;

    private Long firstTimeStamp;

    private String localSticker;

    private boolean isSelected = false;

    private boolean isGetFocus = false;

    private boolean isMosaic = true;

    private boolean isShowGray = true;

    private CloudMaterialBean materialsCutContent;

    private HVEAIFaceTemplate faceTemplates;

    public FaceBlockingInfo() {
    }

    public FaceBlockingInfo(String type, String localSticker, CloudMaterialBean materialsCutContent,
                            boolean isShowGray) {
        this.type = type;
        this.isShowGray = isShowGray;
        this.localSticker = localSticker;
        this.materialsCutContent = materialsCutContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Long getFirstTimeStamp() {
        return firstTimeStamp;
    }

    public void setFirstTimeStamp(Long firstTimeStamp) {
        this.firstTimeStamp = firstTimeStamp;
    }

    public String getLocalSticker() {
        return localSticker;
    }

    public void setLocalSticker(String localSticker) {
        this.localSticker = localSticker;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isGetFocus() {
        return isGetFocus;
    }

    public void setGetFocus(boolean getFocus) {
        isGetFocus = getFocus;
    }

    public boolean isMosaic() {
        return isMosaic;
    }

    public void setMosaic(boolean mosaic) {
        isMosaic = mosaic;
    }

    public boolean isShowGray() {
        return isShowGray;
    }

    public void setShowGray(boolean showGray) {
        isShowGray = showGray;
    }

    public CloudMaterialBean getMaterialsCutContent() {
        return materialsCutContent;
    }

    public void setMaterialsCutContent(CloudMaterialBean materialsCutContent) {
        this.materialsCutContent = materialsCutContent;
    }

    public HVEAIFaceTemplate getFaceTemplates() {
        return faceTemplates;
    }

    public void setFaceTemplates(HVEAIFaceTemplate faceTemplates) {
        this.faceTemplates = faceTemplates;
    }
}

