package com.mk.rewind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseWrapper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "events.db";
	public static final String DATABASE_TABLE = "events";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_NARRATION = "narration";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_DATE_TIME = "date_time";
	public static final String KEY_DATE = "date";
	public static final String KEY_MONTH = "month";
	public static final String KEY_YEAR = "year";
	public static final String KEY_ATTACHMENT = "attachment_id";
	public static final String KEY_ROWID = "_id";

	private static final String DATABASE_CREATE;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append("create table ").append(DATABASE_TABLE).append(" (")
				.append(KEY_ROWID)
				.append(" integer primary key autoincrement, ")
				.append(KEY_NARRATION).append(" text not null, ")
				.append(KEY_LOCATION).append(" text not null, ")
				.append(KEY_DATE).append(" integer, ").append(KEY_MONTH)
				.append(" integer, ").append(KEY_YEAR).append(" integer, ")
				.append(KEY_DATE_TIME).append(" text not null, ")
				.append(KEY_ATTACHMENT).append(" integer ").append(" );");
		DATABASE_CREATE = sb.toString();
	}

	DatabaseWrapper(Context ctx) {
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
