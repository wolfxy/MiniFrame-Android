package org.mini.frame.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Wuquancheng on 2018/8/24.
 */

public class UIAnimation {

    public static void scrollTo(View view, int x, int toX, int y, int toY,  Animation.AnimationListener listener) {
        TranslateAnimation translateAnimation = new TranslateAnimation(x, toX, y, toY);
        translateAnimation.setDuration((long)(400));
        translateAnimation.setAnimationListener(listener);
        view.startAnimation(translateAnimation);
    }
}
