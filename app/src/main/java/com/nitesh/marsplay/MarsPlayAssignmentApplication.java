package com.nitesh.marsplay;

import android.app.Application;

import com.cloudinary.android.MediaManager;


public class MarsPlayAssignmentApplication extends Application {
    private static MarsPlayAssignmentApplication marsPlayAssignmentApplication;

    public MarsPlayAssignmentApplication() {
        marsPlayAssignmentApplication = this;
    }

    public static synchronized MarsPlayAssignmentApplication getInstance() {
        if (marsPlayAssignmentApplication == null) {
            marsPlayAssignmentApplication = new MarsPlayAssignmentApplication();
        }
        return marsPlayAssignmentApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MediaManager.init(this);
    }
}
