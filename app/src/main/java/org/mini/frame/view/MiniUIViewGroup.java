package org.mini.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;
import org.mini.frame.uitools.MiniUIActionAssistant;

/**
 * Created by YXL on 2015/12/9.
 */
public class MiniUIViewGroup extends ViewGroup implements MiniUIViewProtocol
{

    private Object userInfo;

    private boolean maskToBounds = false;

    private int cornerRadius = 0;

	public MiniUIViewGroup(Context context) {
		super(context);
	}

	public MiniUIViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MiniUIViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

    protected void dispatchDraw(Canvas canvas) {
        if (maskToBounds || cornerRadius > 0) {
            Path path = new Path();
            path.addRoundRect(new RectF(0, 0, this.getWidth(), this.getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
            canvas.clipPath(path, Region.Op.REPLACE);
        }
        super.dispatchDraw(canvas);
    }

	public static MiniUIViewGroup create(Context context, int tag, OnClickListener onClickListener ) {

		MiniUIViewGroup vg = new MiniUIViewGroup(context);
		vg.setTag(tag);
		vg.setOnClickListener(onClickListener);
 		return  vg;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getWidth(), getHeight());
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
			v.measure(v.getWidth(), v.getHeight());
		}
	}

    protected void defaultOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public void setFrame(int l, int t, int w, int h) {
        this.setLeft(l);
        this.setTop(t);
        this.setRight(l + w);
        this.setBottom(t + h);
    }

	protected void onMeasure() {

    }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
		    try {
                v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            }
            catch (Exception e) {
		        e.printStackTrace();
            }
		}
	}

	@Override
	public void invalidate(int l, int t, int r, int b) {
		super.invalidate(0, 0, getWidth(), getHeight());

	}

	public <T extends View> T AddView(T view) {
		addView(view);
		return view;
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

    public MiniUIViewGroup setCorner(int cornerRadius, int color) {
        return setCorner(0, cornerRadius, color);
    }

    public MiniUIViewGroup setCornerRadius(int cornerRadius, int color) {
        return setCorner(0, cornerRadius, color);
    }

    public MiniUIViewGroup setCorner(int borderWidth, int cornerRadius, int color) {
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

    public MiniUIViewGroup setWidth(int width) {
        setRight(getLeft() + width);
        return this;
    }

    public MiniUIViewGroup setHeight(int height) {
        setBottom(getTop() + height);
        return this;
    }

    public MiniUIViewGroup setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
        return this;
    }

    public MiniUIViewGroup setPosition(int x, int y) {
	    int w = getWidth();
        setLeft(x);
        setRight(x + w);
        int h = getHeight();
        setTop(y);
        setBottom(y +h );
        return this;
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

    public Object getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Object userInfo) {
        this.userInfo = userInfo;
    }

    public void setTargetAction(final Object target, final String action) {
        MiniUIActionAssistant.setTargetAction(this, target,action);
    }

    public void contentChanged() {
        this.layout(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
    }

    public boolean isMaskToBounds() {
        return maskToBounds;
    }

    public void setMaskToBounds(boolean maskToBounds) {
        this.maskToBounds = maskToBounds;
    }
}
