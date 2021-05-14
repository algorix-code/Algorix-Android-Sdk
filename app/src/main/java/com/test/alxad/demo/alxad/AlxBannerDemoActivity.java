package com.test.alxad.demo.alxad;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alxad.api.AlxBannerAD;
import com.alxad.api.AlxBannerADListener;
import com.test.alxad.demo.AppConfig;
import com.test.alxad.demo.R;

public class AlxBannerDemoActivity extends Activity {
    private final String TAG = "AlxBannerActivity";
    private AlxBannerAD alxBannerAD;
    private TextView mTvTitle;
    private int adSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alx_banner_demo);
        if (getIntent() != null) {
            adSize = getIntent().getIntExtra("adsize", 0);
        }
        AlxBannerAD.AlxAdSize alxAdSize = adSize == 0 ? AlxBannerAD.AlxAdSize.SIZE_320_50 :
                adSize == 1 ? AlxBannerAD.AlxAdSize.SIZE_300_250 :
                        AlxBannerAD.AlxAdSize.SIZE_320_480;
        Log.d(TAG, "adsize : " + alxAdSize);
        alxBannerAD = findViewById(R.id.do_ad_banner);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText("w_h:  " + alxAdSize);
        alxBannerAD.load(this, AppConfig.mBannerPid, new AlxBannerADListener() {
            @Override
            public void onAdLoaded(AlxBannerAD banner) {
                Log.d(TAG, "AlxBannerAD 横幅加载成功");
            }

            @Override
            public void onAdError(AlxBannerAD banner, int errorCode, String errorMsg) {
                Log.d(TAG, "AlxBannerAD 横幅加载失败  errorMsg:" + errorMsg + "  errorCode:" + errorCode);
            }

            @Override
            public void onAdClicked(AlxBannerAD banner) {
                Log.d(TAG, "AlxBannerAD 横幅点击成功");
            }

            @Override
            public void onAdShow(AlxBannerAD banner) {
                Log.d(TAG, "AlxBannerAD 横幅展示成功");
            }
        }, alxAdSize);
    }

    @Override
    protected void onDestroy() {
        if (alxBannerAD != null) {
            alxBannerAD.destory();
        }
        super.onDestroy();
    }

}
