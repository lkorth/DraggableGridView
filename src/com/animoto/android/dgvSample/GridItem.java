package com.animoto.android.dgvSample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridItem extends LinearLayout {

    public static final String name = "GRIDITEM";

    private TextView mCaption;
    private ImageView mImage;
    private int currentPosition = -1;

    public GridItem(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCaption = (TextView) this.findViewById(R.id.grid_item_text);
        mImage = (ImageView) this.findViewById(R.id.grid_item_image);
    }

    public boolean changeDataForCell(int number) {
        setIcon(number);
        return true;
    }

    public void setIcon(int number){
        currentPosition = number;
        mCaption.setText(DraggableGridViewAdapter.mCaptions[number]);
        mImage.setImageResource(DraggableGridViewAdapter.mImages[number]);
    }

    public int getPositionInData() {
        return currentPosition;
    }

}
