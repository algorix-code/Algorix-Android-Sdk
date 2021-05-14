package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alxad.api.AlxRewardVideoAD;
import com.alxad.api.AlxRewardVideoADListener;
import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;

import java.util.Map;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.CUSTOM;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_FAILED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.SHOW_ATTEMPTED;
import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.SHOW_FAILED;
import static com.mopub.mobileads.MoPubFullscreen.ADAPTER_NAME;
/**
 * MoPub Reward广告适配器
 */
public class AlxRewardVideoAdapter extends BaseAd {
    private static final String TAG = "AlxRewardVideoAdapter";
    private String mUnitid = "";
    private AlxRewardVideoAD alxRewardVideoAD;

    private Context mContext;

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @Override
    public String getAdNetworkId() {
        return mUnitid == null ? "" : mUnitid;
    }

    @Override
    protected boolean checkAndInitializeSdk(@NonNull final Activity launcherActivity,
                                            @NonNull final AdData adData) {
        Preconditions.checkNotNull(launcherActivity);
        Preconditions.checkNotNull(adData);
        return false;
    }

    @Override
    protected void load(@NonNull Context context, @NonNull AdData adData) throws Exception {
        Preconditions.checkNotNull(context);
        mContext = context;
        Preconditions.checkNotNull(adData);
        setAutomaticImpressionAndClickTracking(false);
        //解析服务器参数
        parseServiceString(adData.getExtras());
        if (TextUtils.isEmpty(mUnitid)) {
            MoPubLog.log(getAdNetworkId(), LOAD_FAILED, ADAPTER_NAME,
                    MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR.getIntCode(),
                    MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);

            if (mLoadListener != null) {
                mLoadListener.onAdLoadFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            }
            return;
        }
        if (!(context instanceof Activity)) {
            MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "Context passed to load " +
                    "was not an Activity.");
            if (mLoadListener != null) {
                mLoadListener.onAdLoadFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            }
            return;
        }
        loadAds(context);
    }

    @Override
    protected void show() {
        MoPubLog.log(getAdNetworkId(), SHOW_ATTEMPTED, ADAPTER_NAME);
        if (alxRewardVideoAD != null && alxRewardVideoAD.isReady()) {
            if (mContext instanceof Activity) {
                alxRewardVideoAD.showVideo((Activity) mContext);
            } else {
                MoPubLog.log(getAdNetworkId(), SHOW_FAILED, ADAPTER_NAME,
                        MoPubErrorCode.FULLSCREEN_SHOW_ERROR.getIntCode(),
                        MoPubErrorCode.FULLSCREEN_SHOW_ERROR);
                if (mInteractionListener != null) {
                    mInteractionListener.onAdFailed(MoPubErrorCode.FULLSCREEN_SHOW_ERROR);
                }

            }
        } else {
            MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "Failed to show " +
                    "alx rewarded video because it wasn't ready yet.");
            MoPubLog.log(getAdNetworkId(), SHOW_FAILED, ADAPTER_NAME,
                    MoPubErrorCode.FULLSCREEN_SHOW_ERROR.getIntCode(),
                    MoPubErrorCode.FULLSCREEN_SHOW_ERROR);
            if (mInteractionListener != null) {
                mInteractionListener.onAdFailed(MoPubErrorCode.FULLSCREEN_SHOW_ERROR);
            }
        }
    }

    @Override
    protected void onInvalidate() {
        Log.d(TAG, "onInvalidate");
        if (alxRewardVideoAD != null) {
            alxRewardVideoAD.destroy();
            alxRewardVideoAD = null;
        }
    }

    private void parseServiceString(Map<String, String> extras) {
        if (extras == null || extras.size() == 0) {
            return;
        }
        Log.d(TAG, "--------    extras  start   ---------  ");
        mUnitid = extras.get("unitid");
        Log.d(TAG, "unitid   " + mUnitid);
        Log.d(TAG, "--------    extras  end   ---------  ");
    }

    private void loadAds(Context context) {
        alxRewardVideoAD = new AlxRewardVideoAD();
        alxRewardVideoAD.load(context, mUnitid, new AlxRewardVideoADListener() {

            @Override
            public void onRewardedVideoAdLoaded(AlxRewardVideoAD var1) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoaded();
                }
            }

            @Override
            public void onRewardedVideoAdFailed(AlxRewardVideoAD var1, int errCode, String errMsg) {
                MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "Failed to show " +
                        "alx rewarded video. " + errMsg);
                MoPubLog.log(getAdNetworkId(), SHOW_FAILED, ADAPTER_NAME,
                        MoPubErrorCode.FULLSCREEN_SHOW_ERROR.getIntCode(),
                        MoPubErrorCode.FULLSCREEN_SHOW_ERROR);

                if (mInteractionListener != null) {
                    mInteractionListener.onAdFailed(AlxServicesAdapterConfiguration.getMoPubErrorCode(errCode));
                }
            }

            @Override
            public void onRewardedVideoAdPlayStart(AlxRewardVideoAD var1) {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdImpression();
                    mInteractionListener.onAdShown();
                }
            }

            @Override
            public void onRewardedVideoAdPlayEnd(AlxRewardVideoAD var1) {
                MoPubLog.log(getAdNetworkId(), CUSTOM, ADAPTER_NAME, "onRewardedVideoAdPlayEnd");
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AlxRewardVideoAD var2, int errCode, String errMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadFailed(AlxServicesAdapterConfiguration.getMoPubErrorCode(errCode));
                }
            }

            @Override
            public void onRewardedVideoAdClosed(AlxRewardVideoAD var1) {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdDismissed();
                }
            }

            @Override
            public void onRewardedVideoAdPlayClicked(AlxRewardVideoAD var1) {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdClicked();
                }
            }

            @Override
            public void onReward(AlxRewardVideoAD var1) {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdComplete(MoPubReward.success("", 1));
                }
            }
        });
    }
}
