package org.mini.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;

/**
 * Created by YXL on 2015/12/8.
 */
public class MiniUIView extends View implements MiniUIViewProtocol
{

    private int backgroundColor = MiniUIColor.color("#00FFFFFF");
    private int defaultBackGroundColor = backgroundColor;
    private int borderWidth = 0;
    private int borderColor = MiniUIColor.color("#00FFFFFF");
    private int cornerRadius = 0;

	public MiniUIView(Context context) {
		super(context);
	}

	public MiniUIView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MiniUIView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIView setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
		return this;
	}

	public MiniUIView setWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public MiniUIView setHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public MiniUIView setWidth(double width) {
		setWidth(getParentWidthPercent(width));
		return this;
	}

	public MiniUIView setHeight(double height) {
		setHeight(getParentHeightPercent(height));
		return this;
	}



	public void removeFromSuperView() {
		if (getParent() != null && getParent() instanceof ViewGroup) {
			invalidate();//解决移除后有残影的问题
			((ViewGroup) getParent()).removeView(this);
		}
	}

	public MiniUIView setCorner(int cornerRadius, int color) {
		return setCorner(0, cornerRadius, color);
	}

	public MiniUIView setCorner(int borderWidth, int cornerRadius, int color) {
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getDefaultBackGroundColor() {
        return defaultBackGroundColor;
    }

    public void setDefaultBackGroundColor(int defaultBackGroundColor) {
        this.defaultBackGroundColor = defaultBackGroundColor;
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

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void onDraw(Canvas canvas) {
        Paint paint = null;
        if (backgroundColor != defaultBackGroundColor && borderWidth == 0 && cornerRadius == 0) {
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(backgroundColor);
            canvas.drawRect(new Rect(0, 0, this.getWidth(), this.getHeight()),paint);
        }
        if (borderWidth > 0 || cornerRadius > 0) {
            if (paint == null) {
                paint = new Paint();
            }
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
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
