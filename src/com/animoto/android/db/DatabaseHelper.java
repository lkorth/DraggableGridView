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

		ContentValues cv = new ContentValues();
		cv.put(position, 0);
		cv.put(iconNumber, 0);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 1);
		cv.put(iconNumber, 1);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 2);
		cv.put(iconNumber, 2);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 3);
		cv.put(iconNumber, 3);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 4);
		cv.put(iconNumber, 4);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 5);
		cv.put(iconNumber, 5);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 6);
		cv.put(iconNumber, 6);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 7);
		cv.put(iconNumber, 7);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 8);
		cv.put(iconNumber, 8);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 9);
		cv.put(iconNumber, 9);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 10);
		cv.put(iconNumber, 10);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 11);
		cv.put(iconNumber, 11);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 12);
		cv.put(iconNumber, 12);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 13);
		cv.put(iconNumber, 13);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 14);
		cv.put(iconNumber, 14);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 15);
		cv.put(iconNumber, 15);
		db.insert(icons, null, cv);

		cv = new ContentValues();
		cv.put(position, 16);
		cv.put(iconNumber, 16);
		db.insert(icons, null, cv);
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