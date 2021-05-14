package com.test.alxad.demo;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.alxad.api.AlxAdSDK;
import com.anythink.core.api.ATSDK;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ATSDK.init(getApplicationContext(), AppConfig.ALX_APP_ID_TOPON, AppConfig.ALX_KEY_TOPON);
            ATSDK.setNetworkLogDebug(true);
            AlxAdSDK.init(this, AppConfig.ALX_LICENSE, AppConfig.ALX_KEY, AppConfig.ALX_APP_ID);
            AlxAdSDK.setDebug(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

}
