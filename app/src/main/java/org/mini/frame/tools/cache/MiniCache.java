package org.mini.frame.tools.cache;

import android.text.TextUtils;



import org.mini.frame.toolkit.MiniJsonUtil;
import org.mini.frame.application.MiniApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Wuquancheng on 15/5/10.
 */
public class MiniCache {

    private static ACache aCache;

    public static void initCache(String name) {
        aCache = ACache.get(MiniApplication.application.getApplicationContext(), name);
    }

    public static ACache userCache() {
        return aCache;
    }

    public static void set(String key, Object object) {
        ACache cache = userCache();
        String value = MiniJsonUtil.toJsonString(object);
        cache.put(key, value);
    }

    public static void setObject(String key, Object object) {
        ACache cache = userCache();
        String value = MiniJsonUtil.toJsonString(object);
        cache.put(key, value);
    }


    public static void set(String key, String value) {
        ACache cache = userCache();
        cache.put(key, value);
    }

    public static String getString(String key) {
        ACache cache = userCache();
        return cache.getAsString(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        ACache cache = userCache();
        String value = cache.getAsString(key);
        if (value != null) {
            return MiniJsonUtil.stringToObject(value, clazz);
        } else {
            return null;
        }
    }

    public static <T> void setObject(String key, List<T> list) {
        if (!TextUtils.isEmpty(key) && list != null) {
            ACache cache = userCache();
            ArrayList<T> arrayList=new ArrayList<>();
            arrayList.addAll(list);
            cache.put(key, arrayList);
        }
        else {

        }
    }

    public static Object getObject(String key) {
        if (!TextUtils.isEmpty(key)) {
            ACache cache = userCache();
            return cache.getAsObject(key);
        }

        return null;
    }

}
