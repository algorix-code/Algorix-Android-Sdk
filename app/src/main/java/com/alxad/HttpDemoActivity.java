package com.alxad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alxad.comm.AlxFileUtil;
import com.alxad.comm.AlxLog;
import com.alxad.http.AlxDownLoadCallback;
import com.alxad.http.AlxDownloadManager;
import com.alxad.http.AlxHttpCallback;
import com.alxad.http.AlxHttpManager;
import com.alxad.http.AlxHttpMethod;
import com.alxad.http.AlxHttpRequest;
import com.alxad.http.AlxHttpResponse;
import com.alxad.http.AlxHttpTools;
import com.alxad.http.AlxImageManager;
import com.alxad.report.AlxDataReportNew;
import com.alxad.util.AlxFileDownloadUtil;

import org.jetbrains.annotations.NotNull;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpDemoActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG="HttpDemoActivity";

    private Button mBnDemo;
    private Button mBnDemo2;
    private TextView mTvText;
    private ImageView mIvPic;
    private ImageView mIvPic2;
    private ImageView mIvPic3;
    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mContext=this;
        initView();

        x.Ext.init(this.getApplication());
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug
    }

    private void initView(){
        mBnDemo=(Button)findViewById(R.id.bn_demo);
        mBnDemo2=(Button)findViewById(R.id.bn_demo2);
        mTvText=(TextView)findViewById(R.id.tv_text);
        mIvPic=(ImageView)findViewById(R.id.ivPic);
        mIvPic2=(ImageView)findViewById(R.id.ivPic2);
        mIvPic3=(ImageView)findViewById(R.id.ivPic3);

        mBnDemo.setOnClickListener(this);
        mBnDemo2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bn_demo:
                bnImage();
//                requestUrl();
                downloadFile();
                break;
            case R.id.bn_demo2:
//                requestUrlSync();
//                downloadFileSync();
//                reportData();
//                aa();
                bnVideoPlayer();
                break;
        }
    }

    private void bnVideoPlayer(){
        Intent intent=new Intent(this, VideoDemoActivity.class);
        startActivity(intent);
    }

    private void reportData(){
        AlxDataReportNew.getInstance().reportAdvert(AlxDataReportNew.AdvertType.BANNER,"","353453");
    }

    /**
     * 异步请求
     */
    private void requestUrl(){
        String url="http://pv.sohu.com/cityjson";
        url="https://xyz.svr-algorix.com/rtb/sa?sid=60023&token=7b9f41d06393fcdae195e0fbaee34923Y5";
//        url="http://192.168.163.222:8080/alx/sdktest.php";

        String params="{\"id\":\"null|1616411345618\",\"app\":{\"app_id\":\"246a96361196f62202e6033f2e44720eY4\",\"app_name\":\"com.bestai.TextScanner\",\"app_bundle_id\":\"com.bestai.TextScanner\",\"store_url\":\"https://play.google.com/store/apps/details?id=com.bestai.TextScanner\",\"app_category\":\"IAB1\"},\"adslot\":{\"adslot_id\":\"171049\",\"video\":{\"mimes\":[\"video/mp4\"],\"minduration\":0,\"maxduration\":30,\"protocols\":[2,3,5,6,7,8],\"w\":320,\"h\":480,\"startdelay\":0,\"placement\":5,\"linearity\":1,\"skip\":1,\"skipmin\":15,\"skipafter\":15,\"boxingallowed\":0,\"playbackmethod\":[1],\"playbackend\":1,\"pos\":7,\"ext\":{\"rewarded\":false}},\"instl\":1},\"user\":{\"user_id\":\"null\",\"ip\":\"null\",\"gender\":0},\"device\":{\"user_agent\":\"Dalvik/2.1.0 (Linux; U; Android 6.0; LG-H440n Build/MRA58K)\",\"device_id\":[{\"type\":1,\"id\":\"000000000000000\"},{\"type\":2,\"id\":\"A4:4B:D5:91:AD:95\"}],\"advertising_id\":{\"type\":4,\"id\":\"null\"},\"geo\":{\"lat\":\"\",\"lon\":\"\"},\"device_type\":1,\"os\":2,\"os_version\":\"30\",\"brand\":\"Redmi\",\"model\":\"Redmi K30 5G\",\"language\":\"CN\",\"screen_width\":1080,\"screen_height\":2261,\"screen_density\":440,\"carrier_id\":\"null\",\"wireless_network_type\":0},\"regs\":{\"coppa\":0,\"gdpr\":0}}";
        AlxHttpRequest request=new AlxHttpRequest.Builder(url)
                .setRequestCode(100)
                .setParams(params)
                .setRequestMethod(AlxHttpMethod.POST)
                .setContentType(true)
                .builder();
        AlxHttpManager.getInstance().requestApi(request, new AlxHttpCallback() {
            @Override
            public void onHttpSuccess(int requestCode,String result) {
                Log.i("http","onHttpSuccess:"+result);
                mTvText.setText(result);
            }

            @Override
            public void onHttpError(int requestCode,int code,String error) {
                Log.i("http","onHttpError:"+requestCode+":"+code+"--"+error);
                StringBuilder sb=new StringBuilder();
                sb.append(code);
                sb.append("\r\n");
                sb.append(error);
                mTvText.setText(sb.toString());

            }
        });
    }

    private void aa(){
        Thread thread1=new Thread(new MyThread());
        Thread thread2=new Thread(new MyThread());
        Thread thread3=new Thread(new MyThread());
        thread1.start();
        thread2.start();
        thread3.start();
    }

    private class MyThread implements Runnable{
        @Override
        public void run() {
            String url="http://mms.businesswire.com/media/20150623005446/en/473787/21/iab_tech_lab.jpg";
            url="https://iab-publicfiles.s3.amazonaws.com/vast/VAST-4.0-Short-Intro.mp4";
            final AlxHttpResponse response=AlxDownloadManager.getInstance().downloadFileSync(url, AlxFileUtil.getImageSavePath(HttpDemoActivity.this));
            if(response!=null){
                if(response.isOk()){
                    Log.i("http","onHttpSuccess:"+response.responseMsg);
                }else{
                    Log.i("http","onHttpError:"+response.responseMsg);
                }
            }
        }
    }

    /**
     * 同步请求
     */
    private void requestUrlSync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://pv.sohu.com/cityjson";
                url="http://mms.businesswire.com/media/20150623005446/en/473787/21/iab_tech_lab.jpg";
                url="http://192.168.163.222:8080/alx/sdktest.php";
                url="https://xyz.svr-algorix.com/rtb/sa?sid=60023&token=7b9f41d06393fcdae195e0fbaee34923Y5";
                String params="{\"id\":\"null|1616411345618\",\"app\":{\"app_id\":\"246a96361196f62202e6033f2e44720eY4\",\"app_name\":\"com.bestai.TextScanner\",\"app_bundle_id\":\"com.bestai.TextScanner\",\"store_url\":\"https://play.google.com/store/apps/details?id=com.bestai.TextScanner\",\"app_category\":\"IAB1\"},\"adslot\":{\"adslot_id\":\"171049\",\"video\":{\"mimes\":[\"video/mp4\"],\"minduration\":0,\"maxduration\":30,\"protocols\":[2,3,5,6,7,8],\"w\":320,\"h\":480,\"startdelay\":0,\"placement\":5,\"linearity\":1,\"skip\":1,\"skipmin\":15,\"skipafter\":15,\"boxingallowed\":0,\"playbackmethod\":[1],\"playbackend\":1,\"pos\":7,\"ext\":{\"rewarded\":false}},\"instl\":1},\"user\":{\"user_id\":\"null\",\"ip\":\"null\",\"gender\":0},\"device\":{\"user_agent\":\"Dalvik/2.1.0 (Linux; U; Android 6.0; LG-H440n Build/MRA58K)\",\"device_id\":[{\"type\":1,\"id\":\"000000000000000\"},{\"type\":2,\"id\":\"A4:4B:D5:91:AD:95\"}],\"advertising_id\":{\"type\":4,\"id\":\"null\"},\"geo\":{\"lat\":\"\",\"lon\":\"\"},\"device_type\":1,\"os\":2,\"os_version\":\"30\",\"brand\":\"Redmi\",\"model\":\"Redmi K30 5G\",\"language\":\"CN\",\"screen_width\":1080,\"screen_height\":2261,\"screen_density\":440,\"carrier_id\":\"null\",\"wireless_network_type\":0},\"regs\":{\"coppa\":0,\"gdpr\":0}}";
                AlxHttpRequest request=new AlxHttpRequest.Builder(url)
