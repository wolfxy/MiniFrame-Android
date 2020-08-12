package org.mini.frame.intent;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/6/9.
 */
public class MiniIntent extends Intent {

    public static Map<String,MiniIntentParam> EXCHANGER = new HashMap<String,MiniIntentParam>();
    public static String PARAM = "org.org.mini.params";


    public MiniIntent() {
        super();
    }

    public MiniIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public void setParam(MiniIntentParam object) {
        String key = String.valueOf(System.nanoTime());
        EXCHANGER.put(key,object);
        this.putExtra(PARAM, key);
    }

    public static MiniIntentParam getObjectFromIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        String key = intent.getStringExtra(PARAM);
        if (key != null) {
            MiniIntentParam param = (EXCHANGER.get(key));
            if (param != null) {
                EXCHANGER.remove(key);
            }
            return param;
        }
        else {
            return null;
        }
    }

}
