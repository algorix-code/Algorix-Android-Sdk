package com.anythink.custom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alxad.api.AlxAdParam;
import com.alxad.api.AlxAdSDK;
import com.alxad.api.AlxImage;
import com.alxad.api.AlxSdkInitCallback;
import com.alxad.api.nativead.AlxMediaContent;
import com.alxad.api.nativead.AlxMediaView;
import com.alxad.api.nativead.AlxNativeAd;
import com.alxad.api.nativead.AlxNativeAdLoadedListener;
import com.alxad.api.nativead.AlxNativeAdLoader;
import com.alxad.api.nativead.AlxNativeAdView;
import com.alxad.api.nativead.AlxNativeEventListener;
import com.anythink.nativead.api.ATNativePrepareInfo;
import com.anythink.nativead.unitgroup.api.CustomNativeAd;
import com.anythink.nativead.unitgroup.api.CustomNativeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TopOn 信息流广告适配器
 */
public class AlxNativeAdapter extends CustomNativeAdapter {
    private final String TAG = AlxNativeAdapter.class.getSimpleName();

    private String unitid = "";
    private String appid = "";
    private String sid = "";
    private String token = "";
    private Boolean isdebug = false; //判断是否已经执行回调，防止重复执行回调方法

    @Override
    public void loadCustomNetworkAd(Context context, Map<String, Object> serverExtras, Map<String, Object> map1) {
        Log.d(TAG, "alx-topon-adapter-version:" + AlxMetaInf.ADAPTER_VERSION);
        Log.i(TAG, "loadCustomNetworkAd");
        if (parseServer(serverExtras)) {
            initSdk(context);
        }
    }

