package org.mini.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Wuquancheng on 2018/8/15.
 */

public class MiniUICornerViewGroup extends MiniUIViewGroup {

    private int cornerRadius = 0;

    private boolean maskToBounds = false;

    private int backgroundColor = Color.TRANSPARENT;

    public MiniUICornerViewGroup(Context context) {
        super(context);
        this.setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onDraw(Canvas canvas) {
        if (cornerRadius > 0) {
            if (backgroundColor != Color.TRANSPARENT) {
                Paint p = new Paint();
                p.setColor(backgroundColor);
                canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), cornerRadius, cornerRadius, p);
            }
            Path path = new Path();
            path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
            canvas.clipPath(path, Region.Op.REPLACE);
        }
        super.onDraw(canvas);
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public void setFrame(int l, int t, int w, int h) {
        this.setLeft(l);
        this.setTop(t);
        this.setRight(l + w);
        this.setBottom(t + h);
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public boolean isMaskToBounds() {
        return maskToBounds;
    }

    public void setMaskToBounds(boolean maskToBounds) {
        this.maskToBounds = maskToBounds;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
