package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.uitools.MiniUIScreen;

import static org.mini.frame.uitools.MiniDisplayUtil.PX_2_DP;

/**
 * Created by Wuquancheng on 2017/12/20.
 */

public class MiniUIViewKeyboard extends MiniUIViewGroup implements View.OnClickListener{

    private EditText editText;

    public MiniUIViewKeyboard(Context context) {
        super(context);
        initSubViews();
    }

    public MiniUIViewKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSubViews();
    }

    public MiniUIViewKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSubViews();
    }

    private void initSubViews() {
        for (int i = 1; i <= 12; i++) {
            String v = null;
            if (i == 10) {
                v = "0";
            }
            else if (i == 11) {
                v  = "删除";
            }
            else if (i == 12) {
                v = "确定";
            }
            else {
                v = String.valueOf(i);
            }
            MiniUITextViewButton button = MiniUITextViewButton. Create(this.getContext(), v, 10000+i, this);
            button.disableKeySound();
            //one.setKeySound(((HFActivity)getContext()).loadSoundWithResId(R.raw.m_5888));
            this.addView(button);
        }
        setSize(MiniUIScreen.width(), 500);
        this.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        this.setPosition(0, MiniUIScreen.height() - this.getHeight());
        int buttonWidth = (MiniUIScreen.width() - 20) / 4;
        int buttonHeight = (this.getHeight() - 20)/3;
        int btnWidth = buttonWidth - 20;
        int btnHeight = buttonHeight - 20;
        int btnHeightDp = PX_2_DP(btnHeight);
        for(int i = 1; i <= 12; i++) {
            MiniUITextViewButton button = (MiniUITextViewButton)this.findViewWithTag(10000 + i);
            button.setSize(btnWidth, btnHeight);
            int fontSize = 0;
            if (i > 10) {
                fontSize = PX_2_DP((int)(btnHeight*0.40));
            }
            else {
                fontSize = PX_2_DP((int)(btnHeight*0.50));
            }
            button.setPaddingRelative(0,0,0,0);
            button.setPadding(0,(btnHeightDp-fontSize),0,0);
            button.setTextSize(fontSize);
            button.setTextColor(Color.BLACK);
            button.setCorner(20, Color.parseColor("#cccccc"));
            button.setGravity(Gravity.CENTER);

            int row = (i-1)/4;
            int col = (i-1)%4;
            button.setPosition(10 + col * buttonWidth + 10,10 + row * buttonHeight + 10);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getParent() instanceof MiniUIViewGroup && widthMeasureSpec > 0 && heightMeasureSpec > 0)
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof MiniUITextViewButton) {
            MiniUIActivity.playViewClickedSound(v);
            int tag = (Integer)(v.getTag()) - 10000;
            if ( tag == 12 ) {
                this.removeFromSuperView();
            }
            else {
                String textValue;
                if ( tag == 11 ) {
                    textValue = this.editText.getText().toString();
                    if (textValue.length() > 0) {
                        textValue = textValue.substring(0, textValue.length() - 1);
                    }
                }
                else {
                    int value = 0;
                    if (tag != 10) {
                        value = tag;
                    }
                    textValue = this.editText.getText().toString();
                    textValue += String.valueOf(value);
                }
                this.editText.setText(textValue);
                this.editText.setSelection(textValue.length());
            }
        }
    }


    public void setEditText(EditText editText) {
        this.editText = editText;
    }
}
