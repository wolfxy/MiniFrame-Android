package org.mini.frame.activity;

import android.content.Intent;

/**
 * Created by Wuquancheng on 2018/8/18.
 */

public interface MiniUIActivityResultHandler {
    void handleResult(int resultCode, Object data, Intent intent);
}
