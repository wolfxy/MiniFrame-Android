package org.mini.frame.view.tabbar;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.view.MiniUIViewGroup;


/**
 * Created by Wuquancheng on 15/4/5.
 */
public class MiniUITabItemView extends MiniUIViewGroup {

    private ImageView imageView;
    private TextView textView;
    private int normalImageId;
    private int selectedImageId;
    private String title;
    private String tabId;

    private String normalImageURL;
    private String selectedImageURL;

    private ImageView badgeImageView;

    private LinearLayout.LayoutParams badgeViewParams;

    private Class fragmentClazz;

    private Object object;
    private String url;

    private int normalColor;
    private int lightColor;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MiniUITabItemView(Context context, String normalImageURL, String selectedImageURL, String normalTextColorString, String selectedTextColorString, String title, Class activityClazz) {
        this(context, normalImageURL, selectedImageURL, MiniUIColor.color(normalTextColorString), MiniUIColor.color(selectedTextColorString), title, activityClazz);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MiniUITabItemView(Context context, String normalImageURL, String selectedImageURL, int normalTextColor, int selectedTextColor, String title, Class activityClazz) {
        super(context);
        this.normalImageURL = normalImageURL;
        this.selectedImageURL = selectedImageURL;
        this.fragmentClazz = activityClazz;
        this.title = title;
        this.normalColor = normalTextColor;
        this.lightColor =  selectedTextColor;
        this.initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MiniUITabItemView(Context context, int normalImageId, int selectedImageId, String normalTextColorString, String selectedTextColorString, String title, Class activityClazz) {
        this(context, normalImageId, selectedImageId,  MiniUIColor.color(normalTextColorString), MiniUIColor.color(selectedTextColorString), title, activityClazz);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public MiniUITabItemView(Context context, int normalImageId, int selectedImageId, int normalTextColor, int selectedTextColor, String title, Class activityClazz) {
        super(context);
        this.normalImageId = normalImageId;
        this.selectedImageId = selectedImageId;
        this.fragmentClazz = activityClazz;
        this.title = title;
        this.normalColor = normalTextColor;
        this.lightColor = selectedTextColor;
        this.initView();
    }

    public MiniUITabItemView setWebUrl(String url) {
        this.url = url;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
        if (textView != null) {
            textView.setText(title);
        }
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabId() {
        return this.tabId;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initView() {
        imageView = new ImageView(this.getContext());
        imageView.setImageResource(this.normalImageId);
        textView = new TextView(this.getContext());
        textView.setTextSize(12);
        textView.setTextColor(normalColor);
        textView.setText(this.title);
        this.addView(imageView);
        this.addView(textView);
        textView.setGravity(Gravity.CENTER);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutSubviews();
    }

    public void layoutSubviews() {
        int h = this.getHeight();
        if (this.title != null && this.title.length() > 0) {
            imageView.layout(0, (int) (h * 0.2), this.getWidth(), (int) (h * 0.6));
            textView.layout(0, (int) (h * 0.6), this.getWidth(), (int) (h * 0.95));
        }
        else {
            imageView.layout(0, (int) (h * 0.1), this.getWidth(), (int) (h * 0.9));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        defaultOnMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Class getFragmentClazz() {
        return fragmentClazz;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            if (!this.isSelected()) {
                super.setSelected(selected);
                if (selectedImageURL != null) {
                    ImageLoader.getInstance().displayImage(selectedImageURL, imageView);
                }
                else {
                    imageView.setImageResource(selectedImageId);
                }
                textView.setTextColor(lightColor);
            }
        } else {
            if (this.isSelected()) {
                super.setSelected(selected);
                if (normalImageURL != null) {
                    ImageLoader.getInstance().displayImage(normalImageURL, imageView);
                }
                else {
                    imageView.setImageResource(normalImageId);
                }
                textView.setTextColor(normalColor);
            }
        }
    }

    public void setBadge() {
        badgeImageView.setVisibility(View.VISIBLE);
    }

    public void removeBadge() {
        badgeImageView.setVisibility(View.GONE);
    }

    public boolean hasBadge() {
        return (badgeImageView.getVisibility() == View.VISIBLE);
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getUrl() {
        return url;
    }

    public MiniUITabItemView setUrl(String url) {
        this.url = url;
        return this;
    }

    public void setFrame(int left, int top, int width , int height) {
        this.setLeft(left);
        this.setTop(top);
        this.setRight(left+width);
        this.setBottom(top + height);
    }

    public String getTitle() {
        return title;
    }
}
