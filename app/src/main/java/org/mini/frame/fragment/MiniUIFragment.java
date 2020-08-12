package org.mini.frame.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import org.mini.frame.notification.MiniNotificationCenter;
import org.mini.frame.activity.MiniUIActivityResultHandler;
import org.mini.frame.intent.MiniIntent;
import org.mini.frame.intent.MiniIntentParam;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.view.MiniUIViewGroup;

/**
 * Created by Wuquancheng on 2018/8/14.
 */

public abstract class MiniUIFragment {

    private MiniUIViewGroup view;
    private Context context;
    private MiniUIViewGroup contentView;
    protected MiniUIActivity activity;
    protected Handler handler = new Handler();


    public MiniUIFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MiniUIFragment(Context context) {
        this.setContext(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setContext(Context context)
    {
        this.context = context;
        view = new MiniUIViewGroup(context);
        contentView = new MiniUIViewGroup(context);
        contentView.setBackgroundColor(Color.WHITE);
        view.addView(contentView);
    }

    public void layoutFrameViews() {
        contentView.setLeft(0);
        contentView.setTop(0);
        contentView.setRight(view.getWidth());
        contentView.setBottom(view.getHeight());
    }

    public void layoutViews() {
        layoutSubViews();
    }

    public void create() {
        layoutFrameViews();
        this.onCreate();
        layoutViews();
    }

    public void setNeedLayout()
    {
        layoutSubViews();
    }

    public void resume() {
        this.onResume();
    }

    public abstract CharSequence title();

    public abstract void layoutSubViews();

    public abstract void onCreate();

    public abstract void onResume();

    public void onDestroy() {
        MiniNotificationCenter.defaultNotificationCenter().remove(this);
    }

    public Context getContext() {
        return context;
    }

    public MiniUIViewGroup getView() {
        return view;
    }

    public MiniUIViewGroup getContentView() {
        return contentView;
    }


    protected void startActivity(Class activityClass) {
        MiniIntent intent = new MiniIntent(activity,activityClass);
        context.startActivity(intent);
    }

    protected void startActivity(Class<?> clazz, Object data) {
        MiniIntent intent = new MiniIntent(this.getContext(), clazz);
        intent.setParam(new MiniIntentParam().setData(data));
        this.activity.startActivity(intent);
    }

    protected void startActivityForResult(Class<?> clazz, Object parameter, MiniUIActivityResultHandler handler) {
        MiniIntent intent = new MiniIntent(this.getContext(), clazz);
        intent.setParam(new MiniIntentParam().setData(parameter).setResultHandler(handler));
        activity.startActivityForResult(intent, 1000);
    }

    public void setActivity(MiniUIActivity activity) {
        this.activity = activity;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void showWaiting() {
        activity.showWaiting();
    }

    protected void dismissWaiting() {
        activity.dismissWaiting();
    }

    public void viewWillAppear() {

    }

    public void viewDidAppear() {

    }

    public void viewWillDisappear() {

    }

    public void viewDidDisappear() {

    }
}
