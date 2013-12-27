package com.mk.rewinder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class AppListItemAdapter extends ArrayAdapter<AppListItem>
{
	Context context;
	int layoutResourceId;
	AppListItem[] data = null;

	public AppListItemAdapter(Context context, int layoutResourceId, AppListItem[] data) {
		super(context, layoutResourceId, data);

		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		AppListItemHolder holder = null;

		if (row == null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new AppListItemHolder();
			holder.chkBox = (CheckBox) row.findViewById(R.id.applistview_chkbox);
			holder.appName = (TextView) row.findViewById(R.id.applistview_app);
			holder.button = (Button) row.findViewById(R.id.applistview_button);

			row.setTag(holder);
		}
		else
		{
			holder = (AppListItemHolder) row.getTag();
		}

		AppListItem listItem = data[position];
		holder.chkBox.setChecked(listItem.checkBox);
		holder.appName.setText(listItem.appName);
		holder.button.setText(listItem.button);
		
		return row;
	}

	static class AppListItemHolder
	{
		CheckBox chkBox;
		TextView appName;
		Button button;
	}
}
