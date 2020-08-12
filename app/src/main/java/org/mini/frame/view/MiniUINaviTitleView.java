package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.mini.frame.R;

import org.mini.frame.application.MiniConfiguration;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.uitools.MiniUIActionAssistant;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;
import static org.mini.frame.uitools.MiniDisplayUtil.SP_2_PX;

/**
 * Created by Wuquancheng on 2018/8/14.
 */

public class MiniUINaviTitleView extends ViewGroup {

    private final String TAG = MiniUINaviTitleView.class.getSimpleName();
    private MiniUITextView textView;

    private MiniUIViewGroup leftButton;
    private MiniUIViewGroup rightButton;

    private MiniUIImageView leftImageView;

    private int titleTextColor = Color.WHITE;
    private MiniUITextView rightTextButton;

    private int statusBarHeight = 0;

    private int titleFontSize =  SP_2_PX(20);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MiniUINaviTitleView(Context context) {
        super(context);
    }

    public MiniUINaviTitleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void init()
    {
        initSubViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void initSubViews() {
        textView = new MiniUITextView(this.getContext());
        textView.setIncludeFontPadding (false);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleFontSize);
        //textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//        textView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
//        textView.setEllipsize(TextUtils.TruncateAt.valueOf("START"));
        textView.setEllipsize(TextUtils.TruncateAt.valueOf("MIDDLE"));
//        textView.setEllipsize(TextUtils.TruncateAt.valueOf("MARQUEE"));
        textView.setLineSpacing(0, 1);
 //       textView.setSingleLine(true);
        //textView.setPadding(0,0,0,0);
        //textView.setBackgroundColor(Color.RED);
        textView.setMaxLines(1);
   //     textView.setBackgroundColor(Color.RED);

        leftButton = new MiniUIViewGroup(this.getContext());

        leftImageView = new MiniUIImageView(this.getContext());

        leftImageView.setDrawableId(getNaviLeftBackImageId());
        leftButton.addView(leftImageView);

        rightTextButton = new MiniUITextView(this.getContext());
        rightTextButton.setVisibility(GONE);
        rightTextButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rightTextButton.setGravity(Gravity.CENTER);
        this.addView(leftButton);
        this.addView(rightTextButton);
        this.addView(textView);
        layoutSubViews();
    }

    private int getNaviLeftBackImageId()
    {
        int leftButtonId = R.mipmap.arrow_left;
        if (MiniConfiguration.getGlobalUIDelegate() != null) {
            int id = MiniConfiguration.getGlobalUIDelegate().getNaviTitleViewBackImageId();
            if (id > 0) {
                leftButtonId = id;
            }
        }
        return leftButtonId;
    }

    void layoutSubViews()
    {
        int left = DP_2_PX(16);
        int padding = left;
        int top = getStatusBarHeight();
        int leftRightButtonWidth = DP_2_PX(50);
        if (leftButton != null) {
            leftButton.setLeft(left);
            leftButton.setTop(top);
            leftButton.setRight(left + leftRightButtonWidth);
            leftButton.setBottom(this.getHeight());
            leftButton.layout(left, top, left + leftRightButtonWidth, this.getHeight());
            left = leftButton.getRight();
        }
        if (leftImageView != null) {
            int leftImageHeight = (int)(leftButton.getHeight()*0.5);
            leftImageView.fixToHeight(leftImageHeight);
            int leftImageViewTop = (leftButton.getHeight() - leftImageView.getHeight())/2;
            leftImageView.setLeft(0);
            leftImageView.setTop(leftImageViewTop);
            leftImageView.setRight(leftImageView.getWidth());
            leftImageView.setBottom(leftImageViewTop + leftImageHeight);
            leftImageView.layout(0, leftImageViewTop, leftImageView.getWidth(), leftImageViewTop + leftImageHeight);
        }
        if (textView != null) {
            int textTop = getStatusBarHeight();
            textView.setLeft(left);
            textView.setRight(this.getWidth() - left);
            textView.setTop(textTop);
            textView.setBottom(this.getHeight());
            textView.layout(left, textTop , this.getWidth() - left , this.getHeight());
            textView.setPadding(0, (textView.getHeight() - titleFontSize)/2, 0, 0);
            left = textView.getRight();
        }
        if (rightTextButton != null) {
            rightTextButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (this.getHeight() * 0.4));
            rightTextButton.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
            rightTextButton.sizeToFit();
            rightTextButton.setLeft(left);
            rightTextButton.setTop(top);
            rightTextButton.setRight(this.getWidth() - padding);
            rightTextButton.setBottom(this.getHeight());
            rightTextButton.layout(left, top, this.getWidth() - padding, this.getHeight());
        }
        if (rightButton != null) {
            rightButton.setLeft(left);
            rightButton.setTop(top);
            rightButton.setRight(this.getWidth() - padding);
            rightButton.setBottom(this.getHeight());
            rightButton.layout(left, top, this.getWidth() - padding, this.getHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutSubViews();
    }


    public void setTitleColor(int color)
    {
        this.titleTextColor = color;
        textView.setTextColor(color);
    }

    public void setTitle(CharSequence title)
    {
        textView.setText(title);
        int textWidth =  textView.getTextWidth();
        int viewWidth = textView.getWidth();
//        if (textWidth < viewWidth) {
//            textView.setPadding(0, (textView.getHeight() - titleFontSize)/2, 0, 0);
//        }
//        else {
//            textView.setPadding(0, 0, 0, 0);
//        }
        MiniLogger.get().d("%s set title %s, text width: %d, text view width: %d", TAG, title, textWidth, viewWidth);
    }

    public void setLeftButtonImageId(int imageId) {
        if (leftImageView != null)
        leftImageView.setDrawableId(imageId);
    }

    public void hideNaviLeftButton() {
        if (leftButton != null)
        leftButton.setVisibility(GONE);
    }

    public void setLeftButtonTargetAction(Object object, String action) {
        MiniUIActionAssistant.setTargetAction(this.leftButton, object, action);
    }

    public void setRightButtonTitle(String buttonTitle, Object target, String action) {
        if (this.rightButton != null) {
            this.rightButton.removeFromSuperView();
            this.rightButton = null;
        }
        if (rightTextButton != null) {
            rightTextButton.setVisibility(VISIBLE);
            rightTextButton.setText(buttonTitle);
            rightTextButton.setTextColor(titleTextColor);
            rightTextButton.invalidate();
            rightTextButton.setTargetAction(target, action);
        }
    }

    public void hideRightButton() {
        if (rightTextButton != null) {
            rightTextButton.setVisibility(GONE);
            rightTextButton.setText("");
            rightTextButton.invalidate();
        }
        if (rightButton != null) {
            rightButton.setVisibility(GONE);
        }
    }

    public void setNaviLeftButton(MiniUIViewGroup leftButton) {
        if (this.leftButton != null) {
            this.leftButton.removeFromSuperView();
            this.leftButton = null;
            this.leftImageView = null;
        }
        this.leftButton = leftButton;
        if (this.leftButton != null) {
            this.addView(this.leftButton);
        }

        //this.layout(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
    }

    public void setNaviRightButton(MiniUIViewGroup rightButton)
    {
        if (this.rightTextButton != null) {
            this.rightTextButton.setVisibility(GONE);
        }
        if (this.rightButton != null) {
            this.rightButton.removeFromSuperView();
            this.rightButton = null;
        }
        this.rightButton = rightButton;
        if (this.rightButton != null) {
            this.addView(this.rightButton);
        }
    }

    public void setNeedLayout()
    {
        this.invalidate();
        this.requestLayout();
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }
}
