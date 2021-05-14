package com.alxad;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alxad.comm.AlxFileUtil;
import com.alxad.http.AlxHttpTools;
import com.alxad.util.MD5Util;
import com.alxad.video.AlxVideoListener;
import com.alxad.video.AlxVideoView;

public class VideoDemoActivity extends AppCompatActivity implements View.OnClickListener{
    public final String TAG="VideoDemoActivity";

    private AlxVideoView mVideoView;
    private AlxVideoView mVideoView2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);
        initView();
    }

    private void initView(){
        Button bnStart=(Button)findViewById(R.id.bn_start);
        Button bnStart2=(Button)findViewById(R.id.bn_pause);
        mVideoView=(AlxVideoView)findViewById(R.id.alx_video_view);
        mVideoView2=(AlxVideoView)findViewById(R.id.alx_video_view2);

        bnStart.setOnClickListener(this);
        bnStart2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bn_start:
                bnStart();
                break;
            case R.id.bn_pause:
                bnPause();
                break;
        }
    }

    private void bnStart(){
        String url="https://iab-publicfiles.s3.amazonaws.com/vast/VAST-4.0-Short-Intro.mp4";
        url="https://ww0.svr-algorix.com/pic/7f11795b3beea5a271296565a7e706ff.mp4";
//        url="https://ww0.svr-algorix.com/pic/40c9897cf64b31cec0b39ed23cd2302a.mp4";

//        url= AlxFileUtil.getVideoSavePath(this)+ AlxHttpTools.getFileNameByUrl(url);

        mVideoView.setUp(url, new AlxVideoListener() {
            @Override
            public void onBufferStart() {
                Log.i(TAG,"onBufferStart");
            }

            @Override
            public void onRenderingStart() {
                Log.i(TAG,"onRenderingStart");
            }

            @Override
            public void onPlayError(String error) {
                Log.i(TAG,"onPlayError");
            }

            @Override
            public void onPause() {
                Log.i(TAG,"onPause");
            }

            @Override
            public void onStart() {
                Log.i(TAG,"onStart");
            }

            @Override
            public void onCompletion() {
                Log.i(TAG,"onCompletion");
            }

            @Override
            public void onVideoSize(int width, int height) {
                Log.i(TAG,"onVideoSize-"+width+";"+height);
            }

            @Override
            public void onPlayProgress(int progress, int second, int duration) {
                Log.i("VideoDemo-Progress","onPlayProgress-"+progress+"-"+second+"-"+duration);
            }
        });
        mVideoView.start();
    }

    private void bnPause(){
        String url="https://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4";
        mVideoView2.setUp(url, new AlxVideoListener() {
            @Override
            public void onBufferStart() {
                Log.d(TAG,"onBufferStart-2");
            }

            @Override
            public void onRenderingStart() {
                Log.d(TAG,"onRenderingStart-2");
            }

            @Override
            public void onPlayError(String error) {
                Log.d(TAG,"onPlayError-2");
            }

            @Override
            public void onPause() {
                Log.d(TAG,"onPause-2");
            }

            @Override
            public void onStart() {
                Log.d(TAG,"onStart-2");
            }

            @Override
            public void onCompletion() {
                Log.d(TAG,"onCompletion-2");
            }

            @Override
            public void onVideoSize(int width, int height) {
                Log.d(TAG,"onVideoSize-2-"+width+";"+height);
            }

            @Override
            public void onPlayProgress(int progress, int second, int duration) {
                Log.d("VideoDemo-Progress-2","onPlayProgress-"+progress+"-"+second+"-"+duration);
            }
        });
        mVideoView2.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlxVideoView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        AlxVideoView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlxVideoView.onDestroy();
    }


}