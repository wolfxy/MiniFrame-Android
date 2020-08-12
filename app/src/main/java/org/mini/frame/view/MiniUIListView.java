package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;

/**
 * Created by YXL on 2015/12/16.
 */
public class MiniUIListView extends ListView implements MiniUIViewProtocol
{
	public MiniUIListView(Context context) {
		super(context);
		setDividerHeight(0);
		setSelector(android.R.color.transparent);
		setVerticalScrollBarEnabled(false);
	}

	public MiniUIListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// extension_view

 	public MiniUIListView setUIBackGroundDrawable(Drawable drawable){

		setBackgroundDrawable(drawable);
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
	public MiniUIListView setUIPosition(int x, int y) {
		setUIX(x);
		setUIY(y);
		return this;
	}

	public MiniUIListView setUIX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public MiniUIListView setUIY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public MiniUIListView setUIPosition(double x, double y) {
		setUIPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public MiniUIListView setUIX(double x) {
		setUIX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIListView setUIY(double y) {
		setUIY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIListView setUICenter(int x, int y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIListView setUICenterX(int x) {
		setUIX(x - getWidth() / 2);
		return this;
	}

	public MiniUIListView setUICenterY(int y) {
		setUIY(y - getHeight() / 2);
		return this;
	}

	public MiniUIListView setUICenter(double x, double y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIListView setUICenterX(double x) {
		setUICenterX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIListView setUICenterY(double y) {
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

	public MiniUIListView setUIRight(int right) {
		int w = getWidth();
		setRight(right);
		setLeft(right - w);
		return this;
	}

	public MiniUIListView setUIBottom(int bottom) {
		int h = getHeight();
		setBottom(bottom);
		setTop(bottom - h);
		return this;
	}

	public MiniUIListView setUIRight(double right) {
		return setUIRight(getParentWidthPercent(right));
	}

	public MiniUIListView setUIBottom(double bottom) {
		return setUIBottom(getParentHeightPercent(bottom));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIListView setUISize(int width, int height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIListView setUIWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public MiniUIListView setUIHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public MiniUIListView setUISize(double width, double height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIListView setUIWidth(double width) {
		setUIWidth(getParentWidthPercent(width));
		return this;
	}

	public MiniUIListView setUIHeight(double height) {
		setUIHeight(getParentHeightPercent(height));
		return this;
	}

	public MiniUIListView setUISizeKeepCenter(int width, int height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIListView setUIWidthKeepCenter(int width) {
		int x = GetCenterX();
		setRight(getLeft() + width);
		setUICenterX(x);
		return this;
	}

	public MiniUIListView setUIHeightKeepCenter(int height) {
		int y = GetCenterY();
		setBottom(getTop() + height);
		setUICenterY(y);
		return this;
	}

	public MiniUIListView setUISizeKeepCenter(double width, double height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIListView setUIWidthKeepCenter(double width) {
		setUIWidthKeepCenter(getParentWidthPercent(width));
		return this;
	}

	public MiniUIListView setUIHeightKeepCenter(double height) {
		setUIHeightKeepCenter(getParentHeightPercent(height));
		return this;
	}

	public MiniUIListView ScaleSize(double scale) {
		int width = (int) (getWidth() * scale);
		int height = (int) (getHeight() * scale);
		setUISize(width, height);
		return this;
	}

	public MiniUIListView setUIDesignSizeScale(int width, int height) {
		float scaleX = (float) MiniUIScreen.width() / MiniUIScreen.width();
		float scaleY = (float) MiniUIScreen.height() / MiniUIScreen.height();
		float scale = Math.min(scaleX, scaleY);
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIListView setUIDesignSizeScaleX(int width, int height) {
		float scale = (float) MiniUIScreen.width() / MiniUIScreen.width();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIListView setUIDesignSizeScaleY(int width, int height) {
		float scale = (float) MiniUIScreen.height() / MiniUIScreen.height();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIListView setUIBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public MiniUIListView setUITag(int tag) {
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

	public MiniUIListView setUIFillet(int width) {
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

	public MiniUIListView setUIBorder(int cornerRadius, int color) {
		return setUIBorder(0, cornerRadius, color);
	}

	public MiniUIListView setUIBorder(int borderWidth, int cornerRadius, int color) {
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
	public MiniUIListView aid() {
		new MiniUIAid(this);
		return this;
	}

	public MiniUIListView CopyPositionFrom(View view) {
		setUIPosition(view.getLeft(), view.getTop());
		return this;
	}

	public MiniUIListView CopyCenterFrom(View view) {
		setUICenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
		return this;
	}

	public MiniUIListView CopySizeFrom(View view) {
		setUISize(view.getWidth(), view.getHeight());
		return this;
	}

	public MiniUIListView CopyFrameFrom(View view) {
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
	
// extension_view_end
}
