package org.mini.frame.view;

import android.content.Context;
import android.view.View;

/**
 * Created by Wuquancheng on 2018/8/16.
 */

public class MiniUIViewGroupBase extends MiniUIViewGroup {

    public MiniUIViewGroupBase(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getWidth(), getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        }
    }

    public void setFrame(int l, int t, int w, int h) {
        this.setLeft(l);
        this.setTop(t);
        this.setRight(l + w);
        this.setBottom(t + h);
    }
}
