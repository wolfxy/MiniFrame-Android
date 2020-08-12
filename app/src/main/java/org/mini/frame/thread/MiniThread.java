package org.mini.frame.thread;

import android.os.Looper;

public class MiniThread {

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
