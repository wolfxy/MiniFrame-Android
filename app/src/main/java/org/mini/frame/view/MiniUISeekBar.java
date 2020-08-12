package org.mini.frame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.Gravity;

import java.lang.reflect.Field;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;

/**
 * Created by Wuquancheng on 2018/10/16.
 */

@SuppressLint("AppCompatCustomView")
public class MiniUISeekBar extends android.widget.SeekBar{
    public MiniUISeekBar(Context context) {
        super(context);
    }

    public MiniUISeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniUISeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setProgressColor(int color) {
        LayerDrawable progressDrawable = (LayerDrawable) this.getProgressDrawable();
        Drawable[] outDrawables = new Drawable[progressDrawable.getNumberOfLayers()];
        for (int i = 0; i < progressDrawable.getNumberOfLayers(); i++) {
            switch (progressDrawable.getId(i)) {
                case android.R.id.background:// 设置进度条背景
                    outDrawables[i] = progressDrawable.getDrawable(i);
                    break;
                case android.R.id.secondaryProgress:// 设置二级进度条
                    outDrawables[i] = progressDrawable.getDrawable(i);
                    break;
                case android.R.id.progress:// 设置进度条
                    Drawable drawable = new ColorDrawable(color);
                    ClipDrawable proDrawable = new ClipDrawable(drawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
                    outDrawables[i] = proDrawable;
                    break;
                default:
                    break;
            }
        }
        progressDrawable = new LayerDrawable(outDrawables);
        this.setProgressDrawable(progressDrawable);
        setMaxHeight(DP_2_PX(2));
    }

    public void setMaxHeight(int maxHeight) {
        try {
            Class<?> superclass = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            Field mMaxHeight = superclass.getDeclaredField("mMaxHeight");
            if (mMaxHeight != null) {
                mMaxHeight.setAccessible(true);
                mMaxHeight.set(this, maxHeight);
            }
            this.setMinimumHeight(maxHeight);
        }
        catch (Exception e) {

        }
    }
}
