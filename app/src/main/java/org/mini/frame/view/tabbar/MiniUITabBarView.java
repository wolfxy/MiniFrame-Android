package org.mini.frame.view.tabbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.mini.frame.activity.MiniUITabActivity;
import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.MiniStringUtil;
import org.mini.frame.uitools.MiniUIScreen;
import org.mini.webview.MiniUIWebViewFragment;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.fragment.MiniUIFragment;
import org.mini.frame.view.MiniUIView;
import org.mini.frame.view.MiniUIViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuquancheng on 15/4/5.
 */
public class MiniUITabBarView extends ViewGroup implements View.OnClickListener {

    private final static String TAG = MiniUITabBarView.class.getSimpleName();

    public interface MiniTabBarViewListener {
        void willSelectAtIndex(int index, MiniUITabItemView item);
        void didSelectAtIndex(int index, MiniUITabItemView item);
        void willDeSelectAtIndex(int index, MiniUITabItemView item);
        void didDeSelectAtIndex(int index, MiniUITabItemView item);
        void repeatSelectAtIndex(int index, MiniUITabItemView item);
    }

    private MiniUIView shadowView;

    private List<MiniUITabItemView> tabItemViews = new ArrayList<MiniUITabItemView>();
    private MiniTabBarViewListener listener;
    private int portraitShadowImageResourceId;
    private Map<MiniUITabItemView,MiniUIFragment> fragmentHashMap = new HashMap<MiniUITabItemView,MiniUIFragment>();


    private MiniUITabActivity miniUIActivity;

    public MiniUITabBarView(Context context) {
        super(context);

    }

    public MiniUITabBarView(Context context, AttributeSet attrs) {
         super(context, attrs);
    }

    public void setMiniUIActivity(MiniUITabActivity activity) {
        this.miniUIActivity = activity;
    }

    public void destroy()
    {
        for(MiniUITabItemView view : fragmentHashMap.keySet()) {
            MiniUIFragment fragment = fragmentHashMap.get(view);
            fragment.onDestroy();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (shadowView != null) {
            shadowView.layout(0, -1, this.getWidth(), 4);
        }
        if (this.tabItemViews != null && this.tabItemViews.size() > 0) {
            int itemWidth = this.getWidth() / tabItemViews.size();
            int left = 0;
            for (MiniUITabItemView tabItemView : tabItemViews) {
                tabItemView.layout(left, 0, left + itemWidth, this.getHeight());
                left += itemWidth;
            }
        }
        shadowView.bringToFront();
    }

    public void setPortraitShadowImageResourceId(int portraitShadowImageResourceId) {
        this.portraitShadowImageResourceId = portraitShadowImageResourceId;
        if(shadowView == null) {
            shadowView = new MiniUIView(this.getContext());
            this.addView(shadowView);
        }
        shadowView.setBackground(getResources().getDrawable(this.portraitShadowImageResourceId, null));
    }

    public void setListener(MiniTabBarViewListener listener) {
        this.listener = listener;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        if (v instanceof MiniUITabItemView) {
            MiniUITabItemView item = (MiniUITabItemView) v;
            Class clazz = item.getFragmentClazz();
            int index = 0;
            MiniUITabItemView currentItem = null;
            for (MiniUITabItemView itemView : tabItemViews) {
                if (itemView.isSelected()) {
                    currentItem = itemView;
                    break;
                }
            }
            if (item.equals(currentItem)) {
                if (listener != null) {
                    listener.repeatSelectAtIndex(index, item);
                }
                return;
            }
            if (currentItem != null) {
                if (listener != null) {
                    listener.willDeSelectAtIndex(index, currentItem);
                }
                currentItem.setSelected(false);
                if (listener != null) {
                    listener.didDeSelectAtIndex(index, currentItem);
                }
            }
            if (!item.isSelected()) {
                if (listener != null) {
                    listener.willSelectAtIndex(index, item);
                }
                item.setSelected(true);
                if (listener != null) {
                    listener.didSelectAtIndex(index, item);
                }
            }
            else {
                if (listener != null) {
                    listener.repeatSelectAtIndex(index, item);
                }
            }
            MiniUIFragment currentFragment = this.fragmentHashMap.get(currentItem);
            if (currentFragment != null) {
                currentFragment.getView().setVisibility(GONE);
            }
            if (clazz != null) {
                try {
                    ViewGroup contentViewGroup = this.miniUIActivity.getTabContentView();
                    MiniUIFragment fragment = this.fragmentHashMap.get(item);
                    boolean newFragment = false;
                    if (fragment == null) {
                        fragment = (MiniUIFragment) clazz.newInstance();
                        fragment.setContext(this.getContext());
                        fragment.setActivity(this.miniUIActivity);
                        this.fragmentHashMap.put(item, fragment);
                        newFragment = true;
                        if (fragment instanceof MiniUIWebViewFragment) {
                            if (item.getUrl() != null) {
                                ((MiniUIWebViewFragment) fragment).setUrl(item.getUrl());
                            }
                            if (item.getTitle() != null) {
                                ((MiniUIWebViewFragment) fragment).setTitle(item.getTitle());
                            }
                        }
                    }
                    CharSequence title = fragment.title();
                    int height = MiniUIScreen.height() - this.getHeight();
                    if (!MiniStringUtil.isEmpty(title)) {
                        miniUIActivity.setNaviTitle(fragment.title());
                        miniUIActivity.showNaviTitleView();
                        height = height - this.miniUIActivity.getNaviTitleViewHeight();
                    }
                    else {
                        miniUIActivity.hideNaviTitleView();
                    }
                    MiniUIViewGroup viewGroup = fragment.getView();
                    if (newFragment) {
                        viewGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                        contentViewGroup.addView(viewGroup);
                        viewGroup.setLeft(0);
                        viewGroup.setTop(0);
                        viewGroup.setRight(MiniUIScreen.width());
                        viewGroup.setBottom(height);
                        fragment.create();
                    }
                    viewGroup.setVisibility(VISIBLE);

                    MiniLogger.get().d("%s, contentView height: %d", TAG, contentViewGroup.getHeight());

                    this.bringToFront();
                    fragment.onResume();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setSelectedIndex(Integer index) {
        if (index >= 0 && index < tabItemViews.size()) {
            MiniUITabItemView item = tabItemViews.get(index);
            this.onClick(item);
        }
    }

    public void clearTabItemViews() {
        for(MiniUITabItemView view : tabItemViews) {
           this.removeView(view);
        }
        tabItemViews.clear();
    }

    public void addTabItemView(MiniUITabItemView child) {
        super.addView(child);
        tabItemViews.add(child);
        child.setOnClickListener(this);
    }

    public List<MiniUITabItemView> getTabItemViews() {
        return tabItemViews;
    }

    public MiniUIFragment getFragment(MiniUITabItemView v) {
        MiniUIFragment f = this.fragmentHashMap.get(v);
        return f;
    }
}
