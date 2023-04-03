package com.alxad.demo.ironsource;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.alxad.demo.R;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

public class IronSourceRewardedVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "IronSourceRewardedVideoActivity";
    private IronSource mIronSource;
    private TextView mTvTip;
    private Boolean aBoolean;
    private TextView mTvShow;
    private long startTime;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_show);
        initView();
        //IronSource.init(this, AdConfig.IRON_SOURCE_APP_KEY);
        String advertisingId = IronSource.getAdvertiserId(IronSourceRewardedVideoActivity.this);
        // we're using an advertisingId as the 'userId'
        //initIronSource(APP_KEY, advertisingId);
        Log.d(TAG, "advertisid : " + advertisingId);
        IronSource.setAdaptersDebug(true);
        IntegrationHelper.validateIntegration(this);
        IronSource.setUserId(advertisingId);
        IronSource.getAdvertiserId(this);
        //Network Connectivity Status
        IronSource.shouldTrackNetworkState(this, true);
    }

    public void initView() {
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
            bnLoad();
        } else if (id == R.id.tv_show) {
            bnShow();
        }
    }

    private void bnLoad() {
        mTvTip.setText(R.string.loading);
        startTime = System.currentTimeMillis();
        mTvShow.setEnabled(false);

        // Manual
		/*
        IronSource.setManualLoadRewardedVideo(new RewardedVideoManualListener() {
            @Override
            public void onRewardedVideoAdReady() {

            }

            @Override
            public void onRewardedVideoAdLoadFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewardedVideoAvailabilityChanged(boolean b) {

            }

            @Override
            public void onRewardedVideoAdStarted() {

            }

            @Override
            public void onRewardedVideoAdEnded() {

            }

            @Override
            public void onRewardedVideoAdRewarded(Placement placement) {

            }

            @Override
            public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onRewardedVideoAdClicked(Placement placement) {

            }
        });
        IronSource.loadRewardedVideo();
		*/
        // Auto
        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAdOpened() {
                Log.d(TAG, "onRewardedVideoAdOpened");

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAdClosed() {
                Log.d(TAG, "onRewardedVideoAdClosed");

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAvailabilityChanged(boolean b) {
                aBoolean = b;
                Log.d(TAG, "onRewardedVideoAvailabilityChanged:" + b);
                if (b) {
                    Log.d(TAG, "onRewardedVideoAvailabilityChanged true");
                    mTvTip.setText(getString(R.string.format_load_success, (System.currentTimeMillis() - startTime) / 1000));
                    mTvShow.setEnabled(true);
                } else {
                    Log.d(TAG, "onRewardedVideoAvailabilityChanged false");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAdStarted() {
                Log.d(TAG, "onRewardedVideoAdStarted");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAdEnded() {
                Log.d(TAG, "onRewardedVideoAdEnded");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAdRewarded(Placement placement) {
                Log.d(TAG, "onRewardedVideoAdRewarded");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
                Log.d(TAG, "onRewardedVideoAdShowFailed" + ironSourceError);
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_failed, ironSourceError.toString()));
                mTvShow.setEnabled(false);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onRewardedVideoAdClicked(Placement placement) {
                Log.d(TAG, "onRewardedVideoAdClicked");
            }
        });


    }


    private void bnShow() {
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.showRewardedVideo();
        }
    }

    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }
}