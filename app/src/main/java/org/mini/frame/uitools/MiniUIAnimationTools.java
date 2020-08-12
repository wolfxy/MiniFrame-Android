package org.mini.frame.uitools;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by YXL on 2015/12/21.
 */
public class MiniUIAnimationTools {
    public static ScaleAnimation scaleAnimation(double fromX,double toX,double fromY,double toY,long duration){
        return scaleAnimation(fromX,toX,fromY,toY,duration,new LinearInterpolator());
    }

    public static ScaleAnimation scaleAnimation(double fromX,double toX,double fromY,double toY,long duration,Interpolator interpolator){
        return scaleAnimation(fromX,toX,fromY,toY,duration,interpolator,false);
    }
    
    public static ScaleAnimation scaleAnimation(double fromX,double toX,double fromY,double toY,long duration,Interpolator interpolator,boolean fillAfter){
        ScaleAnimation anim = new ScaleAnimation((float)fromX,(float)toX,(float)fromY,(float)toY, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim.setDuration(duration);
        anim.setInterpolator(interpolator);
        anim.setFillAfter(fillAfter);
        return anim;
    }

    public static TranslateAnimation translateAnimation(double fromXDelta,double toXDelta,double fromYDelta,double toYDelta,long duration){
        return translateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta, duration, new LinearInterpolator());
    }
    
    public static TranslateAnimation translateAnimation(double fromXDelta,double toXDelta,double fromYDelta,double toYDelta,long duration,Interpolator interpolator){
    	 return translateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta, duration, interpolator,false);
    }

    public static TranslateAnimation translateAnimation(double fromXDelta,double toXDelta,double fromYDelta,double toYDelta,long duration,Interpolator interpolator,boolean fillAter){
        TranslateAnimation anim = new TranslateAnimation((float)fromXDelta,(float)toXDelta,(float)fromYDelta,(float)toYDelta);
        anim.setDuration(duration);
        anim.setInterpolator(interpolator);
        anim.setFillAfter(fillAter);
        return anim;
    }

    public static AlphaAnimation alphaAnimation(double fromAlpha,double toAlpha,long duration){
        return alphaAnimation(fromAlpha, toAlpha, duration, new LinearInterpolator());
    }
    
    public static AlphaAnimation alphaAnimation(double fromAlpha,double toAlpha,long duration,Interpolator interpolator){
        return alphaAnimation(fromAlpha, toAlpha, duration, interpolator,false);
    }

    public static AlphaAnimation alphaAnimation(double fromAlpha,double toAlpha,long duration,Interpolator interpolator,boolean fillAfter){
        AlphaAnimation anim = new AlphaAnimation((float)fromAlpha,(float)toAlpha);
        anim.setDuration(duration);
        anim.setInterpolator(interpolator);
        anim.setFillAfter(fillAfter);
        return anim;
    }

    public static RotateAnimation rotateAnimation(double fromDegress,double toDegress,long duration){
        return rotateAnimation(fromDegress, toDegress, duration, new LinearInterpolator());
    }

    public static RotateAnimation rotateAnimation(double fromDegress,double toDegress,long duration,Interpolator interpolator){
        return rotateAnimation(fromDegress, toDegress,0.5,0.5, duration, interpolator,false);
    }
    

    public static RotateAnimation rotateAnimation(double fromDegress,double toDegress,double anchorX,double anchorY,long duration){
        return rotateAnimation(fromDegress, toDegress,anchorX,anchorY, duration, new LinearInterpolator(),false);
    }

    public static RotateAnimation rotateAnimation(double fromDegress,double toDegress,double anchorX,double anchorY,long duration,Interpolator interpolator,boolean fillAfter){
        RotateAnimation anim = new RotateAnimation((float)fromDegress,(float)toDegress, Animation.RELATIVE_TO_SELF,(float)anchorX,Animation.RELATIVE_TO_SELF,(float)anchorY);
        anim.setDuration(duration);
        anim.setInterpolator(interpolator);
        anim.setFillAfter(fillAfter);
        return anim;
    }
}
