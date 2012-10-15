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

package com.lukekorth.draggablegridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lukekorth.draggablegridview.sample.Adapter;
import com.lukekorth.draggablegridview.sample.R;

public class GridItem extends LinearLayout {

    private TextView mCaption;
    private ImageView mImage;
    private int currentPosition = -1;

    public GridItem(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCaption = (TextView) findViewById(R.id.grid_item_text);
        mImage = (ImageView) findViewById(R.id.grid_item_image);
    }

    public void setIcon(int number){
        currentPosition = number;
        mCaption.setText(Adapter.mCaptions[number]);
        mImage.setImageResource(Adapter.mImages[number]);
    }

    public int getPositionInData() {
        return currentPosition;
    }

}
