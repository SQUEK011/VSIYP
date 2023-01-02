package com.example.vsiyp.ui.mediaeditor.preview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.vsiyp.ui.common.EditorManager;
import com.huawei.hms.videoeditor.sdk.HVETimeLine;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;

public class ColorCutViewModel extends AndroidViewModel {
    private static final String TAG = "ColorCutViewModel";

    private MutableLiveData<Boolean> isShow = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> isMove = new MutableLiveData<>(false);

    private MutableLiveData<HVEAsset> asset = new MutableLiveData<>();

    private int strength;

    private int color;

    private int shadow;

    public ColorCutViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean b) {
        this.isShow.postValue(b);
    }

    public MutableLiveData<Boolean> getMove() {
        return isMove;
    }

    public void setMove(boolean b) {
        this.isMove.postValue(b);
    }

    public MutableLiveData<HVEAsset> getAsset() {
        return asset;
    }

    public void setAsset(HVEAsset asset) {
        if (asset != null && this.asset.getValue() != asset) {
            strength = 0;
            shadow = 0;
        }
        this.asset.postValue(asset);
    }

    public void setEffectColor(int color) {
        this.color = color;
        refresh();
    }

    public void setEffectStrength(int strength) {
        this.strength = strength;
        refresh();
    }

    public void setShadow(int shadow) {
        this.shadow = shadow;
        refresh();
    }

    public int getStrength() {
        return strength;
    }

    public int getShadow() {
        return shadow;
    }

    public void refresh() {
        HVETimeLine timeLine = EditorManager.getInstance().getTimeLine();
        HuaweiVideoEditor editor = EditorManager.getInstance().getEditor();
        if (timeLine == null || editor == null) {
            return;
        }
        editor.refresh(timeLine.getCurrentTime());
    }
}

