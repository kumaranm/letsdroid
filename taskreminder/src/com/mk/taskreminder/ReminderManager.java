package com.mk.taskreminder;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ReminderManager
{
	private Context mContext;
	private AlarmManager mAlarmManager;

	public ReminderManager(Context ctx) {
		this.mContext = ctx;
		mAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	}

	public void setReminder(Long taskId, Calendar when)
	{
		Intent i = new Intent(mContext, OnAlarmReceiver.class);
		i.putExtra(RemindersDBAdapter.KEY_ROWID, taskId);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
	}
}
