package com.anythink.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxBannerView;
import com.alxad.api.AlxBannerViewAdListener;
import com.thinkup.banner.unitgroup.api.CustomBannerAdapter;
import com.thinkup.core.api.TUBiddingListener;
import com.thinkup.core.api.TUBiddingResult;
import com.thinkup.core.api.MediationInitCallback;

import java.util.Map;

/**
 * Chinese: TopOn Banner广告适配器
 * English: TopOn Banner AD Adapter
 */
public class AlxBannerAdapter extends CustomBannerAdapter {
    private static final String TAG = "AlxBannerAdapter";
    private String unitid = "";
    private String appid = "";
    private String sid = "";
    private String token = "";
    AlxBannerView mBannerView;
    private TUBiddingListener mBiddingListener;

    public void startLoadAd(Context context) {
        Log.d(TAG, "startLoadAd");
        mBannerView = new AlxBannerView(context);
        // auto refresh ad  default = open = 1, 0 = close
        mBannerView.setBannerRefresh(0);
        final AlxBannerViewAdListener alxBannerADListener = new AlxBannerViewAdListener() {
            @Override
            public void onAdLoaded() {
                if (mLoadListener != null) {
                    Log.d(TAG, "load success");
                    mLoadListener.onAdCacheLoaded();
                }
                if (mBiddingListener != null) {
                    double bidPrice = mBannerView.getPrice();
                    Log.d(TAG, "bidding load success: bid price = " + bidPrice);
                    mBiddingListener.onC2SBiddingResultWithCache(AlxSdkInitManager.getBiddingSuccessBean(bidPrice), null);
                }
            }

            @Override
            public void onAdError(int errorCode, String errorMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(errorCode + "", errorMsg);
                }
                if (mBiddingListener != null) {
                    mBiddingListener.onC2SBiddingResultWithCache(TUBiddingResult.fail(errorMsg), null);
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
        mBannerView.loadAd(unitid, alxBannerADListener);
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
                    if (mLoadListener != null) {
                        mLoadListener.onAdLoadError("", "alx unitid | token | sid | appid is empty.");
                    }
                }
            });
        } else {
            if (mLoadListener != null) {
                mLoadListener.onAdLoadError("", "alx unitid | token | sid | appid is empty.");
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