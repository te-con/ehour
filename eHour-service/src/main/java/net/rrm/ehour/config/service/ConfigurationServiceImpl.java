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
import java.util.Locale;

import net.rrm.ehour.audit.Auditable;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.dao.ConfigurationDAO;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.Configuration;

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
				config.setCompleteDayHours(Float.parseFloat(value));
			}
			else if (key.equalsIgnoreCase("localeCurrency"))
			{
				Locale locale;
				
				if (value != null && value.contains("_"))
				{
					String[] split = value.split("_");
					locale = new Locale(split[0], split[1]);
				}
				else
				{
					locale = new Locale("nl", "NL");
				}
				
				config.setCurrency(locale);
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
			else if (key.equalsIgnoreCase("smtpUsername"))
			{
				config.setSmtpUsername(value);
			}			
			else if (key.equalsIgnoreCase("smtpPassword"))
			{
				config.setSmtpPassword(value);
			}			
			else if (key.equalsIgnoreCase("smtpPort"))
			{
				config.setSmtpPort(value);
			}			
			else if (key.equalsIgnoreCase("demoMode"))
			{
				config.setDemoMode(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("initialized"))
			{
				config.setInitialized(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("firstDayOfWeek"))
			{
				config.setFirstDayOfWeek( (int)(Float.parseFloat(value)));
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
		persistConfig("localeCurrency", config.getCurrency().getLanguage() + "_" + config.getCurrency().getCountry());
		
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
		persistConfig("smtpUsername", config.getSmtpUsername());
		persistConfig("smtpPassword", config.getSmtpPassword());
		persistConfig("smtpPort", config.getSmtpPort());
		persistConfig("initialized", config.isInitialized());
		persistConfig("firstDayOfWeek", config.getFirstDayOfWeek());
	}
	
	private void persistConfig(String key, String value)
	{
		Configuration config = new Configuration();
		config.setConfigKey(key);
		config.setConfigValue(value == null ? "" : value);
		
		configDAO.persist(config);
	}

	private void persistConfig(String key, boolean value)
	{
		persistConfig(key, Boolean.toString(value));
	}	

	private void persistConfig(String key, float value)
	{
		persistConfig(key, Float.toString(value));
	}	

	/**
	 * @param configDAO the configDAO to set
	 */
	public void setConfigDAO(ConfigurationDAO configDAO)
	{
		this.configDAO = configDAO;
	}

}
