package org.mini.frame.kit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Wuquancheng on 2018/10/22.
 */

public class XTimer {

    static class XTimerTarget {
        Object target;
        Method method;
    }

    public List<XTimerTarget> targetList;

    Timer timer = null;
    private static XTimer xTimer = null;
    private int time = 0;

    public static XTimer instance() {
        if (xTimer != null) {
            return xTimer;
        }
        synchronized (XTimer.class) {
            xTimer = new XTimer();
            xTimer.start();
        }
        return xTimer;
    }

    private XTimer() {
        timer = new Timer();
        targetList = new ArrayList<>();
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                execute();
            }
        }, 0,1000);
    }

    public void destroy() {
        cancel();
        targetList.clear();
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void execute() {
        time++;
        for (XTimerTarget timerTarget : targetList) {
            try {
                timerTarget.method.setAccessible(true);
                timerTarget.method.invoke(timerTarget.target, time);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Method findMethod(Object target, String method) throws NoSuchMethodException {
        Class c = target.getClass();
        Method m = null;
        while (!c.equals(Object.class)) {
            Method[] methods = c.getDeclaredMethods();
            for (Method _m : methods) {
                if (_m.getName().equals(method)) {
                    m = _m;
                    break;
                }
            }
            c = c.getSuperclass();
        }
        if (m != null) {
            return m;
        }
        else {
            throw new NoSuchMethodException(method);
        }
    }

    public void addTarget(Object target, String method) throws NoSuchMethodException {
        Method m = findMethod(target,method);
        addTarget(target, m);
    }

    public void addTarget(Object target, Method method) {
        if (get(target,method) == null) {
            XTimerTarget t = new XTimerTarget();
            t.target = target;
            t.method = method;
            targetList.add(t);
        }
    }

    private XTimerTarget get(Object target, Method method) {
        for (XTimerTarget t : targetList) {
            if (t.target.equals(target) && t.method.getName().equals(method.getName())) {
                return t;
            }
        }
        return null;
    }

    public void removeTarget(Object target) {
        removeTarget(target, (Method)null);
    }

    public void removeTarget(Object target, String method) throws NoSuchMethodException{
        Method m = findMethod(target,method);
        removeTarget(target, m);
    }

    public void removeTarget(Object target, Method method) {
        List<XTimerTarget> willRemove = new ArrayList<>();
        for (XTimerTarget t : targetList) {
            if (t.target.equals(target) &&(method == null || t.method.getName().equals(method.getName()))) {
                willRemove.add(t);
            }
        }
        targetList.removeAll(willRemove);
    }
}
