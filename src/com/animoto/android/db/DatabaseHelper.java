package com.animoto.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	//database name
	public static final String dbName="draggableGridView";

	//tables
	public static final String icons = "Icons";

	//fields in Icons table
	public static final String position = "position";
	public static final String iconNumber = "iconNumber";

	public static final int dbVersion = 1;

	public DatabaseHelper(Context context) {
		super(context, dbName, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + icons + " (" + iconNumber + " INTEGER PRIMARY KEY, " +
				position + " INTEGER)");

		ContentValues cv;
		for(int i = 0; i < 17; i++){
			cv = new ContentValues();
			cv.put(position, i);
			cv.put(iconNumber, i);
			db.insert(icons, null, cv);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + icons);
		onCreate(db);
	}

	public int getIconPosition(int pos){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DatabaseHelper.icons, new String[] { DatabaseHelper.iconNumber }, DatabaseHelper.position + " =?",
				new String[]{ Integer.toString(pos) }, null, null, null);
		cursor.moveToNext();
		return cursor.getInt(cursor.getColumnIndex(iconNumber));
	}
}