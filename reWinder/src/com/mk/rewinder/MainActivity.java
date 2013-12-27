package com.mk.rewinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.top_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.home:
			setContentView(R.layout.activity_main);
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			return true;
		case R.id.apps:
//			setContentView(R.layout.activity_applist);
			Intent i1 = new Intent(this, AppListActivity.class);
			startActivity(i1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
