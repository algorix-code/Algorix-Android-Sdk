package com.alxad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alxad.admob.demo.AdmobDemoListActivity;
import com.alxad.alx.demo.AlxAdDemoListActivity;
import com.alxad.base.AlxAdBase;
import com.alxad.comm.AlxFileUtil;
import com.alxad.mopub.demo.MoPubDemoListActivity;
import com.alxad.topon.demo.TopOnAdDemoListActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG="MainActivity";

    //申请相关的权限可以更加精准的推送广告资源 权限组 按需添加
    String[] mPermissions = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        //IS_TEST 为true ip赋值为108.160.165.83  为false ip赋值为当前ip地址
        AlxAdBase.IS_TEST = false;
        //android 6.0及以上动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermission();
        }
        initView();
    }


    private void initView() {
        TextView tv_alx_ad = findViewById(R.id.tv_alx_ad);
        TextView tv_top_on_ad = findViewById(R.id.tv_top_on_ad);
        TextView tv_admob_ad = findViewById(R.id.tv_admob_ad);
        TextView tv_mopub_ad = findViewById(R.id.tv_mopub_ad);
        TextView tv_http_test = findViewById(R.id.tv_http_test);
        TextView tv_web_view = findViewById(R.id.tv_web_view);
        TextView tv_clear_cache = findViewById(R.id.tv_clear_cache);
        tv_alx_ad.setOnClickListener(this);
        tv_top_on_ad.setOnClickListener(this);
        tv_admob_ad.setOnClickListener(this);
        tv_http_test.setOnClickListener(this);
        tv_web_view.setOnClickListener(this);
        tv_mopub_ad.setOnClickListener(this);
        tv_clear_cache.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.tv_alx_ad:
                startActivity(new Intent(this, AlxAdDemoListActivity.class));
                break;
            case R.id.tv_top_on_ad:
                startActivity(new Intent(this, TopOnAdDemoListActivity.class));
                break;
            case R.id.tv_admob_ad:
                startActivity(new Intent(this, AdmobDemoListActivity.class));
                break;
            case R.id.tv_http_test:
                startActivity(new Intent(this, HttpDemoActivity.class));
                break;
            case R.id.tv_web_view:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case R.id.tv_mopub_ad:
                startActivity(new Intent(this, MoPubDemoListActivity.class));
                break;
            case R.id.tv_clear_cache:
                bnClearCache();
                break;
        }
    }

    private void bnClearCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //清除log缓存
                    String logDir = AlxFileUtil.getLogSavePath(mContext);
                    AlxFileUtil.clearCache(logDir, 3600);//缓存1小时

                    //清除图片缓存
                    String imageDir = AlxFileUtil.getImageSavePath(mContext);
                    AlxFileUtil.clearCache(imageDir, 0);//0秒

                    //清除视频缓存
                    String videoDir = AlxFileUtil.getVideoSavePath(mContext);
                    AlxFileUtil.clearCache(videoDir, 0);//0秒

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "清除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Log.i(TAG,e.getMessage());
                }
            }
        }).start();
    }
}


