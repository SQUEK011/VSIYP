package com.example.vsiyp.ui.mediaeditor.effect.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.mediaeditor.effect.repository.EffectRepository;
import com.example.vsiyp.ui.mediaeditor.repository.ColumnsListener;
import com.example.vsiyp.ui.mediaeditor.repository.ColumnsRespository;
import com.huawei.hms.videoeditor.materials.HVEColumnInfo;
import com.huawei.hms.videoeditor.sdk.effect.HVEEffect;

import java.util.List;

public class EffectPanelViewModel extends ViewModel {

    private ColumnsRespository columnsRespository;

    private MutableLiveData<List<HVEColumnInfo>> columns = new MutableLiveData<>();

    private MutableLiveData<CloudMaterialBean> selectData = new MutableLiveData<>();

    private MutableLiveData<Boolean> removeData = new MutableLiveData<>();

    private MutableLiveData<Integer> errorType = new MutableLiveData<>();

    public MutableLiveData<Boolean> cancelSelected = new MutableLiveData<>();

    public MutableLiveData<HVEEffect> getSelectEffect() {
        return selectEffect == null ? new MediatorLiveData<>() : selectEffect;
    }

    public void setSelectEffect(HVEEffect selectEffect) {
        if (this.selectEffect == null) {
            return;
        }
        this.selectEffect.postValue(selectEffect);
    }

    private MutableLiveData<HVEEffect> selectEffect;

    public EffectPanelViewModel() {
        columnsRespository = new ColumnsRespository();
        columnsRespository.seatColumnsListener(columnsListener);
    }

    public MutableLiveData<List<HVEColumnInfo>> getColumns() {
        return columns;
    }

    public MutableLiveData<CloudMaterialBean> getSelectData() {
        return selectData;
    }

    public MutableLiveData<Boolean> getRemoveData() {
        return removeData;
    }

    public MutableLiveData<Integer> getErrorType() {
        return errorType;
    }

    public MutableLiveData<Boolean> getCancelSelected() {
        return cancelSelected;
    }

    public void setSelectCutContent(CloudMaterialBean mCutContent) {
        selectData.postValue(mCutContent);
    }

    public HVEEffect addEffect(CloudMaterialBean content, long startTime) {
        if (content == null) {
            return null;
        }
        return EffectRepository.addEffect(content, startTime);
    }

    public boolean deleteEffect(HVEEffect effect) {
        if (effect == null) {
            return false;
        }
        return EffectRepository.deleteEffect(effect);
    }

    public HVEEffect replaceEffect(HVEEffect lastEffect, CloudMaterialBean cutContent, long startTime) {
        if (cutContent == null) {
            return null;
        }
        HVEEffect effect;
        if (lastEffect == null) {
            effect = addEffect(cutContent, startTime);
            setSelectEffect(effect);
            return effect;
        }
        effect = EffectRepository.replaceEffect(lastEffect, cutContent);
        setSelectEffect(effect);
        return effect;
    }

    public void initColumns(String type) {
        columnsRespository.initColumns(type);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        columnsRespository = null;
        columnsListener = null;
    }

    private ColumnsListener columnsListener = new ColumnsListener() {
        @Override
        public void columsData(List<HVEColumnInfo> materialsCutContentList) {
            columns.postValue(materialsCutContentList);
        }

        @Override
        public void errorType(int type) {
            errorType.postValue(type);
        }
    };
}

