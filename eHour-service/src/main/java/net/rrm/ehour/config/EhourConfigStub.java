/**
 * Created on Mar 19, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.config;

import java.io.Serializable;
import java.util.Locale;

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
	private	String		currency = "EUR";
	private	String		localeCountry;
	private String 		localeLanguage;
	private	String		timeZone;
	private	boolean		showTurnover;
	private	String		mailFrom;
	private	String		mailSmtp;
	private	boolean		rememberMeAvailable = true;
	private boolean		demoMode = false;
	
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
	public String getCurrency()
	{
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	/**
	 * @return the localeCountry
	 */
	public String getLocaleCountry()
	{
		return localeCountry;
	}
	/**
	 * @param localeCountry the localeCountry to set
	 */
	public void setLocaleCountry(String localeCountry)
	{
		this.localeCountry = localeCountry;
	}
	/**
	 * @return the localeLanguage
	 */
	public String getLocaleLanguage()
	{
		return localeLanguage;
	}
	/**
	 * @param localeLanguage the localeLanguage to set
	 */
	public void setLocaleLanguage(String localeLanguage)
	{
		this.localeLanguage = localeLanguage;
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
	public boolean isRememberMeAvailable()
	{
		return this.rememberMeAvailable;
	}

	public void setRememberMeAvailable(boolean rma)
	{
		this.rememberMeAvailable = rma;
	}

	public Locale getLocale()
	{
		return getLocaleLanguage() != null ? new Locale(getLocaleLanguage()) : Locale.getDefault();
	}
	public boolean isInDemoMode()
	{
		return demoMode;
	}
	
}
