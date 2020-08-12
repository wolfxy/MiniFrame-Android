package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;

public class MiniUILoadingView extends RelativeLayout {

    private MiniUIViewGroup waitingView = null;

    private  MiniUIGifView gifView = null;

    public MiniUILoadingView(Context context) {
        super(context);
        init();
    }

    public MiniUILoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MiniUILoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MiniUILoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init()
    {
        if (waitingView == null) {
            int waitingViewSize = DP_2_PX(60);
            int imageViewSize = DP_2_PX(30);
            waitingView = new MiniUIViewGroup(this.getContext());
            waitingView.setSize(waitingViewSize, waitingViewSize);
            waitingView.setCorner(20, Color.parseColor("#AA000000"));
            gifView = new MiniUIGifView(this.getContext());
            gifView.setUISize(imageViewSize,imageViewSize);
            gifView.setShowDimension(imageViewSize,imageViewSize);
            gifView.setUICenter(waitingView.getWidth()/2, waitingView.getHeight()/2);
            gifView.setTag(100000);
            waitingView.addView(gifView);
            MiniUITextView textView = new MiniUITextView(this.getContext());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTextColor(Color.WHITE);
            textView.setUISize(waitingView.getWidth(), 100);
            textView.setUIPosition(0,waitingView.getHeight() - 100);
            textView.setTag(100001);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0,0,0,0);
            waitingView. AddView(textView);
            this.addView(waitingView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)waitingView.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            this.setBackgroundColor(Color.argb(10, 0,0,0));
        }
    }

    public void setMessage(CharSequence message, int loadingImageResId)
    {
        gifView.setGifImage(loadingImageResId);
        if (message == null || message.length() == 0) {
            ((MiniUIGifView)waitingView.findViewWithTag(100000)).setUICenterY(waitingView.getHeight()/2);
            waitingView.findViewWithTag(100001).setVisibility(View.GONE);
        }
        else {
            ((MiniUIGifView)waitingView.findViewWithTag(100000)).setUICenterY(120);
            MiniUITextView textView = (MiniUITextView)waitingView.findViewWithTag(100001);
            textView.setVisibility(View.VISIBLE);
            textView.setText(message);
        }
    }
}
