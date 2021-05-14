package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alxad.api.AlxBannerAD;
import com.alxad.api.AlxBannerADListener;
import com.mopub.common.LifecycleListener;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Views;

import java.util.Map;

import static com.mopub.common.logging.MoPubLog.AdapterLogEvent.LOAD_FAILED;
import static com.mopub.mobileads.MoPubInline.ADAPTER_NAME;

/**
 * MoPub Banner广告适配器
 */
public class AlxBannerAdapter extends BaseAd {
    private static final String TAG = "AlxBannerAdapter";
    private String mUnitid = "";
    private AlxBannerAD mBannerView;
    AlxBannerAD.AlxAdSize alxAdSize = AlxBannerAD.AlxAdSize.SIZE_320_50;

    @Override
    protected void load(@NonNull final Context context, @NonNull final AdData adData) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(adData);
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
        //初始化banner宽高
        initAdSize(adData);
        loadAds(context);
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

    private void initAdSize(AdData data) {
        if (data == null || data.getAdWidth() == null || data.getAdHeight() == null) {
            alxAdSize = AlxBannerAD.AlxAdSize.SIZE_320_50;
            return;
        }
        int width_dp = data.getAdWidth();
        int height_dp = data.getAdHeight();
        String size = width_dp + "x" + height_dp;
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
    }


    @Nullable
    @Override
    protected View getAdView() {
        return mBannerView;
    }

    @Override
    protected void onInvalidate() {
        Log.i(TAG, "onInvalidate");
        Views.removeFromParent(mBannerView);
        if (mBannerView != null) {
            mBannerView.destory();
            mBannerView = null;
        }
    }

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @NonNull
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

    private void loadAds(Context context) {
        mBannerView = new AlxBannerAD(context);
        final AlxBannerADListener alxBannerADListener = new AlxBannerADListener() {
            @Override
            public void onAdLoaded(AlxBannerAD banner) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoaded();
                }
            }

            @Override
            public void onAdError(AlxBannerAD banner, int errorCode, String errorMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadFailed(AlxServicesAdapterConfiguration.getMoPubErrorCode(errorCode));
                }
            }

            @Override
            public void onAdClicked(AlxBannerAD banner) {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdClicked();
                }
            }

            @Override
            public void onAdShow(AlxBannerAD banner) {
                if (mInteractionListener != null) {
                    mInteractionListener.onAdShown();
                }
            }
        };
        mBannerView.load(context, mUnitid, alxBannerADListener, alxAdSize);
    }

}
