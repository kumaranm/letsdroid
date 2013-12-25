package com.mk.rewind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventsDBAdapter {

	private static final String DATABASE_NAME = "events.db";
	private static final String DATABASE_TABLE = "events";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_DESC = "description";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_DATE_TIME = "date_time";
	public static final String KEY_DATE = "date";
	public static final String KEY_MONTH = "month";
	public static final String KEY_YEAR = "year";
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
				.append(KEY_DATE).append(" integer, ").append(KEY_MONTH)
				.append(" integer, ").append(KEY_YEAR).append(" integer, ")
				.append(KEY_DATE_TIME).append(" text not null, ")
				.append(KEY_ATTACHMENT).append(" integer ").append(" );");
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

	public long createEvent(String desc, String location, String dateTime,
			int date, int month, int year) {
		ContentValues values = new ContentValues();
		values.put(KEY_DESC, desc);
		values.put(KEY_LOCATION, location);
		values.put(KEY_DATE_TIME, dateTime);
		values.put(KEY_DATE, date);
		values.put(KEY_MONTH, month);
		values.put(KEY_YEAR, year);
		values.put(KEY_ATTACHMENT, 0);
		return db.insert(DATABASE_TABLE, null, values);
	}

	public boolean deleteEvent(long id) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + " = " + id, null) > 0;
	}

	public Cursor fetchAllEvents() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_DESC,
				KEY_LOCATION, KEY_DATE, KEY_MONTH, KEY_YEAR, KEY_DATE_TIME,
				KEY_ATTACHMENT }, null, null, null, null, null);
	}

	public Cursor fetchEvent(long id) {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DESC, KEY_LOCATION, KEY_DATE, KEY_MONTH, KEY_YEAR,
				KEY_DATE_TIME, KEY_ATTACHMENT }, KEY_ROWID + "=" + id, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Fetch all events matching today in past years
	 * 
	 * @param date
	 * @param lookBackYears
	 * @return
	 */
	public Cursor fetchEventsByDay(int date, int month, int year,
			int lookBackYears) {

		String whereClause = KEY_DATE + "=" + date + " AND " + KEY_MONTH + "="
				+ month + " AND ( " + KEY_YEAR + "=" + year + " OR " + KEY_YEAR
				+ ">=" + (year - lookBackYears) + " )";

		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DESC, KEY_LOCATION, KEY_DATE, KEY_MONTH, KEY_YEAR,
				KEY_DATE_TIME, KEY_ATTACHMENT }, whereClause, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Fetch all events in a year
	 * 
	 * @param date
	 * @param lookBackYears
	 * @return
	 */
	public Cursor fetchEventsByYear(int year) {
		String whereClause = KEY_YEAR + "=" + year;

		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DESC, KEY_LOCATION, KEY_DATE_TIME, KEY_ATTACHMENT },
				whereClause, null, null, null, null);
		// TODO add date check where clause
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchEventsByMonthYear(int month, int year) {

		String whereClause = KEY_MONTH + "=" + month + " AND " + KEY_YEAR + "="
				+ year;
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DESC, KEY_LOCATION, KEY_DATE_TIME, KEY_ATTACHMENT },
				whereClause, null, null, null, null);
		// TODO add date check where clause
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateReminder(long id, String desc, String location,
			String dateTime, int date, int month, int year) {
		ContentValues intialValues = new ContentValues();
		intialValues.put(KEY_DESC, desc);
		intialValues.put(KEY_LOCATION, location);
		intialValues.put(KEY_DATE_TIME, dateTime);
		intialValues.put(KEY_DATE, date);
		intialValues.put(KEY_MONTH, month);
		intialValues.put(KEY_YEAR, year);
		return db.update(DATABASE_TABLE, intialValues, KEY_ROWID + "=" + id,
				null) > 0;
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