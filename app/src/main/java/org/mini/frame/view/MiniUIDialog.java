package org.mini.frame.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.uitools.MiniUIScreen;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;

/**
 * Created by Wuquancheng on 2017/12/10.
 */

public class MiniUIDialog extends AlertDialog implements View.OnClickListener {

    public interface MiniDialogCallback {
        void onOkButtonClicked();
        void onCancelButtonClicked();
    }

    MiniUITextView cancelButton;
    MiniUITextView okButton;
    private MiniDialogCallback callback;

    public MiniUIDialog(Context context) {
        super(context);
    }

    public MiniUIDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MiniUIDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private String title;
    private String message;
    private String okButtonTitle;
    private String cancelButtonTitle;
    public void set(String title, String message, String okButton, String cancelButton) {
        this.title = title;
        this.message = message;
        this.okButtonTitle = okButton;
        this.cancelButtonTitle = cancelButton;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showContent() {
        this.setCanceledOnTouchOutside(false);
        if (this.title == null) {
            this.title = "";
        }
        if (this.message == null) {
            this.message = "";
        }
        MiniUIViewGroup group = new MiniUIViewGroup(this.getContext());
        Window window = this.getWindow();
        int width;
        if (MiniUIScreen.width() < MiniUIScreen.height()) {
            width = (int)(MiniUIScreen.width()*0.8);
        }
        else {
            width = MiniUIScreen.width()/2;
        }
        int height = MiniUIScreen.height()/2;
        if (height > width) {
            height = (int)(width * 0.6);
        }
        if (height < 350) {
            height = 350;
        }
        if (height > 800) {
            height = 800;
        }
        View decorView = window.getDecorView();
        int paddingLeft = decorView.getPaddingLeft();
        int paddingTop = decorView.getPaddingTop();
        int paddingRight = decorView.getPaddingRight();
        int paddingBottom = decorView.getPaddingBottom();
        //window.setLayout(width + paddingLeft + paddingRight, height + paddingTop + paddingBottom);
        window.setContentView(group);
        //group.setUISize(width, height);
        group.setBackgroundColor(Color.RED);

        int titleHeight = 100;
        MiniUITextView titleView = new MiniUITextView(this.getContext());
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(16);
        titleView.setUISize(width, titleHeight);
        titleView.setUIPosition(0,0);
        titleView.setUIBackgroundColor(titleBackgroundColor());
        titleView.setPadding(0,0,0,0);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(titleTextColor());
        group.AddView(titleView);

        int bottomButtonHeight = 140;
        int buttonTop = 10;
        MiniUIViewGroup bottomView = new MiniUIViewGroup(this.getContext());
        bottomView.setBackgroundColor(Color.WHITE);
        bottomView.setSize(width, bottomButtonHeight);
        bottomView.setPosition(0, height - bottomButtonHeight);
        group. AddView(bottomView);
        int gap = 30;
        int buttonWidth = (width - 3*gap)/2;
        titleView = new MiniUITextView(this.getContext());
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(16);
        titleView.setText(this.cancelButtonTitle);
        cancelButton = bottomView. AddView(titleView
        ).setUISize(buttonWidth , bottomButtonHeight - gap - buttonTop)
                .setUIBackgroundColor(Color.GRAY)
                .setUIPosition(gap,buttonTop)
                .setUIGravity(Gravity.CENTER)
                ;
        cancelButton.setUIBorder(10, cancelButtonBackgroundColor());
        cancelButton.setOnClickListener(this);

        titleView = new MiniUITextView(this.getContext());
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(16);
        titleView.setText(this.okButtonTitle);
        okButton = bottomView. AddView(titleView
        ).setUISize(buttonWidth, bottomButtonHeight - gap - buttonTop)
                .setUIPosition(width/2 + gap/2, buttonTop)
                .setUIGravity(Gravity.CENTER)
                ;
        okButton.setUIBorder(10, okButtonBackgroundColor());
        okButton.setOnClickListener(this);

        int contentHeight = height - titleHeight - bottomButtonHeight;
        MiniUIScrollView contentView = new MiniUIScrollView(this.getContext());
        contentView.setBackgroundColor(contentBackgroundColor());
        contentView.setUISize(width, contentHeight);
        contentView.setUIPosition(0, titleHeight);
        group. AddView(contentView);

        int padding = DP_2_PX(20);
        contentView.setPadding(padding,padding,padding,padding);
        MiniUITextView contentTextView = new MiniUITextView(this.getContext());
        contentTextView.setTextColor(Color.WHITE);
        contentTextView.setTextSize(16);
        contentTextView.setText(this.message);

        contentTextView.setBackgroundColor(contentBackgroundColor());
        contentTextView.setSingleLine(false);
        contentTextView.setRight(width-2*padding);
        contentTextView.setWidth(width-2*padding);
        contentTextView.setGravity(Gravity.CENTER);
        contentTextView.sizeToFit();
        contentTextView.setUIPosition(padding,padding);
        contentHeight = contentTextView.getHeight() + 2*padding;
        contentView.setUISize(width, contentHeight);
        contentView.addView(contentTextView);
        height = titleHeight + contentHeight + bottomButtonHeight;
        bottomView.setPosition(0, height - bottomButtonHeight);
        group.setSize(width, height);
        window.setLayout(width + paddingLeft + paddingRight, height + paddingTop + paddingBottom);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show(String title, String message, String okButton, MiniDialogCallback callback) {
        show(title, message, okButton, "取消", callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show(String title, String message, String okButton, String cancelButton) {
        show(title, message, okButton, cancelButton, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show(String title, String message, String okButton, String cancelButton, MiniDialogCallback callback) {
        set(title, message, okButton, cancelButton);
        this.callback = callback;
        super.show();
        showContent();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show() {
        super.show();
        showContent();
    }

    @Override
    public void onClick(View v) {
        MiniUIActivity.playViewClickedSound(v);
        if (v.equals(okButton)) {
            if (this.callback != null) {
                this.dismiss();
                this.callback.onOkButtonClicked();
            }
        }
        else if (v.equals(this.cancelButton)) {
            if (this.callback != null) {
                this.dismiss();
                this.callback.onCancelButtonClicked();
            }
        }
    }

    public  MiniUIDialog setCallback(MiniDialogCallback callback) {
        this.callback = callback;
        return this;
    }

    public MiniUIDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MiniUIDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MiniUIDialog setOkButtonTitle(String okButtonTitle) {
        this.okButtonTitle = okButtonTitle;
        return this;
    }

    public MiniUIDialog setCancelButtonTitle(String cancelButtonTitle) {
        this.cancelButtonTitle = cancelButtonTitle;
        return this;
    }

    protected int titleBackgroundColor() {
       return Color.DKGRAY;
    }

    protected int titleTextColor() {
        return Color.BLACK;
    }

    protected int contentTextColor() {
        return Color.BLACK;
    }

    protected int contentBackgroundColor() {
        return Color.parseColor("#F8F8F8");
    }


    protected int cancelButtonTitleColor() {
        return Color.WHITE;
    }

    protected int cancelButtonBackgroundColor() {
        return  Color.parseColor("#663333");
    }

    protected int okButtonTitleColor() {
        return Color.WHITE;
    }

    protected int okButtonBackgroundColor() {
        return  Color.parseColor("#F8F8F8");
    }
}
