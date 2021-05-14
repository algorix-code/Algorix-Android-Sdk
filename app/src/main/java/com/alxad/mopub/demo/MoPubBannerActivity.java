package com.alxad.mopub.demo;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alxad.R;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import static com.mopub.common.logging.MoPubLog.LogLevel.DEBUG;

public class MoPubBannerActivity extends AppCompatActivity {
    private final String TAG = "MoPubBannerActivity";
    private static final String MOPUB_BANNER_TEST_ID = "b195f8dd8ded45fe847ad89ed1d016da";

    private MoPubView moPubView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopub_banner_demo);
        initView();
        final SdkConfiguration.Builder configBuilder = new SdkConfiguration
                .Builder(MOPUB_BANNER_TEST_ID);
        configBuilder.withLogLevel(DEBUG);
        SdkInitializationListener sdkInitializationListener = () -> {
            Log.d(TAG, "初始化完成");
            loadAd();
        };
        MoPub.initializeSdk(this, configBuilder.build(), sdkInitializationListener);
    }

    private void initView() {
        moPubView = findViewById(R.id.mp_view);
    }


    private void loadAd() {
        moPubView.setAdUnitId(MOPUB_BANNER_TEST_ID);
        //  moPubView.setAdSize(MoPubView.MoPubAdSize.MATCH_VIEW);
        moPubView.loadAd();
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(@NonNull MoPubView banner) {
                Log.d(TAG, "横幅加载成功");
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Log.d(TAG, "横幅加载失败:" + errorCode);

            }

            @Override
            public void onBannerClicked(MoPubView banner) {
                Log.d(TAG, "横幅加载成功");
            }

            @Override
            public void onBannerExpanded(MoPubView banner) {
                Log.d(TAG, "横幅展开");
            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {
                Log.d(TAG, "横幅收起");
            }
        });
    }


    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (moPubView != null) {
            moPubView.destroy();
        }
        super.onDestroy();
    }

}
