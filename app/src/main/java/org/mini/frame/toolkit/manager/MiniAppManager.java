package org.mini.frame.toolkit.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Looper;

/**
 * Created by Wuquancheng on 15/5/9.
 */
public class MiniAppManager {
    private static MiniAppManager appManager = null;

    private static Context context;

    private MiniAppManager(Context context) {
        this.context = context;
    }

    public static MiniAppManager instance(Context context) {
        synchronized (MiniAppManager.class) {
            if (appManager == null) {
                appManager = new MiniAppManager(context);
            }
        }
        return appManager;
    }

    public static String getAppMetaData(String key) throws Exception {
        return getAppMetaData(key, null);
    }

    public static String getAppMetaData(String key, String removePrefix) throws Exception {
        ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        Object value = ai.metaData.get(key);
        if (value != null) {
            String v =  value.toString();
            if (removePrefix != null) {
                v = v.replace(removePrefix,"");
            }
            return v;
        }
        return null;
    }

    //得到应用的uid
    public static int getAppUid(){
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.uid;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }
}
