package com.example.vsiyp.ui.mediaeditor.menu;

import androidx.annotation.NonNull;

import java.util.List;

public class EditMenuBean implements Cloneable {
    private int id;

    private int parentId;

    private String name;

    private String cnName;

    private String drawableName;

    private int enable;

    private int isAdvanced;

    private List<EditMenuBean> children;

    private List<EditMenuBean> operates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getDrawableName() {
        return drawableName;
    }

    public void setDrawableName(String drawableName) {
        this.drawableName = drawableName;
    }

    public boolean isEnable() {
        return enable == 1;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getIsAdvanced() {
        return isAdvanced;
    }

    public void setIsAdvanced(int isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    public List<EditMenuBean> getChildren() {
        return children;
    }

    public void setChildren(List<EditMenuBean> children) {
        this.children = children;
    }

    public List<EditMenuBean> getOperates() {
        return operates;
    }

    public void setOperates(List<EditMenuBean> operates) {
        this.operates = operates;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "EditMenuBean{" + "id=" + id + ", parentId=" + parentId + ", name='" + name + '\'' + ", cnName='"
                + cnName + '\'' + ", drawableName='" + drawableName + '\'' + ", enable=" + enable + ", isAdvanced="
                + isAdvanced + ", children=" + children + ", operates=" + operates + '}';
    }
}

