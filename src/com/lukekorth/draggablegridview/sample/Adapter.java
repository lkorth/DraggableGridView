/*
 * Copyright 2012 Luke Korth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lukekorth.draggablegridview.sample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lukekorth.draggablegridview.DraggableGridView;
import com.lukekorth.draggablegridview.DraggableGridViewAdapter;
import com.lukekorth.draggablegridview.GridItem;

import java.util.LinkedList;

public class Adapter extends DraggableGridViewAdapter {

    public static final int[] mImages = {
        R.drawable.sample_img_1, R.drawable.sample_img_2,
        R.drawable.sample_img_3, R.drawable.sample_img_4,
        R.drawable.sample_img_5, R.drawable.sample_img_6,
        R.drawable.sample_img_7, R.drawable.sample_img_8,
        R.drawable.sample_img_9, R.drawable.sample_img_10,
        R.drawable.sample_img_11, R.drawable.sample_img_12,
        R.drawable.sample_img_13, R.drawable.sample_img_14,
        R.drawable.sample_img_15, R.drawable.sample_img_16,
        R.drawable.sample_img_17
    };

    public static final int[] mCaptions = {
        R.string.a, R.string.b,
        R.string.c, R.string.d,
        R.string.e, R.string.f,
        R.string.g, R.string.h,
        R.string.i, R.string.j,
        R.string.k, R.string.l,
        R.string.m, R.string.n,
        R.string.o, R.string.p,
        R.string.q
    };

    private Context mContext;
    private LayoutInflater layoutInflater;
    private LinkedList<Integer> icons;

    public Adapter(Context context) {
        super();
        mContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        icons = new LinkedList<Integer>();
        for(int i = 0; i < 12; i++){
            icons.add(i);
        }
    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getIcon(int position){
        return icons.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int item = icons.get(position);

        Log.i(DraggableGridView.LOG_TAG, "position: " + position + " icon: " + item);

        GridItem icon = (GridItem) layoutInflater.inflate(R.layout.grid_item, null);

        icon.setIcon(position);

        return icon;
    }

    @Override
    public void onRearrange(int oldIndex, int newIndex) {
        int movingIcon = icons.get(oldIndex);
        icons.remove(oldIndex);
        icons.add(newIndex, movingIcon);
        notifyDataSetChanged();
        Log.i(DraggableGridView.LOG_TAG, "New Order: " + icons.toString());
    }

}

