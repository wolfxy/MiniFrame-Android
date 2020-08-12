package org.mini.frame.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.annotation.MiniUIAction;
import org.mini.frame.uitools.MiniUIActionAssistant;

import java.util.ArrayList;
import java.util.List;

import static org.mini.frame.application.MiniApplication.IS_PAD;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;

public class MiniUIPopMenuView extends MiniUIViewGroup
{
    public static int locationLeft = 1;
    public static int locationRight = 2;
    private int padding = DP_2_PX(10);
    private int location = locationLeft;
    private List<View> buttons = new ArrayList<>();
    private MiniUIView maskView;
    public MiniUIPopMenuView(Context context) {
        super(context);
        this.setClickable(true);
        this.setCorner(DP_2_PX(4), MiniUIColor.color("EFEFEF"));
        this.initView();
    }

    public MiniUIPopMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setClickable(true);
        this.initView();
    }

    public MiniUIPopMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
        this.initView();
    }

    public void onLayout(boolean changed, int l, int t, int r, int b)
    {
        this.doLayout(l,t,r,b);
    }

    private void doLayout( int l, int t, int r, int b) {
        int padding = DP_2_PX(10);
        int left = 2*padding;
        int buttonHeight =  DP_2_PX(40);
        int top = padding;
        for(View view : buttons) {
            view.layout(left, top, this.getWidth() - left, top + buttonHeight);
            top = view.getBottom();
        }
    }

    private void initView()
    {
        maskView = new MiniUIView(this.getContext());
       // maskView.setClickable(true);
        maskView.setBackgroundColor(Color.BLACK);
        maskView.setAlpha((float) 0.3);
        MiniUIActionAssistant.setTargetAction(maskView,this,"onMaskViewClicked:");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setButtons(List<View> buttons)
    {
        if (this.buttons != null && this.buttons.size() > 0) {
            for(View v : this.buttons) {
                ((MiniUITextView)v).removeFromSuperView();
            }
            this.buttons.clear();
        }
        this.buttons.addAll(buttons);
        int maxWidth = 0;
        int fontSize = DP_2_PX(IS_PAD?20:14);
        for(View view : this.buttons) {
            if (view instanceof MiniUITextView) {
                MiniUITextView textView = (MiniUITextView)view;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                textView.setTextColor(MiniUIColor.color("#4D4D4D"));
                int viewWidth = ((MiniUITextView)view).getTextWidth();
                if (viewWidth > maxWidth) {
                    maxWidth = viewWidth;
                }
                ((MiniUITextView)view).setUIWidth(viewWidth);
            }
            this.addView(view);
        }
        this.setWidth(maxWidth + 4*padding);
        this.setHeight(this.buttons.size() * DP_2_PX(40) + 2*padding);
    }

    public void show(ViewGroup parent)
    {
        int left = DP_2_PX(4);
        int translationY = left;
        this.setVisibility(VISIBLE);
        this.maskView.setAlpha(0);
        this.maskView.setVisibility(VISIBLE);
        if (this.location == locationRight) {
            left = this.getWidth() - left - this.getWidth();
        }
        if (this.getParent() == null) {
            parent.addView(this.maskView);
            parent.addView(this);
        }
        this.maskView.layout(0,0, parent.getWidth(), parent.getHeight());
        this.layout(left, 0, this.getWidth() + left, this.getHeight());
        this.doLayout(left, 0, this.getWidth() + left, this.getHeight());
        this.setTranslationY(-this.getHeight());
        ObjectAnimator
                .ofFloat(this.maskView, "alpha", 0f, 0.2f)
                .setDuration(200).start();
        ObjectAnimator.ofFloat(this, "translationY", translationY)
                .setDuration(200).start();
    }

    @MiniUIAction
    public void onMaskViewClicked(MiniUIView view)
    {
        dismiss(null);
    }

    public void dismiss(ViewGroup parent)
    {
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofFloat(this.maskView, "alpha", 0.2f, 0)
                .setDuration(200);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                maskView.setVisibility(GONE);
                MiniUIPopMenuView.this.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                maskView.setVisibility(GONE);
                MiniUIPopMenuView.this.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        ObjectAnimator.ofFloat(this, "translationY", -this.getHeight())
                .setDuration(200).start();
        objectAnimator.start();

    }

    public void toggle(ViewGroup parent)
    {
        if (this.getVisibility() == GONE) {
            this.show(parent);
        }
        else {
            this.dismiss(parent);
        }
    }
    public void setLocation(int location) {
        this.location = location;
    }
}
