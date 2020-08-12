package org.mini.frame.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.mini.frame.toolkit.MiniActivityManager;
import org.mini.frame.toolkit.manager.MiniAppManager;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.tools.DateTimeKit;
import org.mini.frame.tools.SDCardUtils;
import org.mini.frame.uitools.MiniUITools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Wuquancheng on 15/5/16.
 */
public class MiniApplication extends Application {

    protected MiniActivityManager.APP_STATUS appStatus;

    public static Application application;

    public static boolean IS_PAD = false;

    private String mode = null;

    private String channel = null;

    private String appId = null;

    private static Handler handler = new Handler();

    public static Handler handler() {
        return handler;
    }

    public void onCreate() {
        super.onCreate();
        application = this;
        onInit();
        IS_PAD = isPad();
    }

    private void onInit() {
        MiniUITools.initFrame(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(application));
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
    }

    public static Context Context() {
        return application.getApplicationContext();
    }

    public void onTerminate() {
        super.onTerminate();
    }

    public void onEnterForeground() {

    }

    public void onEnterBackground() {

    }

    public String getBuild() {
        String build = "";
        try {
            build = MiniAppManager.getAppMetaData("app_build");
            if (build != null && build.length() > 0) {
                build = "(" + build.replace("app_build:", "Build:") + ")";
            } else {
                build = "";
            }
        } catch (Exception e) {
        }
        return build;
    }

    public String getAppMode() {
        if (mode == null) {
            try {
                mode = MiniAppManager.getAppMetaData("app_mode");
                if (mode != null && mode.length() > 0) {
                    mode = mode.replace("app_mode:", "");
                }
            } catch (Exception e) {
            }
        }
        return mode;
    }

    public String getAppId() {
        if (appId == null) {
            try {
                appId = MiniAppManager.getAppMetaData("appId");
                if (appId != null && appId.length() > 0) {
                    appId = appId.replace("appId:", "");
                    if (TextUtils.isEmpty(appId))
                        appId = "20141001";

                } else {
                    appId = "20141001";
                }
            } catch (Exception e) {
            }
        }
        return appId;
    }


    public String getAppChannel() {
        if (channel == null) {
            try {
                channel = MiniAppManager.getAppMetaData("app_channel");
                if (channel != null && channel.length() > 0) {
                    channel = channel.replace("app_channel:", "");
                }
            } catch (Exception e) {
            }
        }
        return channel;
    }

    public void onResume() {
        if (appStatus == null || !MiniActivityManager.APP_STATUS.APP_STATUS_FOREGROUND.equals(appStatus)) {
            onEnterForeground();
        }
        appStatus = MiniActivityManager.appStatus(this.getApplicationContext());
    }

    public void onStop() {
        appStatus = MiniActivityManager.appStatus(this.getApplicationContext());
        if (MiniActivityManager.APP_STATUS.APP_STATUS_BACKGROUND.equals(appStatus)) {
            onEnterBackground();
        }
    }

    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            MiniLogger.get().d("MApplication","发生崩溃异常,重启应用");
            MiniLogger.get().e("MApplication",ex.toString());
            try {
                File file = new File(SDCardUtils.getSDPath() + "/log.txt");
                if (!file.exists()) {
                    boolean su = file.createNewFile();
                    Log.d("log", "onCreate: "+su);
                }
                BufferedSink bufferedsink = Okio.buffer(Okio.appendingSink(file));
                bufferedsink.writeUtf8(DateTimeKit.date2Str(new Date())+":\t"+ ex.getMessage());
                bufferedsink.flush();
                bufferedsink.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            handleUncaughtException(ex);
        }
    };

    protected void handleUncaughtException(Throwable ex) {

    }

    public void restartApp(Class clazz){
        Intent intent = new Intent(this.getApplicationContext(),clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getApplicationContext().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    public static void ActionAfter(long timeInMillisecond, Runnable r) {
        handler.postDelayed(r, timeInMillisecond);
    }

    public static boolean isPad() {
        Context context = application.getBaseContext();
        return (
                context.getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK)>=Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
