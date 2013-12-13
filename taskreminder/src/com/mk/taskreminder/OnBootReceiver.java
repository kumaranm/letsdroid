package com.mk.taskreminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		ReminderManager reminderMgr = new ReminderManager(context);
		RemindersDBAdapter dbHelper = new RemindersDBAdapter(context);
		dbHelper.open();
		Cursor cursor = dbHelper.fetchAllReminders();
		if (cursor != null)
		{
			cursor.moveToFirst();
			int rowIdColumnIndex = cursor.getColumnIndex(RemindersDBAdapter.KEY_ROWID);
			int dateTimeColumnIndex = cursor.getColumnIndex(RemindersDBAdapter.KEY_DATE_TIME);
			while (cursor.isAfterLast() == false)
			{
				Long rowId = cursor.getLong(rowIdColumnIndex);
				String dateTime = cursor.getString(dateTimeColumnIndex);
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat(ReminderEditActivity.DATE_TIME_FORMAT);
				try
				{
					Date date = format.parse(dateTime);
					cal.setTime(date);
					reminderMgr.setReminder(rowId, cal);
				} catch (ParseException e)
				{
					Log.e("OnBootReceiver", e.getMessage(), e);
				}
				Log.d("OnBootReceiver", "Adding alarm from boot.");
				Log.d("OnBootReceiver", "Row Id Column Index - " + rowIdColumnIndex);
				cursor.moveToNext();
			}
			cursor.close();
		}
		dbHelper.close();
	}
}
