package com.animoto.android.dgvSample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animoto.android.dgv.DraggableGridViewCell;

public class Icon extends LinearLayout implements DraggableGridViewCell {

	public static final String name = "ICON";

	private TextView mCaption;
	private ImageView mImage;
	private int currentPosition = -1;

	public Icon(Context context, AttributeSet attr) {
		super(context, attr);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCaption = (TextView) this.findViewById(R.id.grid_item_text);
		mImage = (ImageView) this.findViewById(R.id.grid_item_image);
	}

	@Override
	public boolean changeDataForCell(int number) {
		setIcon(number);
		return true;
	}

	public void setIcon(int number){
		currentPosition = number;
		mCaption.setText(DraggableAdapter.mCaptions[number]);
		mImage.setImageResource(DraggableAdapter.mImages[number]);
	}

	@Override
	public int getPositionInData() throws CellDataNotSetException {
		if (currentPosition == -1) {
			throw new CellDataNotSetException("Custom photo cell doesn't have an underlying model object set");
		} else return currentPosition;
	}

	@Override
	public String getConvertIdentifier() {
		return name;
	}

}
