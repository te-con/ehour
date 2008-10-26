/**
 * Created on Nov 25, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * TODO 
 **/

public class TestLocale
{
	public static void main(String[] argv)
	{
		Locale[] locales = Locale.getAvailableLocales();
		for (int i = 0; i < locales.length; i++)
		{
			if (locales[i].getCountry() != null && locales[i].getCountry().length() == 2)
			{
				System.out.println(locales[i].toString() + "="+  locales[i].getDisplayCountry() + "." 
							+ locales[i].getVariant()
							+ locales[i].getDisplayLanguage());
				
				Currency c = Currency.getInstance(locales[i]);
				System.out.println(c.getSymbol(locales[i]) + "-" + c.getCurrencyCode());
				System.out.println("--");
			}
		}
		
//		Locale locale = new Locale("nl_NL");
		
		Locale locale = new Locale("en", "IE");
		Currency c = Currency.getInstance(locale);
		System.out.println(c.getSymbol());
		System.out.println(locale.getDisplayCountry());
		
		
//		Currency curr = Currency.getInstance(locale);
//		System.out.println(curr.getSymbol(locale));
//		System.out.println(curr.getCurrencyCode());
//		
		Date d= new Date();
		
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.DAY_OF_YEAR, 30);
		cal.set(Calendar.YEAR, 2007);
		
		DateFormat formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);

//		String
		
		
		System.out.println(formatter.format(d));
		
	}
}
