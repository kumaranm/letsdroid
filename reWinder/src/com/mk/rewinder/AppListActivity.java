package com.mk.rewinder;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AppListActivity extends ListActivity
{
	public ListView appListView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_applist);

		AppListItem[] data = new AppListItem[] {
				new AppListItem(false, getString(R.string.applistview_call),
						getString(R.string.applistview_edit_button)),
				new AppListItem(false, getString(R.string.applistview_sms), getString(R.string.applistview_edit_button)) };
		AppListItemAdapter adapter = new AppListItemAdapter(this, R.layout.applistview_item_row, data);

		appListView = (ListView) findViewById(R.id.applistview);

		View header = (View) getLayoutInflater().inflate(R.layout.applistview_header_row, null);
		appListView.addHeaderView(header);
		appListView.setAdapter(adapter);

	}
}
