package com.example.vsiyp.ui.mediaeditor.pip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.vsiyp.ui.common.bean.MediaData;
import com.example.vsiyp.ui.common.utils.BigDecimalUtils;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.asset.HVEVisibleAsset;

public class PictureInPicViewModel extends AndroidViewModel {

    private HuaweiVideoEditor mEditor;

    private MediaData mSelectMediaData;

    private int mSelectPosition = -1;

    private MutableLiveData<Integer> mSelectLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> mUnSelectLiveData = new MutableLiveData<>();

    public PictureInPicViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getSelectLiveData() {
        return mSelectLiveData;
    }

    public MutableLiveData<Integer> getUnSelectLiveData() {
        return mUnSelectLiveData;
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setEditor(HuaweiVideoEditor editor) {
        this.mEditor = editor;
    }

    public MediaData getSelectMediaData() {
        return mSelectMediaData;
    }

    public void setSelectLiveData(int position, MediaData mediaData) {
        if (mSelectMediaData != null) {
            if (mSelectMediaData.getPath().equals(mediaData.getPath())) {
                mSelectPosition = -1;
                mSelectMediaData = null;
                mUnSelectLiveData.postValue(position);
            } else {
                mUnSelectLiveData.postValue(mSelectPosition);
                mSelectPosition = position;
                mSelectMediaData = mediaData;
                mSelectLiveData.postValue(mSelectPosition);
            }
        } else {
            mSelectPosition = position;
            mSelectMediaData = mediaData;
            mSelectLiveData.postValue(mSelectPosition);
        }
    }

    public void setBlendMode(HVEVisibleAsset asset, int mode) {
        if (asset == null) {
            return;
        }
        asset.setBlendMode(mode);

        if (mEditor != null && mEditor.getTimeLine() != null) {
            mEditor.seekTimeLine(mEditor.getTimeLine().getCurrentTime());
        }
    }

    public void setOpacityValue(HVEVisibleAsset asset, int process) {
        if (asset == null) {
            return;
        }
        float value = (float) BigDecimalUtils.div(process, 100);
        asset.setOpacityValue(value);

        if (mEditor != null && mEditor.getTimeLine() != null) {
            mEditor.seekTimeLine(mEditor.getTimeLine().getCurrentTime());
        }
    }
}

