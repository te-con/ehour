/**
 * Created on Mar 17, 2007
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

package net.rrm.ehour.web.admin.config.action;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.service.ConfigurationService;

import org.apache.struts.action.Action;

/**
 * TODO 
 **/

public abstract class BaseConfigAction extends Action
{
	protected EhourConfig			config;
	protected ConfigurationService	configService;

	/**
	 * @return the config
	 */
	public EhourConfig getConfig()
	{
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EhourConfig config)
	{
		this.config = config;
	}

	/**
	 * @param configService the configService to set
	 */
	public void setConfigService(ConfigurationService configService)
	{
		this.configService = configService;
	}
	
}
