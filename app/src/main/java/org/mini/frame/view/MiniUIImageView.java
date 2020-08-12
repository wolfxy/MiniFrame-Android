package org.mini.frame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.mini.frame.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.protocol.MiniUIViewProtocol;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.frame.tools.MiniToolKit;
import org.mini.frame.uitools.MiniUIAid;
import org.mini.frame.uitools.MiniUIActionAssistant;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;

/**
 * Created by YXL on 2015/12/10.
 */
@SuppressLint("AppCompatCustomView")
public class MiniUIImageView extends ImageView implements MiniUIViewProtocol
{
    float cornerRadius = 0;

    private Object userInfo;

	public MiniUIImageView(Context context) {
		super(context);
        this.setScaleType(ScaleType.FIT_XY);
	}

	public MiniUIImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MiniUIImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MiniUIImageView);
        float cornerRadius = typedArray.getDimension(R.styleable.MiniUIImageView_cornerRadius, 0);
        if (cornerRadius > 0) {
            setCornerRadius(cornerRadius);
        }
	}

	public static MiniUIImageView Create(Context context, int resId) {
		return Create(context, resId, ScaleType.FIT_XY);
	}

	public static MiniUIImageView Create(Context context, int resId, ScaleType scaleType) {
		MiniUIImageView iv = new MiniUIImageView(context);
		iv.setScaleType(scaleType);
		if(resId > 0)
			iv.setImageResource(resId);
		iv.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		iv.setRight(iv.getMeasuredWidth());
		iv.setBottom(iv.getMeasuredHeight());
		return iv;
	}

	public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void fixToWidth(int width) {
	    int h = getHeight();
	    int w = getWidth();
        int height = (int)(((float)h/w)*width);
        setLeft(0);
        setRight(width);
        setTop(0);
        setBottom(height);
    }

    public void fixToHeight(int height) {
        int width = (int)(((float)getWidth()/getHeight())*height);
        setLeft(0);
        setRight(width);
        setTop(0);
        setBottom(height);
    }

    public void fixToSize(int height, int width) {
        int _width = (int)(((float)getWidth()/getHeight())*height);
        if (_width > width) {
            height = (int)(height * ((float)width/_width));
            _width = width;
        }
        setLeft(0);
        setRight(_width);
        setTop(0);
        setBottom(height);
    }

	public static MiniUIImageView Create(Context context, Bitmap bitmap) {
		return Create(context, bitmap, ScaleType.CENTER);
	}

	private int drawableId = 0;
	public void setDrawableId(int drawableId) {
	    if (drawableId == 0) {
            setImageDrawable(null);
        }
        else {
	        if (this.drawableId != drawableId) {
                setImageDrawable(getResources().getDrawable(drawableId));
                this.drawableId = drawableId;
            }
        }
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setRight(getMeasuredWidth());
        setBottom(getMeasuredHeight());
    }

    public void resetDrawableId(int drawableId) {
        if (drawableId == 0) {
            setImageDrawable(null);
        }
        else {
            setImageDrawable(getResources().getDrawable(drawableId));
        }
    }

	public static MiniUIImageView Create(Context context, Bitmap bitmap, ScaleType scaleType) {
		MiniUIImageView iv = new MiniUIImageView(context);
		iv.setScaleType(scaleType);
		iv.setImageBitmap(bitmap);
		iv.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		iv.setRight(iv.getMeasuredWidth());
		iv.setBottom(iv.getMeasuredHeight());
		return iv;
	}

	public MiniUIImageView setUIScaleType(ScaleType scaleType) {
		setScaleType(scaleType);
		return this;
	}

    public void setUIFrame(int l, int t, int w, int h) {
        this.setFrame(l, t, l+w, t + h);
    }

	public MiniUIImageView setImageUrl(String url) {
		return setImageUrl(url, 0, 0, 0);
	}

	public MiniUIImageView setImageUrl(String url, int showImageOnLoading) {
		return setImageUrl(url, showImageOnLoading, 0, 0);
	}

	public MiniUIImageView setImageUrl(String url, int showImageOnLoading, int showImageOnFail) {
		return setImageUrl(url, showImageOnLoading, showImageOnFail, 0);
	}

	public MiniUIImageView setImageUrl(String url, int showImageOnLoading, int showImageOnFail, int roundedWidth) {
		DisplayImageOptions.Builder b = new DisplayImageOptions.Builder();
		if (showImageOnLoading > 0)
			b.showImageOnLoading(showImageOnLoading);
		if (showImageOnFail > 0)
			b.showImageOnFail(showImageOnFail);
//		if (roundedWidth == getWidth() / 2)
//			b.displayer(new CircleBitmapDisplayer());
//		else if (roundedWidth > 0)
        if (roundedWidth > 0) {
            b.displayer(new RoundedBitmapDisplayer(roundedWidth));
        }
		b.cacheOnDisk(true);
		b.cacheInMemory(false);
		ImageLoader.getInstance().displayImage(url, this, b.build());
		return this;
	}

    protected void onDraw(Canvas canvas) {
	    if (this.cornerRadius != 0) {
            Path path = new Path();
            Rect rect = new Rect(0, 0, getWidth(), getHeight());
            RectF rectF = new RectF(rect);
            path.addRoundRect(rectF, this.cornerRadius, this.cornerRadius, Path.Direction.CCW);
           // if(Build.VERSION.SDK_INT >= 26){
                canvas.clipPath(path);
            //}else {
            //    canvas.clipPath(path, Region.Op.REPLACE);
            //}
        }
        super.onDraw(canvas);
    }

	// extension_view
	public MiniUIImageView setUIAlpha(float alpha) {
		setAlpha(alpha);
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
	public MiniUIImageView setUIPosition(int x, int y) {
		setUIX(x);
		setUIY(y);
		return this;
	}

	public MiniUIImageView setUIX(int x) {
		int w = getWidth();
		setLeft(x);
		setRight(x + w);
		return this;
	}

	public MiniUIImageView setUIY(int y) {
		int h = getHeight();
		setTop(y);
		setBottom(y + h);
		return this;
	}

	public MiniUIImageView setUIPosition(double x, double y) {
		setUIPosition(getParentWidthPercent(x), getParentHeightPercent(y));
		return this;
	}

	public MiniUIImageView setUIX(double x) {
		setUIX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIImageView setUIY(double y) {
		setUIY(getParentHeightPercent(y));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIImageView setUICenter(int x, int y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIImageView setUICenterX(int x) {
		setUIX(x - getWidth() / 2);
		return this;
	}

	public MiniUIImageView setUICenterY(int y) {
		setUIY(y - getHeight() / 2);
		return this;
	}

	public MiniUIImageView setUICenter(double x, double y) {
		setUICenterX(x);
		setUICenterY(y);
		return this;
	}

	public MiniUIImageView setUICenterX(double x) {
		setUICenterX(getParentWidthPercent(x));
		return this;
	}

	public MiniUIImageView setUICenterY(double y) {
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

	public MiniUIImageView setUIRight(int right) {
		int w = getWidth();
		setRight(right);
		setLeft(right - w);
		return this;
	}

	public MiniUIImageView setUIBottom(int bottom) {
		int h = getHeight();
		setBottom(bottom);
		setTop(bottom - h);
		return this;
	}

	public MiniUIImageView setUIRight(double right) {
		return setUIRight(getParentWidthPercent(right));
	}

	public MiniUIImageView setUIBottom(double bottom) {
		return setUIBottom(getParentHeightPercent(bottom));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MiniUIImageView setUILeft(int left) {
        int w = getWidth();
        setLeft(left);
        setRight(left + w);
        return this;
    }

	public MiniUIImageView setUISize(int width, int height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIImageView setUIWidth(int width) {
		setRight(getLeft() + width);
		return this;
	}

	public MiniUIImageView setUIHeight(int height) {
		setBottom(getTop() + height);
		return this;
	}

	public MiniUIImageView setUISize(double width, double height) {
		setUIWidth(width);
		setUIHeight(height);
		return this;
	}

	public MiniUIImageView setUIWidth(double width) {
		setUIWidth(getParentWidthPercent(width));
		return this;
	}

	public MiniUIImageView setUIHeight(double height) {
		setUIHeight(getParentHeightPercent(height));
		return this;
	}

	public MiniUIImageView setUISizeKeepCenter(int width, int height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIImageView setUIWidthKeepCenter(int width) {
		int x = GetCenterX();
		setRight(getLeft() + width);
		setUICenterX(x);
		return this;
	}

	public MiniUIImageView setUIHeightKeepCenter(int height) {
		int y = GetCenterY();
		setBottom(getTop() + height);
		setUICenterY(y);
		return this;
	}

	public MiniUIImageView setUISizeKeepCenter(double width, double height) {
		setUIWidthKeepCenter(width);
		setUIHeightKeepCenter(height);
		return this;
	}

	public MiniUIImageView setUIWidthKeepCenter(double width) {
		setUIWidthKeepCenter(getParentWidthPercent(width));
		return this;
	}

	public MiniUIImageView setUIHeightKeepCenter(double height) {
		setUIHeightKeepCenter(getParentHeightPercent(height));
		return this;
	}

	public MiniUIImageView ScaleSize(double scale) {
		int width = (int) (getWidth() * scale);
		int height = (int) (getHeight() * scale);
		setUISize(width, height);
		return this;
	}

	public MiniUIImageView setUIDesignSizeScale(int width, int height) {
		float scaleX = (float) MiniUIScreen.width() / MiniUIScreen.width();
		float scaleY = (float) MiniUIScreen.height() / MiniUIScreen.height();
		float scale = Math.min(scaleX, scaleY);
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIImageView setUIDesignSizeScaleX(int width, int height) {
		float scale = (float) MiniUIScreen.width() / MiniUIScreen.width();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	public MiniUIImageView setUIDesignSizeScaleY(int width, int height) {
		float scale = (float) MiniUIScreen.height() / MiniUIScreen.height();
		setUISize((int) (width * scale), (int) (height * scale));
		return this;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public MiniUIImageView setUIBackgroundColor(int color) {
		setBackgroundColor(color);
		return this;
	}

	public MiniUIImageView setUITag(int tag) {
		setTag(tag);
		return this;
	}

	public int GetTag() {
		return MiniToolKit.parseInt(getTag());
	}

	public void removeFromSuperView() {
		if (getParent() != null && getParent() instanceof ViewGroup) {
			invalidate();//解决移除后有残影的问题
			((ViewGroup) getParent()).removeView(this);
		}
	}

	public MiniUIViewGroup GetParent() {
		return (MiniUIViewGroup) getParent();
	}

	public MiniUIImageView setUIFillet(int width) {
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

	public MiniUIImageView setUIBorder(int cornerRadius, int color) {
		return setUIBorder(0, cornerRadius, color);
	}

	public MiniUIImageView setUIBorder(int borderWidth, int cornerRadius, int color) {
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
	public MiniUIImageView aid() {
		new MiniUIAid(this);
		return this;
	}

	public MiniUIImageView CopyPositionFrom(View view) {
		setUIPosition(view.getLeft(), view.getTop());
		return this;
	}

	public MiniUIImageView CopyCenterFrom(View view) {
		setUICenter(view.getLeft() + view.getWidth() / 2, view.getTop() + view.getHeight() / 2);
		return this;
	}

	public MiniUIImageView CopySizeFrom(View view) {
		setUISize(view.getWidth(), view.getHeight());
		return this;
	}

	public MiniUIImageView CopyFrameFrom(View view) {
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

    public Object getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Object userInfo) {
        this.userInfo = userInfo;
    }
}
