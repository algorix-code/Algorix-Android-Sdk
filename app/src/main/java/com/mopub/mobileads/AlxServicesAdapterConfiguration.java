package com.mopub.mobileads;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alxad.api.AlxAdError;
import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxSdkInitCallback;
import com.alxad.base.AlxConst;
import com.mopub.common.BaseAdapterConfiguration;
import com.mopub.common.OnNetworkInitializationFinishedListener;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;

import java.util.Map;

import static com.mopub.common.logging.MoPubLog.SdkLogEvent.CUSTOM_WITH_THROWABLE;


/**
 * MoPub 初始化Alx AdSDK
 */
public class AlxServicesAdapterConfiguration extends BaseAdapterConfiguration {
    private static final String TAG = "AlxServicesAdapter";
    private String mAPPID = "";
    private String mAppkey = "";
    private String mLicense = "";
    private Boolean hasDebug = false;
    //初始化sdk标志位
    static boolean hasInitAlxSDK = false;

    @NonNull
    @Override
    public String getAdapterVersion() {
        return AlxConst.SDK_VERSION;
    }

    @Nullable
    @Override
    public String getBiddingToken(@NonNull Context context) {
        return null;
    }

    @NonNull
    @Override
    public String getMoPubNetworkName() {
        return AlxConst.AD_NETWORK_NAME;
    }

    @NonNull
    @Override
    public String getNetworkSdkVersion() {
        return AlxConst.SDK_VERSION;
    }

    @Override
    public void initializeNetwork(@NonNull Context context, @Nullable Map<String, String>
            configuration, @NonNull OnNetworkInitializationFinishedListener listener) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(listener);
        //解析服务器参数
        parseServiceString(configuration);
        //初始化sdk
        initSDk(context,listener);

    }

    private void parseServiceString(Map<String, String> extras) {
        if (extras == null || extras.size() == 0) {
            return;
        }
        Log.d(TAG, "start init alx sdk ");
        mAPPID = extras.get("appid");
        Log.d(TAG, "appid   " + mAPPID);
        mAppkey = extras.get("appkey");
        Log.d(TAG, "appkey   " + mAppkey);
        mLicense = extras.get("license");
        Log.d(TAG, "license   " + mLicense);
        hasDebug = extras.get("isdebug").equalsIgnoreCase("true");
        Log.d(TAG, "isdebug   " + hasDebug);
        Log.d(TAG, " end init alx sdk");
    }

    private void initSDk(Context context,@NonNull OnNetworkInitializationFinishedListener listener) {
        if (TextUtils.isEmpty(mAPPID)) {
            Log.d(TAG, "alx appid is empty");
            return ;
        }
        if (TextUtils.isEmpty(mAppkey)) {
            Log.d(TAG, "alx appkey is empty");
            return ;
        }
        if (TextUtils.isEmpty(mLicense)) {
            Log.d(TAG, "alx license is empty");
            return ;
        }
        if (!hasInitAlxSDK) {
            try {
                Log.d(TAG, "alx license: " + mLicense + " alx appkey: " + mAppkey + "alx appid: " + mAPPID);
                AlxAdSDK.init(context, mLicense, mAppkey, mAPPID, new AlxSdkInitCallback() {
                    @Override
                    public void onInit(boolean isOk, String msg) {
                        if (isOk) {
                            hasInitAlxSDK = true;
                            listener.onNetworkInitializationFinished(AlxServicesAdapterConfiguration.class,
                                    MoPubErrorCode.ADAPTER_INITIALIZATION_SUCCESS);
                        }else{
                            hasInitAlxSDK = false;
                            listener.onNetworkInitializationFinished(AlxServicesAdapterConfiguration.class,
                                    MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
                        }
                    }
                });
                AlxAdSDK.setDebug(hasDebug);
            } catch (Exception e) {
                e.printStackTrace();
                MoPubLog.log(CUSTOM_WITH_THROWABLE, "Initializing AdMob has encountered " +
                        "an exception.", e);
            }
        }
    }

    /**
     * Converts a given Alx Ads SDK error code into {@link MoPubErrorCode}.
     *
     * @param errorCode Alx Ads SDK  error code.
     * @return an equivalent MoPub SDK error code for the given Alx Ads SDK  error
     * code.
     */
    public static MoPubErrorCode getMoPubErrorCode(int errorCode) {
        switch (errorCode) {
            case AlxAdError.ERR_NETWORK:
                return MoPubErrorCode.NETWORK_INVALID_STATE;
            case AlxAdError.ERR_NO_FILL:
                return MoPubErrorCode.NO_FILL;
            case AlxAdError.ERR_VAST_ERROR:
            case AlxAdError.ERR_PARSE_AD:
                return MoPubErrorCode.INTERNAL_ERROR;
            case AlxAdError.ERR_SERVER:
                return MoPubErrorCode.SERVER_ERROR;
            case AlxAdError.ERR_UNKNOWN:
                return MoPubErrorCode.AD_SHOW_ERROR;
            case AlxAdError.ERR_VIDEO_PLAY_FAIL:
                return MoPubErrorCode.FULLSCREEN_SHOW_ERROR;
            default:
                return MoPubErrorCode.UNSPECIFIED;
        }
    }
}
