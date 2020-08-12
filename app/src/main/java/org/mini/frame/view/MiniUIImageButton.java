package org.mini.frame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIActionAssistant;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;

/**
 * Created by YXL on 2015/12/10.
 */
@SuppressLint("AppCompatCustomView")
public class MiniUIImageButton extends ImageButton implements MiniUIViewProtocol
{

    private StateListDrawable backgroundDrawable;

	public MiniUIImageButton(Context context) {
		super(context);
	}

	public MiniUIImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MiniUIImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}


	public MiniUIImageButton (Context context, int normalResId, int pressedResId) {
        super(context);
		this.setScaleType(ScaleType.CENTER_INSIDE);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setPadding(0, 0, 0, 0);
		StateListDrawable draw = stateListDrawable(normalResId, pressedResId);
		this.setImageDrawable(draw);
		this.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setRight(this.getMeasuredWidth());
		this.setBottom(this.getMeasuredHeight());
	}

	public void setBackground(int normalResId, int pressedResId) {
        backgroundDrawable = stateListDrawable(normalResId, pressedResId);
        this.setBackground(backgroundDrawable);
    }

    public void setSelectedResourceId(int resourceId) {
        getBackgroundDrawable();
        Drawable d  = this.getContext().getResources().getDrawable(resourceId);
        backgroundDrawable.addState(new int[]{ android.R.attr.state_selected}, d);
    }

    public void setNormalResourceId(int resourceId) {
        getBackgroundDrawable();
        Drawable d  = this.getContext().getResources().getDrawable(resourceId);
        backgroundDrawable.addState(new int[]{ -android.R.attr.state_pressed}, d);
    }

    public void setPressResourceId(int resourceId) {
        getBackgroundDrawable();
        Drawable d  = this.getContext().getResources().getDrawable(resourceId);
        backgroundDrawable.addState(new int[]{ android.R.attr.state_pressed}, d);
    }

    private StateListDrawable getBackgroundDrawable() {
        if (backgroundDrawable == null) {
            backgroundDrawable =  new StateListDrawable();
            this.setBackground(backgroundDrawable);
        }
        return backgroundDrawable;
    }


    private StateListDrawable stateListDrawable (int normalResId, int pressedResId) {
        StateListDrawable draw = new StateListDrawable();
        Context context = this.getContext();
        Drawable normal = context.getResources().getDrawable(normalResId);
        Drawable down;
        if (pressedResId > 0) {
            down = context.getResources().getDrawable(pressedResId);
            draw.addState(new int[] { android.R.attr.state_pressed }, down);
        }
        draw.addState(new int[] { -android.R.attr.state_pressed }, normal);
        return draw;
    }

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	// extension_view
	public MiniUIImageButton setUIAlpha(float alpha) {
		setAlpha(alpha);
		return this;
	}
	public MiniUIImageButton setUIBackgroudDrawble(Drawable drawable) {

		setBackgroundDrawable(drawable);
		return this;
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

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIImageButton setUIPosition(int x, int y) {
		setUIX(x);
		setUIY(y);
		return this;
	}

	public MiniUIImageButton setUIX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public MiniUIImageButton setUIY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public MiniUIImageButton setUIPosition(double x, double y) {
		setUIPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public MiniUIImageButton setUIX(double x) {
		setUIX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIImageButton setUIY(double y) {
		setUIY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIImageButton setUICenter(int x, int y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIImageButton setUICenterX(int x) {
		setUIX(x - getWidth() / 2);
		return this;
	}

	public MiniUIImageButton setUICenterY(int y) {
		setUIY(y - getHeight() / 2);
		return this;
	}

	public MiniUIImageButton setUICenter(double x, double y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIImageButton setUICenterX(double x) {
		setUICenterX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIImageButton setUICenterY(double y) {
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

	public MiniUIImageButton setUIRight(int right) {
		int w = getWidth();
		setRight(right);
		setLeft(right - w);
		return this;
	}

	public MiniUIImageButton setUIBottom(int bottom) {
		int h = getHeight();
		setBottom(bottom);
		setTop(bottom - h);
		return this;
	}

	public MiniUIImageButton setUIRight(double right) {
		return setUIRight(getParentWidthPercent(right));
	}

	public MiniUIImageButton setUIBottom(double bottom) {
		return setUIBottom(getParentHeightPercent(bottom));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIImageButton setUISize(int width, int height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIImageButton setUIWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public MiniUIImageButton setUIHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public MiniUIImageButton setUISize(double width, double height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIImageButton setUIWidth(double width) {
		setUIWidth(getParentWidthPercent(width));
		return this;
	}

	public MiniUIImageButton setUIHeight(double height) {
		setUIHeight(getParentHeightPercent(height));
		return this;
	}

	public MiniUIImageButton setUISizeKeepCenter(int width, int height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIImageButton setUIWidthKeepCenter(int width) {
		int x = GetCenterX();
		setRight(getLeft() + width);
		setUICenterX(x);
		return this;
	}

	public MiniUIImageButton setUIHeightKeepCenter(int height) {
		int y = GetCenterY();
		setBottom(getTop() + height);
		setUICenterY(y);
		return this;
	}

	public MiniUIImageButton setUISizeKeepCenter(double width, double height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIImageButton setUIWidthKeepCenter(double width) {
		setUIWidthKeepCenter(getParentWidthPercent(width));
		return this;
	}

	public MiniUIImageButton setUIHeightKeepCenter(double height) {
		setUIHeightKeepCenter(getParentHeightPercent(height));
		return this;
	}

	public MiniUIImageButton ScaleSize(double scale) {
		int width = (int) (getWidth() * scale);
		int height = (int) (getHeight() * scale);
		setUISize(width, height);
		return this;
	}

	public MiniUIImageButton setUIDesignSizeScale(int width, int height) {
		float scaleX = (float) MiniUIScreen.width() / MiniUIScreen.width();
		float scaleY = (float) MiniUIScreen.height() / MiniUIScreen.height();
		float scale = Math.min(scaleX, scaleY);
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIImageButton setUIDesignSizeScaleX(int width, int height) {
		float scale = (float) MiniUIScreen.width() / MiniUIScreen.width();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIImageButton setUIDesignSizeScaleY(int width, int height) {
		float scale = (float) MiniUIScreen.height() / MiniUIScreen.height();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIImageButton setUIBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public MiniUIImageButton setUITag(int tag) {
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

	public MiniUIImageButton setUIFillet(int width) {
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

	public MiniUIImageButton setUIBorder(int cornerRadius, int color) {
		return setUIBorder(0, cornerRadius, color);
	}

	public MiniUIImageButton setUIBorder(int borderWidth, int cornerRadius, int color) {
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
	public MiniUIImageButton aid() {
		new MiniUIAid(this);
		return this;
	}

	public MiniUIImageButton CopyPositionFrom(View view) {
		setUIPosition(view.getLeft(), view.getTop());
		return this;
	}

	public MiniUIImageButton CopyCenterFrom(View view) {
		setUICenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
		return this;
	}

	public MiniUIImageButton CopySizeFrom(View view) {
		setUISize(view.getWidth(), view.getHeight());
		return this;
	}

	public MiniUIImageButton CopyFrameFrom(View view) {
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

    public void setTargetAction(Object object, String action) {
        MiniUIActionAssistant.setTargetAction(this, object, action);
    }
}
