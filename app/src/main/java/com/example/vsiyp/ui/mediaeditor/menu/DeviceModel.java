package com.example.vsiyp.ui.mediaeditor.menu;


import com.huawei.hms.videoeditor.sdk.util.KeepOriginal;

@KeepOriginal
public class DeviceModel {

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DeviceModel{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }

    @Override
    public boolean equals(Object deviceModelObject) {
        if (this == deviceModelObject) {
            return true;
        }
        if (deviceModelObject == null || getClass() != deviceModelObject.getClass()) {
            return false;
        }
        DeviceModel that = (DeviceModel) deviceModelObject;
        return key.equals(that.key) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

