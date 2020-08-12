package org.mini.frame.intent;

import org.mini.frame.activity.MiniUIActivityResultHandler;

/**
 * Created by Wuquancheng on 2018/8/18.
 */

public class MiniIntentParam {
    private Object data;
    private MiniUIActivityResultHandler resultHandler;

    public MiniIntentParam() {

    }

    public Object getData() {
        return data;
    }

    public MiniIntentParam setData(Object data) {
        this.data = data;
        return this;
    }

    public MiniUIActivityResultHandler getResultHandler() {
        return resultHandler;
    }

    public MiniIntentParam setResultHandler(MiniUIActivityResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        return this;
    }
}
