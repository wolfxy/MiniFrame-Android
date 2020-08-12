package org.mini.frame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_SP;
import static org.mini.frame.uitools.MiniDisplayUtil.PX_2_SP;

/**
 * Created by Wuquancheng on 2018/8/15.
 */

public class MiniSegmentView extends MiniUIViewGroup implements View.OnClickListener{

    public interface ViewActionListener {
        void onClick(int index);
    }

    private List<MiniUITextViewButton> buttonList = new ArrayList<MiniUITextViewButton>();

    private int defaultColor;

    private int highlightColor;

    private MiniUIView borderView;

    private int borderColor;

    private ViewActionListener actionListener;

    private int selectedIndex = 0;

    public MiniSegmentView(Context context) {
        super(context);
        this.setWillNotDraw(false);
        borderView = new MiniUIView(context);
        this.addView(borderView);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        borderView.layout(0,0,this.getWidth(),this.getHeight());
        int left = DP_2_PX(1);
        int top = left;
        float fontSize = PX_2_SP(this.getHeight()/2);
        if (buttonList.size() > 0) {
            int width = (this.getWidth()-DP_2_PX(2)) / buttonList.size();
            int height = this.getHeight()-DP_2_PX(2);
            for (MiniUITextViewButton button : buttonList) {
                button.setTextSize(fontSize);
                button.layout(left, top, left + width, top+ height);
                left = button.getRight();
            }
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cornerRadius = DP_2_PX(4);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(this.borderColor);
        paint.setStrokeWidth(DP_2_PX(1));
        canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()),cornerRadius, cornerRadius,paint);
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()),cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(path, Region.Op.REPLACE);
    }


    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setItems(String[] items) {
        for (MiniUITextViewButton button : buttonList) {
            button.removeFromSuperView();
        }
        buttonList.clear();
        int index = 0;
        for (String item : items) {
            MiniUITextViewButton button = new MiniUITextViewButton(this.getContext());
            if (this.defaultColor != 0) {
                button.setBackgroundColor(this.getDefaultColor());
            }
            button.setUserInfo(String.valueOf(index));
            button.setTextSize(DP_2_SP(16));
            button.setCornerRadius(DP_2_PX(1));
            button.setText(item);
            button.setGravity(Gravity.CENTER);
            this.addView(button);
            buttonList.add(button);
            button.setOnClickListener(this);
            index++;
        }
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderView.setBackgroundColor(borderColor);
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
        for (int i = 0; i < buttonList.size(); i++) {
            MiniUITextViewButton button = buttonList.get(i);
            if (index == i) {
                button.setBackgroundColor(this.highlightColor);
                button.setTextColor(this.defaultColor);
            }
            else {
                button.setBackgroundColor(this.defaultColor);
                button.setTextColor(this.highlightColor);
            }
        }
        if (this.actionListener != null) {
            this.actionListener.onClick(index);
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof MiniUITextViewButton) {
            String index = (String)((MiniUITextViewButton)(v)).getUserInfo();
            setSelectedIndex(Integer.parseInt(index));
        }
    }

    public void setActionListener(ViewActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
