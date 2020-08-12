package org.mini.frame.activity;

import android.os.Bundle;

import org.mini.frame.toolkit.MiniURIWrapper;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.webview.MiniUIWebViewFragment;
import org.mini.frame.view.MiniUIViewGroup;

public class MiniUIWebViewActivity extends MiniUIActivity {

    private MiniUIWebViewFragment fragment;
    private MiniUIViewGroup webViewGroup;
    private String url = null;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.url = this.getIntent().getStringExtra("url");
        MiniURIWrapper wrapper = new MiniURIWrapper(url);
        fragment = new MiniUIWebViewFragment();
        fragment.setContext(this.getContentView().getContext());
        fragment.setActivity(MiniUIWebViewActivity.this);
        fragment.setUrl(url);
        String title = wrapper.getParameter("title");
        if (title != null && title.length() > 0) {
            this.setTitle(title);
        }
        webViewGroup = fragment.getView();
        webViewGroup.setLeft(0);
        webViewGroup.setTop(0);
        webViewGroup.setRight(MiniUIScreen.width());
        webViewGroup.setBottom(MiniUIScreen.height() - this.getNaviTitleViewHeight());
        fragment.create();
        miniContentView.addView(webViewGroup);
    }

    public void onStart()
    {
        super.onStart();
        this.fragment.delayLoadContent();

    }

    public void layoutSubViews()
    {
        super.layoutSubViews();
    }

    public void invokeJs(String js) {
        fragment.invokeJs(js);
    }
}
