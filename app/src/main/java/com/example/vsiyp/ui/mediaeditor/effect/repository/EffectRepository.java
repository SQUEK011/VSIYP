package com.example.vsiyp.ui.mediaeditor.effect.repository;

import com.example.vsiyp.ui.common.EditorManager;
import com.example.vsiyp.ui.common.bean.CloudMaterialBean;
import com.example.vsiyp.ui.common.utils.LaneSizeCheckUtils;
import com.huawei.hms.videoeditor.sdk.HVETimeLine;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.effect.HVEEffect;
import com.huawei.hms.videoeditor.sdk.lane.HVEEffectLane;
import com.huawei.hms.videoeditor.sdk.lane.HVEVideoLane;

public class EffectRepository {
    public static HVEEffect addEffect(CloudMaterialBean content, long startTime) {
        if (content == null) {
            return null;
        }
        HuaweiVideoEditor editor = EditorManager.getInstance().getEditor();
        HVEVideoLane videoLane = EditorManager.getInstance().getMainLane();
        if (editor == null || videoLane == null) {
            return null;
        }
        HVEEffectLane effectLane = LaneSizeCheckUtils.getSpecialFreeLan(editor, startTime, startTime + 3000);
        if (effectLane == null) {
            return null;
        }
        HVEEffect effect = effectLane.appendEffect(
                new HVEEffect.Options(content.getName(), content.getId(), content.getLocalPath()), startTime, 3000);
        if (effect == null) {
            return null;
        }
        effect.setEndTime(Math.min(videoLane.getEndTime(), startTime + 3000));
        return effect;
    }

    public static boolean deleteEffect(HVEEffect effect) {
        if (effect == null) {
            return false;
        }
        HVETimeLine timeLine = EditorManager.getInstance().getTimeLine();
        HuaweiVideoEditor editor = EditorManager.getInstance().getEditor();
        if (timeLine == null || editor == null) {
            return false;
        }
        HVEEffectLane lane = timeLine.getEffectLane(effect.getLaneIndex());
        if (lane == null) {
            return false;
        }
        boolean isDelete = lane.removeEffect(effect.getIndex());
        editor.seekTimeLine(timeLine.getCurrentTime());
        return isDelete;
    }

    public static HVEEffect replaceEffect(HVEEffect lastEffect, CloudMaterialBean cutContent) {
        if (cutContent == null || lastEffect == null) {
            return null;
        }
        HuaweiVideoEditor editor = EditorManager.getInstance().getEditor();
        HVETimeLine timeLine = EditorManager.getInstance().getTimeLine();
        if (editor == null || timeLine == null) {
            return null;
        }

        int lastEffectIndex = lastEffect.getIndex();
        int lastEffectLaneIndex = lastEffect.getLaneIndex();

        if (lastEffectIndex < 0 || lastEffectLaneIndex < 0) {
            return null;
        }

        HVEEffectLane effectLane = timeLine.getEffectLane(lastEffectLaneIndex);
        if (effectLane == null) {
            return null;
        }

        long lastStartTime = lastEffect.getStartTime();
        long lastEndTime = lastEffect.getEndTime();

        lastEffect = effectLane.replaceEffect(
                new HVEEffect.Options(cutContent.getName(), cutContent.getId(), cutContent.getLocalPath()), lastEffectIndex,
                lastStartTime, lastEndTime - lastStartTime);

        return lastEffect;
    }
}

