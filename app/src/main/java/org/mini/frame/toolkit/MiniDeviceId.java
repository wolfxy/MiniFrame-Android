package org.mini.frame.toolkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.mini.frame.tools.MiniMd5;

/**
 * Created by Wuquancheng on 2018/1/5.
 */

public class MiniDeviceId {

    private static final String INVALID_DEVICE_ID = "000000000000000";

    private static final String INVALID_BLUETOOTH_ADDRESS = "02:00:00:00:00:00";

    private static final String INVALID_ANDROID_ID = "9774d56d682e549c";

    private static volatile String sDeviceDigest;

    public static String deviceID(Context context) {
        // 双重校验锁
        if (sDeviceDigest == null) {
            synchronized (MiniDeviceId.class){
                if(sDeviceDigest == null){
                    sDeviceDigest = generateDeviceID(context);
                }
            }
        }
        return sDeviceDigest;
    }

    /**
     * 生成设备ID <br/>
     * 优先根据deviceID，蓝牙地址，SERIAL，AndroidID拼接设备ID；
     * 以上唯一标识，凑够两个即可，如果凑不足，则加上UUID；
     * 拼接之后，计算其MD5, 并用base64编码。
     * @return 设备ID
     */
    private static String generateDeviceID(Context context){
        StringBuilder sb = new StringBuilder(32);
        for (int  i = 0; i < 5; i++) {
            String id = getID(context, i);
            sb.append('|');
            sb.append(id);
        }
        if(sb.length() == 0){
            throw new RuntimeException("can not get device id");
        }
        return MiniMd5.md5String(sb.toString());
    }

    private static String getID(Context context, int i) {
        switch (i) {
            case 0:
                return getDeviceId(context);
            case 1:
                return getBlueToothAddress(context);
            case 2:
                return getDeviceSerial();
            case 3:
                return getAndroidID(context);
            case 4:
                return getBuildProperty();
            default:
                return "";
        }
    }

    private static String getDeviceId(Context context) {
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
                if (!TextUtils.isEmpty(deviceId) && !INVALID_DEVICE_ID.equals(deviceId)) {
                    return deviceId;
                }
            } catch (Exception ignore) {
            }
        }
        return "";
    }

    private static String getBlueToothAddress(Context context){
        if (context != null) {
            try {
                String bluetoothAddress = Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
                if (!TextUtils.isEmpty(bluetoothAddress) && !INVALID_BLUETOOTH_ADDRESS.equals(bluetoothAddress)) {
                    return bluetoothAddress;
                }
            }catch (Exception ignore){
            }
        }
        return "";
    }

    private static String getDeviceSerial() {
        if (!TextUtils.isEmpty(Build.SERIAL) && !Build.UNKNOWN.equals(Build.SERIAL)) {
            return Build.SERIAL;
        }
        return "";
    }

    private static String getAndroidID(Context context) {
        if (context != null) {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(androidId) && !INVALID_ANDROID_ID.equals(androidId)) {
                return androidId;
            }
        }
        return "";
    }

    private static String getBuildProperty() {
        StringBuilder stringBuild =  new StringBuilder();
        stringBuild.append(Build.DEVICE).append("$")
                .append(Build.HARDWARE).append("$")
                .append(Build.DISPLAY).append("$")
                .append(Build.PRODUCT).append("$")
                .append(Build.TYPE).append("$")
                .append(Build.BOARD).append("$")
                .append(Build.MANUFACTURER).append("$")
                .append(Build.FINGERPRINT).append("$")
                .append(Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0]).append("$");
        String v = stringBuild.toString();
        return v;
    }

}
