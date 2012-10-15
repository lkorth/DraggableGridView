package com.lukekorth.draggablegridview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

abstract public class DraggableGridViewAdapter extends BaseAdapter {

    @Override
    abstract public int getCount();

    @Override
    abstract public Object getItem(int position);

    abstract public int getIcon(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    abstract public void onRearrange(int oldIndex, int newIndex);

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

}
