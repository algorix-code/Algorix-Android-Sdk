//package com.admob.custom.adapter;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.alxad.api.AlxAdSDK;
//import com.alxad.base.AlxConst;
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.mediation.MediationAdRequest;
//import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
//import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
//
//import org.json.JSONObject;
//
//public class AlxInterstitialAdapter implements CustomEventInterstitial {
//
//    private static final String TAG = "AlxInterstitialAdapter";
//    private String unitid = "";
//    private String appid = "";
//    private String appkey = "";
//    private String license = "";
//    private Boolean isdebug = false;
//    private CustomEventInterstitialListener mListener;
//
//    @Override
//    public void requestInterstitialAd(Context context, CustomEventInterstitialListener customEventInterstitialListener, String s, MediationAdRequest mediationAdRequest, Bundle bundle) {
//        Log.i(TAG, "loadCustomNetworkAd");
//        mListener=customEventInterstitialListener;
//        parseServer(context, s);
//        if (TextUtils.isEmpty(appid)) {
//            if (mListener != null) {
//                Log.i(TAG, "alx appid is empty");
//                AdError error = new AdError(1, "alx appid is empty.", AlxConst.AD_NETWORK_NAME);
//                mListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        if (TextUtils.isEmpty(appkey)) {
//            if (mListener != null) {
//                Log.i(TAG, "alx appkey is empty");
//                AdError error = new AdError(1, "alx appkey is empty.", AlxConst.AD_NETWORK_NAME);
//                mListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        if (TextUtils.isEmpty(unitid)) {
//            if (mListener != null) {
//                Log.i(TAG, "alx unitid is empty");
//                AdError error = new AdError(1, "alx unitid is empty.", AlxConst.AD_NETWORK_NAME);
//                mListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        if (TextUtils.isEmpty(license)) {
//            if (mListener != null) {
//                Log.i(TAG, "alx license is empty");
//                AdError error = new AdError(1, "alx license is empty.", AlxConst.AD_NETWORK_NAME);
//                mListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        try {
//            Log.i(TAG, "alx license: " + license + " alx appkey: " + appkey + "alx appid: " + appid);
//            AlxAdSDK.init(context, license, appkey, appid);
//            AlxAdSDK.setDebug(isdebug);
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//            e.printStackTrace();
//            if (mListener != null) {
//                Log.i(TAG, "alx sdk init error");
//                AdError error = new AdError(1, "alx sdk init error", AlxConst.AD_NETWORK_NAME);
//                mListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        preloadAd();//预加载广告
//
//    }
//
//    @Override
//    public void showInterstitial() {
//
//    }
//
//    @Override
//    public void onDestroy() {
//
//    }
//
//    @Override
//    public void onPause() {
//
//    }
//
//    @Override
//    public void onResume() {
//
//    }
//
//    private void parseServer(Context context, String s) {
//        if (TextUtils.isEmpty(s)) {
//            return;
//        }
//        try {
//            JSONObject json = new JSONObject(s);
//            appid = json.getString("appid");
//            appkey = json.getString("appkey");
//            unitid = json.getString("unitid");
//            license = json.getString("license");
//            isdebug = json.optBoolean("isdebug");
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//    }
//
//    private void preloadAd(){
//
//    }
//}
