package com.anythink.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxRewardVideoAD;
import com.alxad.api.AlxRewardVideoADListener;
import com.alxad.api.AlxSdkInitCallback;
import com.anythink.rewardvideo.unitgroup.api.CustomRewardVideoAdapter;

import java.util.Map;

/**
 * TopOn 激励广告适配器
 */
public class AlxRewardVideoAdapter extends CustomRewardVideoAdapter {

    private static final String TAG = "AlxRewardVideoAdapter";
    private AlxRewardVideoAD alxRewardVideoAD;
    private String unitid = "";
    private String appid = "";
    private String appkey = "";
    private String license = "";
    private Boolean isdebug = false;

    @Override
    public void loadCustomNetworkAd(Context context, Map<String, Object> serverExtras, Map<String, Object> map1) {
        Log.i(TAG, "loadCustomNetworkAd");
        if (serverExtras.containsKey("unitid")) {
            unitid = (String) serverExtras.get("unitid");

        } else {
            if (mLoadListener != null) {
                Log.i(TAG, "alx unitid is empty");
                mLoadListener.onAdLoadError("", "alx unitid is empty.");
            }
            return;
        }
        if (serverExtras.containsKey("appid")) {
            appid = (String) serverExtras.get("appid");

        } else {
            if (mLoadListener != null) {
                Log.i(TAG, "alx appid is empty");
                mLoadListener.onAdLoadError("", "alx appid is empty.");
            }
            return;
        }
        if (serverExtras.containsKey("appkey")) {
            appkey = (String) serverExtras.get("appkey");

        } else {
            if (mLoadListener != null) {
                Log.i(TAG, "alx unitid is empty");
                mLoadListener.onAdLoadError("", "alx unitid is empty.");
            }
            return;
        }
        if (serverExtras.containsKey("license")) {
            license = (String) serverExtras.get("license");

        } else {
            if (mLoadListener != null) {
                Log.i(TAG, "alx license is empty");
                mLoadListener.onAdLoadError("", "alx license is empty.");
            }
            return;
        }
        if (serverExtras.containsKey("isdebug")) {
            String test = serverExtras.get("isdebug").toString();
            if (test.equals("true")) {
                isdebug = true;
            } else {
                isdebug = false;
            }

        }
        initSdk(context);

    }

    private void initSdk(final Context context) {
        try {
            Log.i(TAG, "alx license: " + license + " alx appkey: " + appkey + " alx appid: " + appid);

            AlxAdSDK.setDebug(isdebug);
            AlxAdSDK.init(context, license, appkey, appid, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    //if (isOk){
                    startAdLoad(context);
                    //}
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startAdLoad(Context context) {
        alxRewardVideoAD = new AlxRewardVideoAD();
        alxRewardVideoAD.load(context, unitid, new AlxRewardVideoADListener() {

            @Override
            public void onRewardedVideoAdLoaded(AlxRewardVideoAD var1) {
                if (mLoadListener != null) {
                    mLoadListener.onAdCacheLoaded();
                }
            }

            @Override
            public void onRewardedVideoAdFailed(AlxRewardVideoAD var1, int errCode, String errMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(errCode + "", errMsg);
                }
            }

            @Override
            public void onRewardedVideoAdPlayStart(AlxRewardVideoAD var1) {
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdPlayStart();
                }
            }

            @Override
            public void onRewardedVideoAdPlayEnd(AlxRewardVideoAD var1) {
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdPlayEnd();
                }
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AlxRewardVideoAD var2, int errCode, String errMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(errCode + "", errMsg);
                }
            }

            @Override
            public void onRewardedVideoAdClosed(AlxRewardVideoAD var1) {
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdClosed();
                }
            }

            @Override
            public void onRewardedVideoAdPlayClicked(AlxRewardVideoAD var1) {
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdPlayClicked();
                }
            }

            @Override
            public void onReward(AlxRewardVideoAD var1) {
                if (mImpressionListener != null) {
                    mImpressionListener.onReward();
                }
            }
        });
    }

    @Override
    public void show(Activity activity) {
        if (alxRewardVideoAD != null) {
            alxRewardVideoAD.showVideo(activity);
        }
    }


    @Override
    public void destory() {
        if (alxRewardVideoAD != null) {
            alxRewardVideoAD.destroy();
            alxRewardVideoAD = null;
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

    @Override
    public boolean isAdReady() {
        if (alxRewardVideoAD != null) {
            return alxRewardVideoAD.isReady();
        }
        return false;
    }
}
