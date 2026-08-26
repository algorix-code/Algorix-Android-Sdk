package com.anythink.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxInterstitialAD;
import com.alxad.api.AlxInterstitialADListener;
import com.thinkup.core.api.TUBiddingListener;
import com.thinkup.core.api.TUBiddingResult;
import com.thinkup.core.api.MediationInitCallback;
import com.thinkup.interstitial.unitgroup.api.CustomInterstitialAdapter;

import java.util.Map;

/**
 * Chinese: TopOn 插屏广告适配器
 * English: TopOn interstitial advertising adapter
 */
public class AlxInterstitialAdapter extends CustomInterstitialAdapter {

    private static final String TAG = "AlxInterstitialAdapter";

    private AlxInterstitialAD alxInterstitialAD;
    private String unitid = "";
    private String appid = "";
    private String sid = "";
    private String token = "";
    private TUBiddingListener mBiddingListener;


    private void startLoadAd(Context context) {
        Log.d(TAG, "startLoadAd");
        alxInterstitialAD = new AlxInterstitialAD();
        alxInterstitialAD.load(context, unitid, new AlxInterstitialADListener() {

            @Override
            public void onInterstitialAdLoaded() {
                if (mLoadListener != null) {
                    Log.d(TAG, "load success");
                    mLoadListener.onAdCacheLoaded();
                }
                if (mBiddingListener != null) {
                    double bidPrice = alxInterstitialAD.getPrice();
                    Log.d(TAG, "bidding load success: bid price = " + bidPrice);
                    mBiddingListener.onC2SBiddingResultWithCache(AlxSdkInitManager.getBiddingSuccessBean(bidPrice), null);
                }
            }

            @Override
            public void onInterstitialAdLoadFail(int errorCode, String errorMsg) {
                Log.i(TAG, "onInterstitialAdLoadFail:" + errorCode + " msg:" + errorMsg);
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(errorCode + "", errorMsg);
                }
                if (mBiddingListener != null) {
                    mBiddingListener.onC2SBiddingResultWithCache(TUBiddingResult.fail(errorMsg), null);
                }
            }

            @Override
            public void onInterstitialAdClicked() {
                if (mImpressListener != null) {
                    mImpressListener.onInterstitialAdClicked();
                }
            }

            @Override
            public void onInterstitialAdShow() {
                if (mImpressListener != null) {
                    mImpressListener.onInterstitialAdShow();
                }
            }

            @Override
            public void onInterstitialAdClose() {
                if (mImpressListener != null) {
                    mImpressListener.onInterstitialAdClose();
                }
            }

            @Override
            public void onInterstitialAdVideoStart() {
                if (mImpressListener != null) {
                    mImpressListener.onInterstitialAdVideoStart();
                }
            }

            @Override
            public void onInterstitialAdVideoEnd() {
                if (mImpressListener != null) {
                    mImpressListener.onInterstitialAdVideoEnd();
                }
            }

            @Override
            public void onInterstitialAdVideoError(int errorCode, String errorMsg) {
                if (mImpressListener != null) {
                    mImpressListener.onInterstitialAdVideoError(String.valueOf(errorCode), errorMsg);
                }
            }
        });
    }


    @Override
    public boolean startBiddingRequest(final Context context, Map<String, Object> serverExtra, Map<String, Object> localExtra, final TUBiddingListener biddingListener) {
        Log.d(TAG, "startBiddingRequest");
        AlxSdkInitManager.printSDKInfo(TAG);

        mBiddingListener = biddingListener;
        if (parseServer(serverExtra)) {
            AlxSdkInitManager.getInstance().initSDK(context, serverExtra, new MediationInitCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "AlxSdkInit success");
                    startLoadAd(context);
                }

                @Override
                public void onFail(String s) {
                    Log.d(TAG, "AlxSdkInit fail : " + s);
                    //Chinese: 通过TUBiddingListener，回调竞价失败
                    //English: With TUBiddingListener, the callback bid fails
                    if (mBiddingListener != null) {
                        mBiddingListener.onC2SBiddingResultWithCache(TUBiddingResult.fail(s), null);
                    }
                }
            });
        } else {
            if (mBiddingListener != null) {
                mBiddingListener.onC2SBiddingResultWithCache(TUBiddingResult.fail("alx  unitid | token | sid | appid is empty"), null);
            }
        }

        return true;
    }


    @Override
    public void loadCustomNetworkAd(Context context, Map<String, Object> serverExtra, Map<String, Object> localExtras) {
        Log.d(TAG, "loadCustomNetworkAd");
        AlxSdkInitManager.printSDKInfo(TAG);

        if (parseServer(serverExtra)) {
            AlxSdkInitManager.getInstance().initSDK(context, serverExtra, new MediationInitCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "AlxSdkInit success");
                    startLoadAd(context);
                }

                @Override
                public void onFail(String s) {
                    Log.d(TAG, "AlxSdkInit fail : " + s);
                    //通过TUBiddingListener，回调竞价失败
                    if (mLoadListener != null) {
                        mLoadListener.onAdLoadError("", "alx unitid | token | sid | appid is empty.");
                    }
                }
            });
        } else {
            if (mLoadListener != null) {
                mLoadListener.onAdLoadError("", "alx unitid | token | sid | appid is empty..");
            }
        }
    }

    private boolean parseServer(Map<String, Object> serverExtras) {
        try {
            if (serverExtras.containsKey("appid")) {
                appid = (String) serverExtras.get("appid");
            }
            if (serverExtras.containsKey("sid")) {
                sid = (String) serverExtras.get("sid");
            }
            if (serverExtras.containsKey("token")) {
                token = (String) serverExtras.get("token");
            }
            if (serverExtras.containsKey("unitid")) {
                unitid = (String) serverExtras.get("unitid");
            }

            if (TextUtils.isEmpty(unitid) && serverExtras.containsKey("slot_id")) {
                unitid = (String) serverExtras.get("slot_id");
            }
        } catch (Exception e) {
            Log.e(TAG, "alx parseServer error:" + e.getMessage());
        }

        if (TextUtils.isEmpty(unitid) || TextUtils.isEmpty(token) || TextUtils.isEmpty(sid) || TextUtils.isEmpty(appid)) {
            Log.i(TAG, "alx unitid | token | sid | appid is empty");
            return false;
        }
        return true;
    }





    @Override
    public void show(Activity activity) {
        if (alxInterstitialAD != null) {
            alxInterstitialAD.show(activity);
        }
    }

    @Override
    public void destory() {
        if (alxInterstitialAD != null) {
            alxInterstitialAD.destroy();
            alxInterstitialAD = null;
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
        if (alxInterstitialAD != null) {
            return alxInterstitialAD.isReady();
        }
        return false;
    }
}
