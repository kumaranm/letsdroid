package com.mk.rewind;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class EventEditActivity extends Activity {

	private Button dateButton;
	private EditText narrationText;
	private EditText locationText;
//	private Button captureButton;
//	private Button discardButton;
	private Calendar calendar;
	private static final int DATE_PICKER_DIALOG = 0;
	private static final int CONFIRM_CAPTURE_DIALOG = 1;
	private static final int CONFIRM_DISCARD_DIALOG = 2;
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy kk:mm:ss";
	private EventOperations db;
	private Long rowId;
	private static String fromPage = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_event_modify);

		db = new EventOperations(this);
		db.open();

		calendar = Calendar.getInstance();
		narrationText = (EditText) findViewById(R.id.narrationtext);
		locationText = (EditText) findViewById(R.id.locationtext);
		dateButton = (Button) findViewById(R.id.datebutton);
//		discardButton = (Button) findViewById(R.id.discardbutton);
//		captureButton = (Button) findViewById(R.id.capturebutton);

		rowId = savedInstanceState != null ? savedInstanceState.getLong(DatabaseWrapper.KEY_ROWID) : null;

		registerButtonListenersAndSetDefaultText();

		setRowIdFromIntent();
		populateFields();
	}

	private void setRowIdFromIntent() {
		/*if (rowId == null) {
			Bundle extras = getIntent().getExtras();
			rowId = extras != null ? extras.getLong(DatabaseWrapper.KEY_ROWID) : null;
		}*/
		
		if (getIntent() != null && getIntent().getExtras() != null)
		{
			Bundle extras = getIntent().getExtras();
			if(rowId == null)
			{
				rowId = extras.getString(DatabaseWrapper.KEY_ROWID) != null ? Long.parseLong(extras
						.getString(DatabaseWrapper.KEY_ROWID)) : null;
			}
			fromPage = extras.getString(Helper.FROM_PAGE_KEY) != null ? extras.getString(Helper.FROM_PAGE_KEY) : "";
		}
	}

	private void populateFields() {
		if (rowId != null) {
			Event event = db.getEvent(rowId);
			narrationText.setText(event.getNarration());
			locationText.setText(event.getLocation());
//			discardButton.setText(R.string.event_cancel);
//			captureButton.setText(R.string.event_update);
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			Date date = null;

			try {
				date = dateTimeFormat.parse(event.getDateTime());
				calendar.setTime(date);
			} catch (java.text.ParseException e) {
				Log.e(this.getLocalClassName(), e.getMessage(), e);
			}
			updateDateButtonText();
//			((MenuItem)findViewById(R.id.deleteevent)).setVisible(true);
//			((MenuItem)findViewById(R.id.archiveevent)).setVisible(true);
		}
		else
		{
//			((MenuItem)findViewById(R.id.deleteevent)).setVisible(false);
//			((MenuItem)findViewById(R.id.archiveevent)).setVisible(false);
		}
		/*
		 * else { SharedPreferences prefs =
		 * PreferenceManager.getDefaultSharedPreferences(this); String
		 * defaultTitleKey = getString(R.string.pref_task_title_key); String
		 * defaultTimeKey = getString(R.string.pref_default_time_from_now_key);
		 * String defaultTitle = prefs.getString(defaultTitleKey, ""); String
		 * defaultTime = prefs.getString(defaultTimeKey, ""); if
		 * (!"".equals(defaultTitle)) { mTitleText.setText(defaultTitle); } if
		 * (!"".equals(defaultTime)) { mCalendar.add(Calendar.MINUTE,
		 * Integer.parseInt(defaultTime)); }
		 * 
		 * //below code updates the preference Editor editor = prefs.edit();
		 * editor.putString(defaultTitleKey, "default text"); editor.commit(); }
		 * updateDateButtonText(); updateTimeButtonText();
		 */
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (rowId != null) {
			outState.putLong(DatabaseWrapper.KEY_ROWID, rowId);
		}
	}

	private void registerButtonListenersAndSetDefaultText() {
		dateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_PICKER_DIALOG);
			}
		});
		updateDateButtonText();

		/*discardButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog(CONFIRM_DISCARD_DIALOG);

			}
		});
		captureButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog(CONFIRM_CAPTURE_DIALOG);

			}
		});*/
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_DIALOG:
			return showDatePicker();
		case CONFIRM_DISCARD_DIALOG:
			showConfirmDiscard();
			break;
		case CONFIRM_CAPTURE_DIALOG:
			saveState();
			break;
		}
		return super.onCreateDialog(id);
	}

	private void saveState() {
		String narration = narrationText.getText().toString();
		String location = locationText.getText().toString();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, calendar.get(Calendar.DATE));
		cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

		String dateTime = dateTimeFormat.format(cal.getTime());

		String action = "";
		if (rowId == null) {
			Event event = db.createEvent(narration, location, dateTime, calendar.get(Calendar.DATE),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
			if (event != null) {
				rowId = (long) event.getId();
			}
			action = "Event captured";
		} else {
			db.updateEvent(rowId, narration, location, dateTime, calendar.get(Calendar.DATE),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
			action = "Event updated";
		}

		Intent i = new Intent(this, EventListActivity.class);
		i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(calendar.get(Calendar.YEAR)));
		i.putExtra(DatabaseWrapper.KEY_MONTH, String.valueOf(calendar.get(Calendar.MONTH)));
		i.putExtra(DatabaseWrapper.KEY_DATE, String.valueOf(calendar.get(Calendar.DATE)));
		i.putExtra(Helper.FROM_PAGE_KEY, fromPage);
		setResult(RESULT_OK, i);
//		setContentView(R.layout.activity_event_list);
		Toast.makeText(EventEditActivity.this, action, Toast.LENGTH_SHORT).show();
		finish();
	}

	private DatePickerDialog showDatePicker() {
		DatePickerDialog datePicker = new DatePickerDialog(EventEditActivity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						calendar.set(Calendar.YEAR, year);
						calendar.set(Calendar.MONTH, monthOfYear);
						calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						updateDateButtonText();
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		return datePicker;
	}

	private void updateDateButtonText() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String dateForButton = dateFormat.format(calendar.getTime());
		dateButton.setText(dateForButton);
	}

	private void showConfirmDiscard() {
		AlertDialog.Builder builder = new AlertDialog.Builder(EventEditActivity.this);
		builder.setMessage(R.string.confirm_discard_message).setTitle(R.string.confirm_discard_title)
				.setCancelable(false).setPositiveButton(R.string.event_okay, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_OK);
						Toast.makeText(EventEditActivity.this, "Event discarded", Toast.LENGTH_SHORT).show();
						setContentView(R.layout.activity_event_list);
						finish();
					}
				}).setNegativeButton(R.string.event_go_back, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_CANCELED);
						// Toast.makeText(EventEditActivity.this, "Not saved",
						// Toast.LENGTH_SHORT).show();
					}
				});
		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.rewind_edit_menu, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.discardbutton:
			showDialog(CONFIRM_DISCARD_DIALOG);
			return true;
		case R.id.capturebutton:
			showDialog(CONFIRM_CAPTURE_DIALOG);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
