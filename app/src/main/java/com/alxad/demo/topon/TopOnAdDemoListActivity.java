package com.alxad.demo.topon;

import android.os.Bundle;


import com.alxad.demo.AdConfig;
import com.alxad.demo.BaseListActivity;
import com.alxad.demo.R;
import com.anythink.core.api.ATSDK;

import java.util.ArrayList;
import java.util.List;


public class TopOnAdDemoListActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSdk();
    }

    @Override
    public List<AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();
        AdapterData item = new AdapterData(getString(R.string.banner_ad), TopOnBannerActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.reward_ad), TopOnRewardVideoActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.interstitial_ad), TopOnInterstitialActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.native_ad), TopOnNativeActivity.class);
        list.add(item);

        item = new AdapterData(getString(R.string.splash_ad), TopOnSplashActivity.class);
        list.add(item);

        return list;
    }

    public  void initSdk () {
        ATSDK.init(getApplicationContext(), AdConfig.TOPON_APP_ID, AdConfig.TOPON_KEY);
        ATSDK.setNetworkLogDebug(true);
    }

}