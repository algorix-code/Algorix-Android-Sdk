package com.anythink.custom.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxBannerAD;
import com.alxad.api.AlxBannerADListener;
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
    private String appkey = "";
    private String license = "";
    private String size = "";
    private Boolean isdebug = false;
    AlxBannerAD mBannerView;
    AlxBannerAD.AlxAdSize alxAdSize = AlxBannerAD.AlxAdSize.SIZE_320_50;


    @Override
    public void loadCustomNetworkAd(Context context, Map<String, Object> serverExtras, Map<String, Object> localExtras) {
        Log.i(TAG, "loadCustomNetworkAd");
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serverExtras.containsKey("isdebug")) {
            String test = serverExtras.get("isdebug").toString();
            if (test.equals("true")) {
                isdebug = true;
            } else {
                isdebug = false;
            }
        }
        if (serverExtras.containsKey("size")) {
            size = serverExtras.get("size").toString();
        }
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
                    loadAd(context);
                    //}
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAd(Context context) {
        mBannerView = new AlxBannerAD(context);
        final AlxBannerADListener alxBannerADListener = new AlxBannerADListener() {
            @Override
            public void onAdLoaded(AlxBannerAD banner) {
                if (mLoadListener != null) {
                    mLoadListener.onAdCacheLoaded();
                }
            }

            @Override
            public void onAdError(AlxBannerAD banner, int errorCode, String errorMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(errorCode + "", errorMsg);
                }
            }

            @Override
            public void onAdClicked(AlxBannerAD banner) {
                if (mImpressionEventListener != null) {
                    mImpressionEventListener.onBannerAdClicked();
                }
            }

            @Override
            public void onAdShow(AlxBannerAD banner) {
                if (mImpressionEventListener != null) {
                    mImpressionEventListener.onBannerAdShow();
                }
            }
        };
        mBannerView.load(context, unitid, alxBannerADListener, alxAdSize);
    }

    @Override
    public View getBannerView() {
        return mBannerView;
    }

    @Override
    public void destory() {
        if (mBannerView != null) {
            mBannerView.destory();
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

    // topon 适配器文档 https://docs.toponad.com/#/zh-cn/android/adroid_adv_doc/customnetwork/custom_network_adapter
}

/*
public class FacebookBannerAdapter extends CustomBannerAdapter {

    private String unitid = "";
    AdView mBannerView;
    String size = "";

    @Override
    public void loadCustomNetworkAd(final Context activity, Map<String, Object> serverExtras, Map<String, Object> localExtras) {

        if (serverExtras.containsKey("unit_id")) {
            unitid = (String) serverExtras.get("unit_id");

        } else {
            if (mLoadListener != null) {
                mLoadListener.onAdLoadError("", "facebook unitid is empty.");
            }
            return;
        }

        FacebookInitManager.getInstance().initSDK(activity.getApplicationContext(), serverExtras);

        if (serverExtras.containsKey("size")) {
            size = serverExtras.get("size").toString();
        }

        final AdListener adListener = new AdListener() {
            @Override
            public void onAdLoaded(Ad ad) {
                mBannerView = (AdView) ad;
                if (mLoadListener != null) {
                    mLoadListener.onAdCacheLoaded();

                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(adError.getErrorCode() + "", adError.getErrorMessage());
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (mImpressionEventListener != null) {
                    mImpressionEventListener.onBannerAdClicked();
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                if (mImpressionEventListener != null) {
                    mImpressionEventListener.onBannerAdShow();
                }
            }
        };

        AdView adView = null;
        switch (size) {
            case "320x50":
                adView = new AdView(activity, unitid, AdSize.BANNER_HEIGHT_50);
                break;
            case "320x90":
                adView = new AdView(activity, unitid, AdSize.BANNER_HEIGHT_90);
                break;
            case "300x250":
                adView = new AdView(activity, unitid, AdSize.RECTANGLE_HEIGHT_250);
                break;
            default:
                adView = new AdView(activity, unitid, AdSize.BANNER_HEIGHT_50);
                break;
        }
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
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
    public String getNetworkSDKVersion() {
        return FacebookInitManager.getInstance().getNetworkVersion();
    }

    @Override
    public String getNetworkName() {
        return FacebookInitManager.getInstance().getNetworkName();
    }

    @Override
    public boolean setUserDataConsent(Context context, boolean isConsent, boolean isEUTraffic) {
        return false;
    }

    @Override
    public String getNetworkPlacementId() {
        return unitid;
    }
}

 */