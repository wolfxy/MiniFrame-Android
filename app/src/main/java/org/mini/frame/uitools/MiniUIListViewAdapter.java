package org.mini.frame.uitools;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YXL on 2015/12/16.
 */
public abstract class MiniUIListViewAdapter<T> extends BaseAdapter {

    List<T> data = new ArrayList<T>();

    public MiniUIListViewAdapter(){

    }
    public MiniUIListViewAdapter(List<T> data){
        if(data != null)
            this.data = data;
    }

    public void setData(List<T> data){
        if(data != null)
            this.data = data;
        notifyDataSetChanged();
    }
    
    public List<T> getData(){
    	return data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(getItem(position), position, convertView, parent);
    }

    protected abstract View getView(T object,int position,View convertView, ViewGroup parent);
}
