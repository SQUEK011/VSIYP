package com.example.vsiyp;

import android.content.Context;

public class VideoEditorApplication {
    private static VideoEditorApplication INSTANCE = new VideoEditorApplication();

    private Context context;

    private VideoEditorApplication() {

    }

    public static VideoEditorApplication getInstance() {
        return INSTANCE;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
