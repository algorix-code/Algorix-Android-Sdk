package com.alxad.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxSdkInitCallback;

public class MainApp extends Application {
    private final String TAG = "MainApp";

    private static MainApp mApp;

    public static MainApp getInstance(){
        return mApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
        initAlxAd();
    }

    /**
     * 广告配置
     */
    public void initAlxAd(){
        try {
            //Alx Ad Init
            AlxAdSDK.setAC(true, 3600 * 24);
            AlxAdSDK.init(this, AdConfig.ALX_TOKEN, AdConfig.ALX_SID, AdConfig.ALX_APP_ID, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    Log.i(TAG, Thread.currentThread().getName() + ":" + isOk + "-" + msg);
                }
            });
            AlxAdSDK.setDebug(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //聚合平台配置


}
