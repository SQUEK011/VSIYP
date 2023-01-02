package com.example.vsiyp.ui.mediaeditor.repository;

import com.example.vsiyp.ui.common.bean.CloudMaterialBean;

public class LoadUrlEvent {
    private CloudMaterialBean content;

    private int previousPosition;

    private int position;

    public LoadUrlEvent() {

    }

    public CloudMaterialBean getContent() {
        return content;
    }

    public void setContent(CloudMaterialBean content) {
        this.content = content;
    }

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

    @Override
    public String toString() {
        return "LoadUrlEvent{" + "content=" + content + ", previousPosition=" + previousPosition + ", position="
                + position + '}';
    }
}

