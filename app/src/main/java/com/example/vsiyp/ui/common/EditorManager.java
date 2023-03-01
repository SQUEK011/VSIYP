package com.example.vsiyp.ui.common;

import com.huawei.hms.videoeditor.sdk.HVETimeLine;
import com.huawei.hms.videoeditor.sdk.HuaweiVideoEditor;
import com.huawei.hms.videoeditor.sdk.lane.HVEVideoLane;

import java.lang.ref.WeakReference;

public class EditorManager {
    private WeakReference<HuaweiVideoEditor> editor;

    private static class EditorManagerHolder {
        private static final EditorManager INSTANCE = new EditorManager();
    }

    private EditorManager() {

    }

    public static EditorManager getInstance() {
        return EditorManagerHolder.INSTANCE;
    }

    public void setEditor(HuaweiVideoEditor mEditor) {
        this.editor = new WeakReference<>(mEditor);
    }

    public HuaweiVideoEditor getEditor() {
        if (editor == null) {
            return null;
        }
        return editor.get();
    }

    public HVETimeLine getTimeLine() {
        if (editor == null) {
            return null;
        }
        HuaweiVideoEditor videoEditor = editor.get();
        if (videoEditor == null) {
            return null;
        }
        return videoEditor.getTimeLine();
    }

    public HVEVideoLane getMainLane() {
        if (editor == null) {
            return null;
        }
        HuaweiVideoEditor videoEditor = editor.get();
        if (videoEditor == null) {
            return null;
        }
        HVETimeLine timeline = videoEditor.getTimeLine();
        if (timeline == null) {
            return null;
        }
        if (timeline.getAllVideoLane().size() == 0) {
            return null;
        }
        return timeline.getVideoLane(0);
    }

    public synchronized void recyclerEditor() {
        if (editor == null) {
            return;
        }

        HuaweiVideoEditor huaweiVideoEditor = editor.get();
        if (huaweiVideoEditor != null) {
            huaweiVideoEditor.stopEditor();
            editor = null;
        }
    }
}
