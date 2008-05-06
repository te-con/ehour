/**
 * Created on Jan 27, 2007
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

import java.util.Locale;

import javax.sql.DataSource;

import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.log4j.Logger;

/**
 * Config from database
 **/

public class EhourConfigJdbc extends DatabaseConfiguration implements EhourConfig
{
	private	Logger	logger = Logger.getLogger(this.getClass());
	
	public EhourConfigJdbc(DataSource datasource, String table, String keyColumn, String valueColumn)
	{
		super(datasource, table, keyColumn, valueColumn);
	
		logger.info("Configuration loaded from database");
	}
	
	public float getCompleteDayHours()
	{
		return this.getFloat("completeDayHours", 8);
	}

	public boolean isShowTurnover()
	{
		return this.getBoolean("showTurnOver", true);
	}
	
	public String getTimeZone()
	{
		return this.getString("timezone");
	}

	public Locale getCurrency()
	{
		String split = this.getString("localeCurrency", "nl_NL");
		String[] splitted = split.split("_");
		
		return new Locale(splitted[0], splitted[1]);
	}

	public String[] getAvailableTranslations()
	{
		return this.getString("availableTranslations", "en,nl").split(",");
	}

	public String getMailFrom()
	{
		return this.getString("mailFrom", "devnull@devnull.com");
	}

	public String getMailSmtp()
	{
		return this.getString("mailSmtp", "127.0.0.1");
	}

	public Locale getLocale()
	{
		String country = this.getString("localeCountry", "US");
		String language = this.getString("localeLanguage", "en");
		
		return new Locale(language, country);
	}

	public boolean isInDemoMode()
	{
		return this.getBoolean("demoMode", false);
	}

	public boolean isDontForceLanguage()
	{
		return this.getBoolean("dontForceLanguage", false);
	}

	public boolean isInitialized()
	{
		return this.getBoolean("initialized", true);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getSmtpPassword()
	 */
	public String getSmtpPassword() {
		return this.getString("smtpUsername");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getSmtpUsername()
	 */
	public String getSmtpUsername() {
		return this.getString("smtpPassword");
	}

	/**
	 * 
	 */
	public String getSmtpPort() {
		return this.getString("smtpPort", "25");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getFirstDayOfWeek()
	 */
	public int getFirstDayOfWeek()
	{
		return (int)this.getFloat("firstDayOfWeek", 1);

	}

}
