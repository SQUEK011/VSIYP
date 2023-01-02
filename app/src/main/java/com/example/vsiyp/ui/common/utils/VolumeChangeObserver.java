package com.example.vsiyp.ui.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.huawei.secure.android.common.intent.SafeIntent;

import java.lang.ref.WeakReference;

public class VolumeChangeObserver {
    private static final String ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";

    private static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";

    private Context aContext;

    private OnVolumeChangeListener aOnVolumeChangeListener;

    private VolumeReceiver aVolumeReceiver;

    private AudioManager aAudioManager;

    private static VolumeChangeObserver aVolumeChangeObserver = null;

    public static VolumeChangeObserver getInstace(Context context) {
        synchronized (VolumeChangeObserver.class) {
            if (aVolumeChangeObserver == null) {
                aVolumeChangeObserver = new VolumeChangeObserver(context);
            }
        }
        return aVolumeChangeObserver;
    }

    public VolumeChangeObserver(Context context) {
        aContext = context;
        aAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int getCurrentVolume() {
        return aAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void registerVolumeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_VOLUME_CHANGED);
        aVolumeReceiver = new VolumeReceiver(this);
        aContext.registerReceiver(aVolumeReceiver, intentFilter);
    }

    public void unregisterVolumeReceiver() {
        if (aVolumeReceiver != null) {
            aContext.unregisterReceiver(aVolumeReceiver);
        }
        aVolumeReceiver = null;
        aOnVolumeChangeListener = null;
    }

    public void setOnVolumeChangeListener(OnVolumeChangeListener listener) {
        this.aOnVolumeChangeListener = listener;
    }

    public interface OnVolumeChangeListener {
        void onVolumeChange(int volume);
    }

    private static class VolumeReceiver extends BroadcastReceiver {
        private WeakReference<VolumeChangeObserver> mObserver;

        VolumeReceiver(VolumeChangeObserver observer) {
            mObserver = new WeakReference<>(observer);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isReceiveVolumeChange(intent)) {
                if (mObserver == null) {
                    return;
                }
                VolumeChangeObserver volumeChangeObserver = mObserver.get();
                if (volumeChangeObserver == null) {
                    return;
                }
                OnVolumeChangeListener listener = volumeChangeObserver.aOnVolumeChangeListener;
                if (listener == null) {
                    return;
                }
                listener.onVolumeChange(volumeChangeObserver.getCurrentVolume());
            }
        }

        private boolean isReceiveVolumeChange(Intent intent) {
            if (intent == null) {
                return false;
            }
            SafeIntent safeIntent = new SafeIntent(intent);
            return safeIntent.getAction() != null && safeIntent.getAction().equals(ACTION_VOLUME_CHANGED)
                    && safeIntent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC;
        }
    }
}
