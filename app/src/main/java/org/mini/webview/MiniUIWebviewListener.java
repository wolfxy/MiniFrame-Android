package org.mini.webview;

import android.webkit.WebView;

public interface MiniUIWebviewListener {

    void onPageFinished(WebView view, String url);

    void onReceivedTitle(WebView view, String title);

    void disableScroll();

    void enableScroll();

}
