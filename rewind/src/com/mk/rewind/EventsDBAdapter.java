package com.mk.rewind;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventsDBAdapter {

	private static final String DATABASE_NAME = "events.db";
	private static final String DATABASE_TABLE = "events";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_DESC = "description";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_DATE_TIME = "date_time";
	public static final String KEY_ATTACHMENT = "attachment_id";
	public static final String KEY_ROWID = "_id";

	private static final String DATABASE_CREATE;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private final Context ctx;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append("create table ").append(DATABASE_TABLE).append(" (")
				.append(KEY_ROWID)
				.append(" integer primary key autoincrement, ")
				.append(KEY_DESC).append(" text not null, ")
				.append(KEY_LOCATION).append(" text not null, ")
				.append(KEY_DATE_TIME).append(" text not null );")
				.append(KEY_ATTACHMENT).append(" integer ");
		DATABASE_CREATE = sb.toString();
	}

	public EventsDBAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public EventsDBAdapter open() {
		dbHelper = new DatabaseHelper(ctx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long createEvent(String desc, String location, String dateTime) {
		ContentValues values = new ContentValues();
		values.put(KEY_DESC, desc);
		values.put(KEY_LOCATION, location);
		values.put(KEY_DATE_TIME, dateTime);
		return db.insert(DATABASE_TABLE, null, values);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}
}