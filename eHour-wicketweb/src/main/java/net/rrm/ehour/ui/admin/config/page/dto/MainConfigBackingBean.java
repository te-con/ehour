/**
 * Created on Jul 18, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.admin.config.page.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.sort.LocaleComparator;
import net.rrm.ehour.util.DateUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * TODO 
 **/

public class MainConfigBackingBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -682573285773646807L;
	private	boolean			translationsOnly = false;
	private boolean			smtpAuthentication = false;
	private EhourConfigStub	config;
	private Date			firstWeekStart;

	/**
	 * 
	 * @param config
	 */
	public MainConfigBackingBean(EhourConfigStub config)
	{
		this.config = config;
		
		smtpAuthentication = !StringUtils.isBlank(config.getSmtpUsername()) 
								|| !StringUtils.isBlank(config.getSmtpPassword()); 
		
		Calendar cal = new GregorianCalendar();
		DateUtil.dayOfWeekFix(cal);
		cal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
		firstWeekStart = cal.getTime();
	}
	
	
	/**
	 * Get available languages
	 * @param config
	 * @param onlyAvailable
	 * @return
	 */
	public List<Locale> getAvailableLanguages()
	{
		Locale[]			locales = Locale.getAvailableLocales();
		Map<String, Locale>	localeMap = new HashMap<String, Locale>();
		
		// remove all variants
		for (Locale locale : locales)
		{
			if (isTranslationsOnly()
					&& !ArrayUtils.contains(config.getAvailableTranslations(), locale.getLanguage()))
			{
				continue;
			}
			
			if (localeMap.containsKey(locale.getLanguage()))
			{
				if (locale.getDisplayName().indexOf('(') != -1)
				{
					continue;
				}
			}

			localeMap.put(locale.getLanguage(), locale);
		}
		
		SortedSet<Locale>	localeSet = new TreeSet<Locale>(new LocaleComparator(LocaleComparator.CompareType.LANGUAGE));
		
		for (Locale locale : localeMap.values())
		{
			localeSet.add(locale);
		}
		
		return new ArrayList<Locale>(localeSet);
	}
	
	public boolean isTranslationsOnly()
	{
		return translationsOnly;
	}
	public void setTranslationsOnly(boolean translationsOnly)
	{
		this.translationsOnly = translationsOnly;
	}

	
	/**
	 * Available locales
	 * @return
	 */
	public List<Locale> getAvailableLocales()
	{
		List<Locale> locales = new ArrayList<Locale>();
		
		for (Locale locale : Locale.getAvailableLocales())
		{
			if (!StringUtils.isBlank(locale.getDisplayCountry()))
			{
				locales.add(locale);
			}
		}
		
		Collections.sort(locales, new LocaleComparator(LocaleComparator.CompareType.COUNTRY));
		
		return locales;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Locale> getAvailableCurrencies()
	{
		List<Locale>		locales = getAvailableLocales();
		SortedSet<Locale>	currencyLocales = new TreeSet<Locale>(new Comparator<Locale>()
				{
					public int compare(Locale o1, Locale o2)
					{
						Currency curr1 = Currency.getInstance(o1);
						Currency curr2 = Currency.getInstance(o2);
						
						return (o1.getDisplayCountry() + ": " +  curr1.getSymbol(o1))
								.compareTo(o2.getDisplayCountry() + ": " +  curr2.getSymbol(o2));
					}
				}
		);
		
		for (Locale locale : locales)
		{
			if (!StringUtils.isBlank(locale.getCountry()))
			{
				currencyLocales.add(locale);
			}
		}

		List<Locale> locList = new ArrayList<Locale>(currencyLocales);
		
		return locList;
	}
	/**
	 * @return the localeLanguage
	 */
	public Locale getLocaleLanguage()
	{
		return config.getLocale();
	}
	/**
	 * @param localeLanguage the localeLanguage to set
	 */
	public void setLocaleLanguage(Locale localeLanguage)
	{
		config.setLocaleLanguage(localeLanguage.getLanguage());
	}
	
	public Locale getLocaleCountry()
	{
		return config.getLocale();
	}

	public void setLocaleCountry(Locale localeCountry)
	{
		config.setLocaleCountry(localeCountry.getCountry());
		config.setLocaleLanguage(localeCountry.getLanguage());
	}
	
	public void setCurrency(Locale currencySymbol)
	{
		this.config.setCurrency(currencySymbol);
	}

	/**
	 * @return the config
	 */
	public EhourConfig getConfig()
	{
		return config;
	}

	public boolean isSmtpAuthentication() {
		return smtpAuthentication;
	}

	public void setSmtpAuthentication(boolean smtpAuthentication) {
		this.smtpAuthentication = smtpAuthentication;
	}



	/**
	 * @return the firstWeekStart
	 */
	public Date getFirstWeekStart()
	{
		return firstWeekStart;
	}



	/**
	 * @param firstWeekStart the firstWeekStart to set
	 */
	public void setFirstWeekStart(Date firstWeekStart)
	{
		this.firstWeekStart = firstWeekStart;
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(firstWeekStart);
		config.setFirstDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
	}
}
