package com.mk.rewind;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventArrayAdapter extends BaseAdapter {
	private Context context;
	private final List<Event> eventList;
	private String display = Helper.DISP_YEAR_MONTH_DATE;

	public EventArrayAdapter(Context context, List<Event> values, String display) {
		this.eventList = values;
		this.context = context;
		if (display != null) {
			this.display = display;
		}
	}

	public int getCount() {
		return eventList.size();
	}

	public Object getEvent(int position) {
		return eventList.get(position);
	}

	public long getEventId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Event entry = eventList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.event_row_full, null);
		}
		TextView year = (TextView) convertView.findViewById(R.id.year);

		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(EventEditActivity.DATE_TIME_FORMAT);
		Date date = null;
		Calendar cal = Calendar.getInstance();
		try {
			date = dateTimeFormat.parse(entry.getDateTime());
			cal.setTime(date);
		} catch (java.text.ParseException e) {
			Log.e("EventArrayAdapter", e.getMessage(), e);
		}

		String str = String.valueOf(entry.getYear()) + "\n" + Helper.getShortMonthString(cal.get(Calendar.MONTH)) + "\n"
				+ String.valueOf(entry.getDate());
		// year.setText(String.valueOf(entry.getYear()));

		if (display.equals(Helper.DISP_YEAR)) {
			str = String.valueOf(entry.getYear());
		} else if (display.equals(Helper.DISP_YEAR_MONTH)) {
			str = String.valueOf(entry.getYear()) + "\n" + Helper.getShortMonthString(cal.get(Calendar.MONTH));
		} else if (display.equals(Helper.DISP_MONTH_DATE)) {
			str = Helper.getShortMonthString(cal.get(Calendar.MONTH)) + "\n" + String.valueOf(entry.getDate());
		} else if (display.equals(Helper.DISP_DATE)) {
			str = String.valueOf(entry.getDate());
		}
		year.setText(str);

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
	public Object getItem(int position) {
		return eventList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return eventList.get(position).getId();
	}

}
