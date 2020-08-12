package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIAid;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;

public class MiniUIHScrollView extends HorizontalScrollView implements MiniUIViewProtocol
{

	public MiniUIHScrollView(Context context) {
		super(context);
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

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
			v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		}
	}

	public <T extends View> T AddView(T view) {
		addView(view);
		return view;
	}

	// extension_view


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
	public MiniUIHScrollView setUIPosition(int x, int y) {
		setUIX(x);
		setUIY(y);
		return this;
	}

	public MiniUIHScrollView setUIX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public MiniUIHScrollView setUIY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public MiniUIHScrollView setUIPosition(double x, double y) {
		setUIPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public MiniUIHScrollView setUIX(double x) {
		setUIX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIHScrollView setUIY(double y) {
		setUIY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIHScrollView setUICenter(int x, int y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIHScrollView setUICenterX(int x) {
		setUIX(x - getWidth() / 2);
		return this;
	}

	public MiniUIHScrollView setUICenterY(int y) {
		setUIY(y - getHeight() / 2);
		return this;
	}

	public MiniUIHScrollView setUICenter(double x, double y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIHScrollView setUICenterX(double x) {
		setUICenterX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIHScrollView setUICenterY(double y) {
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

	public MiniUIHScrollView setUIRight(int right) {
		int w = getWidth();
		setRight(right);
		setLeft(right - w);
		return this;
	}

	public MiniUIHScrollView setUIBottom(int bottom) {
		int h = getHeight();
		setBottom(bottom);
		setTop(bottom - h);
		return this;
	}

	public MiniUIHScrollView setUIRight(double right) {
		return setUIRight(getParentWidthPercent(right));
	}

	public MiniUIHScrollView setUIBottom(double bottom) {
		return setUIBottom(getParentHeightPercent(bottom));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIHScrollView setUISize(int width, int height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIHScrollView setUIWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public MiniUIHScrollView setUIHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public MiniUIHScrollView setUISize(double width, double height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIHScrollView setUIWidth(double width) {
		setUIWidth(getParentWidthPercent(width));
		return this;
	}

	public MiniUIHScrollView setUIHeight(double height) {
		setUIHeight(getParentHeightPercent(height));
		return this;
	}

	public MiniUIHScrollView setUISizeKeepCenter(int width, int height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIHScrollView setUIWidthKeepCenter(int width) {
		int x = GetCenterX();
		setRight(getLeft() + width);
		setUICenterX(x);
		return this;
	}

	public MiniUIHScrollView setUIHeightKeepCenter(int height) {
		int y = GetCenterY();
		setBottom(getTop() + height);
		setUICenterY(y);
		return this;
	}

	public MiniUIHScrollView setUISizeKeepCenter(double width, double height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIHScrollView setUIWidthKeepCenter(double width) {
		setUIWidthKeepCenter(getParentWidthPercent(width));
		return this;
	}

	public MiniUIHScrollView setUIHeightKeepCenter(double height) {
		setUIHeightKeepCenter(getParentHeightPercent(height));
		return this;
	}

	public MiniUIHScrollView ScaleSize(double scale) {
		int width = (int) (getWidth() * scale);
		int height = (int) (getHeight() * scale);
		setUISize(width, height);
		return this;
	}

	public MiniUIHScrollView setUIDesignSizeScale(int width, int height) {
		float scaleX = (float) MiniUIScreen.width() / MiniUIScreen.width();
		float scaleY = (float) MiniUIScreen.height() / MiniUIScreen.height();
		float scale = Math.min(scaleX, scaleY);
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIHScrollView setUIDesignSizeScaleX(int width, int height) {
		float scale = (float) MiniUIScreen.width() / MiniUIScreen.width();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIHScrollView setUIDesignSizeScaleY(int width, int height) {
		float scale = (float) MiniUIScreen.height() / MiniUIScreen.height();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIHScrollView setUIBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public MiniUIHScrollView setUITag(int tag) {
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

	public MiniUIHScrollView setUIFillet(int width) {
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

	public MiniUIHScrollView setUIBorder(int cornerRadius, int color) {
		return setUIBorder(0, cornerRadius, color);
	}

	public MiniUIHScrollView setUIBorder(int borderWidth, int cornerRadius, int color) {
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
	public MiniUIHScrollView aid() {
		new MiniUIAid(this);
		return this;
	}

	public MiniUIHScrollView CopyPositionFrom(View view) {
		setUIPosition(view.getLeft(), view.getTop());
		return this;
	}

	public MiniUIHScrollView CopyCenterFrom(View view) {
		setUICenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
		return this;
	}

	public MiniUIHScrollView CopySizeFrom(View view) {
		setUISize(view.getWidth(), view.getHeight());
		return this;
	}

	public MiniUIHScrollView CopyFrameFrom(View view) {
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
