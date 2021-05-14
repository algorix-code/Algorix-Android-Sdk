package com.alxad;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 异常捕获工具
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    private static class SingleHolder {
        private static CrashHandler instance = new CrashHandler();
    }

    public static CrashHandler getDefault() {
        return SingleHolder.instance;
    }

    private CrashHandler() {

    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable ex) {
        ex.printStackTrace();
        PrintWriter pw = null;
        try {
            String packageName = "";
            String versionName = "";
            int versionCode = 0;
            String dirPath = null;
            if (mContext != null) {
                packageName = mContext.getPackageName();
                dirPath = getCrashSavePath(mContext);
                try {
                    PackageInfo pi = mContext.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                    versionName = pi.versionName;
                    versionCode = pi.versionCode;
                } catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    nameNotFoundException.printStackTrace();
                }
            }

            StringBuilder sb = new StringBuilder();

            //当前版本号
            sb.append("App Version:" + versionName + "_" + versionCode);
            sb.append("\r\n");
            //当前系统
            sb.append("OS version:" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
            sb.append("\r\n");
            //制造商
            sb.append("Vendor:" + Build.MANUFACTURER);
            sb.append("\r\n");
            //手机型号
            sb.append("Model:" + Build.MODEL);
            sb.append("\r\n");
            //手机厂商信息
            sb.append("Brand:" + Build.BRAND);
            sb.append("\r\n");
            //CPU架构
            sb.append("CPU ABI:" + Build.CPU_ABI);
            sb.append("\r\n");

            sb.append("Process: ");
            sb.append(packageName);
            sb.append(", PID: ");
            sb.append(android.os.Process.myPid());
            sb.append("\r\n");

//            sb.append(ex.getClass().getName());
//            sb.append(":");
//            sb.append(ex.getMessage());
//
//            sb.append("\r\n");
//            StackTraceElement[] elements=ex.getStackTrace();
//            for(StackTraceElement item:elements){
//                sb.append(" at ");
//                sb.append(item.getClassName());
//                sb.append(".");
//                sb.append(item.getMethodName());
//                sb.append("(");
//                sb.append(item.getFileName());
//                sb.append(":");
//                sb.append(item.getLineNumber());
//                sb.append(")");
//                sb.append("\r\n");
//            }


            if (dirPath != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
                String time = sdf.format(new Date(System.currentTimeMillis()));
                String fileName = "crash-" + time + ".txt";

                File fileDir = new File(dirPath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                File file = new File(fileDir.getPath(), fileName);

                pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                pw.println(sb.toString());

                ex.printStackTrace(pw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }

        if (mDefaultHandler != null) {
            //系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(t, ex);
        }
    }

    public static String getNewSDPath(Context context) {
        String strPath = context.getExternalFilesDir(null).getPath();
        if (TextUtils.isEmpty(strPath)) {
            strPath = getSDPath();
        }
        return strPath;
    }

    private static String getSDPath() {
        String strPath = "";
        try {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                File sdDir = Environment.getExternalStorageDirectory();
                strPath = sdDir.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strPath;
    }

    public static String getCrashSavePath(Context context) {
        String dir = getNewSDPath(context) + "/alx/" + "crash/";
        return dir;
    }


}