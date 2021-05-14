package com.alxad.mopub.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alxad.R;
import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedAdListener;
import com.mopub.mobileads.MoPubRewardedAds;

import java.util.Set;

import static com.mopub.common.logging.MoPubLog.LogLevel.DEBUG;

public class MoPubRewardVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MoPubRewardVideo";

    private static final String MOPUB_REWARD_TEST_ID = "920b6145fb1546cf8b5cf2ac34638bb7";

    private TextView mTvTip;
    private TextView mTvVideoShow;
    private long startTime;
    private MoPubRewardedAdListener rewardedAdListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopub_video_demo);
        final SdkConfiguration.Builder configBuilder = new SdkConfiguration
                .Builder(MOPUB_REWARD_TEST_ID);
        configBuilder.withLogLevel(DEBUG);
        SdkInitializationListener sdkInitializationListener = () -> {
            Log.d(TAG, "初始化完成");
            loadAd();
        };
        MoPub.initializeSdk(this, configBuilder.build(), sdkInitializationListener);
        initView();
    }

    private void initView() {
        TextView tv_video_load = findViewById(R.id.tv_video_load);
        mTvVideoShow = findViewById(R.id.tv_video_show);
        mTvTip = findViewById(R.id.tv_tip);
        mTvVideoShow.setEnabled(false);
        tv_video_load.setOnClickListener(this);
        mTvVideoShow.setOnClickListener(this);
        rewardedAdListener = new MoPubRewardedAdListener() {
            @Override
            public void onRewardedAdLoadSuccess(String adUnitId) {
                Log.d(TAG, "视频广告加载成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告加载成功", Toast.LENGTH_SHORT).show();
                        mTvTip.setText("广告加载成功--耗时-" + (System.currentTimeMillis() - startTime) / 1000 + "-秒");
                        mTvVideoShow.setEnabled(true);
                    }
                });
                // Called when the ad for the given adUnitId has loaded. At this point you should be able to call MoPubRewardedAds.showRewardedAd() to show the ad.
            }
            @Override
            public void onRewardedAdLoadFailure(String adUnitId, MoPubErrorCode errorCode) {
                Log.d(TAG, "视频广告加载错误 " + errorCode);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告加载失败", Toast.LENGTH_SHORT).show();
                        mTvTip.setText("广告加载错误" + errorCode);
                        mTvVideoShow.setEnabled(false);
                    }
                });
                // Called when the ad fails to load for the given adUnitId. The provided error code will provide more insight into the reason for the failure to load.
            }

            @Override
            public void onRewardedAdStarted(String adUnitId) {
                // Called when a rewarded ad starts playing.
                Log.d(TAG, "onRewardedAdStarted");
                Toast.makeText(MoPubRewardVideoActivity.this,
                        "onRewardedAdStarted",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdShowError(String adUnitId, MoPubErrorCode errorCode) {
                //  Called when there is an error while attempting to show the ad.
                Log.d(TAG, "onRewardedAdShowError"+errorCode);
                Toast.makeText(MoPubRewardVideoActivity.this,
                        "onRewardedAdShowError",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdClicked(@NonNull String adUnitId) {
                //  Called when a rewarded ad is clicked.
                Log.d(TAG, "onRewardedAdClicked");
                Toast.makeText(MoPubRewardVideoActivity.this,
                        "onRewardedAdClicked",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdClosed(String adUnitId) {
                // Called when a rewarded ad is closed. At this point your application should resume.
                loadAd();
                Log.d(TAG, "onRewardedAdClosed");
                Toast.makeText(MoPubRewardVideoActivity.this,
                        "onRewardedAdClosed",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedAdCompleted(Set<String> adUnitIds, MoPubReward reward) {
                // Called when a rewarded ad is completed and the user should be rewarded.
                // You can query the reward object with boolean isSuccessful(), String getLabel(), and int getAmount().
                Log.d(TAG, "onRewardedAdCompleted");
                Toast.makeText(MoPubRewardVideoActivity.this,
                        "onRewardedAdCompleted",
                        Toast.LENGTH_SHORT).show();
            }
        };
        MoPubRewardedAds.setRewardedAdListener(rewardedAdListener);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_video_load:
                loadAd();
                break;
            case R.id.tv_video_show:
                if (MoPubRewardedAds.hasRewardedAd(MOPUB_REWARD_TEST_ID)){
                    MoPubRewardedAds.showRewardedAd(MOPUB_REWARD_TEST_ID);
                }
                break;
        }
    }

    /**
     * 加载广告
     */
    public void loadAd() {
        mTvTip.setText("广告加载中...");
        startTime = System.currentTimeMillis();
        MoPubRewardedAds.loadRewardedAd(MOPUB_REWARD_TEST_ID);
    }



}