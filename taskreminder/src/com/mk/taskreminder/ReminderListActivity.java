package com.mk.taskreminder;

import com.mk.taskreminder.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ReminderListActivity extends ListActivity
{

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private RemindersDBAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_reminder_list);

		/*
		 * String[] items = new String[] { "Task 1", "Task 2", "Task 3",
		 * "Task 4" };
		 * 
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * R.layout.reminder_row, R.id.text1, items); setListAdapter(adapter);
		 */

		mDbHelper = new RemindersDBAdapter(this);
		mDbHelper.open();
		fillData();

		registerForContextMenu(getListView());
	}

	private void fillData()
	{
		// TODO Auto-generated method stub
		Cursor reminderCursor = mDbHelper.fetchAllReminders();
		startManagingCursor(reminderCursor);

		// Create an array to specify the fields we want (only the TITLE)
		String[] from = new String[] { RemindersDBAdapter.KEY_TITLE };

		// and an array of the fields we want to bind in the view
		int[] to = new int[] { R.id.text1 };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter reminders = new SimpleCursorAdapter(this, R.layout.reminder_row, reminderCursor, from, to);
		setListAdapter(reminders);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(this, ReminderEditActivity.class);
		i.putExtra(RemindersDBAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.list_menu, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.list_menu_item_longpress, menu);

		/*
		 * Intent i = new Intent(Intent.ACTION_SEND); i.setType("text/plain");
		 * i.putExtra(Intent.EXTRA_TEXT, "Hey Everybody!");
		 * i.putExtra(Intent.EXTRA_SUBJECT, "My Subject"); Intent chooser =
		 * Intent.createChooser(i, "Who Should Handle this?");
		 * startActivity(chooser);
		 */
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.menu_insert:
			createReminder();
			return true;
		case R.id.menu_settings:
			Intent i = new Intent(this, TaskPreferences.class);
			startActivity(i);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void createReminder()
	{
		Intent i = new Intent(this, ReminderEditActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		// Reload the list here
		fillData();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.menu_delete:
			// delete task
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deleteReminder(info.id);
			Toast text = Toast.makeText(this, R.string.menu_delete, 2);
			text.show();
			fillData();
			return true;
		case R.id.menu_edit:
			Intent i = new Intent(this, ReminderEditActivity.class);
			AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item.getMenuInfo();
			i.putExtra(RemindersDBAdapter.KEY_ROWID, info1.id);
			startActivityForResult(i, ACTIVITY_EDIT);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/*@Override
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

	@Override
	protected void onResume()
	{
		super.onResume();
		mDbHelper.open();
		fillData();
	}*/
	
	/*@Override
	protected void onPause()
	{
		super.onPause();
		mDbHelper.close();
	}*/
}
