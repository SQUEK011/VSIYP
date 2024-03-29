package com.example.vsiyp.ui.mediaeditor.texts.repository;

import com.example.vsiyp.ui.common.EditorManager;
import com.huawei.hms.videoeditor.sdk.HVETimeLine;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVisibleAsset;
import com.huawei.hms.videoeditor.sdk.effect.HVEEffect;

public class TextAnimationRepository {
    public static HVEEffect appendEnterAnimation(HVEAsset hveAsset, HVEEffect.Options options, long duration) {
        if (!(hveAsset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) hveAsset;
        return visibleAsset.appendEnterAnimationEffect(options, duration);
    }

    public static HVEEffect appendLeaveAnimation(HVEAsset hveAsset, HVEEffect.Options options, long duration) {
        if (!(hveAsset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) hveAsset;
        return visibleAsset.appendLeaveAnimationEffect(options, duration);
    }

    public static HVEEffect appendCycleAnimation(HVEAsset hveAsset, HVEEffect.Options options, long duration) {
        if (!(hveAsset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) hveAsset;
        return visibleAsset.appendCycleAnimationEffect(options, duration);
    }

    public static boolean removeEnterAnimation(HVEAsset hveAsset) {
        if (!(hveAsset instanceof HVEVisibleAsset)) {
            return false;
        }
        HuaweiVideoEditor huaweiVideoEditor = EditorManager.getInstance().getEditor();
        HVETimeLine hveTimeLine = EditorManager.getInstance().getTimeLine();
        if (huaweiVideoEditor == null || hveTimeLine == null) {
            return false;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) hveAsset;
        huaweiVideoEditor.seekTimeLine(hveTimeLine.getCurrentTime());
        return visibleAsset.removeEnterAnimationEffect();
    }

    public static boolean removeLeaveAnimation(HVEAsset hveAsset) {
        if (!(hveAsset instanceof HVEVisibleAsset)) {
            return false;
        }
        HuaweiVideoEditor huaweiVideoEditor = EditorManager.getInstance().getEditor();
        HVETimeLine hveTimeLine = EditorManager.getInstance().getTimeLine();
        if (huaweiVideoEditor == null || hveTimeLine == null) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) hveAsset;
        huaweiVideoEditor.seekTimeLine(hveTimeLine.getCurrentTime());
        return hveVisibleAsset.removeLeaveAnimationEffect();
    }

    public static boolean removeCycleAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HuaweiVideoEditor huaweiVideoEditor = EditorManager.getInstance().getEditor();
        HVETimeLine timeLine = EditorManager.getInstance().getTimeLine();
        if (huaweiVideoEditor == null || timeLine == null) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        huaweiVideoEditor.seekTimeLine(timeLine.getCurrentTime());
        return hveVisibleAsset.removeCycleAnimationEffect();
    }

    public static boolean setEnterAnimationDuration(HVEAsset asset, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) asset;
        return visibleAsset.setEnterAnimationDuration(duration);
    }

    public static boolean setLeaveAnimationDuration(HVEAsset asset, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) asset;
        return visibleAsset.setLeaveAnimationDuration(duration);
    }

    public static boolean setCycleAnimationDuration(HVEAsset asset, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) asset;
        return visibleAsset.setCycleAnimationDuration(duration);
    }

    public static HVEEffect getEnterAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) asset;
        return visibleAsset.getEnterAnimation();
    }

    public static HVEEffect getLeaveAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) asset;
        return visibleAsset.getLeaveAnimation();
    }

    public static HVEEffect getCycleAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset visibleAsset = (HVEVisibleAsset) asset;
        return visibleAsset.getCycleAnimation();
    }
}

