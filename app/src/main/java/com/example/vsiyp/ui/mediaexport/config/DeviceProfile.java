
package com.example.vsiyp.ui.mediaexport.config;

import android.content.Context;


import com.example.vsiyp.VideoEditorApplication;
import com.example.vsiyp.ui.common.utils.FileUtil;
import com.example.vsiyp.ui.common.utils.GsonUtils;
import com.example.vsiyp.ui.common.utils.KeepOriginal;
import com.example.vsiyp.ui.common.utils.MemoryInfoUtil;
import com.example.vsiyp.ui.common.utils.StringUtil;
import com.example.vsiyp.ui.mediaexport.utils.DeviceUtils;
import com.example.vsiyp.utils.SmartLog;

import java.util.List;

@KeepOriginal
public class DeviceProfile {
    private static final String TAG = "DeviceProfile";

    private boolean isUseSoftEncoder = false;

    private static final class Holder {
        static DeviceProfile sInstance = new DeviceProfile();
    }

    public static DeviceProfile getInstance() {
        return Holder.sInstance;
    }

    private DeviceProfile() {
        String cpu = DeviceUtils.getCPUModel();
        long sizeM = MemoryInfoUtil.getMemorySizeM();
        int sizeG = (int) Math.ceil((double) sizeM / 1024);

        Context context = VideoEditorApplication.getInstance().getContext();
        if (context == null) {
            return;
        }
        String profileContent = FileUtil.readAssetsFile(context, "device_profile.json");
        DeviceProfileCfg profileCfg = GsonUtils.fromJson(profileContent, DeviceProfileCfg.class);
        if (profileCfg == null) {
            SmartLog.w(TAG, "parse device_profile.json failed");
            return;
        }

        List<DeviceProfileCfg.ProfileItem> items = profileCfg.getProfiles();
        for (DeviceProfileCfg.ProfileItem item : items) {
            if (StringUtil.match(item.cpus, cpu) && isBetween(item.memorySizeFrom, item.memorySizeTo, sizeG)) {
                SmartLog.i(TAG, cpu + " matched profile " + item);
                isUseSoftEncoder = item.useSoftEncoder;
                return;
            }
        }
        SmartLog.i(TAG, "use default profile for " + cpu);
    }

    private static boolean isBetween(int from, int to, int current) {
        return from <= current && current <= to;
    }


    public boolean isUseSoftEncoder() {
        return isUseSoftEncoder;
    }


}