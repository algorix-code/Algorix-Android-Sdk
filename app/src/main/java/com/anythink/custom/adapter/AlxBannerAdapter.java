package com.anythink.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxBannerView;
import com.alxad.api.AlxBannerViewAdListener;
import com.alxad.api.AlxSdkInitCallback;
import com.anythink.banner.unitgroup.api.CustomBannerAdapter;

import java.util.Map;

/**
 * TopOn Banner广告适配器
 */
public class AlxBannerAdapter extends CustomBannerAdapter {
    private static final String TAG = "AlxBannerAdapter";
    private String unitid = "";
    private String appid = "";
    private String sid = "";
    private String token = "";
//    private String size = "";
    private Boolean isdebug = false;
    AlxBannerView mBannerView;


    @Override
    public void loadCustomNetworkAd(Context context, Map<String, Object> serverExtras, Map<String, Object> localExtras) {
        Log.d(TAG, "alx-topon-adapter-version:" + AlxMetaInf.ADAPTER_VERSION);
        Log.i(TAG, "loadCustomNetworkAd");
        if (parseServer(serverExtras)) {
            initSdk(context);
        } else {
            if (mLoadListener != null) {
                mLoadListener.onAdLoadError("", "alx apppid | token | sid | appid is empty.");
            }
        }
    }

    private boolean parseServer(Map<String, Object> serverExtras) {
        try {
            if (serverExtras.containsKey("unitid")) {
                unitid = (String) serverExtras.get("unitid");
            }
            if (serverExtras.containsKey("license")) {
                token = (String) serverExtras.get("license");
            } else if (serverExtras.containsKey("token")) {
                token = (String) serverExtras.get("token");
            }
            if (serverExtras.containsKey("appkey")) {
                sid = (String) serverExtras.get("appkey");
            } else if (serverExtras.containsKey("sid")) {
                sid = (String) serverExtras.get("sid");
            }
            if (serverExtras.containsKey("appid")) {
                appid = (String) serverExtras.get("appid");
            }

            if (serverExtras.containsKey("isdebug")) {
                String test = serverExtras.get("isdebug").toString();
                Log.e(TAG, "alx debug mode:" + test);
                if (test.equals("true")) {
                    isdebug = true;
                } else {
                    isdebug = false;
                }
            } else {
                Log.e(TAG, "alx debug mode: false");
            }
            if (serverExtras.containsKey("tag")) {
                String tag = serverExtras.get("tag").toString();
                Log.e(TAG, "alx json tag:" + tag);
            }
//            if (serverExtras.containsKey("size")) {
//                size = serverExtras.get("size").toString();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(unitid) || TextUtils.isEmpty(token) || TextUtils.isEmpty(sid) || TextUtils.isEmpty(appid)) {
            Log.i(TAG, "alx unitid | token | id | appid is empty");
            return false;
        }
        return true;
    }

    private void initSdk(final Context context) {
        try {
            Log.i(TAG, "alx ver:" + AlxAdSDK.getNetWorkVersion() + " alx token: " + token + " alx appid: " + appid + " alx sid: " + sid);

            AlxAdSDK.setDebug(isdebug);

            AlxAdSDK.init(context, token, sid, appid, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    //if (isOk){
                    Log.i(TAG, "sdk onInit:" + isOk);
                    loadAd(context);
                    //}
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAd(Context context) {
        mBannerView = new AlxBannerView(context);
        final AlxBannerViewAdListener alxBannerADListener = new AlxBannerViewAdListener() {
            @Override
            public void onAdLoaded() {
                if (mLoadListener != null) {
                    mLoadListener.onAdCacheLoaded();
                }
            }

            @Override
            public void onAdError(int errorCode, String errorMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(errorCode + "", errorMsg);
                }
            }

            @Override
            public void onAdClicked() {
                if (mImpressionEventListener != null) {
                    mImpressionEventListener.onBannerAdClicked();
                }
            }

            @Override
            public void onAdShow() {
                if (mImpressionEventListener != null) {
                    mImpressionEventListener.onBannerAdShow();
                }
            }

            @Override
            public void onAdClose() {
                if (mImpressionEventListener != null) {
                    mImpressionEventListener.onBannerAdClose();
                }
            }
        };
        // auto refresh ad  default = open = 1, 0 = close
        mBannerView.setBannerRefresh(0);
        mBannerView.loadAd(unitid, alxBannerADListener);
    }

    @Override
    public View getBannerView() {
        return mBannerView;
    }

    @Override
    public void destory() {
        if (mBannerView != null) {
            mBannerView.destroy();
            mBannerView = null;
        }
    }

    @Override
    public String getNetworkPlacementId() {
        return unitid;
    }

    @Override
    public String getNetworkSDKVersion() {
        return AlxAdSDK.getNetWorkVersion();
    }

    @Override
    public String getNetworkName() {
        return AlxAdSDK.getNetWorkName();
    }

}