package com.example.vsiyp.ui.mediaeditor.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class VideoClipsPlayViewModel extends AndroidViewModel {
    private final MutableLiveData<Long> videoDuration = new MutableLiveData<>();

    private final MutableLiveData<Long> currentTime = new MutableLiveData<>();

    private final MutableLiveData<Boolean> playState = new MutableLiveData<>();

    private final MutableLiveData<Boolean> fullScreenState = new MutableLiveData<>();

    private final MutableLiveData<Boolean> showFrameAddDelete = new MutableLiveData<>();

    public VideoClipsPlayViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Long> getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Long duration) {
        this.videoDuration.postValue(duration);
    }

    public MutableLiveData<Long> getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime.postValue(currentTime == null ? Long.valueOf(-1L) : currentTime);
    }

    public MutableLiveData<Boolean> getPlayState() {
        return playState;
    }

    public void setPlayState(Boolean isPlay) {
        if (playState.getValue() == isPlay) {
            return;
        }
        playState.setValue(isPlay);
    }

    public MutableLiveData<Boolean> getFullScreenState() {
        return fullScreenState;
    }

    public void setFullScreenState(boolean isFullScreen) {
        fullScreenState.postValue(isFullScreen);
    }

    public MutableLiveData<Boolean> getShowFrameAddDelete() {
        return showFrameAddDelete;
    }

    public void setFrameAddDeletell(boolean isShowFrameAdd) {
        showFrameAddDelete.postValue(isShowFrameAdd);
    }
}

