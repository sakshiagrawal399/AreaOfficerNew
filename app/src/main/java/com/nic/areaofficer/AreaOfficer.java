package com.nic.areaofficer;

import android.app.Application;

import com.nic.areaofficer.sharedPreference.AreaOfficerPreferenceManager;

public class AreaOfficer extends Application {
    private static AreaOfficer instance = null;
    private static AreaOfficerPreferenceManager preferenceManager;
    public synchronized static AreaOfficer getInstance() {
        return instance;
    }

    public static AreaOfficerPreferenceManager getPreferenceManager() {
        return preferenceManager;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferenceManager = new AreaOfficerPreferenceManager(getApplicationContext());

    }

}
