package org.mini.frame.application;

import org.mini.frame.activity.delegate.MiniUIDelegate;

public class MiniConfiguration
{
    private static MiniUIDelegate globalUIDelegate;

    public static void setGlobalUIDelegate(MiniUIDelegate delegate) {
        if (globalUIDelegate == null) {
            globalUIDelegate = delegate;
        }
    }

    public static MiniUIDelegate getGlobalUIDelegate() {
        return globalUIDelegate;
    }
}
