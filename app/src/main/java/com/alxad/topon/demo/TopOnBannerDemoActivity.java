package com.alxad.topon.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.alxad.AppConfig;
import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;

public class TopOnBannerDemoActivity extends Activity {
    ATBannerView atBannerView;

    private String TAG = "TopOnBannerDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(this);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        //动态添加内容视图
        addContentView(layout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        atBannerView = new ATBannerView(this);
        atBannerView.setPlacementId(AppConfig.BANNER_AD_PID_TOPON);
        //动态添加banner视图（也可在xml布局中直接使用 通过findViewById获取实例）
        layout.addView(atBannerView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        atBannerView.setBannerAdListener(new ATBannerListener() {
            @Override
            public void onBannerLoaded() {
                Log.d(TAG, "横幅加载成功");
            }

            @Override
            public void onBannerFailed(AdError adError) {
                Log.d(TAG, "横幅加载失败:" + adError.getDesc() + "  errorCode:" + adError.getCode());
            }

            @Override
            public void onBannerClicked(ATAdInfo atAdInfo) {
                Log.d(TAG, "横幅加载成功");
            }

            @Override
            public void onBannerShow(ATAdInfo atAdInfo) {
                Log.d(TAG, "横幅展示成功");
            }

            @Override
            public void onBannerClose(ATAdInfo atAdInfo) {
                Log.d(TAG, "横幅关闭成功");
            }

            @Override
            public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
                Log.d(TAG, "横幅自动刷新成功");
            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
                Log.d(TAG, "横幅自动刷新失败");
            }
        });
        atBannerView.loadAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(atBannerView!=null){
            atBannerView.destroy();
        }
    }
}
