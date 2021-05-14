//package com.admob.custom.adapter;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//
//import com.alxad.api.AlxAdSDK;
//import com.alxad.base.AlxConst;
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
//import com.google.android.gms.ads.mediation.customevent.CustomEventNative;
//import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener;
//
//import com.alxad.api.AlxNativeExpressAD;
//import com.alxad.api.AlxNativeExpressADListener;
//
//import org.json.JSONObject;
//
//
//public class AlxNativeAdapter implements CustomEventNative {
//
//    private static final String TAG = "AlxNativeAdapter";
//
//    private String unitid = "";
//    private String appid = "";
//    private String appkey = "";
//    private String license = "";
//    private Boolean isdebug = false;
//
//    private AlxNativeExpressAD mAlxNativeExpressAd;
//
//    @Override
//    public void requestNativeAd(Context context, CustomEventNativeListener customEventNativeListener, String s, NativeMediationAdRequest nativeMediationAdRequest, Bundle bundle) {
//        Log.i(TAG, "AlxNativeAdapter");
//        parseServer(s);
//        if (TextUtils.isEmpty(appid)) {
//            if (customEventNativeListener != null) {
//                Log.i(TAG, "alx appid is empty");
//                AdError error = new AdError(1, "alx appid is empty.", AlxConst.AD_NETWORK_NAME);
//                customEventNativeListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        if (TextUtils.isEmpty(appkey)) {
//            if (customEventNativeListener != null) {
//                Log.i(TAG, "alx appkey is empty");
//                AdError error = new AdError(1, "alx appkey is empty.", AlxConst.AD_NETWORK_NAME);
//                customEventNativeListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        if (TextUtils.isEmpty(unitid)) {
//            if (customEventNativeListener != null) {
//                Log.i(TAG, "alx unitid is empty");
//                AdError error = new AdError(1, "alx unitid is empty.", AlxConst.AD_NETWORK_NAME);
//                customEventNativeListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        if (TextUtils.isEmpty(license)) {
//            if (customEventNativeListener != null) {
//                Log.i(TAG, "alx license is empty");
//                AdError error = new AdError(1, "alx license is empty.", AlxConst.AD_NETWORK_NAME);
//                customEventNativeListener.onAdFailedToLoad(error);
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
//            if (customEventNativeListener != null) {
//                Log.i(TAG, "alx sdk init error");
//                AdError error = new AdError(1, "alx sdk init error", AlxConst.AD_NETWORK_NAME);
//                customEventNativeListener.onAdFailedToLoad(error);
//            }
//            return;
//        }
//
//        loadAds(context,customEventNativeListener);
//    }
//
//    @Override
//    public void onDestroy() {
//        if (mNativeAD != null) {
//            mNativeAD.destroy();
//        }
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
//    private void parseServer(String s) {
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
//    private void loadAds(Context context,final CustomEventNativeListener customEventNativeListener){
//        AlxNativeExpressADListener listener = new AlxNativeExpressADListener() {
//            @Override
//            public void onError(AlxNativeExpressAD alxNativeAD, int i, String s) {
//                if (customEventNativeListener != null) {
//                    AdError adError = new AdError(i, s, AlxConst.AD_NETWORK_NAME);
//                    customEventNativeListener.onAdFailedToLoad(adError);
//                }
//            }
//
//            @Override
//            public void onAdLoaded(AlxNativeExpressAD alxNativeAD) {
//                if (customEventNativeListener != null) {
//                    customEventNativeListener.onAdLoaded(alxNativeAD);
//                }
//            }
//
//            @Override
//            public void onAdClicked() {
//                if (customEventNativeListener != null) {
//                    customEventNativeListener.onAdClicked();
//                }
//            }
//
//            @Override
//            public void onAdShow() {
//                if (customEventNativeListener != null) {
//                    customEventNativeListener.onAdOpened();
//                }
//            }
//
//            @Override
//            public void onRenderSuccess(View view, float v, float v1) {
//                Log.i("onRenderSuccess", "onRenderSuccess");
//
//                if (customEventNativeListener != null) {
//                    customEventNativeListener.onAdImpression();
//                }
//            }
//
//            @Override
//            public void onRenderFailed() {
//                if (customEventNativeListener != null) {
//                    AdError adError = new AdError(0, "onRenderFailed", AlxConst.AD_NETWORK_NAME);
//                    customEventNativeListener.onAdFailedToLoad(adError);
//                }
//            }
//        };
//        mNativeAD = new AlxNativeExpressAD(context, unitid);
//        mNativeAD.load(context, listener);
//    }
//}
