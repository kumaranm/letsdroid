package com.mk.rewind;

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
	public static final String FROM_PAGE_KEY = "FROM_PAGE_KEY";
	public static final String MONTH_LIST_PAGE = "MONTH_LIST_PAGE";
	public static final String MONTH_ALL_LIST_PAGE = "MONTH_ALL_LIST_PAGE";
	public static final String YEAR_LIST_PAGE = "YEAR_LIST_PAGE";
	public static final String YEAR_ALL_LIST_PAGE = "YEAR_ALL_LIST_PAGE";
	public static final String ARCHIVED_LIST_PAGE = "ARCHIVED_LIST_PAGE";
	
	
	public static final String[] MONTHS_SHORT = {"Jan", "Feb", "Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

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
