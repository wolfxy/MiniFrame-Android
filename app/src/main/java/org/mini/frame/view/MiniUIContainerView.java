package org.mini.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build.VERSION;

import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.tools.AndroidVersion;

/**
 * Created by Wuquancheng on 2018/8/15.
 */

public class MiniUIContainerView extends MiniUIViewGroup {

    private int backgroundColor = MiniUIColor.color("#00FFFFFF");
    private int defaultBackGroundColor = backgroundColor;
    private int borderWidth = 0;
    private int borderColor = MiniUIColor.color("#00FFFFFF");
    private int cornerRadius = 0;

    public MiniUIContainerView(Context context) {
        super(context);
        this.setWillNotDraw(false);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (AndroidVersion.compare(VERSION.RELEASE, "4.4.4") >= 0) {
            Paint paint = null;
            if (borderWidth > 0 || cornerRadius > 0) {
                paint = new Paint();
                if (backgroundColor != defaultBackGroundColor) {
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    paint.setColor(backgroundColor);
                    canvas.drawRoundRect(new RectF(1, 1, this.getWidth() - 1, this.getHeight() - 1), cornerRadius, cornerRadius, paint);
                }
            }
            if (borderWidth > 0) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(borderColor);
                paint.setStrokeWidth(borderWidth);
                canvas.drawRoundRect(new RectF(1,1,this.getWidth()-1, this.getHeight()-1), cornerRadius,cornerRadius, paint);
            }
            if (cornerRadius > 0) {
                Path path = new Path();
                path.addRoundRect(new RectF(1, 1, this.getWidth() - 1, this.getHeight() - 1), cornerRadius, cornerRadius, Path.Direction.CW);
                canvas.clipPath(path, Region.Op.REPLACE);
            }
        }
    }

    public void setBackgroundColor(int color) {
        if (AndroidVersion.compare(VERSION.RELEASE, "4.4.4") >= 0) {
            backgroundColor = color;
        }
        else {
            super.setBackgroundColor(color);
        }
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }
}
