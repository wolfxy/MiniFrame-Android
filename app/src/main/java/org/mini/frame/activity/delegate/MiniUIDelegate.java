package org.mini.frame.activity.delegate;

import android.os.Bundle;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.view.MiniUIWebView;


/**
 * Created by Wuquancheng on 2018/8/24.
 */

public interface MiniUIDelegate {

    void willCreate(MiniUIActivity activity, Bundle savedInstanceState);

    void afterCreate(MiniUIActivity activity, Bundle savedInstanceState);

    int getNaviTitleViewHeight();

    int getNaviTitleViewBackgroundColor();

    int getNaviTitleViewTitleColor();

    int getNaviTitleViewBackImageId();

    void webviewCreated(MiniUIWebView view);

    Class webviewActivityClass();

    AlertMessageDialogConfig alertMessageDialogConfig();
}
