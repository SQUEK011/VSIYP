package com.example.vsiyp.ui.mediaeditor.animation.videoanimation.repository;

import com.example.vsiyp.ui.common.EditorManager;
import com.huawei.hms.videoeditor.sdk.HVETimeLine;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.asset.HVEAsset;
import com.huawei.hms.videoeditor.sdk.asset.HVEVisibleAsset;
import com.huawei.hms.videoeditor.sdk.effect.HVEEffect;

public class AnimationRepository {
    public static HVEEffect appendEnterAnimation(HVEAsset asset, HVEEffect.Options options, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.appendEnterAnimationEffect(options, duration);
    }

    public static HVEEffect appendLeaveAnimation(HVEAsset asset, HVEEffect.Options options, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.appendLeaveAnimationEffect(options, duration);
    }

    public static HVEEffect appendCycleAnimation(HVEAsset asset, HVEEffect.Options options, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.appendCycleAnimationEffect(options, duration);
    }

    public static boolean removeEnterAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HuaweiVideoEditor editor = EditorManager.getInstance().getEditor();
        HVETimeLine timeLine = EditorManager.getInstance().getTimeLine();
        if (editor == null || timeLine == null) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        editor.seekTimeLine(timeLine.getCurrentTime());
        return hveVisibleAsset.removeEnterAnimationEffect();
    }

    public static boolean removeLeaveAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HuaweiVideoEditor editor = EditorManager.getInstance().getEditor();
        HVETimeLine timeLine = EditorManager.getInstance().getTimeLine();
        if (editor == null || timeLine == null) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        editor.seekTimeLine(timeLine.getCurrentTime());
        return hveVisibleAsset.removeLeaveAnimationEffect();
    }

    public static boolean removeCycleAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HuaweiVideoEditor editor = EditorManager.getInstance().getEditor();
        HVETimeLine timeLine = EditorManager.getInstance().getTimeLine();
        if (editor == null || timeLine == null) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        editor.seekTimeLine(timeLine.getCurrentTime());
        return hveVisibleAsset.removeCycleAnimationEffect();
    }

    public static boolean setEnterAnimationDuration(HVEAsset asset, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.setEnterAnimationDuration(duration);
    }

    public static boolean setLeaveAnimationDuration(HVEAsset asset, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.setLeaveAnimationDuration(duration);
    }

    public static boolean setCycleAnimationDuration(HVEAsset asset, long duration) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return false;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.setCycleAnimationDuration(duration);
    }

    public static HVEEffect getEnterAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.getEnterAnimation();
    }

    public static HVEEffect getLeaveAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.getLeaveAnimation();
    }

    public static HVEEffect getCycleAnimation(HVEAsset asset) {
        if (!(asset instanceof HVEVisibleAsset)) {
            return null;
        }
        HVEVisibleAsset hveVisibleAsset = (HVEVisibleAsset) asset;
        return hveVisibleAsset.getCycleAnimation();
    }
}

