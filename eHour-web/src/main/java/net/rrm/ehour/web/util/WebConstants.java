package net.rrm.ehour.web.util;

import java.util.HashMap;
import java.util.Map;

public class WebConstants
{
	public final static String	ROLE_CONSULTANT = "ROLE_CONSULTANT";
	public final static String	ROLE_ADMIN = "ROLE_ADMIN";
	public final static String	ROLE_REPORT = "ROLE_REPORT";
	
	public final static String	SESSION_KEY_USER = "eHourUser";

	public final static	String	SESSION_CALENDAR_YEAR_KEY = "cps_year";
	public final static	String	SESSION_CALENDAR_MONTH_KEY = "cps_month";
		
	public final static String REQUEST_NAVCAL_NEXT = "navCalNextMonth";
	public final static String REQUEST_NAVCAL_URL = "navCalURL";
	
	
	public final static String SESSION_NAVCAL_VALID = "navCalValidity";

	public final static int PERIOD_WEEK = 1;
	public final static int PERIOD_MONTH = 2;
	public final static int PERIOD_QUARTER = 3;
	public final static int PERIOD_YEAR = 4;
	public final static int PERIOD_3YEAR = 5;
	public final static int PERIOD_5YEAR = 6;
	
	/**
	 * Get currencies
	 * @return
	 */
	public static Map<String, String> getCurrencies()
	{
		Map<String, String> currencies = new HashMap<String,String>();
		
		currencies.put("Dollar", "$");
		currencies.put("Euro", "&#8364;");
		currencies.put("Yen", "&yen;");
		currencies.put("Pound", "&pound;");
		
		return currencies;
	}
}
