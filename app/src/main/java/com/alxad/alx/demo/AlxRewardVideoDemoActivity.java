package com.alxad.alx.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alxad.AppConfig;
import com.alxad.R;
import com.alxad.api.AlxRewardVideoAD;
import com.alxad.api.AlxRewardVideoADListener;
import com.alxad.comm.AlxLog;

public class AlxRewardVideoDemoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AlxRewardVideoDemoActivity";

    private TextView mTvTip;
    private TextView mTvVideoShow;
    private AlxRewardVideoAD mVideoAD;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alx_video_demo);
        initView();
    }

    private void initView() {
        TextView tv_video_load = findViewById(R.id.tv_video_load);
        mTvVideoShow = findViewById(R.id.tv_video_show);
        mTvTip = findViewById(R.id.tv_tip);
        mTvVideoShow.setEnabled(false);
        tv_video_load.setOnClickListener(this);
        mTvVideoShow.setOnClickListener(this);
        mVideoAD = new AlxRewardVideoAD();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_video_load:
                loadAd();
                break;
            case R.id.tv_video_show:
                if (mVideoAD.isLoaded()) {
                    mVideoAD.showVideo(this);
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
        mVideoAD.load(this, AppConfig.VIDEO_AD_PID, new AlxRewardVideoADListener() {

            @Override
            public void onRewardedVideoAdLoaded(AlxRewardVideoAD var1) {
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
            public void onRewardedVideoAdFailed(AlxRewardVideoAD var1, int errCode, String errMsg) {
                AlxLog.i(TAG, "视频广告加载错误 " + errCode + " " + errMsg);
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
            public void onRewardedVideoAdPlayStart(AlxRewardVideoAD var1) {
                AlxLog.i(TAG, "视频广告展示成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告展示成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onRewardedVideoAdPlayEnd(AlxRewardVideoAD var1) {
                AlxLog.i(TAG, "视频广告观看完整");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告观看完整", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AlxRewardVideoAD var2, int errCode, String errMsg) {
                AlxLog.i(TAG, "视频播放失败:"+errCode+";"+errMsg);
            }

            @Override
            public void onRewardedVideoAdClosed(AlxRewardVideoAD var1) {
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
            public void onRewardedVideoAdPlayClicked(AlxRewardVideoAD var1) {
                AlxLog.i(TAG, "视频广告点击成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "视频广告点击成功", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onReward(AlxRewardVideoAD var1) {
                AlxLog.i(TAG, "视频广告观看完整--给予奖励");
            }
        });

    }
}