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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.rrm.ehour.ui.sort.LocaleComparator;

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
	private	boolean			translationsOnly;
	private	List<Locale>	availableLanguages;
	private	boolean			dontForceLocale;
	private	String			locale;
	private Locale			localeLanguage;
	
	public boolean isTranslationsOnly()
	{
		return translationsOnly;
	}
	public void setTranslationsOnly(boolean translationsOnly)
	{
		this.translationsOnly = translationsOnly;
	}
	public List<Locale> getAvailableLanguages()
	{
		return availableLanguages;
	}
	public void setAvailableLanguages(List<Locale> availableLanguages)
	{
		this.availableLanguages = availableLanguages;
	}
	public boolean isDontForceLocale()
	{
		return dontForceLocale;
	}
	public void setDontForceLocale(boolean dontForceLocale)
	{
		this.dontForceLocale = dontForceLocale;
	}
	public Locale getLocale()
	{
		return locale != null ? new Locale(locale) : Locale.getDefault();
	}
	public void setLocale(String locale)
	{
		this.locale = locale;
	}
	public void setLocale(Locale locale)
	{
		this.locale = locale.getLanguage();
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
	public List<String> getAvailableCurrencies()
	{
		List<Locale> 	locales = getAvailableLocales();
		Set<String>		currencies = new HashSet<String>(); 
		
		for (Locale locale : locales)
		{
			Currency curr = Currency.getInstance(locale);
			currencies.add(curr.getSymbol(locale));
		}
		
		List<String> currList = new ArrayList<String>(currencies);
		Collections.sort(currList);
		
		return currList;
	}
	/**
	 * @return the localeLanguage
	 */
	public Locale getLocaleLanguage()
	{
		return localeLanguage;
	}
	/**
	 * @param localeLanguage the localeLanguage to set
	 */
	public void setLocaleLanguage(Locale localeLanguage)
	{
		this.localeLanguage = localeLanguage;
	}
}
