package com.mk.rewind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper
{

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

	public static final String[] MONTHS_SHORT = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec" };
	public static final String[] MONTHS_LONG = { "January", "Febuary", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December" };

	public static String getShortMonthString(int month)
	{
		return MONTHS_SHORT[month];
	}

	public static String getLongMonthString(int month)
	{
		return MONTHS_LONG[month];
	}

	public static String convertToString(long[] ids)
	{
		StringBuilder str = new StringBuilder("");
		if (ids != null && ids.length > 0)
		{
			for (long id : ids)
			{
				str.append(id).append(",");
			}
			str.setCharAt(str.length() - 1, ' ');
		}
		return str.toString();
	}
}
