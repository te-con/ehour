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

package net.rrm.ehour.config.service;

import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.dao.ConfigurationDAO;
import net.rrm.ehour.config.domain.Configuration;

/**
 * TODO 
 **/

public class ConfigurationServiceImpl implements ConfigurationService
{
	private ConfigurationDAO	configDAO;
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigService#getConfiguration()
	 */
	public EhourConfig getConfiguration()
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
			else if (key.equalsIgnoreCase("currency"))
			{
				config.setCurrency(value);
			}
			else if (key.equalsIgnoreCase("localeCountry"))
			{
				config.setLocaleCountry(value);
			}
			else if (key.equalsIgnoreCase("localeLanguage"))
			{
				config.setLocaleLanguage(value);
			}
			else if (key.equalsIgnoreCase("showTurnOver"))
			{
				config.setShowTurnover(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("timeZone"))
			{
				config.setTimeZone(value);
			}
		}
		
		return config;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigService#persistConfiguration(java.util.List)
	 */
	public void persistConfiguration(EhourConfig config)
	{
		persistConfig("currency", config.getCurrency());
		persistConfig("completeDayHours", config.getCompleteDayHours());
		persistConfig("localeCountry", config.getLocaleCountry());
		persistConfig("localeLanguage", config.getLocaleLanguage());
		persistConfig("timeZone", config.getTimeZone());

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
