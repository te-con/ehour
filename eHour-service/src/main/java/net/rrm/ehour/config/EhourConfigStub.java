/**
 * Created on Mar 19, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.config;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Stub for config 
 **/

public class EhourConfigStub implements EhourConfig, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3017492603595731493L;
	private	String[] 	availableTranslations;
	private	int			completeDayHours;
	private	Locale		currency = new Locale("nl", "NL");
	private String		localeLanguage ="en";
	private String		localeCountry = "NL";
	private	String		timeZone;
	private	boolean		showTurnover;
	private	String		mailFrom;
	private	String		mailSmtp;
	private boolean		demoMode = false;
	private boolean		dontForceLanguage;
	private boolean		initialized;
	
	/**
	 * @return the availableTranslations
	 */
	public String[] getAvailableTranslations()
	{
		return availableTranslations;
	}
	/**
	 * @param availableTranslations the availableTranslations to set
	 */
	public void setAvailableTranslations(String[] availableTranslations)
	{
		this.availableTranslations = availableTranslations;
	}
	/**
	 * @return the completeDayHours
	 */
	public int getCompleteDayHours()
	{
		return completeDayHours;
	}
	/**
	 * @param completeDayHours the completeDayHours to set
	 */
	public void setCompleteDayHours(int completeDayHours)
	{
		this.completeDayHours = completeDayHours;
	}
	/**
	 * @return the currency
	 */
	public Locale getCurrency()
	{
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Locale currency)
	{
		this.currency = currency;
	}

	/**
	 * @return the showTurnover
	 */
	public boolean isShowTurnover()
	{
		return showTurnover;
	}
	/**
	 * @param showTurnover the showTurnover to set
	 */
	public void setShowTurnover(boolean showTurnover)
	{
		this.showTurnover = showTurnover;
	}
	/**
	 * @return the timeZone
	 */
	public String getTimeZone()
	{
		return timeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}
	public String getMailFrom()
	{
		return mailFrom;
	}
	public String getMailSmtp()
	{
		return mailSmtp;
	}
	/**
	 * @param mailFrom the mailFrom to set
	 */
	public void setMailFrom(String mailFrom)
	{
		this.mailFrom = mailFrom;
	}
	/**
	 * @param mailSmtp the mailSmtp to set
	 */
	public void setMailSmtp(String mailSmtp)
	{
		this.mailSmtp = mailSmtp;
	}

	public Locale getLocale()
	{
		if (StringUtils.isBlank(localeLanguage))
		{
			localeLanguage = "en";
		}
		
		if (StringUtils.isBlank(localeCountry))
		{
			localeCountry = "NL";
		}
		
		return new Locale(localeLanguage, localeCountry);
	}
	
	public boolean isInDemoMode()
	{
		return demoMode;
	}
	/**
	 * @param demoMode the demoMode to set
	 */
	public void setDemoMode(boolean demoMode)
	{
		this.demoMode = demoMode;
	}
	/**
	 * @param localeLanguage the localeLanguage to set
	 */
	public void setLocaleLanguage(String localeLanguage)
	{
		this.localeLanguage = localeLanguage;
	}
	/**
	 * @param localeCountry the localeCountry to set
	 */
	public void setLocaleCountry(String localeCountry)
	{
		this.localeCountry = localeCountry;
	}
	public boolean isDontForceLanguage()
	{
		return dontForceLanguage;
	}
	/**
	 * @param dontForceLanguage the dontForceLanguage to set
	 */
	public void setDontForceLanguage(boolean dontForceLanguage)
	{
		this.dontForceLanguage = dontForceLanguage;
	}
	/**
	 * @return the initialized
	 */
	public boolean isInitialized()
	{
		return initialized;
	}
	/**
	 * @param initialized the initialized to set
	 */
	public void setInitialized(boolean initialized)
	{
		this.initialized = initialized;
	}
}
