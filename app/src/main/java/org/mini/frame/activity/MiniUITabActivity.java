package org.mini.frame.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.mini.frame.R;

import org.mini.frame.fragment.MiniUIFragment;
import org.mini.frame.toolkit.MiniUIColor;
import org.mini.frame.view.tabbar.MiniUITabBarView;
import org.mini.frame.view.tabbar.MiniUITabItemView;

import java.util.List;

import static org.mini.frame.uitools.MiniDisplayUtil.DP_2_PX;

public abstract class MiniUITabActivity extends MiniUIActivity implements MiniUITabBarView.MiniTabBarViewListener {

    protected MiniUITabBarView tabBarView;
    private ViewGroup tabContentView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNaviLeftButton();
        LayoutInflater.from(getBaseContext()).inflate(R.layout.tab_container, miniContentView);
        tabBarView = findViewById(R.id.tab_bar_view);
        tabContentView = findViewById(R.id.tab_content_view);
        tabBarView.setMiniUIActivity(MiniUITabActivity.this);

        tabBarView.setListener(this);
        tabBarView.setBackgroundColor(tabBarViewBackGroundColor());
        tabBarView.clearTabItemViews();
        int shadowId = tabbarShadowImageId();
        if (shadowId > 0) {
            tabBarView.setPortraitShadowImageResourceId(shadowId);
        }
        int height = tabBarView.getHeight();
        MiniUITabItemView[] items = tabItemItems();
        int x = 0;
        int itemWidth = tabBarView.getWidth()/items.length;
        for (MiniUITabItemView itemView : items) {
            itemView.setFrame(x,0,itemWidth,height);
            x = itemView.getRight();
            tabBarView.addTabItemView(itemView);
        }

         tabBarView.setSelectedIndex(defaultSelectedIndex());

    }


    public ViewGroup getTabContentView()
    {
        return tabContentView;
    }

    public ViewGroup getContentView() {
        return tabContentView;
    }

    public abstract MiniUITabItemView[] tabItemItems();

    public int defaultSelectedIndex() {
        return 0;
    }

    public int tabBarViewBackGroundColor()
    {
        return MiniUIColor.color("#F8F8F8");
    }

    public abstract int tabbarShadowImageId();

    public void onDestroy()
    {
        tabBarView.destroy();
        super.onDestroy();
    }

    @Override
    public void willSelectAtIndex(int index, MiniUITabItemView item) {
    }

    @Override
    public void didSelectAtIndex(int index, MiniUITabItemView item) {
        if (index == 0) {

        }
    }

    @Override
    public void willDeSelectAtIndex(int index, MiniUITabItemView item) {

    }

    @Override
    public void didDeSelectAtIndex(int index, MiniUITabItemView item) {

    }

    @Override
    public void repeatSelectAtIndex(int index, MiniUITabItemView item) {

    }

//    public int getNaviTitleViewHeight() {
//        return 0;
//    }

    protected void onResume() {
        super.onResume();
        List<MiniUITabItemView> items = tabBarView.getTabItemViews();
        for (MiniUITabItemView v : items) {
            if (v.isSelected()) {
                MiniUIFragment fragment = tabBarView.getFragment(v);
                if (fragment != null ) {
                    fragment.resume();
                }
            }
        }
    }
}
