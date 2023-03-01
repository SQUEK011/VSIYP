package com.example.vsiyp.ui.common.bean;

import com.huawei.hms.videoeditor.sdk.effect.HVEEffect;

public class FilterData {
    private String effectName;

    private String effectPath;

    private String mEffectId;

    private HVEEffect effect;

    private long startTime;

    private long endTime;

    private float mStrength;

    public FilterData(String effectName, String effectPath, String effectId, HVEEffect effect, long startTime,
                      long endTime, float strength) {
        this.effectName = effectName;
        this.effectPath = effectPath;
        this.mEffectId = effectId;
        this.effect = effect;
        this.startTime = startTime;
        this.endTime = endTime;
        this.mStrength = strength;
    }

    public String getEffectId() {
        return mEffectId;
    }

    public HVEEffect getEffect() {
        return effect;
    }

    public void setEffect(HVEEffect effect) {
        this.effect = effect;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public float getStrength() {
        return mStrength;
    }

    @Override
    public String toString() {
        return "FilterData{" + "effectName='" + effectName + '\'' + ", effectPath='" + effectPath + '\''
                + ", effectId='" + mEffectId + '\'' + ", effect=" + effect + ", startTime=" + startTime + ", endTime="
                + endTime + ", strength=" + mStrength + '}';
    }
}
