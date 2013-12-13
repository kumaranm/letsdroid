package com.mk.taskreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RemindersDBAdapter
{

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "reminders";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_DATE_TIME = "reminder_date_time";
	public static final String KEY_ROWID = "_id";

	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE;

	static
	{
		StringBuilder sb = new StringBuilder();
		sb.append("create table ").append(DATABASE_TABLE).append(" (").append(KEY_ROWID)
				.append(" integer primary key autoincrement, ").append(KEY_TITLE).append(" text not null, ")
				.append(KEY_BODY).append(" text not null, ").append(KEY_DATE_TIME).append(" text not null );");
		DATABASE_CREATE = sb.toString();
	}

	private final Context mCtx;

	public RemindersDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public RemindersDBAdapter open()
	{
		mDBHelper = new DatabaseHelper(mCtx);
		mDb = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		mDBHelper.close();
	}

	public long createReminder(String title, String body, String reminderDateTime)
	{
		ContentValues intialValues = new ContentValues();
		intialValues.put(KEY_TITLE, title);
		intialValues.put(KEY_BODY, body);
		intialValues.put(KEY_DATE_TIME, reminderDateTime);
		return mDb.insert(DATABASE_TABLE, null, intialValues);
	}

	public boolean deleteReminder(long id)
	{
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + id, null) > 0;
	}

	public Cursor fetchAllReminders()
	{
		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_DATE_TIME }, null, null,
				null, null, null);
	}

	public Cursor fetchReminder(long id)
	{
		Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_DATE_TIME },
				KEY_ROWID + "=" + id, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateReminder(long id, String title, String body, String reminderDateTime)
	{
		ContentValues intialValues = new ContentValues();
		intialValues.put(KEY_TITLE, title);
		intialValues.put(KEY_BODY, body);
		intialValues.put(KEY_DATE_TIME, reminderDateTime);
		return mDb.update(DATABASE_TABLE, intialValues, KEY_ROWID + "=" + id, null) > 0;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// TODO Auto-generated method stub
			// Not used, but you could upgrade the database with ALTER
			// Scripts
		}

	}
}
