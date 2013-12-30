package com.mk.rewind;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {

	public static final int ALL = -99999;
	public static final String ARCHIVED_KEY = "ARCHIVED";
	public static final String DISP_YEAR = "DISP_YEAR";
	public static final String DISP_YEAR_MONTH = "DISP_YEAR_MONTH";
	public static final String DISP_YEAR_MONTH_DATE = "DISP_YEAR_MONTH_DATE";
	public static final String DISP_MONTH_DATE = "DISP_MONTH_DATE";
	public static final String DISP_DATE = "DISP_DATE";
	
	public static final String[] MONTHS_SHORT = {"JAN", "FEB", "MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

	public static Map<String, Map<String, List<Event>>> filterEvents(List<Event> eventList) {
		Map<String, Map<String, List<Event>>> yearMap = new HashMap<String, Map<String, List<Event>>>(10);

		for (Event event : eventList) {

		}

		return yearMap;
	}

	public static String getMonthString(int month) {
		/*Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		return new SimpleDateFormat("MMM").format(cal.getTime());*/
		return MONTHS_SHORT[month];
	}
}
