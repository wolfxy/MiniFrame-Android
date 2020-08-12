package org.mini.frame.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;

import org.mini.frame.toolkit.MiniUIColor;

import static org.mini.frame.application.MiniApplication.IS_PAD;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_SP;
import static org.mini.frame.uitools.MiniDisplayUtil.PX_2_SP;

/**
 * Created by Wuquancheng on 2018/8/17.
 */

public class MiniDefaultCellView extends MiniUIViewGroup{

    private MiniUITextView titleView;
    private MiniUITextView valueView;
    private MiniUIImageView accessoryImageView;

    private MiniUIView separatorLine;

    private int accessoryResId;

    public MiniDefaultCellView(Context context) {
        super(context);
        titleView = new MiniUITextView(context);
        titleView.setTextColor(MiniUIColor.color("#999999"));
        titleView.setGravity(Gravity.CENTER | Gravity.LEFT);
        this.addView(titleView);

        valueView = new MiniUITextView(context);
        valueView.setTextColor(MiniUIColor.color("#999999"));
        valueView.setGravity(Gravity.CENTER | Gravity.RIGHT);
        this.addView(valueView);

        separatorLine = new MiniUIView(context);
        separatorLine.setBackgroundColor(MiniUIColor.color("#DDDDDD"));
        this.addView(separatorLine);

        accessoryImageView = new MiniUIImageView(context);
        accessoryImageView.setVisibility(GONE);
        this.addView(accessoryImageView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int padding = DP_2_PX(20);
        int left = padding;
        int textFontSize = DP_2_SP(IS_PAD?20:16);
        int  accessorySize = (int)(this.getHeight()*0.5);
        int textWidth = this.getWidth()/2;
        titleView.setTextSize(textFontSize);
        titleView.setLeft(0);
        titleView.setRight(textWidth);
        titleView.sizeToFit();
        int top = (this.getHeight() - titleView.getHeight())/2;
        titleView.layout(left ,top, this.getWidth()/2, top + titleView.getHeight());
        left = titleView.getRight();
        valueView.setTextSize(textFontSize);
        valueView.layout(left, top, this.getWidth() - padding - accessorySize , titleView.getBottom());
        separatorLine.layout(padding, this.getHeight() - 2, this.getWidth() - padding, this.getHeight());
        if (accessoryImageView.getVisibility() == VISIBLE) {
            left = this.getWidth() - padding - accessorySize + DP_2_PX(8);
            top = (this.getHeight() - accessorySize) / 2;
            accessoryImageView.layout(left, top, left + accessorySize, top + accessorySize);
        }
    }

    public void setTitleValue(String title, String value) {
        setTitle(title);
        setValue(value);
    }

    public int getAccessoryResId() {
        return accessoryResId;
    }

    public void setAccessoryResId(int accessoryResId) {
        this.accessoryResId = accessoryResId;
        if (accessoryResId > 0) {
            accessoryImageView.setDrawableId(accessoryResId);
            accessoryImageView.setVisibility(VISIBLE);
        }
        else {
            accessoryImageView.setVisibility(GONE);
        }
    }

    public void hidAccessoryView(boolean hide) {
        accessoryImageView.setVisibility(hide?GONE:VISIBLE);
    }

    public void setTitle(String title) {
        titleView.setText(title == null?"":title);
    }

    public void setValue(String value) {
        valueView.setText(value==null?"":value);
    }

}
