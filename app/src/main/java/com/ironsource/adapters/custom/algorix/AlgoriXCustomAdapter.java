package com.ironsource.adapters.custom.algorix;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxSdkInitCallback;
import com.ironsource.mediationsdk.adunit.adapter.BaseAdapter;
import com.ironsource.mediationsdk.adunit.adapter.listener.NetworkInitializationListener;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdapterErrors;

import java.util.Map;

public class AlgoriXCustomAdapter extends BaseAdapter {

    private static final String TAG = "AlgoriXCustomAdapter";
    private String unitid = "";
    private String appid = "";
    private String sid = "";
    private String token = "";
    private Boolean isdebug = true;
    private Map<String, Object> serverExtras;
    private NetworkInitializationListener mNetworkInitializationListener;

    @Override
    public void init(AdData adData, Context context, NetworkInitializationListener networkInitializationListener) {
        Log.d(TAG, "alx-ironsource-adapter-version:" + AlxMetaInf.ADAPTER_VERSION);
        Log.i(TAG, "AlgoriX SDK Init");

        try {
            mNetworkInitializationListener = networkInitializationListener;
            if (parseServer(adData)) {
                Log.i(TAG, "alx ver:" + AlxAdSDK.getNetWorkVersion() + " alx token: " + token + " alx appid: " + appid + " alx sid: " + sid);

                AlxAdSDK.setDebug(isdebug);
                AlxAdSDK.init(context, token, sid, appid, new AlxSdkInitCallback() {
                    @Override
                    public void onInit(boolean isOk, String msg) {
                        if (isOk) {
                            mNetworkInitializationListener.onInitSuccess();
                        } else {
                            mNetworkInitializationListener.onInitFailed(AdapterErrors.ADAPTER_ERROR_MISSING_PARAMS, "AlxSdk Init Failed");
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private boolean parseServer(AdData adData) {
        try {
            serverExtras = adData.getConfiguration();
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

            if (serverExtras.containsKey("isdebug")) {
                String debug = serverExtras.get("isdebug").toString();
                Log.e(TAG, "alx debug mode:" + debug);
                if (TextUtils.equals(debug,"true")) {
                    isdebug = true;
                } else {
                    isdebug = false;
                }
            } else {
                Log.e(TAG, "alx debug mode: false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(unitid) || TextUtils.isEmpty(token) || TextUtils.isEmpty(sid) || TextUtils.isEmpty(appid)) {
            Log.i(TAG, "alx unitid | token | sid | appid is empty");
            if (mNetworkInitializationListener != null) {
                mNetworkInitializationListener.onInitFailed(AdapterErrors.ADAPTER_ERROR_MISSING_PARAMS, "alx unitid | token | sid | appid is empty");
            }
            return false;
        }
        return true;
    }

    @Override
    public String getNetworkSDKVersion() {
        return AlxAdSDK.getNetWorkVersion();
    }

    @Override
    public String getAdapterVersion() {
        return AlxMetaInf.ADAPTER_VERSION;
    }
}

