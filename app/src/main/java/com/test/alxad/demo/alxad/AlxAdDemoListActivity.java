package com.test.alxad.demo.alxad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.test.alxad.demo.R;


public class AlxAdDemoListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alx_ad_demo);
        initView();
    }

    private void initView() {
        TextView tv_banner_320_50 = findViewById(R.id.tv_banner_320_50);
        TextView tv_banner_300_250 = findViewById(R.id.tv_banner_300_250);
        TextView tv_banner_300_480 = findViewById(R.id.tv_banner_300_480);
        TextView tv_video_ad = findViewById(R.id.tv_video_ad);
        tv_banner_320_50.setOnClickListener(this);
        tv_banner_300_250.setOnClickListener(this);
        tv_banner_300_480.setOnClickListener(this);
        tv_video_ad.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_banner_320_50:
                startActivity(new Intent(this, AlxBannerDemoActivity.class)
                        .putExtra("adsize",0));
                break;
            case R.id.tv_banner_300_250:
                startActivity(new Intent(this, AlxBannerDemoActivity.class)
                        .putExtra("adsize",1));
                break;
            case R.id.tv_banner_300_480:
                startActivity(new Intent(this, AlxBannerDemoActivity.class)
                        .putExtra("adsize",2));
                break;
            case R.id.tv_video_ad:
                startActivity(new Intent(this, AlxRewardVideoDemoActivity.class));
                break;
        }
    }
}