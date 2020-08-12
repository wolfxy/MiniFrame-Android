package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TimePicker;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;

/**
 * Created by YXL on 2015/12/8.
 */
public class MiniUITimePicker extends TimePicker implements MiniUIViewProtocol
{

	public MiniUITimePicker(Context context) {
		super(context);
	}

	public MiniUITimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MiniUITimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public static MiniUITimePicker Create(Context context, int tag, OnTimeChangedListener onTimeChangedListener, OnClickListener onClickListener) {

		MiniUITimePicker v = new MiniUITimePicker(context);

		v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		v.setRight(v.getMeasuredWidth());
		v.setBottom(v.getMeasuredHeight());
		v.setTag(tag);
		v.setOnClickListener(onClickListener);
		v.setOnTimeChangedListener(onTimeChangedListener);
		return v;
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
    
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

    public void removeFromSuperView() {
        if (getParent() != null && getParent() instanceof ViewGroup) {
            invalidate();//解决移除后有残影的问题
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public void setCorner(int cornerRadius, int color) {
	    setCorner(0, cornerRadius, color);
    }

    public void setCornerRadius(int cornerRadius, int color) {
	    setCorner(0, cornerRadius, color);
    }

    public void setCorner(int borderWidth, int cornerRadius, int color) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = cornerRadius;
            innerRadii[i] = cornerRadius;
        }
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(borderWidth, borderWidth, borderWidth, borderWidth), innerRadii));
        sd.getPaint().setColor(color);
        setBackgroundDrawable(sd);
    }

    public void setWidth(int width) {
        setRight(getLeft() + width);
    }

    public void setHeight(int height) {
        setBottom(getTop() + height);
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setPosition(int x, int y) {
        int w = getWidth();
        setLeft(x);
        setRight(x + w);
        int h = getHeight();
        setTop(y);
        setBottom(y +h );
    }

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