    private boolean parseServer(Map<String, Object> serverExtras) {
        try {
            if (serverExtras.containsKey("unitid")) {
                unitid = (String) serverExtras.get("unitid");
            }
            if (serverExtras.containsKey("appid")) {
                appid = (String) serverExtras.get("appid");

            } else if (serverExtras.containsKey("appkey")) {
                appid = (String) serverExtras.get("appkey");
            }
            if (serverExtras.containsKey("appkey")) {
                sid = (String) serverExtras.get("appkey");
            } else if (serverExtras.containsKey("sid")) {
                sid = (String) serverExtras.get("sid");
            }
            if (serverExtras.containsKey("license")) {
                token = (String) serverExtras.get("license");
            } else if (serverExtras.containsKey("token")) {
                token = (String) serverExtras.get("token");
            }

            if (serverExtras.containsKey("isdebug")) {
                String test = serverExtras.get("isdebug").toString();
                Log.e(TAG, "alx debug mode:" + test);
                if (test.equals("true")) {
                    isdebug = true;
                } else {
                    isdebug = false;
                }
            } else {
                Log.e(TAG, "alx debug mode: false");
            }
            if (serverExtras.containsKey("tag")) {
                String tag = serverExtras.get("tag").toString();
                Log.e(TAG, "alx json tag:" + tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(unitid) || TextUtils.isEmpty(token) || TextUtils.isEmpty(sid) || TextUtils.isEmpty(appid)) {
            Log.i(TAG, "alx unitid | token | sid | appid is empty");
            if (mLoadListener != null) {
                mLoadListener.onAdLoadError("", "alx unitid | token | sid | appid is empty.");
            }
            return false;
        }
        return true;
    }

    private void initSdk(final Context context) {
        try {
            Log.i(TAG, "alx ver:" + AlxAdSDK.getNetWorkVersion() + " alx token: " + token + " alx appid: " + appid + " alx sid: " + sid);

            AlxAdSDK.setDebug(isdebug);
            AlxAdSDK.init(context, token, sid, appid, new AlxSdkInitCallback() {
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

    private void startAdLoad(final Context context) {
        AlxNativeAdLoadedListener loadListener = new AlxNativeAdLoadedListener() {
            @Override
            public void onAdFailed(int errorCode, String errorMsg) {
                Log.i(TAG, "onAdLoadedFail:" + errorCode + ";" + errorMsg);
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError(errorCode + "", errorMsg);
                }
            }

            @Override
            public void onAdLoaded(List<AlxNativeAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    if (mLoadListener != null) {
                        mLoadListener.onAdLoadError("100", "no fill");
                    }
                    return;
                }

                AlgorixNativeAd[] result = new AlgorixNativeAd[ads.size()];
                boolean isOk = false;
                try {
                    for (int i = 0; i < ads.size(); i++) {
                        AlxNativeAd item = ads.get(i);
                        AlgorixNativeAd bean = new AlgorixNativeAd(context, item);
                        bean.setAdData();
                        result[i] = bean;
                    }
                    isOk = true;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    isOk = false;
                    if (mLoadListener != null) {
                        mLoadListener.onAdLoadError("101", e.getMessage());
                    }
                }
                if (isOk) {
                    if (mLoadListener != null) {
                        mLoadListener.onAdCacheLoaded(result);
                    }
                }
            }
        };

        AlxNativeAdLoader loader = new AlxNativeAdLoader.Builder(context, unitid).build();
        loader.loadAd(new AlxAdParam.Builder().build(), loadListener);
    }

    private class AlgorixNativeAd extends CustomNativeAd {

        private Context mContext;

        private AlxNativeAd mNativeAd;
        private AlxNativeAdView mAdContainer;
        private AlxMediaView mMediaView;

        public AlgorixNativeAd(Context context, AlxNativeAd nativeAd) {
            mContext = context.getApplicationContext();
            mNativeAd = nativeAd;
        }

        public void setAdData() {
            if (mNativeAd == null) {
                return;
            }
            bindListener();

            setTitle(mNativeAd.getTitle());
            setDescriptionText(mNativeAd.getDescription());

            String iconUrl = "";
            String imageUrl = "";
            if (mNativeAd.getIcon() != null) {
                iconUrl = mNativeAd.getIcon().getImageUrl();
            }
            List<String> list = new ArrayList<>();
            List<AlxImage> imageList = mNativeAd.getImages();
            if (imageList != null && imageList.size() > 0) {
                AlxImage image0 = imageList.get(0);
                if (image0 != null) {
                    imageUrl = image0.getImageUrl();
                }
                for (AlxImage item : imageList) {
                    if (item != null && item.getImageUrl() != null) {
                        list.add(item.getImageUrl());
                    }
                }
            }
            setIconImageUrl(iconUrl);
            setMainImageUrl(imageUrl);
            setImageUrlList(list);
            setAdFrom(mNativeAd.getAdSource());
            setCallToActionText(mNativeAd.getCallToAction());
        }

        @Override
        public Bitmap getAdLogo() {
            if (mNativeAd != null) {
                return mNativeAd.getAdLogo();
            }
            return null;
        }

        @Override
        public void prepare(View view, ATNativePrepareInfo nativePrepareInfo) {
            if (view == null) {
                return;
            }

            try {
                if (mAdContainer == null) {
                    return;
                }
                if (nativePrepareInfo != null) {
                    List<View> clickViewList = nativePrepareInfo.getClickViewList();
                    if (clickViewList != null && !clickViewList.isEmpty()) {
                        for (int i = 0; i < clickViewList.size(); i++) {
                            String key = String.valueOf(1000 + i);
                            mAdContainer.addView(key, clickViewList.get(i));
                        }
                    }
                }
                if (mMediaView != null) {
                    mAdContainer.setMediaView(mMediaView);
                }
                mAdContainer.setNativeAd(mNativeAd);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public boolean isNativeExpress() {
            Log.d(TAG, "isNativeExpress");
            return false;
        }

        @Override
        public ViewGroup getCustomAdContainer() {
            Log.d(TAG, "getCustomAdContainer");
            mAdContainer = new AlxNativeAdView(mContext);
            return mAdContainer;
        }

        @Override
        public View getAdMediaView(Object... objects) {
            Log.d(TAG, "getAdMediaView");
            try {
                if (mMediaView != null) {
                    mMediaView.destroy();
                    mMediaView = null;
                }
                mMediaView = new AlxMediaView(mContext);
                if (mNativeAd != null && mNativeAd.getMediaContent() != null) {
                    mNativeAd.getMediaContent().setVideoLifecycleListener(new AlxMediaContent.VideoLifecycleListener() {

                        @Override
                        public void onVideoStart() {
                            notifyAdVideoStart();
                        }

                        @Override
                        public void onVideoEnd() {
                            notifyAdVideoEnd();
                        }
                    });
                    mMediaView.setMediaContent(mNativeAd.getMediaContent());
                }
                return mMediaView;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        public void clear(View view) {
            Log.d(TAG, "clear");
            try {
                if (mMediaView != null) {
                    mMediaView.destroy();
                    mMediaView = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void destroy() {
            Log.d(TAG, "destroy");
            try {
                if (mMediaView != null) {
                    mMediaView.destroy();
                    mMediaView = null;
                }
                if (mAdContainer != null) {
                    mAdContainer.destroy();
                    mAdContainer = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }

        private void bindListener() {
            if (mNativeAd == null) {
                return;
            }
            mNativeAd.setNativeEventListener(new AlxNativeEventListener() {
                @Override
                public void onAdClicked() {
                    notifyAdClicked();
                }

                @Override
                public void onAdImpression() {
                    notifyAdImpression();
                }

                @Override
                public void onAdClosed() {
                    notifyAdDislikeClick();
                }
            });
        }

    }

    @Override
    public void destory() {
    }

    @Override
    public String getNetworkName() {
        return AlxAdSDK.getNetWorkName();
    }

    @Override
    public String getNetworkPlacementId() {
        return unitid;
    }

    @Override
    public String getNetworkSDKVersion() {
        return AlxAdSDK.getNetWorkVersion();
    }
}