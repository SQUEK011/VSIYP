package com.example.vsiyp.ui.mediaeditor.filter.aifilter.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.vsiyp.ui.common.EditorManager;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.huawei.hms.videoeditor.materials.HVELocalMaterialInfo;
import com.huawei.hms.videoeditor.materials.HVEMaterialsManager;
import com.huawei.hms.videoeditor.sdk.HVETimeLine;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.ai.HVEExclusiveFilter;
import com.huawei.hms.videoeditor.sdk.effect.HVEEffect;
import com.huawei.hms.videoeditor.sdk.lane.HVEEffectLane;

import java.util.List;

public class ExclusiveFilterPanelViewModel extends AndroidViewModel {
    public static final String FILTER_TYPE_SINGLE = "filterSingle";

    public static final String FILTER_TYPE_CLONE = "filterClone";

    public static final int FILTER_CHANGE = 0;

    public static final int FILTER_LAST = 1;

    public ExclusiveFilterPanelViewModel(@NonNull Application application) {
        super(application);
    }

    public List<HVELocalMaterialInfo> queryAllMaterialsByType(int type) throws ClassCastException {
        List<HVELocalMaterialInfo> cutContents = HVEMaterialsManager.queryLocalMaterialByType(type);
        return cutContents;
    }

    public void updateFilter(CloudMaterialBean materialsCutContent) throws ClassCastException {
        HVEExclusiveFilter filter = new HVEExclusiveFilter();
        filter.updateEffectName(materialsCutContent.getId(), materialsCutContent.getName());
    }

    public void deleteFilterData(String contentId) throws ClassCastException {
        if (TextUtils.isEmpty(contentId)) {
            return;
        }

        HVEExclusiveFilter filter = new HVEExclusiveFilter();
        filter.deleteExclusiveEffect(contentId);
    }

    public void deleteFilterEffect(HVEEffect effect) {
        HVETimeLine mTimeLine = EditorManager.getInstance().getTimeLine();
        HuaweiVideoEditor mEditor = EditorManager.getInstance().getEditor();
        if (mEditor == null || mTimeLine == null || effect == null) {
            return;
        }
        HVEEffectLane lane = mTimeLine.getEffectLane(effect.getLaneIndex());
        if (lane == null) {
            return;
        }
        lane.removeEffect(effect.getIndex());
        mEditor.seekTimeLine(mTimeLine.getCurrentTime());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

