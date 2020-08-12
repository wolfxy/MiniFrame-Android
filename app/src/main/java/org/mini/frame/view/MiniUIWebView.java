package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.mini.webview.protocol.MiniUIWebViewBridgeDelegate;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;

public class MiniUIWebView extends WebView implements MiniUIViewProtocol
{

    public boolean scrollEnable = true;

    private static String userAgent = "mini-web(Android)";

    public static MiniUIWebViewBridgeDelegate bridgeDelegate;

    public static void setUserAgent(String userAgent) {
        MiniUIWebView.userAgent = userAgent;
    }

	public MiniUIWebView(Context context) {
		super(context);
	}

    public MiniUIWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initSettingsConfig()
    {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setSupportZoom(false);
        this.getSettings().setBuiltInZoomControls(false);
        this.getSettings().setDisplayZoomControls(false);

        //支持html5获取当前的位置信息
    //        WebSettings webSettings = mWebView.getSettings();
        this.getSettings().setGeolocationEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setDatabaseEnabled(true);
        this.getSettings().setPluginState(WebSettings.PluginState.ON);
        this.getSettings().setUserAgentString(userAgent);
    }

	public void removeFromSuperView() {
		if (getParent() instanceof ViewGroup) {
			invalidate();//解决移除后有残影的问题
			((ViewGroup) getParent()).removeView(this);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getParent() instanceof MiniUIViewGroup && widthMeasureSpec > 0 && heightMeasureSpec > 0)
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		else
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void setVisibility(int visibility) {
		if(visibility != View.VISIBLE){
			invalidate();
		}
		super.setVisibility(visibility);
	}

    protected int musicId = -1;
    public void setKeySound(int musicId) {
        this.musicId = musicId;

    }
    public int getKeySound() {
        return this.musicId;
    }

    public void disableKeySound() {
        this.musicId = -2;
    }

    public void scrollTo(int x, int y) {
        if (scrollEnable) {
            super.scrollTo(x, y);
        }
    }

    public void manualScrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    public void computeScroll() {
        if (scrollEnable) {
            super.computeScroll();
        }
    }

    public boolean isScrollEnable() {
        return scrollEnable;
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }

    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
    {
        if (scrollEnable) {
            return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
        }
        return false;
    }

    public void disableLongClick() {
        this.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    public int getContentHeight() {
        measure(0, 0);
        return getMeasuredHeight();
    }

    public void loadHtmlContent(String html) {
        loadDataWithBaseURL("", html, "text/html", "utf-8", "");
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.getScrollY() <= 0)
                    this.scrollTo(0, 1);
                break;
            case MotionEvent.ACTION_UP:
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void invokeJs(String js) {
        if (js.startsWith("javascript:")) {
            this.loadUrl(js);
        } else {
            String javascript = "javascript:window." + js;
            this.loadUrl(javascript);
        }
    }

    public void disableLongPress()
    {
        this.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }
}
