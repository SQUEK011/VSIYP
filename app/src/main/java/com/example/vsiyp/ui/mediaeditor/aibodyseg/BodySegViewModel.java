package com.example.vsiyp.ui.mediaeditor.aibodyseg;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.vsiyp.utils.SmartLog;
import com.huawei.hms.videoeditor.sdk.ai.HVEAIInitialCallback;
import com.huawei.hms.videoeditor.sdk.ai.HVEAIProcessCallback;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVisibleAsset;

public class BodySegViewModel extends AndroidViewModel {
    private static final String TAG = "SegmentationViewModel";

    private MutableLiveData<Integer> bodySeg = new MutableLiveData<>();

    private HVEAsset selectedAsset;

    public BodySegViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getBodySegEnter() {
        return bodySeg;
    }

    public void setBodySegEnter(Integer segmentationEnter) {
        this.bodySeg.postValue(segmentationEnter);
    }

    public void setSelectedAsset(HVEAsset selectedAsset) {
        this.selectedAsset = selectedAsset;
    }

    public void initializeBodySeg(int segPart, HVEAIInitialCallback callback){
        if(selectedAsset == null){
            SmartLog.e(TAG, "selectedAsset is null");
            return;
        }
        ((HVEVisibleAsset) selectedAsset).initBodySegEngine(segPart, callback);
    }

    public void bodySegDetect(HVEAIProcessCallback callback){
        if(selectedAsset == null){
            SmartLog.e(TAG, "selectedAsset is null");
            return;
        }
        ((HVEVisibleAsset) selectedAsset).addBodySegEffect(callback);
    }

    public boolean removeCurrentEffect() {
        if (selectedAsset == null) {
            SmartLog.e(TAG, "selectedAsset is null");
            return false;
        }
        return ((HVEVisibleAsset) selectedAsset).removeBodySegEffect();
    }

    public void interruptCurrentEffect() {
        if (selectedAsset == null) {
            SmartLog.e(TAG, "selectedAsset is null");
            return;
        }
        ((HVEVisibleAsset) selectedAsset).interruptBodySegEffect();
    }

    public void releaseBodySegEngine() {
        if (selectedAsset == null) {
            SmartLog.e(TAG, "selectedAsset is null");
            return;
        }
        ((HVEVisibleAsset) selectedAsset).releaseBodySegEngine();
    }
}
