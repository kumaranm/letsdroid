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
import android.widget.Toast;

public class YearListActivity extends ListActivity {

	private EventOperations db;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private String[] years;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_year_list);

		db = new EventOperations(this);
		db.open();
		fillData();
	}

	private void fillData()
	{
		years = db.getYears();

		// populateDummyData(lst);
		if (years == null)
		{
			years = new String[0];
		}
		years[0] = getString(R.string.all_years);

		String[] yearsDisp = new String[years.length];
		yearsDisp[0] = years[0];
		yearsDisp[0] += " (" + db.getAllEventsCount() + ")";
		for (int i = 1; i < years.length; i++)
		{
			yearsDisp[i] = years[i];
			yearsDisp[i] += " (" + db.getAllEventsByYearCount(Integer.parseInt(years[i])) + ")";
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.year_row, R.id.eventtext, yearsDisp);
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
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(Helper.ALL));
			i.putExtra(Helper.FROM_PAGE_KEY, Helper.YEAR_ALL_LIST_PAGE);
			startActivityForResult(i, ACTIVITY_EDIT);
		} else {
			Intent i = new Intent(this, MonthListActivity.class);
			i.putExtra(DatabaseWrapper.KEY_YEAR, years[position]);
			i.putExtra(Helper.FROM_PAGE_KEY, Helper.YEAR_LIST_PAGE);
//			startActivity(i);
			 startActivityForResult(i, ACTIVITY_EDIT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// Reload the list here
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
