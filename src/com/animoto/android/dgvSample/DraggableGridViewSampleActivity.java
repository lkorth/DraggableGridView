package com.animoto.android.dgvSample;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.animoto.android.db.DatabaseHelper;
import com.animoto.android.dgv.DraggableGridView;
import com.animoto.android.dgv.OnRearrangeListener;

public class DraggableGridViewSampleActivity extends Activity implements OnRearrangeListener {

	private DraggableGridView dgv;
	private DatabaseHelper dbh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dbh = new DatabaseHelper(this);

		dgv = ((DraggableGridView)findViewById(R.id.vgv));
		dgv.setAdapter(new DgvDatabaseAdapter(this));
		dgv.setOnRearrangeListener(this);
	}

	@Override
	public void onRearrange(int oldIndex, int newIndex) {
		SQLiteDatabase db = dbh.getWritableDatabase();

		int oldIcon = dbh.getIconPosition(oldIndex);

		String query;
		if (newIndex < oldIndex) {
			query = "UPDATE " + DatabaseHelper.icons +  " SET " + DatabaseHelper.position + " = " + DatabaseHelper.position +
					" + 1 WHERE " + DatabaseHelper.position + " >= " + newIndex + " AND " + DatabaseHelper.position + " < " + oldIndex;
		} else {
			query = "UPDATE " + DatabaseHelper.icons +  " SET " + DatabaseHelper.position + " = " + DatabaseHelper.position +
					" - 1 WHERE " + DatabaseHelper.position + " <= " + newIndex + " AND " + DatabaseHelper.position + " > " + oldIndex;
		}

		db.rawQuery(query, null);

		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.position, newIndex);
		db.update(DatabaseHelper.icons, cv, DatabaseHelper.iconNumber + "= " + oldIcon, null);
	}


}