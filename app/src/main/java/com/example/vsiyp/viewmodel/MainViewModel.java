package com.example.vsiyp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.huawei.hms.videoeditor.sdk.HVEProject;
import com.huawei.hms.videoeditor.sdk.HVEProjectManager;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<List<HVEProject>> mDraftProjects = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void initDraftProjects() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<HVEProject> draftProjects = HVEProjectManager.getDraftProjects();
                mDraftProjects.postValue(draftProjects);
            }
        }.start();
    }

    public MutableLiveData<List<HVEProject>> getDraftProjects() {
        return mDraftProjects;
    }
}
