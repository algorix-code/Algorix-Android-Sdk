package com.alxad.admob.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alxad.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdmobRewardVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AdmobRewardVideo";
    private static final String ADMOB_REWARD_TEST_ID = "ca-app-pub-4844642095927993/4713662017";
    private RewardedAd rewardedAd;
    private TextView mTvTip;
    private TextView mTvVideoShow;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admob_reward_video_demo);
        initView();
        rewardedAd = createAndLoadRewardedAd();
    }

    private void initView() {
        mTvVideoShow = findViewById(R.id.tv_video_show);
        mTvTip = findViewById(R.id.tv_tip);
        mTvVideoShow.setEnabled(false);
        mTvVideoShow.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_video_show) {
            if (rewardedAd.isLoaded()) {
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onRewardedAdOpened() {
                        Log.d(TAG, "onRewardedAdOpened");
                        Toast.makeText(AdmobRewardVideoActivity.this,
                                "Rewarded ad opened",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onRewardedAdClosed() {
                        Log.d(TAG, "onRewardedAdClosed");
                        rewardedAd = createAndLoadRewardedAd();
                        Toast.makeText(AdmobRewardVideoActivity.this,
                                "Rewarded ad closed",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        Log.d(TAG, "onUserEarnedReward");
                        // User earned reward.
                    }

                    @Override
                    public void onRewardedAdFailedToShow(AdError adError) {
                        Log.d(TAG, "广告加载错误Code  " + adError.getCode()
                                + "Message  " + adError.getMessage());

                    }
                };
                rewardedAd.show(this, adCallback);
            } else {
                Log.d(TAG, "The rewarded ad wasn't loaded yet.");
            }
        }
    }

    public RewardedAd createAndLoadRewardedAd() {
        mTvTip.setText("广告加载中...");
        startTime = System.currentTimeMillis();
        RewardedAd rewardedAd = new RewardedAd(this, ADMOB_REWARD_TEST_ID);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Log.d(TAG, "视频广告加载成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告加载成功", Toast.LENGTH_SHORT).show();
                        mTvTip.setText("广告加载成功--耗时-" + (System.currentTimeMillis() - startTime) / 1000 + "-秒");
                        mTvVideoShow.setEnabled(true);
                    }
                });
            }

            @Override
            public void onRewardedAdFailedToLoad(final LoadAdError adError) {
                Log.d(TAG, "视频广告加载错误 " + adError.getCode() + " " + adError.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告加载失败", Toast.LENGTH_SHORT).show();
                        mTvTip.setText("广告加载错误" + adError.getMessage());
                        mTvVideoShow.setEnabled(false);
                    }
                });
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

}