//                        .setParams(params)
                        .setRequestMethod(AlxHttpMethod.GET)
                        .setContentType(false)
                        .builder();
                final AlxHttpResponse response=AlxHttpManager.getInstance().requestApiSync(request);
                if(response!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String data;
                            if(response.isOk()){
                                data=response.responseMsg;
                                Log.i("http","onHttpSuccess:"+response.responseMsg);
                            }else{
                                Log.i("http","onHttpError:"+response.responseMsg);
                                data=response.responseCode+"\r\n";
                                data+=response.responseMsg;
                            }
                            mTvText.setText(data);
                        }
                    });
                }

            }
        }).start();
    }

    private void downloadFileSync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://mms.businesswire.com/media/20150623005446/en/473787/21/iab_tech_lab.jpg";
//                url="https://iab-publicfiles.s3.amazonaws.com/vast/VAST-4.0-Short-Intro.mp4";
                url="http://map-mobile-lbsapp.cdn.bcebos.com/map/BaiduMaps_Android_15-5-0_1009176a.apk";
                final AlxHttpResponse response=AlxDownloadManager.getInstance().downloadFileSync(url, AlxFileUtil.getImageSavePath(HttpDemoActivity.this));
                if(response!=null){
                    if(response.isOk()){
                        Log.i("http","onHttpSuccess:"+response.responseMsg);
                    }else{
                        Log.i("http","onHttpError:"+response.responseCode+"--"+response.responseMsg);
                    }
                }
            }
        }).start();
    }

    private void downloadFile(){
        String url="http://mms.businesswire.com/media/20150623005446/en/473787/21/iab_tech_lab.jpg";
//        url="https://iab-publicfiles.s3.amazonaws.com/vast/VAST-4.0-Short-Intro.mp4";
        url="http://map-mobile-lbsapp.cdn.bcebos.com/map/BaiduMaps_Android_15-5-0_1009176a.apk";
        url="https://ww0.svr-algorix.com/pic/40c9897cf64b31cec0b39ed23cd2302a.mp4";

        long startTime=System.currentTimeMillis();
        AlxLog.i(TAG,"http-start:"+startTime);
        AlxDownloadManager.getInstance().downloadFile(url, AlxFileUtil.getVideoSavePath(mContext),new AlxDownLoadCallback(){

            @Override
            public void onSuccess(File file) {
                long endTime=System.currentTimeMillis();
                Log.i(TAG,"http-onSuccess:"+file.getAbsolutePath());
                Log.i(TAG,"http-onSuccess-end:"+endTime+";"+(endTime-startTime));
            }

            @Override
            public void onError(int code,String error) {
                long endTime=System.currentTimeMillis();
                Log.i(TAG,"http-onError:"+code+"--"+error);
                Log.i(TAG,"http-onError:"+endTime+";"+(endTime-startTime));
            }

            @Override
            public void onProgress(int progress) {
                Log.i("http","onProgress:"+progress);
            }
        });

//        okhttpRequest();
//        multiThreadDownload(url);
    }

    private void bnImage(){
        String url="http://mms.businesswire.com/media/20150623005446/en/473787/21/iab_tech_lab.jpg";

        String url2="http://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";


        AlxImageManager.getInstance().load(url,mIvPic,R.drawable.alx_icon);

        AlxImageManager.getInstance().load(url,mIvPic2);

        AlxImageManager.getInstance().load(url2,mIvPic3,R.drawable.alx_icon);


//        Glide.with(this).load(url).into(mIvPic2);

//        Glide.with(this).load(url).into(mIvPic3);
//
//        x.image().bind(mIvPic3,url);

//        x.image().bind(mIvPic3,url);
//
//        threadDown(url);
    }


    private void threadDown(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                downImage(url);
            }
        }).start();
    }

    private void downImage(String url){
//        url="http://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";
        if(url==null){
            return;
        }
        HttpURLConnection connection=null;
        InputStream is=null;
        try {
            URL urls=new URL(url);

            connection=(HttpURLConnection)urls.openConnection();
            connection.connect();


            StringBuilder logBuilder=new StringBuilder();
            logBuilder.append("protocol="+connection.getURL().getProtocol());
            logBuilder.append("\r\n");
            logBuilder.append("responseCode="+connection.getResponseCode());
            logBuilder.append("\r\n");
            logBuilder.append("contentType="+connection.getContentType());
            logBuilder.append("\r\n");
            logBuilder.append("follow="+connection.getInstanceFollowRedirects());
            logBuilder.append("\r\n");
            logBuilder.append("realLocal="+connection.getHeaderField("Location"));
            Log.i("lwl",logBuilder.toString());




            is=connection.getInputStream();
            StringBuilder sb=new StringBuilder();
            byte[] buffer=new byte[1024];
            int length=0;
            while((length=is.read(buffer))!=-1){
                sb.append(new String(buffer,0,length));
            }
            Log.i("lwl","img-size:"+sb.length()+"="+connection.getContentLength());

        } catch (MalformedURLException e) {
            Log.i("lwl","error-1:"+e.getMessage());
            e.printStackTrace();
        }catch (Exception e){
            Log.i("lwl","error-2:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                if(is!=null){
                    is.close();
                }
            }catch (Exception e){

            }

            if(connection!=null){
                connection.disconnect();
            }
        }

    }

    private void okhttpRequest(){
        final String url="https://ww0.svr-algorix.com/pic/40c9897cf64b31cec0b39ed23cd2302a.mp4";
//        final String url="http://map-mobile-lbsapp.cdn.bcebos.com/map/BaiduMaps_Android_15-5-0_1009176a.apk";
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        final long startTime=System.currentTimeMillis();
        AlxLog.i(TAG,"okhttp:start:"+startTime);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AlxLog.i(TAG,"okhttp:onFailure:"+e.getMessage()+"--"+System.currentTimeMillis());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                AlxLog.i(TAG,"okhttp:onResponse-end:"+System.currentTimeMillis());


                try{
                    String fileName=AlxHttpTools.getFileNameByUrl(url);
                    String dirFileName=AlxFileUtil.getVideoSavePath(HttpDemoActivity.this);
                    File file=new File(dirFileName,fileName);

                    OutputStream output = null;

                    File dir = new File(dirFileName);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (file.exists()) {
                        if (file.isFile()) {
                            file.delete();
                        }
                    }
                    new File(dirFileName).mkdir();//
                    file.createNewFile();//

                    InputStream input = response.body().byteStream();

//                    long splitNum=10*1024*1024;//10MB
//                    long contentLength=response.body().contentLength();
//
//
//                    long halfNum=-1;
//                    if(contentLength>splitNum){
//                        halfNum=contentLength>>1;
//                    }



                    output = new FileOutputStream(file);
                    byte[] buffer = new byte[8 * 1024];
                    int iLen = -1;
                    long total=0;
                    while ((iLen = input.read(buffer)) != -1) {
                        total+=iLen;
//                        AlxLog.i(TAG,"okhttp:onResponse-progress:"+total);
                        output.write(buffer, 0, iLen);
                    }
                    output.flush();

                }catch (Exception e){
                    e.printStackTrace();
                    AlxLog.i(TAG,"okhttp:onResponse-error:"+e.getMessage());
                }

                long endTime=System.currentTimeMillis();
                AlxLog.i(TAG,"okhttp:onResponse-end:"+endTime+"--"+(endTime-startTime));
            }
        });

//        try {
//            Response response=client.newCall(request).execute();
//            response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 同一个文件用多线程下载
     * @param url
     */
    private void multiThreadDownload(final String url){
//        final String url="https://ww0.svr-algorix.com/pic/40c9897cf64b31cec0b39ed23cd2302a.mp4";
//        final String url="http://map-mobile-lbsapp.cdn.bcebos.com/map/BaiduMaps_Android_15-5-0_1009176a.apk";
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime=System.currentTimeMillis();
                AlxLog.i(TAG,"down-start:"+startTime);

                String fileName="down_"+AlxHttpTools.getFileNameByUrl(url);
                AlxFileDownloadUtil down=new AlxFileDownloadUtil(url,AlxFileUtil.getVideoSavePath(mContext),fileName);
                boolean result=down.startTask();

                long endTime=System.currentTimeMillis();

                AlxLog.i(TAG,"down-end:"+result+";;"+endTime+"--"+(endTime-startTime));
            }
        }).start();
    }

    private void xutilHttp() {
        RequestParams params = new RequestParams("https://www.baidu.com/");
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        try {
            String result=x.http().getSync(params,String.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }



}
