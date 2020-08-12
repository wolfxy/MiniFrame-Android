package org.mini.frame.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.uitools.MiniUIScreen;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;
import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_SP;

/**
 * Created by Wuquancheng on 2017/12/10.
 */

public class MiniUIDownloadDialog extends AlertDialog implements View.OnClickListener {


    MiniUITextView cancelButton;
    MiniUITextView okButton;
    MiniUIView progressbarView;
    MiniUIView progressBackgroundView;
    TextView titleView;
    private MiniUIDialog.MiniDialogCallback callback;

    public MiniUIDownloadDialog(Context context) {
        super(context);
    }

    public MiniUIDownloadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MiniUIDownloadDialog(Context context, int themeResId) {
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

        int titleHeight = 100;
        titleView = new MiniUITextView(this.getContext());
        titleView.setText(this.title);
        titleView.setTextSize(DP_2_SP(16));
        titleView.setBackgroundColor(titleBackgroundColor());
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(titleTextColor());
        titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleView.setLeft(0);
        titleView.setTop(0);
        titleView.setRight(width);
        titleView.setBottom(titleHeight);
        group. AddView(titleView);

        int bottomButtonHeight = 140;
        int buttonTop = 10;
        MiniUIViewGroup bottomView = new MiniUIViewGroup(this.getContext());
        bottomView.setBackgroundColor(Color.WHITE);
        bottomView.setSize(width, bottomButtonHeight);
        bottomView.setPosition(0, height - bottomButtonHeight);
        group. AddView(bottomView);
        int gap = 30;
        int buttonWidth = (width - 3*gap)/2;

        MiniUITextView miniUITextView = new MiniUITextView(this.getContext());
        miniUITextView.setText(this.okButtonTitle);
        miniUITextView.setTextSize(16);
        miniUITextView.setTextColor(Color.WHITE);
        okButton = bottomView.AddView(miniUITextView)
        .setUISize(buttonWidth, bottomButtonHeight - gap - buttonTop)
                .setUIPosition((width - buttonWidth)/2, buttonTop)
                .setUIGravity(Gravity.CENTER)
                ;
        okButton.setUIBorder(10, okButtonBackgroundColor());
        okButton.setOnClickListener(this);

        int contentHeight = height - titleHeight - bottomButtonHeight;
        int padding = DP_2_PX(20);
        progressBackgroundView = new MiniUIView(this.getContext());
        progressBackgroundView.setCornerRadius(DP_2_PX(6));
        int progressBackgroundViewHeight = DP_2_PX(12);
        progressBackgroundView.setBackgroundColor(MiniUIColor.color("dddddd"));
        progressBackgroundView.setRight(width-padding);
        progressBackgroundView.setLeft(padding);
        int top = titleHeight + (contentHeight - progressBackgroundViewHeight)/2;
        progressBackgroundView.setTop(top);
        progressBackgroundView.setBottom(top + progressBackgroundViewHeight);
        group.addView(progressBackgroundView);

        progressbarView = new MiniUIView(this.getContext());
        progressbarView.setCornerRadius(DP_2_PX(6));
        group.addView(progressbarView);
        int left = progressBackgroundView.getLeft();
        progressbarView.setLeft(progressBackgroundView.getLeft());
        progressbarView.setTop(progressBackgroundView.getTop());
        progressbarView.setBottom(progressBackgroundView.getBottom());
        progressbarView.setRight(left);
        progressbarView.setBackgroundColor(Color.YELLOW);


        height = titleHeight + contentHeight + bottomButtonHeight;
        bottomView.setPosition(0, height - bottomButtonHeight);
        group.setSize(width, height);
        window.setLayout(width + paddingLeft + paddingRight, height + paddingTop + paddingBottom);
        titleView.layout(0,0,width, titleHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show(String title, String message, String okButton, MiniUIDialog.MiniDialogCallback callback) {
        show(title, message, okButton, "取消", callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show(String title, String message, String okButton, String cancelButton) {
        show(title, message, okButton, cancelButton, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show(String title, String message, String okButton, String cancelButton, MiniUIDialog.MiniDialogCallback callback) {
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

    public MiniUIDownloadDialog setCallback(MiniUIDialog.MiniDialogCallback callback) {
        this.callback = callback;
        return this;
    }

    public MiniUIDownloadDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MiniUIDownloadDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public MiniUIDownloadDialog setOkButtonTitle(String okButtonTitle) {
        this.okButtonTitle = okButtonTitle;
        return this;
    }

    public MiniUIDownloadDialog setCancelButtonTitle(String cancelButtonTitle) {
        this.cancelButtonTitle = cancelButtonTitle;
        return this;
    }

    protected int titleBackgroundColor() {
        return Color.parseColor("#F8F8F8");
    }

    protected int titleTextColor() {
        return MiniUIColor.color("#555555");
    }

    protected int contentTextColor() {
        return MiniUIColor.color("#555555");
    }

    protected int contentBackgroundColor() {
        return Color.WHITE;
    }


    protected int cancelButtonTitleColor() {
        return Color.WHITE;
    }


    protected int okButtonTitleColor() {
        return Color.WHITE;
    }


    protected int cancelButtonBackgroundColor() {
        return Color.BLUE;
    }

    protected int okButtonBackgroundColor() {
        return  Color.BLUE;
    }

    public void setProgress(float progress)
    {
        String floatDesc = String.format("%02d", (int)(progress*100));
        titleView.setText(this.title + "("+ floatDesc + "%)");
        int width = (int)(progressBackgroundView.getWidth()*progress);
        if (width < 2*progressbarView.getCornerRadius()) {
            width = 2*progressbarView.getCornerRadius();
        }
        int right = progressbarView.getLeft() + width;
        progressbarView.setRight(right);
    }
}
