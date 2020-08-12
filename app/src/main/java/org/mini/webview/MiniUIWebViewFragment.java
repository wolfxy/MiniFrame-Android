package org.mini.webview;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.mini.frame.R;

import org.mini.frame.application.MiniConfiguration;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.MiniURIWrapper;
import org.mini.webview.protocol.MiniUIWebChromeClient;
import org.mini.webview.protocol.MiniUIWebViewClient;
import org.mini.frame.fragment.MiniUIFragment;
import org.mini.frame.view.MiniUIWebView;

import java.util.Map;

import static org.mini.frame.application.MiniApplication.ActionAfter;


public class MiniUIWebViewFragment extends MiniUIFragment implements MiniUIWebviewListener {
    private static final String TAG = MiniUIWebViewFragment.class.getSimpleName();
    protected MiniPullToRefreshWebView pullToRefreshWebView;
    protected MiniUIWebView webview;

    protected String url;
    protected String title;
    protected boolean overwriteTitle = true;

    protected boolean pullRefresh = true;

    protected MiniUIWebViewDelegate webviewPlugin;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onCreate()
    {
        pullToRefreshWebView = new MiniPullToRefreshWebView(this.getContext());
        pullToRefreshWebView.getLoadingLayoutProxy().setLoadingDrawable(this.getContext().getResources().getDrawable(R.mipmap.pull_refresh_icon));
        this.getContentView().addView(pullToRefreshWebView);
        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> pullToRefreshBase) {
                loadContent();
            }
        });

        webview = (MiniUIWebView)pullToRefreshWebView.getRefreshableView();
        webviewPlugin = new MiniUIWebViewDelegate(webview, this.activity, this);
        webview.setWebChromeClient(new MiniUIWebChromeClient(webviewPlugin));
        webview.setWebViewClient(new MiniUIWebViewClient(webviewPlugin));
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.disableLongPress();
        if (MiniConfiguration.getGlobalUIDelegate() != null) {
            MiniConfiguration.getGlobalUIDelegate().webviewCreated((MiniUIWebView)webview);
        }
        MiniURIWrapper wrapper = new MiniURIWrapper(url);
        String pull_to_refresh = wrapper.getParameter("pull_to_refresh");
        if ("0".equals(pull_to_refresh)) {
            pullRefresh = false;
        }
    }

    public void delayLoadContent()
    {
        if (pullRefresh) {
            ActionAfter(100, new Runnable() {
                @Override
                public void run() {
                    pullToRefreshWebView.startRefreshing();
                }
            });
        }
        else {
            ActionAfter(100, new Runnable() {
                @Override
                public void run() {
                    loadContent();
                }
            });
        }
    }

    public void loadContent()
    {
        if (!pullRefresh) {
            this.activity.showWaiting();
        }
        MiniLogger.get().d("MiniWebViewFragment load content %s", url);
        if (MiniUIWebView.bridgeDelegate != null) {
            Map headers = MiniUIWebView.bridgeDelegate.headers(this.url, this.webview);
            if (headers != null) {
                webview.loadUrl(this.url, headers);
            }
            else {
                webview.loadUrl(this.url);
            }
        }
        else {
            webview.loadUrl(this.url);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public CharSequence title() {
        return title;
    }

    @Override
    public void layoutSubViews()
    {
        int w  = this.getContentView().getWidth();
        int h =  this.getContentView().getHeight();
        MiniLogger.get(TAG).d("MiniWebViewFragment %d %d", w, h);
        pullToRefreshWebView.setTop(0);
        ViewGroup.LayoutParams layoutParams = pullToRefreshWebView.getLayoutParams();
        layoutParams.height = h;
        pullToRefreshWebView.setLayoutParams(layoutParams);
        pullToRefreshWebView.layout(0,0, w, h);
    }

    @Override
    public void onResume()
    {

    }

    protected void hideNaviButtons()
    {
        this.activity.hideNaviRightButton();
        this.activity.hideNaviLeftButton();
    }

    protected void reloadNaviButtons()
    {
        if (webviewPlugin != null)
        {
            webviewPlugin.reloadNaviButtons();
        }
    }

    public void onPageFinished(WebView view, String url)
    {
        this.activity.dismissWaiting();
        pullToRefreshWebView.stopRefreshing();
    }

    public void onReceivedTitle(WebView view, String title)
    {
        if (!overwriteTitle) {
            return;
        }
        MiniLogger.get(TAG).d("web view title %s", title);
        if (title != null && title.length() > 0) {
            this.activity.setTitle(title);
        }
    }

    public void disableScroll()
    {
        pullRefresh = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                pullToRefreshWebView.setMode(PullToRefreshBase.Mode.DISABLED);
            }
        });
    }

    public void enableScroll()
    {
        pullRefresh = true;
        handler.post(new Runnable() {
            @Override
            public void run() {
                pullToRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        });
    }

    public void invokeJs(String js) {
        this.webview.invokeJs(js);
    }
}
