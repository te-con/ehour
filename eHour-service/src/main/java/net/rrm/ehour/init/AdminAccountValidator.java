/**
 * Created on Jan 28, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.init;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;

/**
 * Checks whether the admin account is present when eHour is started first 
 **/

public class AdminAccountValidator
{
	private final static Logger logger = Logger.getLogger(AdminAccountValidator.class);
	private EhourConfig ehourConfig;
	private UserService	userService;
	private ConfigurationService	configService;
	
	
	/**
	 * Check whether the admin account exists.
	 */
	public void checkAdminAccount()
	{
		if (!ehourConfig.isInitialized())
		{
			logger.info("eHour not initialized, initializing...");
			updateAdminPassword();
		}
		else
		{
			logger.info("eHour already initialized");
		}
	}
	
	/**
	 * Update the admin password to it's default value
	 */
	private void updateAdminPassword()
	{
		logger.info("Setting password of admin account to default value");
		
		User user = userService.getUser("admin");
		
		if (user == null)
		{
			logger.warn("Admin account not found, maybe the SQL scripts failed to run properly?");
		}
		else
		{
			user.setPassword("admin");
			try
			{
				userService.persistUser(user);
				logger.info("Password set to default value.");
				
				setEhourInitialized();
			} catch (PasswordEmptyException e)
			{
				e.printStackTrace();
			} catch (ObjectNotUniqueException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Update eHour's config to initialized
	 */
	private void setEhourInitialized()
	{
		EhourConfigStub config =  configService.getConfiguration();
		config.setInitialized(true);
		configService.persistConfiguration(config);

		logger.info("eHour's state to initialized");
		
	}
	
	/**
	 * @param ehourConfig the ehourConfig to set
	 */
	public void setEhourConfig(EhourConfig ehourConfig)
	{
		this.ehourConfig = ehourConfig;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @param configService the configService to set
	 */
	public void setConfigService(ConfigurationService configService)
	{
		this.configService = configService;
	}	
}
