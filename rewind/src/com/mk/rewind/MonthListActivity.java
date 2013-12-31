package com.mk.rewind;

import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MonthListActivity extends ListActivity {

	private EventOperations db;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private int year = -1;
	private String[] months;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month_list);

		if(savedInstanceState != null)
		{
			year = savedInstanceState.getInt(DatabaseWrapper.KEY_YEAR);
		}

		setDataFromIntent();
		db = new EventOperations(this);
		db.open();
		fillData();
	}

	private void setDataFromIntent() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			year = extras != null && extras.getString(DatabaseWrapper.KEY_YEAR) != null ? Integer.parseInt(extras
					.getString(DatabaseWrapper.KEY_YEAR)) : -1;
		}
	}

	private void fillData() {

		String str = String.valueOf(((TextView) findViewById(R.id.monthlisttitle)).getText());
		if (str.indexOf(">") > 0) {
			str = str.substring(str.indexOf("> ") + 2);
			str = year + " > " + str;
		} else {
			str = year + " > " + str;
		}
		((TextView) findViewById(R.id.monthlisttitle)).setText(str);

		Calendar cal = Calendar.getInstance();

		months = db.getMonths(year);

		// populateDummyData(lst);
		if (months == null) {
			months = new String[1];
		}
		months[0] = getString(R.string.all_months);

		String[] monthDisp = new String[months.length];
		monthDisp[0] = months[0];
		for (int i = 1; i < months.length; i++) {
			monthDisp[i] = Helper.getLongMonthString(Integer.parseInt(months[i]));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.year_row, R.id.eventtext, monthDisp);
		setListAdapter(adapter);
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

		if (position == 0) {
			Intent i = new Intent(this, EventListActivity.class);
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(year));
			i.putExtra(DatabaseWrapper.KEY_MONTH, String.valueOf(Helper.ALL));
			i.putExtra(Helper.FROM_PAGE_KEY, Helper.MONTH_ALL_LIST_PAGE);
			 startActivityForResult(i, ACTIVITY_EDIT);
//			startActivity(i);
		} else {
			Intent i = new Intent(this, EventListActivity.class);
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(year));
			i.putExtra(DatabaseWrapper.KEY_MONTH, months[position]);
			i.putExtra(Helper.FROM_PAGE_KEY, Helper.MONTH_LIST_PAGE);
//			startActivity(i);
			 startActivityForResult(i, ACTIVITY_EDIT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// Reload the list here
		setIntent(intent);
		setDataFromIntent();
		fillData();
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * super.onCreateOptionsMenu(menu);
	 * getMenuInflater().inflate(R.menu.rewind_menu, menu); return true; }
	 */

	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.rewind_edit_menu, menu);
	}*/

	/*
	 * @Override public boolean onMenuItemSelected(int featureId, MenuItem item)
	 * { switch (item.getItemId()) { case R.id.addevent: createEvent(); return
	 * true; case R.id.settings: // Intent i = new Intent(this,
	 * TaskPreferences.class); // startActivity(i); // return true; } return
	 * super.onMenuItemSelected(featureId, item); }
	 * 
	 * private void createEvent() { Intent i = new Intent(this,
	 * EventEditActivity.class); startActivityForResult(i, ACTIVITY_CREATE); }
	 * 
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent intent) { super.onActivityResult(requestCode,
	 * resultCode, intent); // Reload the list here fillData(); }
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (year != -1) {
			outState.putInt(DatabaseWrapper.KEY_YEAR, year);
		}
	}

	/*@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.deleteevent:
			// delete task
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			// db.deleteEvent(info.id);
			Toast text = Toast.makeText(this, R.string.delete_event, 2);
			text.show();
			fillData();
			return true;
			
			 * case R.id.editevent: Intent i = new Intent(this,
			 * EventEditActivity.class); AdapterContextMenuInfo info1 =
			 * (AdapterContextMenuInfo) item.getMenuInfo();
			 * i.putExtra(DatabaseWrapper.KEY_ROWID, info1.id);
			 * startActivityForResult(i, ACTIVITY_EDIT); return true;
			 
		}
		return super.onContextItemSelected(item);
	}*/

}
