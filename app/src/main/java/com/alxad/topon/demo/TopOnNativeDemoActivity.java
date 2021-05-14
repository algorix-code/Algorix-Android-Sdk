package com.alxad.topon.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.alxad.AppConfig;
import com.alxad.base.AlxAdResponse;
import com.alxad.control.nativead.AlxNativeAD;
import com.alxad.control.nativead.AlxNativeAdListener;
import com.alxad.R;
public class TopOnNativeDemoActivity extends Activity {

    private final static String TAG = "NativeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topon_native_demo);
        AlxNativeAD nativeAD = new AlxNativeAD(this.getApplication(), AppConfig.NATIVE_AD_PID);
        nativeAD.load(new AlxNativeAdListener() {
            @Override
            public void onError(AlxAdResponse var1, int errorCode, String errorMsg) {

                Log.i(TAG, "onError:" + errorMsg);
            }

            @Override
            public void onAdLoaded(AlxAdResponse var1) {
                Log.i(TAG, "onAdLoaded:" + var1.toString());
            }

            @Override
            public void onAdClicked(AlxAdResponse var1) {

            }

            @Override
            public void onAdShow(AlxAdResponse var1) {

            }
        });

    }
}
