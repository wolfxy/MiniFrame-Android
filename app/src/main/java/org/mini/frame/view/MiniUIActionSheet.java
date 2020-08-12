package org.mini.frame.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;

import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.annotation.MiniUIAction;

import java.util.ArrayList;
import java.util.List;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_SP;

/**
 * Created by Wuquancheng on 2018/8/19.
 */

public class MiniUIActionSheet extends MiniUIViewGroup {

    public interface MiniUIActionSheetCallback {
        void onItemClicked(int index);
    }

    private MiniUIActionSheetCallback callback;
    public List<String> items = new ArrayList<String>();
    public List<MiniUITextViewButton> buttonList = new ArrayList<MiniUITextViewButton>();
    private MiniUITextViewButton cancelButton;
    private MiniUIActivity activity;
    private MiniUIViewGroup maskView = null;
    public MiniUIActionSheet(Context context) {
        super(context);
        cancelButton = new MiniUITextViewButton(context);
        cancelButton.setText("取 消");
        cancelButton.setTextSize(DP_2_SP(18));
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setTargetAction(this, "dismiss");
        cancelButton.setBackgroundColor(Color.WHITE);
        cancelButton.setTextColor(Color.BLUE);
        cancelButton.setCorner(DP_2_PX(6), Color.WHITE);
        this.addView(cancelButton);
        this.setClickable(true);
        maskView = new MiniUIViewGroup(context);
        maskView.setClickable(true);
        //maskView.setTargetAction(this, "dismiss");
        maskView.setBackgroundColor(Color.argb(20,0,0,0));
    }

    public MiniUIActionSheet addText(String text) {
        items.add(text);
        return this;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public int layout() {
        int left = DP_2_PX(20);
        int gap = left/5;
        int top = left;
        int itemHeight = DP_2_PX(44);
        int right = this.getWidth() - left;
        for (MiniUITextViewButton button : buttonList) {
            button.layout(left, top, right, top + itemHeight);
            top = button.getBottom() + gap;
        }
        top = top + left/2;
        cancelButton.layout(left, top, right, top + itemHeight);
        top = cancelButton.getBottom() + left;
        return top;
    }

    private void initSubViews() {
        int textSize = DP_2_SP(18);
        int textColor = MiniUIColor.color("#555555");
        int index = 0;
        for(String item : items) {
            MiniUITextViewButton button = new MiniUITextViewButton(this.getContext());
            button.setUserInfo(String.valueOf(index));
            button.setTextSize(textSize);
            button.setTextColor(textColor);
            button.setText(item);
            button.setGravity(Gravity.CENTER);
            button.setBackgroundColor(Color.WHITE);
            button.setCorner(DP_2_PX(6), Color.WHITE);
            this.addView(button);
            buttonList.add(button);
            button.setTargetAction(this, "handleButtonTap:");
            index++;
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showInActivity(MiniUIActivity activity, MiniUIActionSheetCallback callback){
        this.callback = callback;
        this.activity = activity;
        ViewGroup contentView = activity.getContentView();
        initSubViews();
        this.setLeft(0);
        this.setRight(contentView.getWidth());
        int height = layout();
        maskView.setFrame(0,0, contentView.getWidth(), contentView.getHeight());
        //maskView.setZ(999);
        contentView.addView(maskView);
        this.setFrame(0, maskView.getHeight() - height, maskView.getWidth(), maskView.getHeight());
        maskView.addView(this);
    }

    @MiniUIAction
    public void handleButtonTap(MiniUITextViewButton button) {
        String index = (String)button.getUserInfo();
        if (this.callback != null) {
            this.callback.onItemClicked(Integer.parseInt(index));
        }
        dismiss();
    }

    @MiniUIAction
    public void dismiss() {
        maskView.removeFromSuperView();
    }
}
