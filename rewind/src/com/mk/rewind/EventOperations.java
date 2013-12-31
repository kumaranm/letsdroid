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
	private static final int UNARCHIVED = 0;
	private static final int ARCHIVED = 1;

	private String[] TABLE_COLUMNS = { DatabaseWrapper.KEY_ROWID, DatabaseWrapper.KEY_NARRATION,
			DatabaseWrapper.KEY_LOCATION, DatabaseWrapper.KEY_DATE_TIME, DatabaseWrapper.KEY_DATE,
			DatabaseWrapper.KEY_MONTH, DatabaseWrapper.KEY_YEAR, DatabaseWrapper.KEY_ARCHIVED,
			DatabaseWrapper.KEY_ATTACHMENT };
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

	public Event createEvent(String narration, String location, String dateTime, int date, int month, int year) {
		Event event = null;
		ContentValues values = new ContentValues();
		values.put(DatabaseWrapper.KEY_NARRATION, narration);
		values.put(DatabaseWrapper.KEY_LOCATION, location);
		values.put(DatabaseWrapper.KEY_DATE_TIME, dateTime);
		values.put(DatabaseWrapper.KEY_DATE, date);
		values.put(DatabaseWrapper.KEY_MONTH, month);
		values.put(DatabaseWrapper.KEY_YEAR, year);
		values.put(DatabaseWrapper.KEY_ARCHIVED, UNARCHIVED);
		values.put(DatabaseWrapper.KEY_ATTACHMENT, 0);
		long id = db.insert(DatabaseWrapper.DATABASE_TABLE, null, values);
		Log.d("createEvent", "record inserted in db -" + id);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ROWID + "=" + id
				+ " AND " + DatabaseWrapper.KEY_ARCHIVED + " = " + UNARCHIVED, null, null, null, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			event = parseEvent(cursor);
		}
		cursor.close();
		return event;
	}

	private Event parseEvent(Cursor cursor) {
		Event event = null;
		event = new Event();
		event.setId(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_ROWID)));
		event.setNarration(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.KEY_NARRATION)));
		event.setLocation(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.KEY_LOCATION)));
		event.setDateTime(cursor.getString(cursor.getColumnIndex(DatabaseWrapper.KEY_DATE_TIME)));
		event.setDate(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_DATE)));
		event.setMonth(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_MONTH)));
		event.setYear(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_YEAR)));
		event.setArchived(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_ARCHIVED)));
		event.setAttachmentId(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_ATTACHMENT)));
		return event;
	}

	public void deleteEvent(Event event) {
		deleteEvent(event.getId());
	}

	public void deleteEvent(long id) {
		Log.i("deleteEvent", "deleting event-" + id);
		db.delete(DatabaseWrapper.DATABASE_TABLE, DatabaseWrapper.KEY_ROWID + "=" + id, null);
	}
	
	public void deleteEvents(long[] ids)
	{
		Log.i("deleteEvent", "deleting events-" + ids);
		db.delete(DatabaseWrapper.DATABASE_TABLE, DatabaseWrapper.KEY_ROWID + " IN (" + Helper.convertToString(ids) + ")", null);
	}

	public boolean archiveEvent(long id) {
		return updateArchiveEvent(id, true);
	}

	public boolean unArchiveEvent(long id) {
		return updateArchiveEvent(id, false);
	}

	private boolean updateArchiveEvent(long id, boolean isArchive) {
		Log.i("updateArchiveEvent", "event-" + id + ", isArchive-" + isArchive);
		ContentValues values = new ContentValues();
		values.put(DatabaseWrapper.KEY_ARCHIVED, isArchive ? ARCHIVED : UNARCHIVED);
		return db.update(DatabaseWrapper.DATABASE_TABLE, values, DatabaseWrapper.KEY_ROWID + "=" + id, null) > 0;
	}
	
	public boolean toggleArchiveEvents(long[] ids, boolean isArchive)
	{
		Log.i("updateArchiveEvent", "event-" + ids);
		ContentValues values = new ContentValues();
		values.put(DatabaseWrapper.KEY_ARCHIVED, isArchive ? ARCHIVED : UNARCHIVED);
		return db.update(DatabaseWrapper.DATABASE_TABLE, values,
				DatabaseWrapper.KEY_ROWID + " IN (" + Helper.convertToString(ids) + " )", null) > 0;
	}

	/*
	 * public List<Event> getAllEvents() { List<Event> eventList = new
	 * ArrayList<Event>(0); Cursor cursor =
	 * db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, null, null, null,
	 * null, null); if (cursor.getCount() > 0) { cursor.moveToFirst(); while
	 * (!cursor.isAfterLast()) { Event event = parseEvent(cursor);
	 * eventList.add(event); cursor.moveToNext(); } } cursor.close(); return
	 * eventList; }
	 */

	public List<Event> getAllEvents() {
		List<Event> eventList = new ArrayList<Event>(0);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ARCHIVED + "="
				+ UNARCHIVED, null, null, null, DatabaseWrapper.KEY_YEAR + " DESC , " + DatabaseWrapper.KEY_MONTH
				+ " DESC , " + DatabaseWrapper.KEY_DATE + " DESC ");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Event event = parseEvent(cursor);
				eventList.add(event);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return eventList;
	}

	public List<Event> getAllEventsByYear(int year) {
		List<Event> eventList = new ArrayList<Event>(0);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ARCHIVED + "="
				+ UNARCHIVED + " AND " + DatabaseWrapper.KEY_YEAR + "=" + year, null, null, null,
				DatabaseWrapper.KEY_MONTH + " DESC , " + DatabaseWrapper.KEY_DATE + " DESC ");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Event event = parseEvent(cursor);
				eventList.add(event);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return eventList;
	}

	public List<Event> getAllEventsByYearMonth(int year, int month) {
		List<Event> eventList = new ArrayList<Event>(0);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ARCHIVED + "="
				+ UNARCHIVED + " AND " + DatabaseWrapper.KEY_YEAR + "=" + year + " AND " + DatabaseWrapper.KEY_MONTH
				+ "=" + month, null, null, null, DatabaseWrapper.KEY_DATE + " DESC ");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Event event = parseEvent(cursor);
				eventList.add(event);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return eventList;
	}

	public List<Event> getAllEventsByMonthDate(int month, int date) {
		List<Event> eventList = new ArrayList<Event>(0);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ARCHIVED + "="
				+ UNARCHIVED + " AND " + DatabaseWrapper.KEY_DATE + "=" + date + " AND " + DatabaseWrapper.KEY_MONTH
				+ "=" + month, null, null, null, DatabaseWrapper.KEY_YEAR + " DESC ");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Event event = parseEvent(cursor);
				eventList.add(event);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return eventList;
	}

	public List<Event> getAllArchivedEvents() {
		List<Event> eventList = new ArrayList<Event>(0);
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ARCHIVED + "="
				+ ARCHIVED, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Event event = parseEvent(cursor);
				eventList.add(event);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return eventList;
	}

	public String[] getYears() {
		String[] years = null;
		Cursor cursor = db.rawQuery("select DISTINCT " + DatabaseWrapper.KEY_YEAR + " from "
				+ DatabaseWrapper.DATABASE_TABLE + " where " + DatabaseWrapper.KEY_ARCHIVED + "=" + UNARCHIVED
				+ " ORDER BY " + DatabaseWrapper.KEY_YEAR + " DESC ", null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			years = new String[cursor.getCount() + 1];
			int i = 1;
			while (!cursor.isAfterLast()) {
				years[i] = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_YEAR)));
				i++;
				cursor.moveToNext();
			}

		}
		cursor.close();
		return years;
	}

	public String[] getMonths(int year) {
		String[] months = null;
		Cursor cursor = db.rawQuery("select DISTINCT " + DatabaseWrapper.KEY_MONTH + " from "
				+ DatabaseWrapper.DATABASE_TABLE + " where " + DatabaseWrapper.KEY_ARCHIVED + "=" + UNARCHIVED
				+ " AND " + DatabaseWrapper.KEY_YEAR + "=" + year + " ORDER BY " + DatabaseWrapper.KEY_MONTH + " ASC ",
				null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			months = new String[cursor.getCount() + 1];

			int i = 1;
			while (!cursor.isAfterLast()) {
				months[i] = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseWrapper.KEY_MONTH)));
				i++;
				cursor.moveToNext();
			}
		}
		cursor.close();
		return months;
	}

	public Event getEvent(long id) {
		Event event = null;
		Cursor cursor = db.query(DatabaseWrapper.DATABASE_TABLE, TABLE_COLUMNS, DatabaseWrapper.KEY_ROWID + "=" + id
				/*+ " AND " + DatabaseWrapper.KEY_ARCHIVED + " = " + UNARCHIVED*/, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			event = parseEvent(cursor);
		}
		cursor.close();
		return event;
	}

	public boolean updateEvent(long id, String narration, String location, String dateTime, int date, int month,
			int year) {
		ContentValues values = new ContentValues();
		values.put(DatabaseWrapper.KEY_NARRATION, narration);
		values.put(DatabaseWrapper.KEY_LOCATION, location);
		values.put(DatabaseWrapper.KEY_DATE_TIME, dateTime);
		values.put(DatabaseWrapper.KEY_DATE, date);
		values.put(DatabaseWrapper.KEY_MONTH, month);
		values.put(DatabaseWrapper.KEY_YEAR, year);
		return db.update(DatabaseWrapper.DATABASE_TABLE, values, DatabaseWrapper.KEY_ROWID + "=" + id, null) > 0;
	}
}
