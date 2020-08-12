package org.mini.frame.toolkit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.ClipboardManager;

import org.mini.frame.activity.MiniUIActivity;

import java.io.File;

public class MiniSystem {

    public static void copyText(String text, Context context) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(text);
    }

    public static String getFromClip(Context context) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clip.hasText()) {
            return clip.getText().toString();
        } else {
            return null;
        }
    }

    /**
     * 打开安装apk
     *
     * @param filePath apk安装路径
     */
    public static void installApk(Context context, String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    /**
     * 某个程序是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            PackageManager info = context.getPackageManager();
            info.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static CharSequence getVersion() {
        return MiniPackageUtils.getAppVersion();
    }

    public static int getBuildCode()
    {
        return MiniPackageUtils.getAppVersionCode();
    }

    public static String getFullVersion()
    {
        return  getVersion() + "("+getBuildCode()+")";
    }

    public static void call(String mobile, Activity activity)
    {
        if (mobile == null || mobile.length() == 0) {
            return;
        }
        String phone = mobile.toLowerCase();
        if (phone.startsWith("tel:")) {
            phone = phone.substring(4);
        }
        callPhone(activity, mobile);
    }

    public static void callPhone(final Activity activity, final String mobile) {
        if (activity instanceof MiniUIActivity) {
            ((MiniUIActivity)activity).requestCallPhonePermission(
                new RequestPermissionsCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onRequestPermissionsResult(boolean granted) {
                        if (granted) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                            activity.startActivity(intent);
                        }
                    }
            });
        }
    }
}
