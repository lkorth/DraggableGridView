package com.animoto.android.dgvSample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.animoto.android.db.DatabaseHelper;
import com.animoto.android.dgv.DraggableGridViewAdapter;

public class DgvDatabaseAdapter extends DraggableGridViewAdapter {

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

	public DgvDatabaseAdapter(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public int getCount() {
		return mImages.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DatabaseHelper dbh = new DatabaseHelper(mContext);
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor cursor = db.query(DatabaseHelper.icons, new String[] { DatabaseHelper.iconNumber }, DatabaseHelper.position + " =?",
				new String[]{ Integer.toString(position) }, null, null, null);
		cursor.moveToNext();
		int item = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.iconNumber));

		System.out.println("position: " + position + " icon: " + item);

		Icon icon = (Icon) this.getConvertibleCell(Icon.name);

		if(icon == null){
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			icon = (Icon) layoutInflater.inflate(R.layout.photo_cell, null);
		}

		icon.setIcon(position);

		db.close();
		dbh.close();

		return icon;
	}

}

