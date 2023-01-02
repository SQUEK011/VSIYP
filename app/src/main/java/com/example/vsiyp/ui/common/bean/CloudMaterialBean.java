package com.example.vsiyp.ui.common.bean;


import com.huawei.hms.videoeditor.sdk.util.KeepOriginal;

import java.util.Objects;

@KeepOriginal
public class CloudMaterialBean {
    private String previewUrl;

    private String id;

    private String name;

    private String localPath;

    private long duration;

    private int type;

    private String categoryName;

    private int localDrawableId;

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLocalDrawableId() {
        return localDrawableId;
    }

    public void setLocalDrawableId(int localDrawableId) {
        this.localDrawableId = localDrawableId;
    }

    @Override
    public String toString() {
        return "CloudMaterialBean{" + "type=" + type + ", previewUrl='" + previewUrl + '\'' + ", id='" + id + '\''
                + ", name=" + name + ", localPath='" + localPath + '\'' + ", duration='" + duration + '\''
                + ", categoryName='" + categoryName + '\'' + ", localDrawableId='" + localDrawableId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CloudMaterialBean that = (CloudMaterialBean) o;
        return type == that.type && duration == that.duration && localDrawableId == that.localDrawableId
                && Objects.equals(previewUrl, that.previewUrl) && Objects.equals(localPath, that.localPath)
                && Objects.equals(categoryName, that.categoryName) && Objects.equals(name, that.name)
                && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(previewUrl, id, name, localPath, duration, type, categoryName, localDrawableId);
    }
}

