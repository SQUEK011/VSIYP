package com.example.vsiyp.ui.mediaeditor.texts.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.vsiyp.ui.common.bean.CloudMaterialBean;

public class TextPanelViewModel extends AndroidViewModel {

    private CloudMaterialBean mFontContent;

    private CloudMaterialBean mBubblesContent;

    private CloudMaterialBean mFlowerContent;

    private CloudMaterialBean mAnimaText;

    private MutableLiveData<String> animColumn = new MutableLiveData<>();

    private MutableLiveData<String> fontColumn = new MutableLiveData<>();

    public TextPanelViewModel(@NonNull Application application) {
        super(application);

    }

    public CloudMaterialBean getFontContent() {
        return mFontContent;
    }

    public void setFontContent(CloudMaterialBean mFontContent) {
        this.mFontContent = mFontContent;
    }

    public CloudMaterialBean getBubblesContent() {
        return mBubblesContent;
    }

    public void setBubblesContent(CloudMaterialBean mBubblesContent) {
        this.mBubblesContent = mBubblesContent;
    }

    public CloudMaterialBean getFlowerContent() {
        return mFlowerContent;
    }

    public void setFlowerContent(CloudMaterialBean mFlowerContent) {
        this.mFlowerContent = mFlowerContent;
    }

    public CloudMaterialBean getAnimaText() {
        return mAnimaText;
    }

    public void setAnimColumn(String animColumnStr) {
        animColumn.postValue(animColumnStr);
    }

    public MutableLiveData<String> getAnimColumn() {
        return animColumn;
    }

    public void setFontColumn(String fontColumnStr) {
        fontColumn.postValue(fontColumnStr);
    }

    public MutableLiveData<String> getFontColumn() {
        return fontColumn;
    }

    public void setAnimaText(CloudMaterialBean animaText) {
        this.mAnimaText = animaText;
    }
}

