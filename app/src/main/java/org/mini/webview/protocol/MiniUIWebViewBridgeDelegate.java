package org.mini.webview.protocol;


import org.mini.frame.toolkit.MiniURIWrapper;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.view.MiniUIWebView;

import java.util.Map;

public interface MiniUIWebViewBridgeDelegate {

    Map<String,String> headers(String url, MiniUIWebView miniUIWebView);
    void perform(MiniURIWrapper wrapper, MiniUIWebView webView, MiniUIActivity activity);
}
