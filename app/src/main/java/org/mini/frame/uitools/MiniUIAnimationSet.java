package org.mini.frame.uitools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

/**
 * Created by hucheng on 2017/3/24.
 */

public class MiniUIAnimationSet extends AnimationSet {


    public MiniUIAnimationSet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniUIAnimationSet(boolean shareInterpolator) {
        super(shareInterpolator);
    }

    public MiniUIAnimationSet AddAnimation(Animation animation){

        addAnimation(animation);
        return this;
    }

    public MiniUIAnimationSet setUIFillAfter(boolean fillAfter){

        setFillAfter(fillAfter);
        return this;
    }
}
