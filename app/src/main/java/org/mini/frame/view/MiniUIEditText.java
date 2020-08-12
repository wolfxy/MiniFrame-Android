package org.mini.frame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mini.frame.R;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;

/**
 * Created by YXL on 2015/12/11.
 */
public class MiniUIEditText extends android.support.v7.widget.AppCompatEditText implements  View.OnFocusChangeListener {

    private Object userInfo;

    public MiniUIEditText(Context context){
		super(context);
        setOnFocusChangeListener(this);
    }

    public MiniUIEditText(Context context, AttributeSet attrs) {
        //, android.R.attr.editTextStyle
       super(context, attrs);
        setOnFocusChangeListener(this);
    }

    public MiniUIEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnFocusChangeListener(this);
    }

	public void removeFromSuperView() {
		if (getParent() instanceof ViewGroup) {
			invalidate();//解决移除后有残影的问题
			((ViewGroup) getParent()).removeView(this);
		}
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
}