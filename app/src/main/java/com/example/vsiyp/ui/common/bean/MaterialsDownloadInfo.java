package com.example.vsiyp.ui.common.bean;

public class MaterialsDownloadInfo {

    private int previousPosition = -1;

    private int position;

    private int dataPosition;

    private String contentId;

    private int progress;

    private CloudMaterialBean materialBean;

    private int state;

    public int getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public void setDataPosition(int dataPosition) {
        this.dataPosition = dataPosition;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMaterialLocalPath(String localPath) {
        if (this.materialBean != null) {
            this.materialBean.setLocalPath(localPath);
        }
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public CloudMaterialBean getMaterialBean() {
        return materialBean;
    }

    public void setMaterialBean(CloudMaterialBean materialBean) {
        this.materialBean = materialBean;
    }
}

