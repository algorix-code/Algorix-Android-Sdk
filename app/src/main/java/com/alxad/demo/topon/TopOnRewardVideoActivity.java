package com.alxad.demo.topon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alxad.demo.AdConfig;
import com.alxad.demo.BaseActivity;
import com.alxad.demo.R;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.rewardvideo.api.ATRewardVideoAd;
import com.anythink.rewardvideo.api.ATRewardVideoListener;


public class TopOnRewardVideoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "TopOnRewardActivity";
    private TextView mTvTip;
    private TextView mTvShow;
    private ATRewardVideoAd mVideoAD;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_show);
        initView();
    }

    private void initView() {
        TextView tv_load = findViewById(R.id.tv_load);
        mTvShow = findViewById(R.id.tv_show);
        mTvTip = findViewById(R.id.tv_tip);
        mTvShow.setEnabled(false);
        tv_load.setOnClickListener(this);
        mTvShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_load) {
            loadAd();
        } else if (id == R.id.tv_show) {
            if (mVideoAD == null) {
                Toast.makeText(this, getString(R.string.show_ad_no_load), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mVideoAD.isAdReady()) {
                mVideoAD.show(this);
            } else {
                Toast.makeText(this, "isAdReady()==false", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 加载广告
     */
    public void loadAd() {
        mTvTip.setText(R.string.loading);
        startTime = System.currentTimeMillis();

        mVideoAD = new ATRewardVideoAd(this, AdConfig.TOPON_VIDEO_AD_PID);
        mVideoAD.setAdListener(new ATRewardVideoListener() {

            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i(TAG, "onRewardedVideoAdLoaded:" + getThreadName());
                Toast.makeText(getBaseContext(), getString(R.string.load_success), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_success, (System.currentTimeMillis() - startTime) / 1000));
                mTvShow.setEnabled(true);
            }

            @Override
            public void onRewardedVideoAdFailed(AdError errorCode) {
                Log.i(TAG, "onRewardedVideoAdFailed:" + errorCode.getCode() + " " + errorCode.getDesc() + ";" + getThreadName());
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
                mTvTip.setText(R.string.load_failed);
                mTvShow.setEnabled(false);
            }

            @Override
            public void onRewardedVideoAdPlayStart(ATAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayStart:" + getThreadName());
            }

            @Override
            public void onRewardedVideoAdPlayEnd(ATAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayEnd:" + getThreadName());
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError errorCode, ATAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayFailed:" + getThreadName());
            }

            @Override
            public void onRewardedVideoAdClosed(ATAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdClosed:" + getThreadName());
                mTvShow.setEnabled(false);
                mTvTip.setText("");
            }

            @Override
            public void onRewardedVideoAdPlayClicked(ATAdInfo entity) {
                Log.i(TAG, "onRewardedVideoAdPlayClicked:" + getThreadName());
            }

            @Override
            public void onReward(ATAdInfo entity) {
                Log.i(TAG, "onReward: " + getThreadName());
            }
        });
        mVideoAD.load();
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

}