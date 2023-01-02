package com.example.vsiyp.ui.common.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityContainer {
    private static final ActivityContainer INSTANCE = new ActivityContainer();

    private static List<Activity> activityStack = new ArrayList<Activity>();

    private ActivityContainer() {
    }

    public static ActivityContainer getInstance() {
        return INSTANCE;
    }

    /**
     * addActivity
     *
     * @param aty aty
     */
    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    /**
     * removeActivity
     *
     * @param aty aty
     */
    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }
}
