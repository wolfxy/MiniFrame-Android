package org.mini.frame.toolkit;

import android.os.Environment;
import android.text.TextUtils;

import org.mini.frame.log.MiniLogger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Wuquancheng on 2018/1/5.
 */

public class MiniDevice {
    private final Properties properties = new Properties();
    private static MiniDevice device = null;
    private static String ROM_INFO = null;
    private MiniDevice() {
        try {
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }
        catch (Exception e) {
            MiniLogger.get().e(e);
        }
    }

    public boolean containsKey(final Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(final Object value) {
        return properties.containsValue(value);
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    public String getProperty(final String name) {
        return properties.getProperty(name);
    }

    public String getProperty(final String name, final String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public Enumeration<Object> keys() {
        return properties.keys();
    }

    public Set<Object> keySet() {
        return properties.keySet();
    }

    public int size() {
        return properties.size();
    }

    public Collection<Object> values() {
        return properties.values();
    }

    public static MiniDevice instance() {
        if (device == null) {
            device = new MiniDevice();
        }
        return device;
    }

    public boolean isMiUI() {
        String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
        boolean is =  properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;

        return is;
    }

    public boolean isHwEmui() {
        final String KEY_MIUI_VERSION_CODE = "ro.build.hw_emui_api_level";
        if (properties.containsKey(KEY_MIUI_VERSION_CODE)) {
            return true;
        }
        return false;
    }

    public boolean isFlyme() {
        try {
            /* 获取魅族系统操作版本标识*/
            String meizuFlymeOSFlag  = properties.getProperty("ro.build.display.id","");
            if (TextUtils.isEmpty(meizuFlymeOSFlag)){
                return false;
            }else if (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")){
                return  true;
            }else {
                return false;
            }
        } catch (final Exception e) {
            return false;
        }
    }

    public String romInfo()
    {
        if (ROM_INFO == null) {
            if (properties != null) {
                String name = properties.getProperty("ro.product.name","");
                String model = properties.getProperty("ro.product.model","");
                ROM_INFO = name + " " + model;
            }
        }
        return ROM_INFO;
    }


}
