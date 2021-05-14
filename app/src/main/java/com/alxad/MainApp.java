package com.alxad;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxSdkInitCallback;
import com.anythink.core.api.ATSDK;
import com.google.android.gms.ads.MobileAds;

public class MainApp extends Application {
    private final String TAG = "MainApp";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            //Alx Ad Init
            AlxAdSDK.init(this, AppConfig.ALX_LICENSE, AppConfig.ALX_KEY, AppConfig.ALX_APP_ID, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    Log.i(TAG, Thread.currentThread().getName() + ":" + isOk + "-" + msg);
                }
            });
            AlxAdSDK.setDebug(true);
            //TopOn Ad Init
            ATSDK.init(getApplicationContext(), AppConfig.ALX_APP_ID_TOPON, AppConfig.ALX_KEY_TOPON);
            ATSDK.setNetworkLogDebug(true);
            //Google Ad Init
            MobileAds.initialize(this);
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
