package com.mk.rewind;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventArrayAdapter extends BaseAdapter implements OnClickListener
{
	private Context context;
	private final List<Event> eventList;

	public EventArrayAdapter(Context context, List<Event> values) {
		this.eventList = values;
		this.context = context;
	}

	public int getCount()
	{
		return eventList.size();
	}

	public Object getEvent(int position)
	{
		return eventList.get(position);
	}

	public long getEventId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Event entry = eventList.get(position);
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.event_row_full, null);
		}
		TextView year = (TextView) convertView.findViewById(R.id.year);
		year.setText(String.valueOf(entry.getYear()));

		TextView location = (TextView) convertView.findViewById(R.id.location);
		location.setText(entry.getLocation());

		TextView narration = (TextView) convertView.findViewById(R.id.narration);
		narration.setText(entry.getNarration());
		return convertView;

		/*
		 * LayoutInflater inflater = (LayoutInflater)
		 * context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 * 
		 * View rowView = inflater.inflate(R.layout.event_row_full, parent,
		 * false); TextView yearView = (TextView)
		 * rowView.findViewById(R.id.rowyeartext); TextView locationView =
		 * (TextView) rowView.findViewById(R.id.rowlocationtext); TextView
		 * narrationView = (TextView)
		 * rowView.findViewById(R.id.rownarrationtext);
		 * yearView.setText(eventList[position].getYear());
		 * locationView.setText(eventList[position].getLocation());
		 * narrationView.setText(eventList[position].getNarration()); return
		 * rowView;
		 */
	}

	@Override
	public Object getItem(int position)
	{
		return eventList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return eventList.get(position).getId();
	}

	@Override
	public void onClick(View arg0)
	{

	}
}
