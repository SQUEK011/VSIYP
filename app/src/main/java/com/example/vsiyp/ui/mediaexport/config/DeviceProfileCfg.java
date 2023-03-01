
package com.example.vsiyp.ui.mediaexport.config;

import androidx.annotation.NonNull;

import com.example.vsiyp.ui.common.utils.KeepOriginal;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

@KeepOriginal
public class DeviceProfileCfg {

    @SerializedName("profiles")
    @Expose
    List<ProfileItem> profiles = new ArrayList<>();

    public List<ProfileItem> getProfiles() {
        return profiles;
    }

    @KeepOriginal
    public static class ProfileItem {
        @SerializedName("cpus")
        @Expose
        List<String> cpus = new ArrayList<>();

        @SerializedName("memorySizeFrom")
        @Expose
        int memorySizeFrom = 0;

        @SerializedName("memorySizeTo")
        @Expose
        int memorySizeTo = 1024;

        @SerializedName("maxPipNum")
        @Expose
        int maxPipNum = 6;

        @SerializedName("exportThreadNum")
        @Expose
        int exportThreadNum = 2;

        @SerializedName("supportResolution")
        @Expose
        String supportResolution = "4k";

        @SerializedName("useSoftEncoder")
        @Expose
        boolean useSoftEncoder = false;

        @NonNull
        @Override
        public String toString() {
            return "ProfileItem{" +
                    "memorySizeFrom:" +
                    memorySizeFrom +
                    ", memorySizeTo:" +
                    memorySizeTo +
                    ", maxPipNum:" +
                    maxPipNum +
                    ", exportThreadNum:" +
                    exportThreadNum +
                    ", supportResolution:" +
                    supportResolution +
                    '}';
        }
    }
}