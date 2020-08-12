package org.mini.frame.uitools;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.mini.frame.application.MiniApplication;

/**
 * Created by Wuquancheng on 15/4/5.
 */
public class MiniDisplayUtil {

    public static Context context = null;

    public static int DP_2_SP(float dipValue) {
        return DP_2_SP(dipValue, MiniApplication.application.getApplicationContext());
    }

    public static int DP_2_SP(float dipValue, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)((dipValue * displayMetrics.density / displayMetrics.scaledDensity) + 0.5);
    }

    public static int SP_2_DP(float spValue) {
        return SP_2_DP(spValue, MiniApplication.application.getApplicationContext());
    }

    public static int SP_2_DP(float spValue, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)((spValue * displayMetrics.scaledDensity)/displayMetrics.density + 0.5);
    }


    public static int DP_2_PX(float dipValue) {
        return DP_2_PX(dipValue, MiniApplication.application.getApplicationContext());
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int DP_2_PX(float dipValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int PX_2_DP(float pxValue) {
        return PX_2_DP(pxValue, MiniApplication.Context());
    }

    public static int PX_2_DP(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int PX_2_SP(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int PX_2_SP(float pxValue) {
       return PX_2_SP(pxValue, MiniApplication.Context());
    }


    public static int SP_2_PX(float spValue) {
        return SP_2_PX(spValue, MiniApplication.Context());
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int SP_2_PX(float spValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int WindowWidth(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }
}
