/**
 * Created on 18-dec-2006
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
import java.util.Properties;

import net.rrm.ehour.audit.AuditType;

/**
 * Configuration based on .properties file
 **/

public class EhourConfigProps implements EhourConfig
{
	private	Properties	props;

	public void setPropertiesConfig(Properties props)
	{
		this.props = props;
	}
	
	public float getCompleteDayHours()
	{
		return Float.parseFloat(props.getProperty("completeDayHours"));
	}

	public boolean isShowTurnover()
	{
		return Boolean.valueOf(props.getProperty("showTurnOver"));
	}

	public String getTimeZone()
	{
		return props.getProperty("timezone");
	}

	public Locale getCurrency()
	{
		String split = props.getProperty("currency");
		
		String[] splitted = split.split("_");
		
		return new Locale(splitted[0], splitted[1]);

	}

	public String[] getAvailableTranslations()
	{
		return props.getProperty("localeLanguage").split(",");
	}

	public String getMailFrom()
	{
		return props.getProperty("mailFrom");
	}

	public String getMailSmtp()
	{
		return props.getProperty("mailSmtp");
	}
	
	public Locale getLocale()
	{
		String country = props.getProperty("localeCountry");
		String language = props.getProperty("localeLanguage");
		
		return new Locale(language, country);	
	}

	public boolean isInDemoMode()
	{
		return Boolean.valueOf(props.getProperty("demoMode"));
	}

	public boolean isDontForceLanguage()
	{
		return Boolean.valueOf(props.getProperty("dontForceLanguage"));
	}

	public boolean isInitialized()
	{
		Boolean init = Boolean.valueOf(props.getProperty("initialized"));
		
		return init == null ? true : init;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getSmtpPassword()
	 */
	public String getSmtpPassword() {
		return props.getProperty("smtpPassword");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getSmtpUsername()
	 */
	public String getSmtpUsername() {
		return props.getProperty("smtpUsername");
	}
	
	public String getSmtpPort()
	{
		return props.getProperty("smtpPort");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getFirstDayOfWeek()
	 */
	public int getFirstDayOfWeek()
	{
		return Integer.valueOf(props.getProperty("firstDayOfWeek"));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getAuditType()
	 */
	public AuditType getAuditType()
	{
		return AuditType.fromString(props.getProperty("auditType"));
	}
	
}
