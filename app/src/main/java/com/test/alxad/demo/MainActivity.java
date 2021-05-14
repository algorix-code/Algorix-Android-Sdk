package com.test.alxad.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.test.alxad.demo.alxad.AlxAdDemoListActivity;
import com.test.alxad.demo.toponad.TopOnAdDemoListActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //申请相关的权限可以更加精准的推送广告资源 权限组 按需添加
    String[] mPermissions = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android 6.0及以上动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermission();
        }
        initView();
    }


    private void initView() {
        TextView tv_alx_ad = findViewById(R.id.tv_alx_ad);
        TextView tv_top_on_ad = findViewById(R.id.tv_top_on_ad);
        tv_alx_ad.setOnClickListener(this);
        tv_top_on_ad.setOnClickListener(this);
    }

    /**
     * 权限判断和申请
     */
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String strPermission : mPermissions) {
                if (ContextCompat.checkSelfPermission(this,
                        strPermission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, mPermissions, 6);
                }
            }
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_alx_ad:
                startActivity(new Intent(this, AlxAdDemoListActivity.class));
                break;
            case R.id.tv_top_on_ad:
                startActivity(new Intent(this, TopOnAdDemoListActivity.class));
                break;
        }
    }
}


