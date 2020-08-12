package org.mini.frame.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by Wuquancheng on 2018/8/15.
 */

public class MiniPullToRefreshListView extends PullToRefreshListView {

    private BaseAdapter baseAdapter;

    public MiniPullToRefreshListView(Context context) {
        super(context);
    }

    public MiniPullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setBaseAdapter(BaseAdapter baseAdapter) {
        this.baseAdapter = baseAdapter;
        super.setAdapter(baseAdapter);
    }


    public void reloadData() {
        if (baseAdapter != null) {
            baseAdapter.notifyDataSetChanged();
        }
    }
}
