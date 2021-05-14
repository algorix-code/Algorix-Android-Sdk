package com.admob.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alxad.BuildConfig;
import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxRewardVideoAD;
import com.alxad.api.AlxRewardVideoADListener;
import com.alxad.api.AlxSdkInitCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.MediationRewardedAd;
import com.google.android.gms.ads.mediation.MediationRewardedAdCallback;
import com.google.android.gms.ads.mediation.MediationRewardedAdConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.rewarded.RewardItem;

import org.json.JSONObject;

import java.util.List;

/**
 * Google Mobile ads 激励广告适配器
 */
public class AlxRewardVideoAdapter extends Adapter implements MediationRewardedAd
        , AlxRewardVideoADListener {
    private final String TAG = "AlxRewardVideoAdapter";
    public final String AD_NETWORK_NAME = "Algorix";

    private AlxRewardVideoAD alxRewardVideoAD;
    private String mUnitid = "";
    private String mAPPID = "";
    private String mAppkey = "";
    private String mLicense = "";
    private Boolean hasDebug = false;
    private Context mContext;
    private MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> mediationAdLoadCallBack;
    private MediationRewardedAdCallback mMediationRewardedAdCallback;

    @Override
    public void initialize(Context context,InitializationCompleteCallback initializationCompleteCallback
            , List<MediationConfiguration> list) {
        for (MediationConfiguration configuration : list) {
            Bundle serverParameters = configuration.getServerParameters();
            String serviceString = serverParameters.getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
            if (!TextUtils.isEmpty(serviceString)) {
                parseServiceString(serviceString);
            }
        }
        if (initSDk(context)) {
            initializationCompleteCallback.onInitializationSucceeded();
        } else {
            initializationCompleteCallback.onInitializationFailed("alx sdk init error");
        }
    }

    @Override
    public VersionInfo getVersionInfo() {
        String versionString = BuildConfig.VERSION_NAME;
        String[] splits = versionString.split("\\.");

        if (splits.length >= 3) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]) * 100 + Integer.parseInt(splits[3]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

    @Override
    public VersionInfo getSDKVersionInfo() {
        String versionString = AlxAdSDK.getNetWorkVersion();
        String[] splits = versionString.split("\\.");
        if (splits.length >= 3) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]);
            return new VersionInfo(major, minor, micro);
        }
        return new VersionInfo(0, 0, 0);
    }

    @Override
    public void showAd(Context context) {
        if (!(context instanceof Activity)) {
            mMediationRewardedAdCallback.onAdFailedToShow(new AdError(1,
                    "An activity context is required to show Sample rewarded ad."
                    , AD_NETWORK_NAME)
            );
            return;
        }
        mContext = context;
        if (!alxRewardVideoAD.isReady()) {
            mMediationRewardedAdCallback.onAdFailedToShow(new AdError(1, "No ads to show."
                    , AD_NETWORK_NAME));
            return;
        }
        alxRewardVideoAD.showVideo((Activity) context);
    }

    @Override
    public void loadRewardedAd(MediationRewardedAdConfiguration configuration
            , MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> mediationAdLoadCallback) {
        Log.d(TAG, "loadRewardedAd: ");
        Context context = configuration.getContext();
        mediationAdLoadCallBack = mediationAdLoadCallback;
        Bundle serverParameters = configuration.getServerParameters();
        String serviceString = serverParameters.getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        if (!TextUtils.isEmpty(serviceString)) {
            parseServiceString(serviceString);
        }
        initSDk(context);
    }

    private boolean initSDk(final Context context) {
        if (TextUtils.isEmpty(mUnitid)) {
            Log.d(TAG, "alx unitid is empty");
            mediationAdLoadCallBack.onFailure(new AdError(1, "alx unitid is empty."
                    , AD_NETWORK_NAME));
            return false;
        }
        if (TextUtils.isEmpty(mAPPID)) {
            Log.d(TAG, "alx appid is empty");
            mediationAdLoadCallBack.onFailure(new AdError(1, "alx appid is empty."
                    , AD_NETWORK_NAME));
            return false;
        }
        if (TextUtils.isEmpty(mAppkey)) {
            Log.d(TAG, "alx appkey is empty");
            mediationAdLoadCallBack.onFailure(new AdError(1, "alx appkey is empty."
                    , AD_NETWORK_NAME));
            return false;
        }
        if (TextUtils.isEmpty(mLicense)) {
            Log.d(TAG, "alx license is empty");
            mediationAdLoadCallBack.onFailure(new AdError(1, "alx license is empty"
                    , AD_NETWORK_NAME));
            return false;
        }
        try {
            Log.d(TAG, "alx license: " + mLicense + " alx appkey: " + mAppkey + "alx appid: " + mAPPID);
            AlxAdSDK.init(context, mLicense, mAppkey, mAPPID, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    //sdk初始化成功 加载广告
                    alxRewardVideoAD = new AlxRewardVideoAD();
//                    alxRewardVideoAD.setTimeout(5,5);
                    alxRewardVideoAD.load(context, mUnitid, AlxRewardVideoAdapter.this);
                }
            });
            AlxAdSDK.setDebug(hasDebug);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private void parseServiceString(String serviceString) {
        if (TextUtils.isEmpty(serviceString)) {
            Log.d(TAG, "serviceString  is empty ");
            return;
        }
        Log.d(TAG, "serviceString   " + serviceString);
        try {
            JSONObject jsonObject = new JSONObject(serviceString);
            mUnitid = jsonObject.optString("unitid");
            mAPPID = jsonObject.optString("appid");
            mAppkey = jsonObject.optString("appkey");
            mLicense = jsonObject.optString("license");
            hasDebug = jsonObject.optBoolean("isdebug");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRewardedVideoAdLoaded(AlxRewardVideoAD var1) {
        if (mediationAdLoadCallBack != null)
            mMediationRewardedAdCallback = (MediationRewardedAdCallback) mediationAdLoadCallBack
                    .onSuccess(this);

    }

    @Override
    public void onRewardedVideoAdFailed(AlxRewardVideoAD var1, int errCode, String errMsg) {
        Log.d(TAG, "onVideoLoadFail: " + errMsg);
        if (mediationAdLoadCallBack != null) mediationAdLoadCallBack
                .onFailure(new AdError(errCode, errMsg, AD_NETWORK_NAME));
    }

    @Override
    public void onRewardedVideoAdPlayStart(AlxRewardVideoAD var1) {
        if (mMediationRewardedAdCallback != null && mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(() -> {
                //必须在主线程中回调下面三个方法
                mMediationRewardedAdCallback.reportAdImpression();
                mMediationRewardedAdCallback.onAdOpened();
                mMediationRewardedAdCallback.onVideoStart();
            });
        }
    }

    @Override
    public void onRewardedVideoAdPlayEnd(AlxRewardVideoAD var1) {
        Log.d(TAG, "onRewardedVideoAdPlayEnd: ");
        if (mMediationRewardedAdCallback != null) mMediationRewardedAdCallback.onVideoComplete();
    }

    @Override
    public void onRewardedVideoAdPlayFailed(AlxRewardVideoAD var2, int errCode, String errMsg) {
        Log.d(TAG, "onShowFail: " + errMsg);
        if (mMediationRewardedAdCallback != null) mMediationRewardedAdCallback.onAdFailedToShow(
                new AdError(errCode, errMsg, AD_NETWORK_NAME));
    }

    @Override
    public void onRewardedVideoAdClosed(AlxRewardVideoAD var1) {
        Log.d(TAG, "onRewardedVideoAdClosed: ");
        if (mMediationRewardedAdCallback != null) {
            mMediationRewardedAdCallback.onAdClosed();
        }
    }

    @Override
    public void onRewardedVideoAdPlayClicked(AlxRewardVideoAD var1) {
        Log.d(TAG, "onRewardedVideoAdPlayClicked: ");
        if (mMediationRewardedAdCallback != null) mMediationRewardedAdCallback.reportAdClicked();
    }

    @Override
    public void onReward(AlxRewardVideoAD var1) {
        Log.d(TAG, "onReward: ");
        if (mMediationRewardedAdCallback != null) {
            mMediationRewardedAdCallback.onUserEarnedReward(new RewardItem() {
                @Override
                public String getType() {
                    // mintegral SDK does not provide a reward type.
                    return "";
                }

                @Override
                public int getAmount() {
                    // mintegral SDK does not provide reward amount, default to 1.
                    return 1;
                }
            });
        }
    }
}
