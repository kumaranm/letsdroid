package com.mk.rewind;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class EventListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

		String[] events = new String[] { "event1", "event2" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.event_row, R.id.eventtext, events);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.rewind_menu, menu);
		return true;
	}

}
