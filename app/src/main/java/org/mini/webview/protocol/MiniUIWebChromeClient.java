package org.mini.webview.protocol;


import android.net.Uri;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.mini.frame.log.MiniLogger;


/**
 * Created by Wuquancheng on 16/9/7.
 */
public class MiniUIWebChromeClient extends WebChromeClient {

    private final static String TAG = MiniUIWebChromeClient.class.getSimpleName();
    private MiniUIWebViewProtocol commWebPlug;

    public MiniUIWebChromeClient(MiniUIWebViewProtocol commWebPlug) {
        this.commWebPlug = commWebPlug;

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (commWebPlug != null) {
            commWebPlug.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        MiniLogger.get(TAG).d("web view title %s", title);
        if (commWebPlug != null) {
            commWebPlug.onReceivedTitle(view, title);
        }
    }


    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        if (commWebPlug != null) {
            commWebPlug.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        if (commWebPlug != null) {
            return commWebPlug.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
        else {
            return true;
        }
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        if (commWebPlug != null) {
            commWebPlug.openFileChooser(uploadFile, acceptType, capture);
        }
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (commWebPlug != null) {
            commWebPlug.openFileChooser(uploadMsg, acceptType, "");
        }
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        if (commWebPlug != null) {
            commWebPlug.openFileChooser(uploadMsg, "");
        }
    }

}

