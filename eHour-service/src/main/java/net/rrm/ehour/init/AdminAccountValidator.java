/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.init;

import javax.annotation.PostConstruct;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Checks whether the admin account is present when eHour is started first 
 **/
@Service
public class AdminAccountValidator
{
	private final static Logger LOGGER = Logger.getLogger(AdminAccountValidator.class);
	
	@Autowired
	private EhourConfig ehourConfig;
	@Autowired
	private UserService	userService;
	@Autowired
	private ConfigurationService	configService;
	
	
	/**
	 * Check whether the admin account exists.
	 */
	@PostConstruct
	public void checkAdminAccount()
	{
		if (!ehourConfig.isInitialized())
		{
			LOGGER.info("eHour not initialized, initializing...");
			updateAdminPassword();
		}
		else
		{
			LOGGER.info("eHour already initialized");
		}
	}
	
	/**
	 * Update the admin password to it's default value
	 */
	private void updateAdminPassword()
	{
		LOGGER.info("Setting password of admin account to default value");
		
		User user = userService.getUser("admin");
		
		if (user == null)
		{
			LOGGER.warn("Admin account not found, maybe the SQL scripts failed to run properly?");
		}
		else
		{
			user.setUpdatedPassword("admin");
			try
			{
				userService.persistUser(user);
				LOGGER.info("Password set to default value: " + user.getPassword());
				
				setEhourInitialized();
			} catch (PasswordEmptyException e)
			{
				LOGGER.error("Supplied password is empty - how can this happen?");
			} catch (ObjectNotUniqueException e)
			{
				LOGGER.error("Admin account already exists", e);
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

		LOGGER.info("eHour's state to initialized");
		
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
