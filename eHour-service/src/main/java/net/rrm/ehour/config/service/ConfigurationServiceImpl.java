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

package net.rrm.ehour.config.service;

import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.dao.ConfigurationDAO;
import net.rrm.ehour.config.domain.Configuration;

import org.apache.log4j.Logger;

/**
 * Configuration service
 **/

public class ConfigurationServiceImpl implements ConfigurationService
{
	private ConfigurationDAO	configDAO;
	private	Logger				logger = Logger.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigService#getConfiguration()
	 */
	public EhourConfigStub getConfiguration()
	{
		List<Configuration> configs = configDAO.findAll();
		EhourConfigStub		config = new EhourConfigStub();
		String				key, value;
		
		for (Configuration configuration : configs)
		{
			key = configuration.getConfigKey();
			value = configuration.getConfigValue();
			
			if (key.equalsIgnoreCase("availableTranslations"))
			{
				config.setAvailableTranslations(value.split(","));
			}
			else if (key.equalsIgnoreCase("completeDayHours"))
			{
				config.setCompleteDayHours(Integer.parseInt(value));
			}
			else if (key.equalsIgnoreCase("localeCurrency"))
			{
				config.setCurrency(value);
			}
			else if (key.equalsIgnoreCase("localeLanguage"))
			{
				config.setLocaleLanguage(value);
			}
			else if (key.equalsIgnoreCase("localeCountry"))
			{
				config.setLocaleCountry(value);
			}
			else if (key.equalsIgnoreCase("showTurnOver"))
			{
				config.setShowTurnover(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("timeZone"))
			{
				config.setTimeZone(value);
			}
			else if (key.equalsIgnoreCase("mailFrom"))
			{
				config.setMailFrom(value);
			}
			else if (key.equalsIgnoreCase("mailSmtp"))
			{
				config.setMailSmtp(value);
			}
			else if (key.equalsIgnoreCase("rememberMeAvailable"))
			{
				config.setRememberMeAvailable(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("demoMode"))
			{
				config.setDemoMode(Boolean.parseBoolean(value));
			}			
		}
		
		return config;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigService#persistConfiguration(java.util.List)
	 */
	public void persistConfiguration(EhourConfig config)
	{
		logger.debug("Persisting config");
		persistConfig("localeCurrency", config.getCurrency());
		
		// TODO change to Integer and use null
		if (config.getCompleteDayHours() != 0)
		{
			persistConfig("completeDayHours", config.getCompleteDayHours());
		}
		persistConfig("localeCountry", config.getLocale().getCountry());
		persistConfig("localeLanguage", config.getLocale().getLanguage());
		persistConfig("dontForceLanguage", config.isDontForceLanguage());
		persistConfig("showTurnOver", config.isShowTurnover());
		persistConfig("mailFrom", config.getMailFrom());
		persistConfig("mailSmtp", config.getMailSmtp());
		persistConfig("rememberMeAvailable", config.isRememberMeAvailable());

	}
	
	private void persistConfig(String key, String value)
	{
		Configuration config = new Configuration();
		config.setConfigKey(key);
		config.setConfigValue(value);
		
		configDAO.persist(config);
	}

	private void persistConfig(String key, boolean value)
	{
		persistConfig(key, Boolean.toString(value));
	}	

	private void persistConfig(String key, int value)
	{
		persistConfig(key, Integer.toString(value));
	}	

	/**
	 * @param configDAO the configDAO to set
	 */
	public void setConfigDAO(ConfigurationDAO configDAO)
	{
		this.configDAO = configDAO;
	}

}
