package com.mk.rewind;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EventOperations {

	private DatabaseWrapper dbHelper;
	private String[] TABLE_COLUMNS = { DatabaseWrapper.KEY_ROWID, DatabaseWrapper.KEY_NARRATION,
			DatabaseWrapper.KEY_LOCATION, DatabaseWrapper.KEY_DATE_TIME };
	private SQLiteDatabase db;

	public EventOperations(Context ctx) {
		dbHelper = new DatabaseWrapper(ctx);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Event createEvent(String desc, String location, String dateTime, int date, int month, int year) {
		ContentValues values = new ContentValues();
		values.put(DatabaseWrapper.KEY_NARRATION, desc);
		values.put(DatabaseWrapper.KEY_LOCATION, location);
		values.put(DatabaseWrapper.KEY_DATE_TIME, dateTime);
		values.put(DatabaseWrapper.KEY_DATE, date);
		values.put(DatabaseWrapper.KEY_MONTH, month);
		values.put(DatabaseWrapper.KEY_YEAR, year);
		values.put(DatabaseWrapper.KEY_ATTACHMENT, 0);
		long id = db.insert(DatabaseWrapper.DATABASE_TABLE, null, values);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ROWID + "=" + id,
				null, null, null, null);

		cursor.moveToFirst();
		Event event = parseEvent(cursor);
		cursor.close();
		return event;
	}

	private Event parseEvent(Cursor cursor) {
		Event event = new Event();
		event.setId(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_ROWID)));
		event.setNarration(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.KEY_NARRATION)));
		event.setLocation(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.KEY_LOCATION)));
		event.setDateTime(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.KEY_DATE_TIME)));
		event.setDate(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_DATE)));
		event.setMonth(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_MONTH)));
		event.setYear(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_YEAR)));
		return event;
	}

	public void deleteEvent(Event event) {
		Log.i("deleteEvent", "deleting event-" + event.getId());
		db.delete(DatabaseWrapper.DATABASE_TABLE, DatabaseWrapper.KEY_ROWID + "=" + event.getId(), null);
	}

	public void deleteEvent(long id) {
		Log.i("deleteEvent", "deleting event-" + id);
		db.delete(DatabaseWrapper.DATABASE_TABLE, DatabaseWrapper.KEY_ROWID + "=" + id, null);
	}

	public List<Event> getAllEvents() {
		List<Event> eventList = new ArrayList<Event>(0);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = parseEvent(cursor);
			eventList.add(event);
			cursor.moveToNext();
		}
		cursor.close();
		return eventList;
	}

}
