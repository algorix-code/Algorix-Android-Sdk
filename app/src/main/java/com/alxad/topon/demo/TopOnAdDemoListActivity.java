package com.alxad.topon.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alxad.R;

public class TopOnAdDemoListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_on_ad_demo);
        initView();
    }

    private void initView() {
        TextView tv_banner = findViewById(R.id.tv_banner);
        TextView tv_video_ad = findViewById(R.id.tv_video_ad);
        tv_banner.setOnClickListener(this);
        tv_video_ad.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_banner:
                startActivity(new Intent(this, TopOnBannerDemoActivity.class));
                break;
            case R.id.tv_video_ad:
                startActivity(new Intent(this, TopOnVideoDemoActivity.class));
                break;
        }
    }
}