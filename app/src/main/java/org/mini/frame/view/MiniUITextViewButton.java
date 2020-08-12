package org.mini.frame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIActionAssistant;
import org.mini.frame.uitools.MiniUIScreen;


/**
 * Created by YXL on 2015/12/10.
 */
@SuppressLint("AppCompatCustomView")
public class MiniUITextViewButton extends TextView implements MiniUIViewProtocol
{
	protected boolean useDownState = true;

	protected int musicId = -1;

	private int cornerRadius = 0;

	public Object userInfo;

	private Handler handler = new Handler();

	public MiniUITextViewButton(Context context) {
		super(context);
		this.setClickable(true);

	}

	public MiniUITextViewButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MiniUITextViewButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (useDownState && isEnabled()) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setAlpha(0.6f);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setAlpha(1f);
                    }
                },200);
			} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
				setAlpha(1f);
			}
		}
		boolean ret = super.onTouchEvent(event);
		return ret;
	}


    public static MiniUITextViewButton Create(Context context, String text, int tag, OnClickListener onClickListener) {
		return Create(context, text, 14, Color.WHITE, tag, onClickListener);
	}

	public static MiniUITextViewButton Create(Context context, String text, float textSize, int textColor, int tag, OnClickListener onClickListener) {
		MiniUITextViewButton v = new MiniUITextViewButton(context);
		v.setText(text);
		v.setTextSize(textSize);
		v.setTextColor(textColor);
		v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		v.setRight(v.getMeasuredWidth());
		v.setBottom(v.getMeasuredHeight());
		v.setGravity(Gravity.CENTER);
		v.setTag(tag);
		v.setOnClickListener(onClickListener);
		return v;
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
	public boolean performClick() {
		return super.performClick();
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
//		if (getParent() instanceof MiniUIViewGroup && widthMeasureSpec > 0 && heightMeasureSpec > 0)
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//		else
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	public void setVisibility(int visibility) {
		if(visibility != View.VISIBLE){
			invalidate();
		}
		super.setVisibility(visibility);
	}

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

    public void setUIFrame(int l, int t, int w, int h) {
        this.setLeft(l);
        this.setTop(t);
        this.setRight(l + w);
        this.setBottom(t + h);
    }

    public void setTargetAction(Object object, String action) {
        MiniUIActionAssistant.setTargetAction(this, object, action);
    }

    int bottomBorderWidth = 0;
	int bottomBorderColor = -1;
    public void setBottomBorderWidth(int bottomBorderWidth) {
	    this.bottomBorderWidth = bottomBorderWidth;
    }

    public void setBottomBorderColor(int bottomBorderColor) {
	    this.bottomBorderColor = bottomBorderColor;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.bottomBorderWidth > 0) {
            Paint paint = new Paint();
            paint.setStrokeWidth(this.bottomBorderWidth);
            paint.setColor(bottomBorderColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight(), paint);
        }
        if (this.cornerRadius > 0) {
            Path path = new Path();
            RectF r = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            path.addRoundRect(r,cornerRadius, cornerRadius, Path.Direction.CW);
            canvas.clipPath(path, Region.Op.REPLACE);
        }
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getTextWidth() {
        TextPaint paint = this.getPaint();
        float v = paint.measureText(this.getText().toString());
        if (v > (int)v){
            v =  (int)v + 1;
        }
        return (int)v + this.getPaddingLeft() + this.getPaddingRight();
    }
}
