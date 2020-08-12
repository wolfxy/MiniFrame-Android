package org.mini.frame.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;

import org.mini.frame.toolkit.MiniUIColor;

import static org.mini.frame.application.MiniApplication.IS_PAD;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_SP;

/**
 * Created by Wuquancheng on 2018/8/19.
 */

public class MiniUIOptionItemView extends MiniUIViewGroup{
    public enum OptionItemType{
        Radio,
        CheckBox,
        Text
    }

    protected OptionItemType itemType;

    protected boolean selected;

    protected MiniUIImageView typeIcon;
    protected int iconImageNormalResId;
    protected int iconImageSelectedResId;

    protected MiniUIView bottomLine;

    protected MiniUITextView textView;

    protected String value;

    private int iconSize = DP_2_PX(IS_PAD?26:20);

    public MiniUIOptionItemView(Context context) {
        super(context);
        typeIcon = new MiniUIImageView(context);
        this.addView(typeIcon);
        textView = new MiniUITextView(context);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setTextColor(MiniUIColor.color("#666666"));
        textView.setTextSize(DP_2_SP(IS_PAD?20:16));
        textView.setSingleLine(false);
        this.addView(textView);
        bottomLine = new MiniUIView(context);
        bottomLine.setBackgroundColor(MiniUIColor.color("#dddddd"));
        this.addView(bottomLine);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = DP_2_PX(10);
        int gap = top;
        typeIcon.layout(top, top, top + iconSize, top + iconSize);
        int left = typeIcon.getRight() + gap;
        textView.setLeft(left);
        textView.setTop(top);
        textView.setRight(this.getWidth() - gap);
        textView.sizeToFit();
        int height =  textView.getHeight() + 2*gap;
        left  = DP_2_PX(15);
        bottomLine.layout(left,height -1, this.getWidth()-left, height);
        this.setBottom(t + height);
    }

    public OptionItemType getItemType() {
        return itemType;
    }

    public void setItemType(OptionItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            typeIcon.resetDrawableId(this.iconImageSelectedResId);
        }
        else {
            typeIcon.resetDrawableId(this.iconImageNormalResId);
        }
    }

    public int getIconImageNormalResId() {
        return iconImageNormalResId;
    }

    public void setIconImageNormalResId(int iconImageNormalResId) {
        this.iconImageNormalResId = iconImageNormalResId;
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIconImageSelectedResId() {
        return iconImageSelectedResId;
    }

    public void setIconImageSelectedResId(int iconImageSelectedResId) {
        this.iconImageSelectedResId = iconImageSelectedResId;
    }

    public void setBottomLineHidden(boolean hidden) {
        this.bottomLine.setVisibility(hidden?GONE:VISIBLE);
    }
}
