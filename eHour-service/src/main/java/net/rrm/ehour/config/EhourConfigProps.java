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
	
	public int getCompleteDayHours()
	{
		return Integer.parseInt(props.getProperty("completeDayHours"));
	}

	public boolean isShowTurnover()
	{
		return Boolean.valueOf(props.getProperty("showTurnOver"));
	}

	public String getTimeZone()
	{
		return props.getProperty("timezone");
	}

	public String getLocaleCurrency()
	{
		return props.getProperty("localeCurrency");
	}

	public String getLocaleLanguage()
	{
		return props.getProperty("localeLanguage");
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

	public boolean isRememberMeAvailable()
	{
		return Boolean.valueOf(props.getProperty("rememberMeAvailable"));
	}
	
	public Locale getLocale()
	{
		String localeCode = props.getProperty("locale");
		
		String[] localeCodes = localeCode.split("_");
		
		return new Locale(localeCodes[0], localeCodes[1]);	
	}

	public boolean isInDemoMode()
	{
		return Boolean.valueOf(props.getProperty("demoMode"));
	}
	
}
