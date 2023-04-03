package com.alxad.demo.ironsource;

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
import com.ironsource.mediationsdk.sdk.InterstitialListener;

public class IronSourceInterstitialActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "IronSourceInterstitial";
    private IronSource mIronSource;
    private TextView mTvTip;
    private TextView mTvShow;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_show);
        initView();
        String advertisingId = IronSource.getAdvertiserId(IronSourceInterstitialActivity.this);
        // we're using an advertisingId as the 'userId'
        //initIronSource(APP_KEY, advertisingId);
        Log.d(TAG, "advertisid : " + advertisingId);
        IntegrationHelper.validateIntegration(this);
        IronSource.setUserId(advertisingId);
        IronSource.getAdvertiserId(this);
        //Network Connectivity Status
        IronSource.shouldTrackNetworkState(this, true);
        // IronSource.init(this, "6315421",IronSource.AD_UNIT.INTERSTITIAL);
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
            bnLoad();
        } else if (id == R.id.tv_show) {
            bnShow();
        }
    }

    private void bnLoad() {
        mTvTip.setText(R.string.loading);
        startTime = System.currentTimeMillis();
        mTvShow.setEnabled(false);
        IronSource.setInterstitialListener(new InterstitialListener() {
            @Override
            public void onInterstitialAdReady() {
                Log.d(TAG, "onInterstitialAdReady");
                Toast.makeText(getBaseContext(), getString(R.string.load_success), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_success, (System.currentTimeMillis() - startTime) / 1000));
                mTvShow.setEnabled(true);
            }

            @Override
            public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                Log.d(TAG, "onInterstitialAdLoadFailed" + ironSourceError);
                Toast.makeText(getBaseContext(), getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
                mTvTip.setText(getString(R.string.format_load_failed, ironSourceError.toString()));
                mTvShow.setEnabled(false);
            }

            @Override
            public void onInterstitialAdOpened() {

            }

            @Override
            public void onInterstitialAdClosed() {
                Log.d(TAG, "onInterstitialAdClosed");
            }

            @Override
            public void onInterstitialAdShowSucceeded() {
                Log.d(TAG, "onInterstitialAdShowSucceeded");
            }

            @Override
            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                Log.d(TAG, "onInterstitialAdShowFailed: " + ironSourceError.toString());
            }

            @Override
            public void onInterstitialAdClicked() {
                Log.d(TAG, "onInterstitialAdClicked");
            }
        });
        IronSource.loadInterstitial();
    }


    private void bnShow() {
        if (IronSource.isInterstitialReady()) {
            IronSource.showInterstitial();
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