package com.mk.taskreminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderEditActivity extends Activity
{

	private Button mDateButton;
	private Button mTimeButton;
	private EditText mTitleText;
	private Button mConfirmButton;
	private EditText mBodyText;
	private Calendar mCalendar;
	private static final int DATE_PICKER_DIALOG = 0;
	private static final int TIME_PICKER_DIALOG = 1;
	private static final int CONFIRM_SAVE_DIALOG = 2;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "kk:mm";
	private RemindersDBAdapter mDbHelper;
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
	private Long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mDbHelper = new RemindersDBAdapter(this);

		setContentView(R.layout.activity_reminder_edit);

		mCalendar = Calendar.getInstance();
		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		mDateButton = (Button) findViewById(R.id.reminder_date);
		mTimeButton = (Button) findViewById(R.id.reminder_time);
		mConfirmButton = (Button) findViewById(R.id.confirm);

		mRowId = savedInstanceState != null ? savedInstanceState.getLong(RemindersDBAdapter.KEY_ROWID) : null;

		registerButtonListenersAndSetDefaultText();

		mDbHelper.open();
		setRowIdFromIntent();
		populateFields();

	}

	private void setRowIdFromIntent()
	{
		if (mRowId == null)
		{
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(RemindersDBAdapter.KEY_ROWID) : null;
		}
	}

	/*
	 * @Override protected void onPause() { super.onPause(); mDbHelper.close();
	 * }
	 */

	/*
	 * @Override protected void onResume() { super.onResume(); mDbHelper.open();
	 * setRowIdFromIntent(); populateFields(); }
	 */

	private void populateFields()
	{
		if (mRowId != null)
		{
			Cursor reminder = mDbHelper.fetchReminder(mRowId);
			startManagingCursor(reminder);
			mTitleText.setText(reminder.getString(reminder.getColumnIndexOrThrow(RemindersDBAdapter.KEY_TITLE)));
			mBodyText.setText(reminder.getString(reminder.getColumnIndexOrThrow(RemindersDBAdapter.KEY_BODY)));
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			Date date = null;

			try
			{
				String dateString = reminder
						.getString(reminder.getColumnIndexOrThrow(RemindersDBAdapter.KEY_DATE_TIME));
				date = dateTimeFormat.parse(dateString);
				mCalendar.setTime(date);
			} catch (java.text.ParseException e)
			{
				Log.e("ReminderEditActivity", e.getMessage(), e);
			}
			updateDateButtonText();
			updateTimeButtonText();
		}
		else
		{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String defaultTitleKey = getString(R.string.pref_task_title_key);
			String defaultTimeKey = getString(R.string.pref_default_time_from_now_key);
			String defaultTitle = prefs.getString(defaultTitleKey, "");
			String defaultTime = prefs.getString(defaultTimeKey, "");
			if (!"".equals(defaultTitle))
			{
				mTitleText.setText(defaultTitle);
			}
			if (!"".equals(defaultTime))
			{
				mCalendar.add(Calendar.MINUTE, Integer.parseInt(defaultTime));
			}
			
			//below code updates the preference
			/*Editor editor = prefs.edit();
			editor.putString(defaultTitleKey, "default text");
			editor.commit();*/
		}
		updateDateButtonText();
		updateTimeButtonText();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		if (mRowId != null)
		{
			outState.putLong(RemindersDBAdapter.KEY_ROWID, mRowId);
		}
	}

	private void registerButtonListenersAndSetDefaultText()
	{
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				showDialog(DATE_PICKER_DIALOG);
			}
		});
		updateDateButtonText();

		mTimeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				showDialog(TIME_PICKER_DIALOG);
			}
		});

		updateTimeButtonText();

		mConfirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				showDialog(CONFIRM_SAVE_DIALOG);

			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{

		switch (id) {
		case DATE_PICKER_DIALOG:
			return showDatePicker();
		case TIME_PICKER_DIALOG:
			return showTimePicker();
		case CONFIRM_SAVE_DIALOG:
			showConfirmSave();
			// return null;
		}
		return super.onCreateDialog(id);
	}

	private Dialog showTimePicker()
	{
		boolean is24Hr = true;
		TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				mCalendar.set(Calendar.MINUTE, minute);
				updateTimeButtonText();
			}
		}, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), is24Hr);
		return timePicker;
	}

	private void updateTimeButtonText()
	{
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
		String timeForButton = timeFormat.format(mCalendar.getTime());
		mTimeButton.setText(timeForButton);
	}

	private DatePickerDialog showDatePicker()
	{

		DatePickerDialog datePicker = new DatePickerDialog(ReminderEditActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
					{
						// TODO Auto-generated method stub
						mCalendar.set(Calendar.YEAR, year);
						mCalendar.set(Calendar.MONTH, monthOfYear);
						mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						updateDateButtonText();
					}
				}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
		return datePicker;
	}

	private void updateDateButtonText()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String dateForButton = dateFormat.format(mCalendar.getTime());
		mDateButton.setText(dateForButton);
	}

	private void showConfirmSave()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ReminderEditActivity.this);
		builder.setMessage("Are you sure you want to save the task?").setTitle("Confirm save action?")
				.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO
						saveState();
						setResult(RESULT_OK);
						Toast.makeText(ReminderEditActivity.this, "Task saved", Toast.LENGTH_SHORT).show();
						finish();
					}

					private void saveState()
					{
						// TODO Auto-generated method stub
						String title = mTitleText.getText().toString();
						String body = mBodyText.getText().toString();
						SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
						String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());

						if (mRowId == null)
						{
							long id = mDbHelper.createReminder(title, body, reminderDateTime);
							if (id > 0)
							{
								mRowId = id;
							}
						}
						else
						{
							mDbHelper.updateReminder(mRowId, title, body, reminderDateTime);
						}
						new ReminderManager(ReminderEditActivity.this).setReminder(mRowId, mCalendar);
					}
				}).setNegativeButton("Go back", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						setResult(RESULT_CANCELED);
						Toast.makeText(ReminderEditActivity.this, "Not saved", Toast.LENGTH_SHORT).show();
					}
				});
		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.reminder_edit, menu);
		return true;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

}