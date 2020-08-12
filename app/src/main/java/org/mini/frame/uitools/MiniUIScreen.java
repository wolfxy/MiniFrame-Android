package org.mini.frame.uitools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import org.mini.frame.toolkit.MiniDeviceUtils;
import org.mini.frame.application.MiniApplication;

/**
 * Created by Wuquancheng on 2017/12/7.
 */

public class MiniUIScreen {

    static boolean mHasCheckAllScreen  = false;
    static boolean mIsAllScreenDevice  = false;

    public static int width ()
    {
        WindowManager wm = (WindowManager) MiniApplication.application.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static int height()
    {
        WindowManager wm = (WindowManager) MiniApplication.application.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (isAllScreenDevice()) {
            wm.getDefaultDisplay().getRealSize(point);
            return point.y;
        }
        else {
            wm.getDefaultDisplay().getSize(point);
            return point.y;
        }
    }

    public static int getStatusBarHeight(Activity activity)
    {
        //特殊设备的状态栏
        if("W1011A".equals(MiniDeviceUtils.getModel())) {
            //状态栏在底部的条中
            return 0;
        }
        Resources resources =  MiniApplication.application.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return resources.getDimensionPixelSize(resourceId);
        } else {
            Rect rectangle = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
            return rectangle.top;
        }
    }

    public static boolean isAllScreenDevice() {
        if (mHasCheckAllScreen)
        {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            return false;
        }
        WindowManager windowManager = (WindowManager) MiniApplication.application.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true;
            }
        }
        return mIsAllScreenDevice;
    }
}
