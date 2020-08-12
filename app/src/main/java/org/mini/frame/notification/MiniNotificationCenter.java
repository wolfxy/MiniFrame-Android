package org.mini.frame.notification;

import android.os.Handler;

import org.mini.frame.annotation.MiniUINotification;
import org.mini.frame.log.MiniLogger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/5/3.
 */
public class MiniNotificationCenter {

    private static MiniNotificationCenter notificationCenter = new MiniNotificationCenter();

    private final static String TAG = MiniNotificationCenter.class.getSimpleName();

    private List<MiniNotification> notifications = new ArrayList<MiniNotification>();

    private Map<String,List<Object>> observers = new HashMap<String, List<Object>>();
    private Map<String, String> methodMap = new HashMap();

    private Object lock = new Object();

    private Handler handler = new Handler();

    public static MiniNotificationCenter defaultNotificationCenter() {
        return notificationCenter;
    }

    private List<Object> getObservers(String key) {
        synchronized (lock) {
            List<Object> lst = observers.get(key);
            if (lst == null) {
                lst = new ArrayList<Object>();
                observers.put(key, lst);
            }
            return lst;
        }
    }

    public void register(String key, Object observer) {
        register(key, observer, null);
    }

    public void register(String key, Object observer, String method) {
        synchronized (lock) {
            List<Object> lst = getObservers(key);
            if (!lst.contains(observer)) {
                lst.add(observer);
            }

            if (method != null) {
                methodMap.put(observer.getClass().getName()+"@@"+key, method);
            }
        }
    }

    public void remove(Object object) {
        remove(object,null);
    }

    public synchronized void remove(Object object, String key) {
        synchronized (lock) {
            if (key == null) {
                List<String> emptyKeys = new ArrayList<String>();
                for (String k : observers.keySet()) {
                    List<Object> objects = observers.get(k);
                    if (objects != null && objects.size() > 0) {
                        if (objects.contains(object)) {
                            objects.remove(object);
                        }
                        if (objects.size() == 0) {
                            emptyKeys.add(k);
                        }
                    }
                }
                if (emptyKeys.size() > 0) {
                    for (String k : emptyKeys) {
                        observers.remove(k);
                    }
                }
            } else {
                List<Object> lst = getObservers(key);
                if (lst != null) {
                    lst.remove(object);
                }
                if (lst == null || lst.size() == 0) {
                    observers.remove(key);
                }
                String mapKey = object.getClass().getName() + "@@" + key;
                if (methodMap.containsKey(mapKey)) {
                    methodMap.remove(mapKey);
                }
            }
        }
    }

    public void post(MiniNotification notification) {
        new NotificationDispatcher(notification).start();
    }

    public void post(String key) {
        MiniNotification notification = new MiniNotification();
        notification.setKey(key);
        new NotificationDispatcher(notification).start();
    }

    public void post(String key, Object object) {
        MiniNotification notification = new MiniNotification();
        notification.setKey(key);
        notification.setSource(object);
        new NotificationDispatcher(notification).start();
    }

    class NotificationDispatcher extends Thread {
        MiniNotification notification;
        public NotificationDispatcher(MiniNotification notification) {
            this.notification = notification;
        }

        public void run() {
            synchronized (lock) {
                try {
                    String key = notification.getKey();
                    MiniLogger.get(TAG).d("NotificationDispatcher %s", key);
                    List<Object> objects = getObservers(key);
                    if (objects != null && objects.size() > 0) {
                        for (final Object object : objects) {
                            String methodKey = object.getClass().getName() + "@@" + key;
                            MiniLogger.get(TAG).d("NotificationDispatcher %s, methodKey", key, methodKey);
                            Method m =  null;
                            if (methodMap.containsKey(methodKey)) {
                                String methodName = methodMap.get(methodKey);
                                m = object.getClass().getDeclaredMethod(methodName, MiniNotification.class);
                            }
                            else {
                                m = getNotificationMethod(object.getClass(), key);
                            }
                            if (m != null) {
                                MiniLogger.get(TAG).d("NotificationDispatcher %s, methodKey, method %s", key, methodKey, m.getName());
                                if (!m.isAccessible()) {
                                    m.setAccessible(true);
                                }
                                final Method method = m;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Class[] parameterTypes =  method.getParameterTypes();
                                            if (parameterTypes != null && parameterTypes.length > 0) {
                                                method.invoke(object, notification);
                                            }
                                            else {
                                                method.invoke(object);
                                            }
                                        } catch (Exception e) {
                                            MiniLogger.get().e(e.getMessage(), e);
                                        }
                                    }
                                });
                            }
                            else {
                                MiniLogger.get(TAG).d("NotificationDispatcher %s, methodKey, not found any method for it.", key, methodKey);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    MiniLogger.get().e(e);
                }
            }
        }

        Method getNotificationMethod(Class clazz, String key) {
            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null) {
                for (Method m : methods) {
                    MiniUINotification noti = m.getAnnotation(MiniUINotification.class);
                    if (noti != null && noti.value().equals(key)) {
                        return m;
                    }
                }
            }
            if (!clazz.getSuperclass().equals(Object.class)) {
                return getNotificationMethod(clazz.getSuperclass(),key);
            }
            return null;
        }
    }
}
