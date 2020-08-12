package org.mini.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.mini.frame.activity.MiniUIImageBrowserActivity;
import org.mini.frame.application.MiniConfiguration;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.notification.MiniNotification;
import org.mini.frame.notification.MiniNotificationCenter;
import org.mini.frame.toolkit.MiniJsonUtil;
import org.mini.frame.toolkit.MiniSystem;
import org.mini.frame.toolkit.MiniURIWrapper;
import org.mini.pay.alipay.MiniAliPayment;
import org.mini.pay.wxpay.MiniWXPayment;
import org.mini.webview.protocol.MiniUIWebViewProtocol;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.activity.MiniUIWebViewActivity;
import org.mini.frame.annotation.MiniUINotification;
import org.mini.frame.intent.MiniIntent;
import org.mini.frame.view.MiniUIWebView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniUIWebViewDelegate implements MiniUIWebViewProtocol
{
    private static final String TAG = MiniUIWebViewDelegate.class.getSimpleName();
    private static String MINI_SCHEME  = "mini";
    private static String MINI_PATH_NAVI_BUTTON        = "navi_button";
    private static String MINI_PATH_SHOW_WAITING       = "show_waiting";
    private static String MINI_PATH_DISMISS_WAITING    = "dismiss_waiting";
    private static String MINI_PATH_ALERT_MESSAGE      = "alert_message";
    private static String MINI_PATH_TOAST_MESSAGE      = "toast_message";
    private static String MINI_PATH_CLOSE_MESSAGE      = "close_current_webview";
    private static String MINI_PATH_DISABLE_SCROLL     = "disable_scroll";
    private static String MINI_PATH_ENABLE_SCROLL      = "enable_scroll";
    private static String MINI_PATH_OPEN_IMAGES        = "open_images";
    private static String MINI_PATH_SET_TITLE          = "set_title";

    private static String MINI_PATH_BACK          = "back";
    private static String MINI_PATH_TEL            = "tel";

    private static String MINI_PATH_REACTIVE          = "re_active";
    private static String MINI_PATH_VERSION          = "version";
    private static String MINI_PATH_PAY_RESULT          = "pay_result";
    private static String MINI_PATH_PAY =           "pay";
    /**注册通知*/
    private static String MINI_PATH_REG_NOTI = "reg-notification";
    /**广播通知*/
    private static String MINI_PATH_POST_NOTI = "post-notification";


    public static final String CE_WEBVIEW_NOTI = "WEBVIEW_NOTI";//web 页面 通知
    private Map<String, String> notiHandleMap= new HashMap<String, String>();

    private Map naviButtonInfo;

    private MiniUIWebviewListener webviewListener;
    private MiniUIActivity activity;
    private MiniUIWebView webView;

    public MiniUIWebViewDelegate(MiniUIWebView webView, MiniUIActivity activity, MiniUIWebviewListener webviewListener)
    {
        this.activity = activity;
        this.webviewListener = webviewListener;
        this.webView = webView;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        webviewListener.onReceivedTitle(view, title);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {

    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {

    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {

    }

    private void processMiniRequest(MiniURIWrapper wrapper, WebView view) {
        String path = wrapper.getPath();
        if (MINI_PATH_NAVI_BUTTON.equals(path)) {
            String buttonInfo = wrapper.getParameter("button_info");
            HashMap buttonMap = MiniJsonUtil.stringToObject(buttonInfo, HashMap.class);
            this.setNaviButtons(buttonMap);
        }
        else if (MINI_PATH_SHOW_WAITING.equals(path)) {
            this.activity.showWaiting();
        }
        else if (MINI_PATH_DISMISS_WAITING.equals(path)) {
            this.activity.dismissWaiting();
        }
        else if (MINI_PATH_ALERT_MESSAGE.equals(path)) {
            String message = wrapper.getParameter("msg");
            this.activity.alertMessage(message);
        }
        else if (MINI_PATH_TOAST_MESSAGE.equals(path)) {
            String message = wrapper.getParameter("msg");
            this.activity.toastMessage(message);
        }
        else if (MINI_PATH_CLOSE_MESSAGE.equals(path)) {
            this.activity.back();
        }
        else if (MINI_PATH_DISABLE_SCROLL.equals(path)) {
            this.webviewListener.disableScroll();
        }
        else if (MINI_PATH_ENABLE_SCROLL.equals(path)) {
            this.webviewListener.enableScroll();
        }
        else if (MINI_PATH_REG_NOTI.equals(path)) {
            String name = wrapper.getParameter("name");
            String js = wrapper.getParameter("callback");
            if (name != null && js != null) {
                this.notiHandleMap.put(name, js);
                MiniNotificationCenter.defaultNotificationCenter().register(name, this, "onReceivedSendSuccessNotification");
            }
        }
        else if (MINI_PATH_POST_NOTI.equals(path)) {
            String name = wrapper.getParameter("name");
            if (name != null) {
                MiniNotification notification = new MiniNotification();
                notification.setKey(name);
                notification.setInfo("name",name);
                MiniNotificationCenter.defaultNotificationCenter().post(notification);
            }
        }
        else if (MINI_PATH_SET_TITLE.equals(path)) {
            String title = wrapper.getParameter("title");
            this.activity.setNaviTitle(title);
        }
        else if (MINI_PATH_OPEN_IMAGES.equals(path)) {
            String images = wrapper.getParameter("images");
            if (images != null) {
                Map map = MiniJsonUtil.stringToObject(images, HashMap.class);
                List images_url = (List)map.get("images");
                if (images_url != null && images_url.size() > 0) {
                    Object object = (map.get("defIndex"));
                    Integer defIndex = 0;
                    if (object != null) {
                        if (object instanceof String) {
                            defIndex = Integer.parseInt(object.toString());
                        }
                        else if (object instanceof Double){
                            defIndex = ((Double)object).intValue();
                        }
                    }
                    ArrayList urls = new ArrayList(images_url.size());
                    for(Object string : images_url) {
                        urls.add(string);
                    }
                    if (MiniUIWebViewDelegate.this.activity != null) {
                        MiniUIImageBrowserActivity.startImageBrowser(MiniUIWebViewDelegate.this.activity, urls, defIndex);
                    }
                }
            }
        }
        else if (MINI_PATH_BACK.equals(path)) {
            this.activity.back();
        }
        else if (MINI_PATH_TEL.equals(path)) {
            String phone = wrapper.getParameter("num");
            MiniSystem.call(phone, this.activity);
        }
        else if (MINI_PATH_REACTIVE.equals(path)) {

        }
        else if (MINI_PATH_VERSION.equals(path)) {
            String callback = wrapper.getParameter("callback");
            String version = MiniSystem.getVersion().toString();
            int code = MiniSystem.getBuildCode();
            webView.invokeJs(String.format("%s('%s', '%d')", callback, version, code));
        }
        else if (MINI_PATH_PAY_RESULT.equals(path)) {

        }
        else if (MINI_PATH_PAY.equals(path)) {
            String data = wrapper.getParameter("data");
            String method = wrapper.getParameter("method");
            final String callback = wrapper.getParameter("callback");
            if ("ali_pay".equals(method)) {
                MiniAliPayment.shard().pay(this.activity, data, new MiniAliPayment.MiniAliPaymentListener() {
                    @Override
                    public void onResult(boolean success, String desc) {
                        webView.invokeJs(String.format("%s(%s, '%s')", callback, success?"true":"false",desc));
                    }
                });
            }
            else if("wx_pay".equals(method)) {
                MiniWXPayment.shard().pay(this.activity, data, new MiniWXPayment.MiniWXPaymentListener() {
                    @Override
                    public void onResult(boolean success, String desc) {
                        webView.invokeJs(String.format("%s(%s, '%s')", callback, success?"true":"false",desc));
                    }
                });
            }
        }
        else {
            if (MiniUIWebView.bridgeDelegate != null) {
                MiniUIWebView.bridgeDelegate.perform(wrapper, webView, MiniUIWebViewDelegate.this.activity);
            }
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        MiniURIWrapper wrapper = new MiniURIWrapper(url);
        String scheme = wrapper.getScheme();
        MiniLogger.get(TAG).d("%s", url);
        if (MINI_SCHEME.equals(scheme)) {
            this.processMiniRequest(wrapper, webView);
            return true;
        }
        else {
            String target = wrapper.getParameter("target");
            if ("_blank".equals(target)) {
                url = url.replace("_blank", "blank");
                MiniIntent miniIntent = new MiniIntent(MiniUIWebViewDelegate.this.activity, webviewActivityClass());
                miniIntent.putExtra("url", url);
                MiniUIWebViewDelegate.this.activity.startActivity(miniIntent);
                MiniLogger.get().d("%s - %s - _blank return true;", TAG, url);
                return true;
            }
        }

        MiniLogger.get().d("%s - %s - _blank return false;", TAG, url);
        return false;
    }

    private Class webviewActivityClass()
    {
        if (MiniConfiguration.getGlobalUIDelegate() != null) {
            Class clazz = MiniConfiguration.getGlobalUIDelegate().webviewActivityClass();
            if (clazz != null && MiniUIWebViewActivity.class.isAssignableFrom(clazz)) {
                return clazz;
            }
        }
        return MiniUIWebViewActivity.class;
    }

    public void reloadNaviButtons()
    {
        if (naviButtonInfo != null) {

        }
    }

    public void setNaviButtons(Map buttonInfo)
    {
        this.naviButtonInfo = buttonInfo;

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {

        this.webviewListener.onPageFinished(view, url);
    }

    @Override
    public void onReceiveValue(Uri uri) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }

    @MiniUINotification(CE_WEBVIEW_NOTI)
    public void onReceivedSendSuccessNotification(MiniNotification notification) {
        Object name = notification.getKey();
        if (name != null) {
            String js = this.notiHandleMap.get(name);
            if (js != null) {
                this.invokeJs(js);
            }
        }
    }

    public void invokeJs(String js) {
        if (this.webView != null) {
            this.webView.invokeJs(js);
        }
    }

    @Override
    public void setHomeUrl(String url) {

    }

    @Override
    public void releaseResource() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void destroy() {

    }

}
