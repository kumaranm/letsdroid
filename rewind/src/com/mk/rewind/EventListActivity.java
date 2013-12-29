package com.mk.rewind;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventListActivity extends ListActivity {

	private EventOperations db;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private int year = -1;
	private int month = -1;
	private int archived = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		/*
		 * String[] events = new String[] { "event1", "event2" };
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * R.layout.event_row, R.id.eventtext, events); setListAdapter(adapter);
		 */

		year = savedInstanceState != null ? savedInstanceState.getInt(DatabaseWrapper.KEY_YEAR) : -1;
		month = savedInstanceState != null ? savedInstanceState.getInt(DatabaseWrapper.KEY_MONTH) : -1;
		archived = savedInstanceState != null ? savedInstanceState.getInt(Helper.ARCHIVED_KEY) : 0;

		setDataFromIntent();

		db = new EventOperations(this);
		db.open();
		fillData();
	}

	private void setDataFromIntent() {
		if (year == -1) {
			Bundle extras = getIntent().getExtras();
			year = extras != null && extras.getString(DatabaseWrapper.KEY_YEAR) != null ? Integer.parseInt(extras
					.getString(DatabaseWrapper.KEY_YEAR)) : -1;
		}
		if (month == -1) {
			Bundle extras = getIntent().getExtras();
			month = extras != null && extras.getString(DatabaseWrapper.KEY_MONTH) != null ? Integer.parseInt(extras
					.getString(DatabaseWrapper.KEY_MONTH)) : -1;
		}
		if (archived == 0) {
			Bundle extras = getIntent().getExtras();
			archived = extras != null && extras.getString(Helper.ARCHIVED_KEY) != null ? Integer.parseInt(extras
					.getString(Helper.ARCHIVED_KEY)) : 0;
		}
	}

	private void fillData() {
		List<Event> lst = new ArrayList<Event>(0);
		String text = null;
		String display = null;
		if (year != -1 && month != -1 && (year != Helper.ALL && month != Helper.ALL)) {
			lst = db.getAllEventsByYearMonth(year, month);
			text = "Events during " + Helper.getMonthString(month) + ", " + year + " \n";
			display = Helper.DISP_DATE;
		} else if (year == Helper.ALL) {
			lst = db.getAllEvents();
			text = "ALL Events \n";
			display = Helper.DISP_YEAR_MONTH_DATE;
		} else if (year != -1 && month == Helper.ALL) {
			lst = db.getAllEventsByYear(year);
			text = "Events in " + year + "\n";
			display = Helper.DISP_MONTH_DATE;
		} else if (archived == 1) {
			lst = db.getAllArchivedEvents();
			text = "Archived Events \n";
			display = Helper.DISP_YEAR_MONTH_DATE;
		} else {
			Calendar cal = Calendar.getInstance();
			lst = db.getAllEventsByMonthDate(cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			text = "Events today - " + cal.get(Calendar.DATE) + " " + Helper.getMonthString(cal.get(Calendar.MONTH)) + "\n";
			display = Helper.DISP_YEAR;
		}
		// populateDummyData(lst);

		if (text != null) {
			((TextView) findViewById(R.id.eventstoday)).setText(text);
		}
		if (lst != null && lst.size() > 0) {

			EventArrayAdapter adapter = new EventArrayAdapter(this, lst, display);

			setListAdapter(adapter);
		}
	}

	private void populateDummyData(List<Event> lst) {
		for (int i = 0; i < 10; i++) {
			Event event = new Event();
			event.setYear(200 + i);
			event.setLocation("Chennai " + i);
			event.setNarration("This is narration number " + i);
			lst.add(event);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(this, EventEditActivity.class);
		i.putExtra(DatabaseWrapper.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.rewind_menu, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.rewind_longpress_menu, menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.homeevent:
			Intent i2 = new Intent(this, EventListActivity.class);
			startActivity(i2);
			return true;
		case R.id.addevent:
			createEvent();
			return true;
		case R.id.listallevents:
			Intent i = new Intent(this, YearListActivity.class);
			startActivity(i);
			return true;
		case R.id.listarchivedevents:
			Intent i1 = new Intent(this, EventListActivity.class);
			i1.putExtra(Helper.ARCHIVED_KEY, String.valueOf(1));
			startActivityForResult(i1, ACTIVITY_EDIT);
//			startActivity(i1);
			return true;
		case R.id.settings:
			// Intent i = new Intent(this, TaskPreferences.class);
			// startActivity(i);
			// return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void createEvent() {
		Intent i = new Intent(this, EventEditActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// Reload the list here
		fillData();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (year != -1) {
			outState.putInt(DatabaseWrapper.KEY_YEAR, year);
		}
		if (month != -1) {
			outState.putInt(DatabaseWrapper.KEY_MONTH, month);

		}
		if (archived != 0) {
			outState.putInt(Helper.ARCHIVED_KEY, archived);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.deleteevent:
			// delete task
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			db.deleteEvent(info.id);
			Toast text = Toast.makeText(this, R.string.delete_event, Toast.LENGTH_SHORT);
			text.show();
			fillData();
			return true;
		case R.id.editevent:
			Intent i = new Intent(this, EventEditActivity.class);
			AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item.getMenuInfo();
			i.putExtra(DatabaseWrapper.KEY_ROWID, info1.id);
			startActivityForResult(i, ACTIVITY_EDIT);
			return true;
		}
		return super.onContextItemSelected(item);
	}

}
