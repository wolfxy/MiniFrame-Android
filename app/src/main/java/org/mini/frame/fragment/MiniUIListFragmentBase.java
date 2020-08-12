package org.mini.frame.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Wuquancheng on 2018/8/14.
 */

public abstract class MiniUIListFragmentBase extends MiniUIFragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView> {

    protected BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return MiniUIListFragmentBase.this.getItemsCount();
        }

        @Override
        public Object getItem(int position) {
            return MiniUIListFragmentBase.this.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return MiniUIListFragmentBase.this.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return MiniUIListFragmentBase.this.getItemView(position, convertView, parent);
        }
    };

    public abstract CharSequence title();

    public abstract int getItemsCount();

    public abstract Object getItem(int position);

    public abstract long getItemId(int position);

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    public  abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);

    public abstract void onRefresh(PullToRefreshBase<ListView> var1);

}
