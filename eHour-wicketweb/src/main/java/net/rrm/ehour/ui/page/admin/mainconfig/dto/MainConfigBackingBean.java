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

package net.rrm.ehour.ui.page.admin.mainconfig.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.sort.LocaleComparator;

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
	private	boolean			dontForceLocale;
	private	String			locale;
	private Locale			localeLanguage;
	private EhourConfigStub	config;

	/**
	 * 
	 * @param config
	 */
	public MainConfigBackingBean(EhourConfigStub config)
	{
		this.config = config;
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


	public boolean isDontForceLocale()
	{
		return dontForceLocale;
	}
	public void setDontForceLocale(boolean dontForceLocale)
	{
		this.dontForceLocale = dontForceLocale;
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
	public List<CurrencyChoice> getAvailableCurrencies()
	{
		List<Locale> 			locales = getAvailableLocales();
		Map<String, CurrencyChoice>	currencies = new HashMap<String, CurrencyChoice>();
		for (Locale locale : locales)
		{
			Currency curr = Currency.getInstance(locale);
			
			if (!curr.getCurrencyCode().equals("EUR"))
			{
				currencies.put(locale.getCountry(), new CurrencyChoice(locale.getDisplayCountry() + ": " + curr.getSymbol(locale), 
													curr.getCurrencyCode()));
			}
		}
		
		List<CurrencyChoice> uniqueCurrencies = new ArrayList<CurrencyChoice>(currencies.values());
		
		uniqueCurrencies.add(new CurrencyChoice("Euro: &#8364;", "EUR"));
		
		Collections.sort(uniqueCurrencies);
		
		return uniqueCurrencies;
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

	/**
	 * 
	 * @author Thies
	 * (sometimes getters & setters are overrated)
	 *
	 */
	public class CurrencyChoice implements Comparable<CurrencyChoice>
	{
		public String displayName;
		public String localizedSymbol;
		
		public CurrencyChoice(String displayName, String localizedSymbol)
		{
			this.displayName = displayName;
			this.localizedSymbol = localizedSymbol;
		
		}

		public int compareTo(CurrencyChoice o)
		{
			return displayName.compareTo(o.displayName);
		}
	}

	/**
	 * @return the currencyChoice
	 */
	public CurrencyChoice getCurrencyChoice()
	{
		Currency curr = Currency.getInstance(config.getCurrency());
		
		Locale locale = config.getLocale();
		
		CurrencyChoice choice = new CurrencyChoice(locale.getDisplayCountry() + ": " + curr.getSymbol(locale), 
				 									config.getCurrency());
		
		return choice;
	}
	/**
	 * @param currencyChoice the currencyChoice to set
	 */
	public void setCurrencyChoice(CurrencyChoice currencyChoice)
	{
		this.config.setCurrency(currencyChoice.localizedSymbol);
	}
	
	public void setCurrency(String currencySymbol)
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
}
