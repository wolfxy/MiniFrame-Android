package org.mini.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextClock;
import android.widget.TextView;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;

import java.lang.reflect.Field;

/**
 * Created by yangc on 2017/6/13.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MiniUITextClock extends TextClock implements MiniUIViewProtocol
{

    public static float defaultSize = 14;
    public static int defaultColor = Color.WHITE;

    private TextPaint m_TextPaint;
    private int strokeColor;
    private int strokeWidth;

    public MiniUITextClock(Context context) {
        super(context);
        m_TextPaint = getPaint();
    }

    public MiniUITextClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static MiniUITextClock Create(Context context, CharSequence text) {
        return Create(context, text, defaultSize);
    }

    public static MiniUITextClock Create(Context context, CharSequence text, float size) {
        return Create(context, text, size, defaultColor);
    }

    public static MiniUITextClock Create(Context context, CharSequence text, float size, int color) {
        return Create(context, text, size, color, true);
    }

    public static MiniUITextClock Create(Context context, CharSequence text, float size, int color, boolean singleLine) {
        MiniUITextClock v = new MiniUITextClock(context);
        v.setText(text);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setSingleLine(singleLine);
        v.setGravity(Gravity.LEFT);
        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setRight(v.getMeasuredWidth());
        v.setBottom(v.getMeasuredHeight());
        return v;
    }



    public static MiniUITextClock Create(Context context, int resId) {
        return Create(context, resId, defaultSize);
    }

    public static MiniUITextClock Create(Context context, int resId, float size) {
        return Create(context, resId, size, defaultColor);
    }

    public static MiniUITextClock Create(Context context, int resId, float size, int color) {
        return Create(context, resId, size, color, true);
    }

    public static MiniUITextClock Create(Context context, int resId, float size, int color, boolean singleLine) {
        MiniUITextClock v = new MiniUITextClock(context);
        v.setText(resId);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setSingleLine(singleLine);
        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setRight(v.getMeasuredWidth());
        v.setBottom(v.getMeasuredHeight());
        return v;
    }

    public MiniUITextClock setUIGravity(int gravity) {
        setGravity(gravity);
        return this;
    }

    public MiniUITextClock SizeToFit() {
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setUISize(getMeasuredWidth(), getMeasuredHeight());
        return this;
    }

    public MiniUITextClock setUIText(CharSequence text) {
        setText(text);
        SizeToFit();
        return this;
    }

    public MiniUITextClock setUITextKeepCenter(CharSequence text) {
        setText(text);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setUISizeKeepCenter(getMeasuredWidth(), getMeasuredHeight());
        return this;
    }

    public MiniUITextClock setUITextKeepRight(CharSequence text) {
        setText(text);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int right = getRight();
        setUISize(getMeasuredWidth(), getMeasuredHeight());
        setUIRight(right);
        return this;
    }

    public MiniUITextClock setUITextBold(boolean bold) {
        getPaint().setFakeBoldText(bold);
        return this;
    }

    public MiniUITextClock setUISideLine(int color, int width) {
        strokeColor = color;
        strokeWidth = width;
        return this;
    }

    public MiniUITextClock setUIFormat24Hour(String format) {
        setFormat24Hour(format);
        return this;
    }
    public MiniUITextClock setUIFormat12Hour(String format) {
        setFormat12Hour(format);
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (strokeWidth > 0) {

            int color = getCurrentTextColor();
            setTextColorUseReflection(strokeColor);
            m_TextPaint.setStrokeWidth(strokeWidth); // 描边宽度
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE); // 描边种类
            m_TextPaint.setFakeBoldText(true); // 外层text采用粗体
            m_TextPaint.setShadowLayer(1, 0, 0, 0); // 字体的阴影效果，可以忽略
            super.onDraw(canvas);

            setTextColorUseReflection(color);
            m_TextPaint.setStrokeWidth(0);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            m_TextPaint.setFakeBoldText(false);
            m_TextPaint.setShadowLayer(0, 0, 0, 0);
        }
        super.onDraw(canvas);
    }

    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }

    // extension_view
    public MiniUITextClock setUIAlpha(float alpha) {
        setAlpha(alpha);
        return this;
    }

    public Object tagObject1;
    public Object tagObject2;
    public Object tagObject3;
    public Object tagObject4;
    public Object tagObject5;

    public int getParentWidthPercent(double x) {
        int w = MiniUIScreen.width();
        if (getParent() != null && getParent() instanceof ViewGroup) {
            int pw = ((ViewGroup) getParent()).getWidth();
            if (pw > 0)
                w = pw;
        }
        return (int) (w * x);
    }

    public int getParentHeightPercent(double y) {
        int h = MiniUIScreen.height();
        if (getParent() != null && getParent() instanceof ViewGroup) {
            int ph = ((ViewGroup) getParent()).getHeight();
            if (ph > 0)
                h = ph;
        }
        return (int) (h * y);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MiniUITextClock setUIPosition(int x, int y) {
        setUIX(x);
        setUIY(y);
        return this;
    }

    public MiniUITextClock setUIX(int x) {
        int w = getWidth();
        setLeft(x);
        setRight(x + w);
        return this;
    }

    public MiniUITextClock setUIY(int y) {
        int h = getHeight();
        setTop(y);
        setBottom(y + h);
        return this;
    }

    public MiniUITextClock setUIPosition(double x, double y) {
        setUIPosition(getParentWidthPercent(x), getParentHeightPercent(y));
        return this;
    }

    public MiniUITextClock setUIX(double x) {
        setUIX(getParentWidthPercent(x));
        return this;
    }

    public MiniUITextClock setUIY(double y) {
        setUIY(getParentHeightPercent(y));
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MiniUITextClock setUICenter(int x, int y) {
        setUICenterX(x);
        setUICenterY(y);
        return this;
    }

    public MiniUITextClock setUICenterX(int x) {
        setUIX(x - getWidth() / 2);
        return this;
    }

    public MiniUITextClock setUICenterY(int y) {
        setUIY(y - getHeight() / 2);
        return this;
    }

    public MiniUITextClock setUICenter(double x, double y) {
        setUICenterX(x);
        setUICenterY(y);
        return this;
    }

    public MiniUITextClock setUICenterX(double x) {
        setUICenterX(getParentWidthPercent(x));
        return this;
    }

    public MiniUITextClock setUICenterY(double y) {
        setUICenterY(getParentHeightPercent(y));
        return this;
    }

    public int GetCenterX() {
        return getLeft() + getWidth() / 2;
    }

    public int GetCenterY() {
        return getTop() + getHeight() / 2;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public MiniUITextClock setUIRight(int right) {
        int w = getWidth();
        setRight(right);
        setLeft(right - w);
        return this;
    }

    public MiniUITextClock setUIBottom(int bottom) {
        int h = getHeight();
        setBottom(bottom);
        setTop(bottom - h);
        return this;
    }

    public MiniUITextClock setUIRight(double right) {
        return setUIRight(getParentWidthPercent(right));
    }

    public MiniUITextClock setUIBottom(double bottom) {
        return setUIBottom(getParentHeightPercent(bottom));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MiniUITextClock setUISize(int width, int height) {
        setUIWidth(width);
        setUIHeight(height);
        return this;
    }

    public MiniUITextClock setUIWidth(int width) {
        setRight(getLeft() + width);
        return this;
    }

    public MiniUITextClock setUIHeight(int height) {
        setBottom(getTop() + height);
        return this;
    }

    public MiniUITextClock setUISize(double width, double height) {
        setUIWidth(width);
        setUIHeight(height);
        return this;
    }

    public MiniUITextClock setUIWidth(double width) {
        setUIWidth(getParentWidthPercent(width));
        return this;
    }

    public MiniUITextClock setUIHeight(double height) {
        setUIHeight(getParentHeightPercent(height));
        return this;
    }

    public MiniUITextClock setUISizeKeepCenter(int width, int height) {
        setUIWidthKeepCenter(width);
        setUIHeightKeepCenter(height);
        return this;
    }

    public MiniUITextClock setUIWidthKeepCenter(int width) {
        int x = GetCenterX();
        setRight(getLeft() + width);
        setUICenterX(x);
        return this;
    }

    public MiniUITextClock setUIHeightKeepCenter(int height) {
        int y = GetCenterY();
        setBottom(getTop() + height);
        setUICenterY(y);
        return this;
    }

    public MiniUITextClock setUISizeKeepCenter(double width, double height) {
        setUIWidthKeepCenter(width);
        setUIHeightKeepCenter(height);
        return this;
    }

    public MiniUITextClock setUIWidthKeepCenter(double width) {
        setUIWidthKeepCenter(getParentWidthPercent(width));
        return this;
    }

    public MiniUITextClock setUIHeightKeepCenter(double height) {
        setUIHeightKeepCenter(getParentHeightPercent(height));
        return this;
    }

    public MiniUITextClock ScaleSize(double scale) {
        int width = (int) (getWidth() * scale);
        int height = (int) (getHeight() * scale);
        setUISize(width, height);
        return this;
    }

    public MiniUITextClock setUIDesignSizeScale(int width, int height) {
        float scaleX = (float) MiniUIScreen.width() / MiniUIScreen.width();
        float scaleY = (float) MiniUIScreen.height() / MiniUIScreen.height();
        float scale = Math.min(scaleX, scaleY);
        setUISize((int) (width * scale), (int) (height * scale));
        return this;
    }

    public MiniUITextClock setUIDesignSizeScaleX(int width, int height) {
        float scale = (float) MiniUIScreen.width() / MiniUIScreen.width();
        setUISize((int) (width * scale), (int) (height * scale));
        return this;
    }

    public MiniUITextClock setUIDesignSizeScaleY(int width, int height) {
        float scale = (float) MiniUIScreen.height() / MiniUIScreen.height();
        setUISize((int) (width * scale), (int) (height * scale));
        return this;
    }
    public MiniUITextClock setUIBackgroudDrawble(Drawable drawable) {

        setBackgroundDrawable(drawable);
        return this;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MiniUITextClock setUIBackgroundColor(int color) {
        setBackgroundColor(color);
        return this;
    }

    public MiniUITextClock setUITag(int tag) {
        setTag(tag);
        return this;
    }

    public int GetTag() {
        return MiniToolKit.parseInt(getTag());
    }

    public void removeFromSuperView() {
        if (getParent() instanceof ViewGroup) {
            invalidate();//解决移除后有残影的问题
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public MiniUIViewGroup GetParent() {
        return (MiniUIViewGroup) getParent();
    }

    public MiniUITextClock setUIFillet(int width) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = width;
            innerRadii[i] = width;
        }
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(0, 0, 0, 0), innerRadii));
        int color = Color.GRAY;
        if (getBackground() instanceof ColorDrawable) {
            color = ((ColorDrawable) getBackground()).getColor();
        } else if (getBackground() instanceof ShapeDrawable) {
            color = ((ShapeDrawable) getBackground()).getPaint().getColor();
        }
        sd.getPaint().setColor(color);

        setBackgroundDrawable(sd);
        return this;
    }

    public MiniUITextClock setUIBorder(int cornerRadius, int color) {
        return setUIBorder(0, cornerRadius, color);
    }

    public MiniUITextClock setUIBorder(int borderWidth, int cornerRadius, int color) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = cornerRadius;
            innerRadii[i] = cornerRadius;
        }
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(borderWidth, borderWidth, borderWidth, borderWidth), innerRadii));
        sd.getPaint().setColor(color);

        setBackgroundDrawable(sd);
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MiniUITextClock aid() {
        new MiniUIAid(this);
        return this;
    }

    public MiniUITextClock CopyPositionFrom(View view) {
        setUIPosition(view.getLeft(), view.getTop());
        return this;
    }

    public MiniUITextClock CopyCenterFrom(View view) {
        setUICenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
        return this;
    }

    public MiniUITextClock CopySizeFrom(View view) {
        setUISize(view.getWidth(), view.getHeight());
        return this;
    }

    public MiniUITextClock CopyFrameFrom(View view) {
        CopyPositionFrom(view);
        CopySizeFrom(view);
        return this;
    }

    public Point GetScreenPosition() {
        Point point = new Point((int) (getLeft() + getWidth() * (1 - getScaleX()) / 2), (int) (getTop() + getHeight() * (1 - getScaleY()) / 2));
        ViewParent parent = getParent();
        while (parent instanceof ViewGroup && parent != MiniUIActivity.topActivity.miniContentView) {
            ViewGroup g = (ViewGroup) parent;
            point.x += (int) (g.getLeft() + g.getWidth() * (1 - g.getScaleX()) / 2);
            point.y += (int) (g.getTop() + g.getHeight() * (1 - g.getScaleY()) / 2);
            parent = g.getParent();
        }
        return point;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// anim

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getParent() instanceof MiniUIViewGroup && widthMeasureSpec > 0 && heightMeasureSpec > 0)
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setVisibility(int visibility) {
        if(visibility != View.VISIBLE){
            invalidate();
        }
        super.setVisibility(visibility);
    }

    protected int musicId = -1;
    public void setKeySound(int musicId) {
        this.musicId = musicId;

    }
    public int getKeySound() {
        return this.musicId;
    }

    public void disableKeySound() {
        this.musicId = -2;
    }
}
