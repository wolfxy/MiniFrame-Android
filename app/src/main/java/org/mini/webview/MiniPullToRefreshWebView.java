package org.mini.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import org.mini.frame.view.MiniUIWebView;

public class MiniPullToRefreshWebView extends com.handmark.pulltorefresh.library.PullToRefreshWebView {

    public MiniPullToRefreshWebView(Context context) {
        super(context);
    }

    public MiniPullToRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniPullToRefreshWebView(Context context, Mode mode) {
        super(context, mode);
    }

    public MiniPullToRefreshWebView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    protected WebView createRefreshableView(Context context, AttributeSet attrs) {
        MiniUIWebView webView = new MiniUIWebView(context, attrs);
        webView.initSettingsConfig();
        return webView;
    }
}
