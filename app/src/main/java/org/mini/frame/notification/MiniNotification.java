package org.mini.frame.notification;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/5/3.
 */
public class MiniNotification {
    public static String NOTIFICATION_INFO_KEY = "NOTIFICATION_INFO_KEY";
    private Map<String,Object> info;
    private Object source;
    private String key;

    public MiniNotification() {

    }

    public MiniNotification(String key) {
        this(key, null, null);
    }

    public MiniNotification(String key, Map<String,Object> info) {
        this(key, null, info);
    }

    public MiniNotification(String key, Object source, Map<String,Object> info) {
        this.key = key;
        this.source = source;
        this.info = info;
    }

    public void setInfo(String key, String name) {
        if (info == null) {
            info = new HashMap<>();
        }
        info.put(key, name);
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfoObject(Map<String, Object> info) {
        this.info = info;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setInfoObject(String key, Object v) {
        if (info == null) {
            info = new HashMap<String, Object>();
        }
        info.put(key,v);
    }

    public void setInfoObject(Object v) {
        setInfoObject(NOTIFICATION_INFO_KEY, v);
    }

    public Object getInfoObject(String key) {
        if (info != null) {
            return info.get(key);
        }
        return null;
    }

    public Object getInfoObject() {
        return getInfoObject(NOTIFICATION_INFO_KEY);
    }
}
