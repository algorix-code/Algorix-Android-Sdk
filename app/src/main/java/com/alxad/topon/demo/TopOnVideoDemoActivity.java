package com.alxad.topon.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alxad.AppConfig;
import com.alxad.R;
import com.alxad.comm.AlxLog;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.rewardvideo.api.ATRewardVideoAd;
import com.anythink.rewardvideo.api.ATRewardVideoExListener;

public class TopOnVideoDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TopOnVideoDemoActivity";
    private TextView mTvTip;
    private TextView mTvVideoShow;
    private ATRewardVideoAd mVideoAD;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topon_video_demo);
        initView();
    }

    private void initView() {
        TextView tv_video_load = findViewById(R.id.tv_video_load);
        mTvVideoShow = findViewById(R.id.tv_video_show);
        mTvTip = findViewById(R.id.tv_tip);
        mTvVideoShow.setEnabled(false);
        tv_video_load.setOnClickListener(this);
        mTvVideoShow.setOnClickListener(this);
        mVideoAD = new ATRewardVideoAd(this,AppConfig.VIDEO_AD_PID_TOPON);
        mVideoAD.setAdListener(new ATRewardVideoExListener() {

            @Override
            public void onDeeplinkCallback(ATAdInfo adInfo, boolean isSuccess) {
                AlxLog.i(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                AlxLog.i(TAG, "视频广告加载成功");
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
            public void onRewardedVideoAdFailed(AdError errorCode) {
                AlxLog.i(TAG, "视频广告加载错误 " + errorCode.getCode() + " " + errorCode.getDesc());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告加载失败", Toast.LENGTH_SHORT).show();
                        mTvTip.setText("广告加载错误");
                        mTvVideoShow.setEnabled(false);
                    }
                });
            }

            @Override
            public void onRewardedVideoAdPlayStart(ATAdInfo entity) {
                AlxLog.i(TAG, "视频广告展示成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告展示成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onRewardedVideoAdPlayEnd(ATAdInfo entity) {
                AlxLog.i(TAG, "视频广告观看完整");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告观看完整", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError errorCode, ATAdInfo entity) {
                AlxLog.i(TAG, "视频播放失败");
            }

            @Override
            public void onRewardedVideoAdClosed(ATAdInfo entity) {
                AlxLog.i(TAG, "点击按钮关闭广告");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvVideoShow.setEnabled(false);
                        mTvTip.setText("广告未加载");
                    }
                });
            }

            @Override
            public void onRewardedVideoAdPlayClicked(ATAdInfo entity) {
                AlxLog.i(TAG, "视频广告点击成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告点击成功", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onReward(ATAdInfo entity) {
                AlxLog.i(TAG, "视频广告观看完整--给予奖励");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_video_load:
                loadAd();
                break;
            case R.id.tv_video_show:
                if (mVideoAD.isAdReady()) {
                    mVideoAD.show(this);
                } else {
                    loadAd();
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
        mVideoAD.load();
    }

}
