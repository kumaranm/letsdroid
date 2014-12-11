package com.mk.rewind;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class EventListActivity extends ListActivity {

	private EventOperations db;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	
	private static String fromPage = Helper.HOME_PAGE;
	private int year = -1;
	private int month = -1;
//	private int archived = 0;
//	private int date = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		/*
		 * String[] events = new String[] { "event1", "event2" };
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * R.layout.event_row, R.id.eventtext, events); setListAdapter(adapter);
		 */
		fromPage = Helper.HOME_PAGE;
		if (savedInstanceState != null)
		{
			year = savedInstanceState.getInt(DatabaseWrapper.KEY_YEAR);
			month = savedInstanceState.getInt(DatabaseWrapper.KEY_MONTH);
//			archived = savedInstanceState.getInt(Helper.ARCHIVED_KEY);
		}
		setDataFromIntent();

		db = new EventOperations(this);
		db.open();
		fillData();
	}

	private void setDataFromIntent() {

		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			fromPage = extras.getString(Helper.FROM_PAGE_KEY) != null ? extras.getString(Helper.FROM_PAGE_KEY) : Helper.HOME_PAGE;
			year = extras.getString(DatabaseWrapper.KEY_YEAR) != null ? Integer.parseInt(extras
					.getString(DatabaseWrapper.KEY_YEAR)) : -1;
			month = extras.getString(DatabaseWrapper.KEY_MONTH) != null ? Integer.parseInt(extras
					.getString(DatabaseWrapper.KEY_MONTH)) : -1;
			/*archived = extras.getString(Helper.ARCHIVED_KEY) != null ? Integer.parseInt(extras
					.getString(Helper.ARCHIVED_KEY)) : 0;*/
/*			date = extras.getString(DatabaseWrapper.KEY_DATE) != null ? Integer.parseInt(extras
					.getString(DatabaseWrapper.KEY_DATE)) : -1;*/
		}
	}

	private void fillData()
	{
		boolean showBack = false;
		List<Event> lst = new ArrayList<Event>(0);
		long count = 0;
		String text = null;
		String display = null;
		if (Helper.ARCHIVED_LIST_PAGE.equals(fromPage)/* && archived == 1*/)
		{
			lst = db.getAllArchivedEvents();
			count = db.getAllArchivedEventsCount();
			text = "Archived Events (" + count + ")";
			display = Helper.DISP_YEAR_MONTH_DATE;
			showBack = true;
		}
		else if ((Helper.MONTH_LIST_PAGE.equals(fromPage) || Helper.NEW_EVENT_PAGE.equals(fromPage) ) && year != -1 && month != -1
				&& (year != Helper.ALL && month != Helper.ALL))
		{
			lst = db.getAllEventsByYearMonth(year, month);
			count = db.getAllEventsByYearMonthCount(year, month);
			text = "... during " + Helper.getLongMonthString(month) + ", " + year + " (" + count + ")";
			display = Helper.DISP_DATE;
			showBack = true;
		}
		else if (Helper.YEAR_ALL_LIST_PAGE.equals(fromPage) && (year != -1 || year == Helper.ALL))
		{
			lst = db.getAllEvents();
			count = db.getAllEventsCount();
			text = "ALL Events (" + count + ")";
			display = Helper.DISP_YEAR_MONTH_DATE;
			showBack = true;
		}
		else if (Helper.MONTH_ALL_LIST_PAGE.equals(fromPage) && year != -1 && (month != -1 || month == Helper.ALL))
		{
			lst = db.getAllEventsByYear(year);
			count = db.getAllEventsByYearCount(year);
			text = "... in " + year + " (" + count + ")";
			display = Helper.DISP_MONTH_DATE;
			showBack = true;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			lst = db.getAllEventsByMonthDate(cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			count = db.getAllEventsByMonthDateCount(cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			text = "On this day " + Helper.getPaddedDateString(cal.get(Calendar.DATE)) + " "
					+ Helper.getLongMonthString(cal.get(Calendar.MONTH)) + " (" + count + ") ";
			display = Helper.DISP_YEAR;
			showBack = true;
		}
		// populateDummyData(lst);
		if(showBack)
		{
			 // get action bar   
	        ActionBar actionBar = getActionBar();
	        // Enabling Up / Back navigation
	        actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		if (text != null)
		{
			((TextView) findViewById(R.id.eventstoday)).setText(text);
		}
		// if (lst != null && lst.size() > 0) {

		EventArrayAdapter adapter = new EventArrayAdapter(this, lst, display);

		setListAdapter(adapter);
		// }
		getListView().setSelector(R.drawable.rewind_edit_menu_selector);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {

			private Set<Long> ids = new HashSet<Long>();

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu)
			{
				// once on initial creation
				MenuInflater inflater = getMenuInflater();
				if (Helper.ARCHIVED_LIST_PAGE.equals(fromPage))
				{
					inflater.inflate(R.menu.multisel_unarchive_menu, menu);

				}
				else
				{
					inflater.inflate(R.menu.multisel_archive_menu, menu);
				}
				mode.setTitle("Select events");
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item)
			{
				// any time a contextual action button is clicked
				switch (item.getItemId()) {
	            case R.id.deleteevent:
	            	deleteEvents();
	                mode.finish();
	                break;
	            case R.id.archiveevent:
	            	toggleArchiveEvents(true);
	                mode.finish();
	                break; 
	            case R.id.unarchiveevent:
	            	toggleArchiveEvents(false);
	                mode.finish();
	                break;    
	            default:
	                break;
	            }
				fillData();
				return true;
			}

			private void deleteEvents()
			{
				long[] checkedItemIds = getListView().getCheckedItemIds();
				checkedItemIds = new long[ids.size()];
				int i=0;
				for (long id: ids)
				{
					checkedItemIds[i] = id;
					i++;
				}
				if(i > 0)
				{
					db.deleteEvents(checkedItemIds);
					if(i == 1)
					{
						Toast.makeText(EventListActivity.this, "Deleted " + getListView().getCheckedItemCount() +
		                        " event", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(EventListActivity.this, "Deleted " + getListView().getCheckedItemCount() +
		                        " events", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			
			private void toggleArchiveEvents(boolean archive)
			{
				long[] checkedItemIds = getListView().getCheckedItemIds();
				checkedItemIds = new long[ids.size()];
				int i=0;
				for (long id: ids)
				{
					checkedItemIds[i] = id;
					i++;
				}
				if(i > 0)
				{
					db.toggleArchiveEvents(checkedItemIds, archive);
					String start = archive ? "Archived " : "Restored ";
					String end = i == 1 ? " event" : " events";
					Toast.makeText(EventListActivity.this, start + getListView().getCheckedItemCount() + end,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onDestroyActionMode(ActionMode arg0)
			{
				// when the action mode is closed

			}

			@Override
			public boolean onPrepareActionMode(ActionMode arg0, Menu arg1)
			{
				// after creation and any time the ActionMode is invalidated
				return true;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
			{
				// Called when an item is checked or unchecked during selection
				// mode.
//				int checkedItems = getListView().getCheckedItemCount();
//				mode.setTitle(String.valueOf(checkedItems) + " Selected");

				final int checkedCount = getListView().getCheckedItemCount();
				switch (checkedCount) {
				case 0:
					mode.setSubtitle(null);
					break;
				case 1:
					mode.setSubtitle("1 event selected");
					break;
				default:
					mode.setSubtitle("" + checkedCount + " events selected");
					break;
				}
				long itemId = getListView().getAdapter().getItemId(position); 
				if (checked)
				{
					ids.add(itemId);
				}
				else
				{
					ids.remove(itemId);
				}
			}
		});

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
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(this, EventEditActivity.class);
		i.putExtra(DatabaseWrapper.KEY_ROWID, String.valueOf(id)); 
		if (year != -1)
		{
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(year));
//			i.putExtra(Helper.FROM_PAGE_KEY, fromPage);
		}
		if (month != -1)
		{
			i.putExtra(DatabaseWrapper.KEY_MONTH, String.valueOf(month));
		}
		i.putExtra(Helper.FROM_PAGE_KEY, fromPage); 
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.rewind_menu, menu);
		
		// Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchevent)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
		
		return super.onCreateOptionsMenu(menu);
	}

	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.rewind_edit_menu, menu);
	}*/

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.homeevent:
			Intent i2 = new Intent(this, EventListActivity.class);
			i2.putExtra(Helper.FROM_PAGE_KEY, Helper.HOME_PAGE);
			startActivity(i2);
			return true;
		case R.id.addevent:
			createEvent();
			return true;
		case R.id.listallevents:
			fromPage = Helper.HOME_PAGE;
			Intent i = new Intent(this, YearListActivity.class);
			startActivityForResult(i, ACTIVITY_EDIT);
			return true;
		case R.id.listarchivedevents:
			Intent i1 = new Intent(this, EventListActivity.class);
//			i1.putExtra(Helper.ARCHIVED_KEY, String.valueOf(1));
			i1.putExtra(Helper.FROM_PAGE_KEY, Helper.ARCHIVED_LIST_PAGE);
			startActivityForResult(i1, ACTIVITY_EDIT);
//			startActivity(i1);
			return true;
		case R.id.settings:
			Intent i3 = new Intent(this, EventSettingActivity.class);
			startActivity(i3);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void createEvent() {
		Intent i = new Intent(this, EventEditActivity.class);
		i.putExtra(Helper.FROM_PAGE_KEY, Helper.NEW_EVENT_PAGE);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// Reload the list here
		setIntent(intent);
		setDataFromIntent(); 
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
		/*if (archived != 0) {
			outState.putInt(Helper.ARCHIVED_KEY, archived);
		}*/
	}

	/*@Override
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
	}*/

	@Override
	public void onBackPressed()
	{
		if (Helper.MONTH_LIST_PAGE.equals(fromPage))
		{
			fromPage=Helper.HOME_PAGE;
			Intent i = new Intent(this, MonthListActivity.class);
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(year));
			i.putExtra(DatabaseWrapper.KEY_MONTH, String.valueOf(month));
			setResult(RESULT_OK, i);
			finish();
		}
		else if (Helper.YEAR_LIST_PAGE.equals(fromPage))
		{
			fromPage=Helper.HOME_PAGE;
			Intent i = new Intent(this, YearListActivity.class);
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(year));
			setResult(RESULT_OK, i);
			finish();
		}
		else if (Helper.MONTH_ALL_LIST_PAGE.equals(fromPage))
		{
			fromPage=Helper.HOME_PAGE;
			Intent i = new Intent(this, MonthListActivity.class);
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(year));
			i.putExtra(DatabaseWrapper.KEY_MONTH, String.valueOf(Helper.ALL));
			setResult(RESULT_OK, i);
			finish();
		}
		else if (Helper.YEAR_ALL_LIST_PAGE.equals(fromPage))
		{
			fromPage=Helper.HOME_PAGE;
			Intent i = new Intent(this, YearListActivity.class);
			i.putExtra(DatabaseWrapper.KEY_YEAR, String.valueOf(Helper.ALL));
			setResult(RESULT_OK, i);
			finish();
		}else if (Helper.ARCHIVED_LIST_PAGE.equals(fromPage))
		{
			fromPage = Helper.HOME_PAGE;
			Intent i = new Intent(this, EventListActivity.class);
			setResult(RESULT_OK, i);
			finish();
		}else if(fromPage.equals(Helper.NEW_EVENT_PAGE))
		{
			fromPage = Helper.HOME_PAGE; 
			Intent i = new Intent(this, EventListActivity.class);
			startActivity(i);
		}
		else if(fromPage.equals(Helper.HOME_PAGE))
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		/*else
		{
			Log.i("HA", "Finishing");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}*/
		/*else{ 
			super.onBackPressed();
		}*/
	}
}
