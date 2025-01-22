package com.alxad.demo.admob;

import android.os.Bundle;


import com.alxad.demo.BaseListActivity;
import com.alxad.demo.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class AdmobDemoListActivity extends BaseListActivity {

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initSdk();
    }

    @Override
    public List<BaseListActivity.AdapterData> initAdapterData() {
        List<AdapterData> list = new ArrayList<>();

        AdapterData bannerItem = new AdapterData(getString(R.string.banner_ad), AdmobBannerActivity.class);
        AdapterData rewardItem = new AdapterData(getString(R.string.reward_ad), AdmobRewardVideoActivity.class);
        AdapterData interstitialItem = new AdapterData(getString(R.string.interstitial_ad), AdmobInterstitialActivity.class);
        AdapterData nativeItem = new AdapterData(getString(R.string.native_ad), AdmobNativeActivity.class);
        list.add(bannerItem);
        list.add(rewardItem);
        list.add(interstitialItem);
        list.add(nativeItem);
        return list;
    }

    public void initSdk(){
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

}