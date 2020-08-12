package org.mini.frame.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mini.frame.R;

import org.mini.frame.activity.delegate.AlertMessageDialogConfig;
import org.mini.frame.annotation.MiniUIClickAction;
import org.mini.frame.application.MiniConfiguration;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.notification.MiniNotificationCenter;
import org.mini.frame.thread.MiniThread;
import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.toolkit.RequestPermissionsCallback;
import org.mini.frame.activity.delegate.MiniUIDelegate;
import org.mini.frame.annotation.MiniUIAction;
import org.mini.frame.intent.MiniIntent;
import org.mini.frame.intent.MiniIntentParam;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.view.MiniUINaviTitleView;
import org.mini.frame.view.MiniUIPopMenuView;
import org.mini.frame.view.MiniUIViewGroup;
import org.mini.frame.view.MiniUILoadingView;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_SP;

/**
 * Created by YXL on 2015/12/8.
 */
public class MiniUIActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = MiniUIActivity.class.getSimpleName();
	public  InputMethodManager  manager;
	public static MiniUIActivity topActivity;
	public ViewGroup miniContentView;
	private MiniUINaviTitleView naviTitleView;
	private ViewGroup miniContainerView;
	private boolean destroyed;
	private LinkedList<OnKeyListener> keyListeners = new LinkedList<>();
    public static SoundPool soundPool = null;
    protected static int defaultKeySound = -1;

    protected Configuration configuration;
    protected int loadingImageResId;

    private MiniUIPopMenuView leftPopView;
    private MiniUIPopMenuView rightPopView;

    protected MiniIntentParam intentParam;

    protected MiniUIActivityResultHandler resultHandler1001 = null;

    private MiniUILoadingView waitingView;

    protected PullToRefreshScrollView scrollContainerView;

    protected MiniUIViewGroup scrollContentView;

    private MiniUIDelegate activityDelegate;

    protected Handler handler = new Handler();

    private int REQUEST_PERMISSIONS_CODE = 10;

    private Map<Integer, Method> viewActionMethodMap = new HashMap<>(0);

    private Map<String, RequestPermissionsCallback> permissionsCallbackMap = new HashMap<>();

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		MiniLogger.get(TAG).w("finalize", this.toString());
	}

	protected void setDefaultKeySound(int resId) {
        initSoundPool();
	    if (defaultKeySound == -1) {
            defaultKeySound = loadSoundWithResId(resId);
        }
    }

    public ViewGroup getContentView() {
	    return miniContentView;
    }

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

	    MiniUIDelegate delegate = this.getActivityDelegate();
	    if (delegate != null) {
	        delegate.willCreate(this, savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.setContentView(R.layout.container);
		topActivity = this;
        miniContainerView = findViewById(R.id.container_view);

        naviTitleView = findViewById(R.id.naviTitleView);
        int statusBarHeight = getStatusBarHeight();
        naviTitleView.setStatusBarHeight(getStatusBarHeight());
        naviTitleView.setBackgroundColor(getNaviTitleViewBackgroundColor());
        int height = getNaviTitleViewHeight();
        MiniLogger.get(TAG).d("navi title view height : %d, statusBarHeight is %d", height, statusBarHeight);
        ViewGroup.LayoutParams params = naviTitleView.getLayoutParams();
        params.height = height;
        naviTitleView.init();
        naviTitleView.setTitleColor(getNaviTitleViewTitleColor());
        naviTitleView.setLeftButtonTargetAction(this, "back");
		miniContentView = findViewById(R.id.content_view);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        intentParam = MiniIntent.getObjectFromIntent(this.getIntent());
        this.loadingImageResId = R.mipmap.loading;
        if (delegate != null) {
            delegate.afterCreate(this, savedInstanceState);
        }
	}

	@MiniUIAction
	public void setContentView(int id)
    {
        LayoutInflater.from(getBaseContext()).inflate(id, miniContentView);
    }

    public void hideNaviTitleView() {
	    this.naviTitleView.setVisibility(View.GONE);
        //contentView.setTop(0);
        //contentView.setBottom(containerView.getHeight());
    }

    public void showNaviTitleView()
    {
        this.naviTitleView.setVisibility(View.VISIBLE);
        //contentView.setTop(this.naviTitleView.getHeight());
        //contentView.setBottom(containerView.getHeight());
    }

	public int getNaviTitleViewHeight() {
        MiniUIDelegate delegate = this.getActivityDelegate();
        if (delegate != null) {
            return delegate.getNaviTitleViewHeight() + getStatusBarHeight();
        }
        else {
            return DP_2_PX(40) + getStatusBarHeight();
        }
    }

    public int getNaviTitleViewBackgroundColor() {
        MiniUIDelegate delegate = this.getActivityDelegate();
        if (delegate != null) {
            return delegate.getNaviTitleViewBackgroundColor();
        }
        else {
            return MiniUIColor.color("#5BC300");
        }
    }

    public int getNaviTitleViewTitleColor() {
        MiniUIDelegate delegate = this.getActivityDelegate();
        if (delegate != null) {
            return delegate.getNaviTitleViewTitleColor();
        }
	    return Color.WHITE;
    }

	public void setNaviTitle(CharSequence title) {
        if (naviTitleView != null) {
            naviTitleView.setTitle(title);
        }
    }

    public void setTitle(CharSequence title) {
	    if (title != null) {
	        if (naviTitleView != null) {
                naviTitleView.setTitle(title.toString());
            }
        }
    }

    public void setNaviLeftButtonImageId(int imageId) {
	    if (naviTitleView != null)
	    naviTitleView.setLeftButtonImageId(imageId);
    }

    public void setNaviRightButtonString(String title, Object target, String action) {
        if (naviTitleView != null)
        naviTitleView.setRightButtonTitle(title, target, action);
    }

    public void hideNaviRightButton() {
        if (naviTitleView != null)
        naviTitleView.hideRightButton();
    }

    public void hideNaviLeftButton() {
        if (naviTitleView != null)
        naviTitleView.hideNaviLeftButton();
    }

	protected int getStatusBarHeight() {
	    return MiniUIScreen.getStatusBarHeight(MiniUIActivity.this);
    }
	protected void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void setKeepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

	protected void initSoundPool() {
        if (soundPool == null) {
            soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        }
    }

    public void onStart() {
	    scanViewClickAction();
        layoutViews();
	    super.onStart();
    }

    private void scanViewClickAction()
    {
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            MiniUIClickAction annotation = m.getAnnotation(MiniUIClickAction.class);
            if (annotation != null && annotation.value() > 0) {
                findViewById(annotation.value()).setOnClickListener(this);
                viewActionMethodMap.put(annotation.value(), m);
            }
        }
    }

	@Override
	protected void onStop() {
		super.onStop();
		System.gc();
	}

	public void addOnKeyListener(OnKeyListener listener) {
		if (!keyListeners.contains(listener)) {
			keyListeners.addFirst(listener);
		}
	}

	public void removeOnKeyListener(OnKeyListener listener) {
		keyListeners.remove(listener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		topActivity = this;
	}

	@Override
	protected void onDestroy() {
        destroyed = true;
        MiniNotificationCenter.defaultNotificationCenter().remove(this);
		super.onDestroy();
	}


//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if(MiniUIEventTools.getDisableTouchEventCounter() > 0)
//			return false;
//		if(ev.getAction() == MotionEvent.ACTION_DOWN){
//			if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
//				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//		}
//		return super.dispatchTouchEvent(ev);
//	}
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if(MiniUIEventTools.getDisableKeyEventCounter() > 0)
//			return false;
//		return super.dispatchKeyEvent(event);
//	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		View v = getWindow().getCurrentFocus();
//		if (v != null)
//			v.onKeyDown(keyCode, event);
//		for (OnKeyListener onKeyListener : keyListeners) {
//			if (onKeyListener.onKey(null, keyCode, event)) {
//				return true;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}

//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//        View v = getWindow().getCurrentFocus();
//        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER ) {
//            if (handleViewClicked(v)) {
//                return true;
//            }
//            if (v instanceof MiniUIEditText) {
//                ((MiniUIEditText)v).requestFocusAndShowInput();
//                return true;
//            }
//        }
//		for (OnKeyListener onKeyListener : keyListeners) {
//			if (onKeyListener.onKey(v, keyCode, event)) {
//				return true;
//			}
//		}
//		return super.onKeyUp(keyCode, event);
//	}

//	@Override
//	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//		for (OnKeyListener onKeyListener : keyListeners) {
//			if (onKeyListener.onKey(null, keyCode, event)) {
//				return true;
//			}
//		}
//		return super.onKeyLongPress(keyCode, event);
//	}
//
//	@Override
//	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
//		for (OnKeyListener onKeyListener : keyListeners) {
//			if (onKeyListener.onKey(null, keyCode, event)) {
//				return true;
//			}
//		}
//		return super.onKeyMultiple(keyCode, repeatCount, event);
//	}

//	@Override
//	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
//		for (OnKeyListener onKeyListener : keyListeners) {
//			if (onKeyListener.onKey(null, keyCode, event)) {
//				return true;
//			}
//		}
//		return super.onKeyShortcut(keyCode, event);
//	}

	public boolean isActivityDestroyed() {
		return destroyed;
	}

	public void handleMessage(Message msg) {

	}
	
	public void startActivity(Class cls){
		startActivity(new Intent(this,cls));
	}

	public int loadSoundWithResId(int resId) {
        initSoundPool();
        return soundPool.load(this, resId, 1);
    }

    public static void playViewClickedSound(View view) {
        if (view instanceof MiniUIViewProtocol) {
            int soundId = ((MiniUIViewProtocol)view).getKeySound();
            if (soundId == -2) {
                return;
            }
            if (soundId != -1) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
            else {
                if (defaultKeySound != -1) {
                    soundPool.play(defaultKeySound, 1, 1, 0, 0, 1);
                }
            }
        }
    }

    public void showWaiting() {
        showWaiting(null, loadingImageResId);
    }

    public void showWaitingInView(ViewGroup viewGroup) {
        showWaitingInView(viewGroup, null);
    }

    public void showWaitingInView(ViewGroup viewGroup, String message) {
        showWaitingInView(viewGroup, message, loadingImageResId);
    }

    public void showWaiting(String message) {
        showWaiting(message, loadingImageResId);
    }

    private void execShowWaiting(ViewGroup parentView, String message, int loadingImageResId)
    {
        if (waitingView == null) {
            waitingView = new MiniUILoadingView(this);
            parentView.addView(waitingView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) waitingView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
        else {
            ((ViewGroup)waitingView.getParent()).removeView(waitingView);
            parentView.addView(waitingView);
        }
        waitingView.setMessage(message, loadingImageResId);
        waitingView.setVisibility(View.VISIBLE);
        waitingView.bringToFront();
    }

    public void showWaitingInView(final ViewGroup parentView, final String message, final int loadingImageResId) {
        if (MiniThread.isMainThread()) {
            execShowWaiting(parentView, message, loadingImageResId);
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    execShowWaiting(parentView, message, loadingImageResId);
                }
            });
        }
    }

    public void showWaiting(final String message, final int loadingImageResId)
    {
        if (MiniThread.isMainThread()) {
            execShowWaiting(miniContentView, message, loadingImageResId);
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    execShowWaiting(miniContentView, message, loadingImageResId);
                }
            });
        }
    }

    private void execDismissWaiting()
    {
        if (waitingView != null) {
            waitingView.setVisibility(View.GONE);
        }
    }

    public void dismissWaiting() {
        if (MiniThread.isMainThread()) {
            execDismissWaiting();
        }
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    execDismissWaiting();
                }
            });
        }
    }

    @Override
    public void onClick(View view)
    {
        handleViewClicked(view);
        int id = view.getId();
        Method method = viewActionMethodMap.get(id);
        if (method != null) {
            if (method.getTypeParameters() != null && method.getTypeParameters().length > 0) {
                try {
                    method.invoke(this, view);
                } catch (Exception e) {
                    MiniLogger.get(TAG).e(e);
                }
            }
            else {
                try {
                    method.invoke(this);
                } catch (Exception e) {
                    MiniLogger.get(TAG).e(e);
                }
            }
        }
    }

    protected boolean handleViewClicked(View view)
    {
        if (view != null) {
            if (view instanceof MiniUIViewProtocol) {
                playViewClickedSound(view);
            }
        }
        return false;
    }

    protected void layoutViews()
    {
	    if (scrollContainerView != null) {
            scrollContainerView.layout(0, 0, this.getContentView().getWidth(), this.getContentView().getHeight());
            scrollContentView.layout(0, 0, scrollContainerView.getWidth(), scrollContainerView.getHeight());
        }
        layoutSubViews();
    }

    public void setScrollViewTop(int top) {
        if (scrollContainerView != null) {
            scrollContainerView.setTop(top);
            scrollContainerView.setBottom(this.getContentView().getHeight());
            scrollContentView.setTop(0);
            scrollContentView.setBottom(scrollContainerView.getHeight());
        }
    }

    protected void layoutSubViews() {

    }

    protected boolean isPortrait() {
	    if (configuration != null) {
            return this.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        else {
	        return this.miniContentView.getHeight() >= this.miniContentView.getWidth();
        }
    }

    protected boolean isLandscape() {
	    if (configuration != null) {
            return this.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        else {
            return this.miniContentView.getHeight() < this.miniContentView.getWidth();
        }
    }

    public void orientationChanged() {
//        containerView.setRight( MiniUIScreen.width());
//        containerView.setBottom( MiniUIScreen.height() - getStatusBarHeight());
            if (miniContentView != null) {
                miniContentView.setTop(getNaviTitleViewHeight());
                miniContentView.setRight(miniContainerView.getWidth());
                miniContentView.setBottom(miniContainerView.getHeight());
            }
        if (naviTitleView != null) {
            naviTitleView.setRight(miniContainerView.getWidth());
            naviTitleView.setBottom(getNaviTitleViewHeight());
        }
        layoutViews();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.configuration = newConfig;
        orientationChanged();
        layoutViews();
    }

    @MiniUIAction
    public void back() {
	    finish();
    }

    protected MiniIntentParam getIntentParam() {
	    return intentParam;
    }

    protected Object getIntentParamData() {
        if (intentParam != null) {
            return intentParam.getData();
        }
        return null;
    }

    public void startActivity(Class<?> clazz, Object data) {
        MiniIntent intent = new MiniIntent(this, clazz);
        intent.setParam(new MiniIntentParam().setData(data));
        this.startActivity(intent);
    }

    public void startActivityForResult(Class<?> clazz, Object parameter, MiniUIActivityResultHandler handler) {
        MiniIntent intent = new MiniIntent(this, clazz);
        intent.setParam(new MiniIntentParam().setData(parameter).setResultHandler(handler));
        startActivityForResult(intent, 1000);
    }

    public void startActivityForResult(Intent intent, MiniUIActivityResultHandler handler) {
        this.resultHandler1001 = handler;
        startActivityForResult(intent, 1001);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1001) {
            this.resultHandler1001.handleResult(resultCode, null, data);
        }
        else if (requestCode == 1000){
            MiniIntentParam param = MiniIntent.getObjectFromIntent(data);
            if (param != null && param.getResultHandler() != null) {
                param.getResultHandler().handleResult(resultCode, param.getData(), data);
            }
        }
    }

    protected void setResultObject(Object data) {
        MiniIntent intent = new MiniIntent();
        if (this.intentParam != null) {
            this.intentParam.setData(data);
            intent.setParam(this.intentParam);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public MiniUIDelegate getActivityDelegate() {
	    if (activityDelegate == null) {
	        activityDelegate = MiniConfiguration.getGlobalUIDelegate();
        }
        return activityDelegate;
    }

    public void setActivityDelegate(MiniUIDelegate activityDelegate) {
        this.activityDelegate = activityDelegate;
    }

    public void setLoadingImageResId(int loadingImageResId) {
        this.loadingImageResId = loadingImageResId;
    }

    public MiniUINaviTitleView getNaviTitleView() {
        return naviTitleView;
    }

    public void bringNaviTitleViewToFront() {
	    if (naviTitleView != null) {
	        naviTitleView.bringToFront();
        }
    }

    public void dismissLeftPopView()
    {
        if (leftPopView != null) {
            leftPopView.dismiss(this.miniContentView);
        }
    }

    public void dismissRightPopView()
    {
        if (rightPopView != null) {
            rightPopView.dismiss(this.miniContentView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showLeftPopView(List<View> buttonViews)
    {
        if (leftPopView == null)
        {
            leftPopView = new MiniUIPopMenuView(this);
            leftPopView.setLocation(MiniUIPopMenuView.locationLeft);
        }
        leftPopView.setButtons(buttonViews);
        leftPopView.toggle(this.miniContentView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showRightPopView(List<View> buttonViews) {
        if (rightPopView == null)
        {
            rightPopView = new MiniUIPopMenuView(this);
            leftPopView.setLocation(MiniUIPopMenuView.locationRight);
        }
        rightPopView.setButtons(buttonViews);
        rightPopView.toggle(this.miniContentView);
    }

    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        RequestPermissionsCallback callback = permissionsCallbackMap.get(permissions[0]);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (callback != null) {
                    callback.onRequestPermissionsResult(true);
                }
                else {
                    callback.onRequestPermissionsResult(false);
                }

            } else {
                callback.onRequestPermissionsResult(false);
            }
            permissionsCallbackMap.remove(permissions[0]);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(String permission, RequestPermissionsCallback callback) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsCallbackMap.put(permission, callback);
            String[] permissions = new String[]{permission};
            this.requestPermissions(permissions, REQUEST_PERMISSIONS_CODE);
        }
        else {
            callback.onRequestPermissionsResult(true);
        }
    }

    public void requestCameraPermission(RequestPermissionsCallback callback)
    {
        requestPermission(Manifest.permission.CAMERA, callback);
    }

    public void requestStoragePermission(RequestPermissionsCallback callback)
    {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, callback);
    }

    public void requestCallPhonePermission(RequestPermissionsCallback callback)
    {
        requestPermission(Manifest.permission.CALL_PHONE, callback);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void alertMessage(final String message)
    {
        final AlertMessageDialogConfig config = this.activityDelegate.alertMessageDialogConfig();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(config.alertMessageDialogResourceId);

        handler.post(new Runnable() {
            @Override
            public void run() {
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                TextView textView = alertDialog.findViewById(config.alertMessageDialogMessageViewId);
                textView.setText(message);
                alertDialog.findViewById(config.alertMessageDialogPositiveButtonId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.findViewById(config.alertMessageDialogNegativeButtonId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.findViewById(config.alertMessageDialogNegativeButtonId).setVisibility(View.GONE);
            }
        });
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
