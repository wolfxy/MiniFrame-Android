package org.mini.webview.protocol;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.mini.frame.log.MiniLogger;


public class MiniUIWebViewClient extends WebViewClient {
    private final static String TAG = MiniUIWebViewClient.class.getSimpleName();
    private MiniUIWebViewProtocol commWebPlug;

    public MiniUIWebViewClient(MiniUIWebViewProtocol commWebPlug) {
        this.commWebPlug = commWebPlug;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (commWebPlug != null) {
            return commWebPlug.shouldOverrideUrlLoading(view, url);
        }
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (commWebPlug != null) {
            commWebPlug.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (commWebPlug != null) {
            commWebPlug.onPageFinished(view, url);
        }
        String title = view.getTitle();
        MiniLogger.get(TAG).d("webview on pageFinished title %s", title);
        if ("about:blank".equals(title)) {
            setHtmlTitle(view, "详情");
        } else {
            setHtmlTitle(view, title);
        }
    }

    private void setHtmlTitle(WebView view, String title)
    {
        if (commWebPlug != null) {
            commWebPlug.onReceivedTitle(view, title);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

        if (error.getPrimaryError() == SslError.SSL_DATE_INVALID

                || error.getPrimaryError() == SslError.SSL_EXPIRED

                || error.getPrimaryError() == SslError.SSL_INVALID

                || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {

            handler.proceed();

        } else {

            handler.cancel();

        }

        super.onReceivedSslError(view, handler, error);

    }


}
