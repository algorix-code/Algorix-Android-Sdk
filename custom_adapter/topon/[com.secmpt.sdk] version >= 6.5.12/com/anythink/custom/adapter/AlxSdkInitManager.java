package com.anythink.custom.adapter;

import android.content.Context;
import android.util.Log;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxSdkInitCallback;
import com.secmtp.sdk.core.api.ATAdConst;
import com.secmtp.sdk.core.api.ATBiddingNotice;
import com.secmtp.sdk.core.api.ATBiddingResult;
import com.secmtp.sdk.core.api.ATInitMediation;
import com.secmtp.sdk.core.api.ATSDK;
import com.secmtp.sdk.core.api.MediationInitCallback;
import com.secmtp.sdk.core.api.bridge.ATAdapterBridgeConst;

import java.util.Map;
import java.util.UUID;

public class AlxSdkInitManager extends ATInitMediation {

    private volatile static AlxSdkInitManager sInstance;
    private String TAG = "AlxSdkInitManager";
    Boolean success = false;

    private String appid = "";
    private String sid = "";
    private String token = "";

    private AlxSdkInitManager() {

    }

    @Override
    public int getAdapterBridgeVersion() {
        return ATAdapterBridgeConst.ADAPTER_BRIDGE_VERSIONCODE;
    }

    public static AlxSdkInitManager getInstance() {
        if (sInstance == null) {
            synchronized (AlxSdkInitManager.class) {
                if (sInstance == null)
                    sInstance = new AlxSdkInitManager();
            }
        }
        return sInstance;
    }

    public synchronized void initSDK(Context context, Map<String, Object> serviceExtras) {
        initSDK(context, serviceExtras, null);
    }

    @Override
    public void initSDK(Context context, Map<String, Object> serviceExtras, MediationInitCallback mediationInitCallback) {
        Log.d(TAG, "initSDK");
        String error = "";
        try {
            if (serviceExtras.containsKey("appid")) {
                appid = (String) serviceExtras.get("appid");
            }
            if (serviceExtras.containsKey("sid")) {
                sid = (String) serviceExtras.get("sid");
            }
            if (serviceExtras.containsKey("token")) {
                token = (String) serviceExtras.get("token");
            }

            Boolean isDebug = null;
            if (serviceExtras.containsKey("isdebug")) {
                Object obj = serviceExtras.get("isdebug");
                String debug = null;
                if (obj instanceof String) {
                    debug = (String) obj;
                }
                Log.e(TAG, "alx debug mode:" + debug);
                if (debug != null) {
                    if (debug.equalsIgnoreCase("true")) {
                        isDebug = Boolean.TRUE;
                    } else if (debug.equalsIgnoreCase("false")) {
                        isDebug = Boolean.FALSE;
                    }
                }
            }

            AlxAdSDK.init(context, token, sid, appid, new AlxSdkInitCallback() {
                @Override
                public void onInit(boolean isOk, String msg) {
                    Log.d(TAG, "Alx sdk init success");
                    success = true;
                }
            });
            if (isDebug != null) {
                AlxAdSDK.setDebug(isDebug);
            }
        } catch (Exception e) {
            success = false;
            error = e.getMessage();
            Log.e(TAG, "Alx sdk init failed:" + e.getMessage());
        }

        if (mediationInitCallback != null) {
            if (success) {
                mediationInitCallback.onSuccess();
            } else {
                mediationInitCallback.onFail("AlxSdk initSDK failed:" + error);
            }
        }

    }

    public static ATBiddingResult getBiddingSuccessBean(double bidPrice) {
        //get currency
        ATAdConst.CURRENCY currency = ATAdConst.CURRENCY.USD;

        //get uuid
        String token = UUID.randomUUID().toString();

        //BiddingNotice
        ATBiddingNotice biddingNotice = null;
        return ATBiddingResult.success(bidPrice, token, biddingNotice, currency);
    }

    public static void printSDKInfo(String tag) {
        Log.d(tag, "alx-topon-adapter-version:" + AlxMetaInf.ADAPTER_VERSION);
        Log.d(tag, "topon-sdk-version:" + ATSDK.getSDKVersionName());
    }

}

