/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.admin.config.dto;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.common.sort.LocaleComparator;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Configuration backing bean
 * Created on Apr 21, 2009, 3:13:25 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */

public class MainConfigBackingBean implements Serializable
{
	private static final long serialVersionUID = -682573285773646807L;
	private	boolean			translationsOnly = false;
	private boolean			smtpAuthentication = false;
	private EhourConfigStub	config;
	private Date			firstWeekStart;

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
	public static List<Locale> getAvailableLocales()
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
	public static List<Locale> getAvailableCurrencies()
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

		return new ArrayList<Locale>(currencyLocales);
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
