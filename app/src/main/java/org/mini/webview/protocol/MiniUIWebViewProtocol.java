package org.mini.webview.protocol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public interface MiniUIWebViewProtocol {

    //from web chrome client
    void onProgressChanged(WebView view, int newProgress);

    void onReceivedTitle(WebView view, String title) ;

    void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) ;

    boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) ;

    void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture);

    void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType);

    void openFileChooser(ValueCallback<Uri> uploadMsg);


    //from web view client
    boolean shouldOverrideUrlLoading(WebView view, String url) ;

    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageFinished(WebView view, String url);

    void onReceiveValue(Uri uri);

    void onActivityResult(int requestCode, int resultCode, Intent intent);

    void setHomeUrl(String url);

    void releaseResource();

    void onResume();

    void destroy();
}
