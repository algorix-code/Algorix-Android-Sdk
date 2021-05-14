package com.alxad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class WebViewActivity extends Activity {
    WebView mWebView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mContext = this.getApplicationContext();
//        intent:
//        //HOST/URI-path // Optional host
//        #Intent;
//            package=ru.yandex.searchplugin;
//            S.market_referrer=appmetrica_tracking_id%253D97197865817892333%2526ym_tracking_id%253D4429832783289170879;
//        end

//        Intent data = getIntent();
//        String market_referrer = data.getStringExtra("market_referrer"); // 要跳转的链接
//        Toast.makeText(this, market_referrer, Toast.LENGTH_LONG).show();
        mWebView = this.findViewById(R.id.webview);
        try {
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setUseWideViewPort(false);
            mWebView.getSettings().setLoadWithOverviewMode(false);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        String url = request.getUrl().toString();
                        if (url.contains("market://details?")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setPackage("com.android.vending");
                            intent.setData(Uri.parse(url));
                            mContext.startActivity(intent);
                            return true;
                        } else if (url.contains("https://play.google.com/store/apps/details?")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(Uri.parse(url));
                            mContext.startActivity(intent);
                            return true;
                        }
                    }

                    return super.shouldOverrideUrlLoading(view, request);

                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url.contains("market://details?")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.android.vending");
                        intent.setData(Uri.parse(url));
                        mContext.startActivity(intent);
                        return true;
                    } else if (url.contains("https://play.google.com/store/apps/details?")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse(url));
                        mContext.startActivity(intent);
                        return true;
                    }

                    return super.shouldOverrideUrlLoading(view, url);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        mWebView.loadUrl("https://static.doadx.com/test/test2.html");
    }
}
