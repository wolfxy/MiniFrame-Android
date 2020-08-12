package org.mini.frame.uitools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mini.frame.activity.MiniUIActivity;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_SP;

/**
 * Created by YXL on 2015/12/9.
 */
public class MiniUITools
{
    public static void initFrame(Context context) {

//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        DisplayMetrics dm = new DisplayMetrics();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            display.getRealMetrics(dm);
//        } else {
//            display.getMetrics(dm);
//        }
//        MiniUIScreen.height() = dm.heightPixels;
//        MiniUIScreen.width() = dm.widthPixels;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
//        Point realPoint = new Point();
//        wm.getDefaultDisplay().getRealSize(realPoint);
    }

    public static boolean isNavigationBarShow(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(menu || back) {
                return false;
            }else {
                return true;
            }
        }
    }

    public static int getNavigationBarHeight(Context activity) {
        if (!isNavigationBarShow(activity)){
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    public static int getSceenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight()+getNavigationBarHeight(activity);
    }

	public static void toast(String text) {
		Toast mToast = null;
		if (mToast == null) {
			mToast = Toast.makeText(MiniUIActivity.topActivity, "", Toast.LENGTH_SHORT);
			LinearLayout layout = (LinearLayout) mToast.getView();
			TextView tv = (TextView) layout.getChildAt(0);
			tv.setTextSize(DP_2_SP(20));
		}
		//mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setText(text);
		mToast.show();
	}

    public static void toastLongTime(String text) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(MiniUIActivity.topActivity, "", Toast.LENGTH_LONG);
            LinearLayout layout = (LinearLayout) mToast.getView();
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setTextSize(DP_2_SP(20));
        }
        //mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(text);
        mToast.show();
    }

	public static boolean isInRangeOfView(View view, float px, float py) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		if (px < x || px > (x + view.getWidth()) || py < y || py > (y + view.getHeight())) {
			return false;
		}
		return true;
	}


	public static void hideSoftKeyBoard() {
		hideSoftKeyBoard(MiniUIActivity.topActivity.getCurrentFocus());
	}

	public static void hideSoftKeyBoard(View focus) {
		if (focus != null) {
			InputMethodManager imm = ((InputMethodManager) MiniUIActivity.topActivity.getSystemService(Context.INPUT_METHOD_SERVICE));
			imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
		}

	}

	public static Bitmap convertViewToBitmap(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
		view.draw(new Canvas(bitmap));
		return bitmap;
	}

	@SuppressLint("NewApi")
	public static Bitmap blurBitmap(Bitmap bitmap, int radius) {
		if (Build.VERSION.SDK_INT < 17) {
			return MiniUIBitmapTools.fastBlur(MiniUIActivity.topActivity, bitmap, radius);
		}
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		RenderScript rs = RenderScript.create(MiniUIActivity.topActivity.getApplicationContext());
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
		blurScript.setRadius(radius);
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);
		allOut.copyTo(outBitmap);
		rs.destroy();
		return outBitmap;
	}


}
