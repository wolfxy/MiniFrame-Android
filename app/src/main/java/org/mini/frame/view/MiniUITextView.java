package org.mini.frame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.kit.Size;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIActionAssistant;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;

import java.lang.reflect.Field;

/**
 * Created by YXL on 2015/12/10.
 */
@SuppressLint("AppCompatCustomView")
public class MiniUITextView extends TextView implements MiniUIViewProtocol
{
	private Object userInfo;

	public MiniUITextView(Context context) {
		super(context);
	}

    public MiniUITextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	public MiniUITextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MiniUITextView setUIGravity(int gravity) {
		setGravity(gravity);
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
	public MiniUITextView setUIPosition(int x, int y) {
		setUIX(x);
		setUIY(y);
		return this;
	}

	public MiniUITextView setUIX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public MiniUITextView setUIY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public MiniUITextView setUIPosition(double x, double y) {
		setUIPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public MiniUITextView setUIX(double x) {
		setUIX(getParentWidthPercent(x));
		return this;
	}

	public MiniUITextView setUIY(double y) {
		setUIY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUITextView setUICenter(int x, int y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUITextView setUICenterX(int x) {
		setUIX(x - getWidth() / 2);
		return this;
	}

	public MiniUITextView setUICenterY(int y) {
		setUIY(y - getHeight() / 2);
		return this;
	}

	public MiniUITextView setUICenter(double x, double y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUITextView setUICenterX(double x) {
		setUICenterX(getParentWidthPercent(x));
		return this;
	}

	public MiniUITextView setUICenterY(double y) {
        setUICenterY(getParentHeightPercent(y));
        return this;

    }
	public MiniUITextView setUISize(int width, int height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUITextView setUIWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public MiniUITextView setUIHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public MiniUITextView setUISize(double width, double height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

    public MiniUITextView setUISize(double width, int height) {
        setUIWidth(width);
        setUIHeight(height);
        return this;
    }

    public MiniUITextView setUISize(int width, double height) {
        setUIWidth(width);
        setUIHeight(height);
        return this;
    }

	public MiniUITextView setUIWidth(double width) {
		setUIWidth(getParentWidthPercent(width));
		return this;
	}

	public MiniUITextView setUIHeight(double height) {
		setUIHeight(getParentHeightPercent(height));
		return this;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUITextView setUIBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public void removeFromSuperView() {
		if (getParent() instanceof ViewGroup) {
			invalidate();//解决移除后有残影的问题
			((ViewGroup) getParent()).removeView(this);
		}
	}


	public MiniUITextView setUIBorder(int cornerRadius, int color) {
		return setUIBorder(0, cornerRadius, color);
	}

	public MiniUITextView setUIBorder(int borderWidth, int cornerRadius, int color) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Size getLayoutHeight() {
        Layout.Alignment alignment  = Layout.Alignment.ALIGN_NORMAL;
        int textAlignment = this.getTextAlignment();
        if(textAlignment == View.TEXT_ALIGNMENT_CENTER) {
            alignment = Layout.Alignment.ALIGN_CENTER;
        }
        StaticLayout staticLayout = new StaticLayout(this.getText(),
                this.getPaint(), this.getWidth() , alignment, this.getLineSpacingMultiplier(), this.getLineSpacingExtra(), true);
        return new Size(staticLayout.getWidth(),staticLayout.getHeight());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Size sizeToFit() {
        if (this.getText() != null && this.getText().length() > 0 && this.getWidth() > 0) {
            Size size = this.getLayoutHeight();
            int height = size.getHeight();
            height = height + this.getPaddingTop() + this.getPaddingBottom();
            this.setUIHeight(height);
            return size;
        }
        else {
            return new Size(0,0);
        }
    }

    public int getTextWidth() {
        TextPaint paint = this.getPaint();
        float v = paint.measureText(this.getText().toString());
        if (v > (int)v){
            return (int)v + 1;
        }
        else {
            return (int)v;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void setTargetAction(Object target, String action) {
        MiniUIActionAssistant.setTargetAction(this,target,action);
    }


    public Object getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Object userInfo) {
        this.userInfo = userInfo;
    }
}
