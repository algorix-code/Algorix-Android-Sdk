package com.admob.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxBannerAD;
import com.alxad.api.AlxBannerADListener;
import com.alxad.api.AlxSdkInitCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

import org.json.JSONObject;

/**
 * Google AdMob 平台 Banner广告自定义事件适配器
 */
public class AlxBannerAdapter implements CustomEventBanner {

    private static final String TAG = "AlxBannerAdapter";
    public final String AD_NETWORK_NAME = "Algorix";

    private String unitid = "";
    private String appid = "";
    private String appkey = "";
    private String license = "";
    private Boolean isdebug = false;
    AlxBannerAD mBannerView;
    AlxBannerAD.AlxAdSize alxAdSize = AlxBannerAD.AlxAdSize.SIZE_320_50;


    @Override
    public void requestBannerAd(final Context context, final CustomEventBannerListener customEventBannerListener, String s, final AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle) {
        Log.d(TAG, "loadCustomNetworkAd");
        if (context == null) {
            throw new NullPointerException("context is empty object");
        }
        parseServer(s);
        if (TextUtils.isEmpty(appid)) {
            if (customEventBannerListener != null) {
                Log.d(TAG, "alx appid is empty");
                AdError error = new AdError(1, "alx appid is empty.", AD_NETWORK_NAME);
                customEventBannerListener.onAdFailedToLoad(error);
            }
            return;
        }

        if (TextUtils.isEmpty(appkey)) {
            if (customEventBannerListener != null) {
                Log.d(TAG, "alx appkey is empty");
                AdError error = new AdError(1, "alx appkey is empty.", AD_NETWORK_NAME);
                customEventBannerListener.onAdFailedToLoad(error);
            }
            return;
        }

        if (TextUtils.isEmpty(unitid)) {
            if (customEventBannerListener != null) {
                Log.d(TAG, "alx unitid is empty");
                AdError error = new AdError(1, "alx unitid is empty.", AD_NETWORK_NAME);
                customEventBannerListener.onAdFailedToLoad(error);
            }
            return;
        }

        if (TextUtils.isEmpty(license)) {
            if (customEventBannerListener != null) {
                Log.d(TAG, "alx license is empty");
                AdError error = new AdError(1, "alx license is empty.", AD_NETWORK_NAME);
                customEventBannerListener.onAdFailedToLoad(error);
            }
            return;
        }

        try {
            Log.d(TAG, "alx license: " + license + " alx appkey: " + appkey + "alx appid: " + appid);
            AlxAdSDK.init(context, license, appkey, appid, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    loads(context, customEventBannerListener, adSize);
                }
            });
            AlxAdSDK.setDebug(isdebug);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            e.printStackTrace();
            if (customEventBannerListener != null) {
                Log.d(TAG, "alx sdk init error");
                AdError error = new AdError(1, "alx sdk init error", AD_NETWORK_NAME);
                customEventBannerListener.onAdFailedToLoad(error);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        if (mBannerView != null) {
            mBannerView.destory();
            mBannerView = null;
        }
    }


    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    private void parseServer(String s) {
        if (TextUtils.isEmpty(s)) {
            Log.d(TAG, "serviceString  is empty ");
            return;
        }
        Log.d(TAG, "serviceString   " + s);
        try {
            JSONObject json = new JSONObject(s);
            appid = json.getString("appid");
            appkey = json.getString("appkey");
            unitid = json.getString("unitid");
            license = json.getString("license");
            isdebug = json.optBoolean("isdebug");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void loads(Context context, final CustomEventBannerListener listener, AdSize adSize) {
        int width_dp = 0;
        int height_dp = 0;
        if (adSize != null) {
            width_dp = adSize.getWidth();
            height_dp = adSize.getHeight();
        }
        String size = width_dp + "x" + height_dp;
        Log.i(TAG, "width x height=" + size);
        switch (size) {
            case "300x250":
                alxAdSize = AlxBannerAD.AlxAdSize.SIZE_300_250;
                break;
            case "320x480":
                alxAdSize = AlxBannerAD.AlxAdSize.SIZE_320_480;
                break;
            default:
                alxAdSize = AlxBannerAD.AlxAdSize.SIZE_320_50;
                break;
        }


        mBannerView = new AlxBannerAD(context);
        final AlxBannerADListener alxBannerADListener = new AlxBannerADListener() {
            @Override
            public void onAdLoaded(AlxBannerAD banner) {
                if (listener != null) {
                    listener.onAdLoaded(banner);
                }
            }

            @Override
            public void onAdError(AlxBannerAD banner, int errorCode, String errorMsg) {
                if (listener != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        AdError adError = new AdError(errorCode, errorMsg, AD_NETWORK_NAME);
                        listener.onAdFailedToLoad(adError);
                    });
                }
            }

            @Override
            public void onAdClicked(AlxBannerAD banner) {
                if (listener != null) {
                    listener.onAdClicked();
                }
            }

            @Override
            public void onAdShow(AlxBannerAD banner) {
                if (listener != null) {
                    listener.onAdOpened();
                }
            }
        };
        mBannerView.load(context, unitid, alxBannerADListener, alxAdSize);
    }
}
