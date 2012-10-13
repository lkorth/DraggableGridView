package com.animoto.android.dgvSample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.animoto.android.dgv.OnRearrangeListener;

import java.util.LinkedList;

public class DraggableGridViewAdapter extends BaseAdapter implements OnRearrangeListener {

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

    public DraggableGridViewAdapter(Context context) {
        super();
        mContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        icons = new LinkedList<Integer>();
        for(int i = 0; i < 17; i++){
            icons.add(i);
        }
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public int getIcon(int position){
        return icons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int item = icons.get(position);

        System.out.println("position: " + position + " icon: " + item);

        Icon icon = (Icon) layoutInflater.inflate(R.layout.photo_cell, null);

        icon.setIcon(position);

        return icon;
    }

    @Override
    public void onRearrange(int oldIndex, int newIndex) {
        int movingIcon = icons.get(oldIndex);
        icons.remove(oldIndex);
        icons.add(newIndex, movingIcon);
        notifyDataSetChanged();
    }

}

