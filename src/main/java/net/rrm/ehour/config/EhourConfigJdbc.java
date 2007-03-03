/**
 * Created on Jan 27, 2007
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
	
	public int getCompleteDayHours()
	{
		return this.getInt("completeDayHours");
	}

	public boolean isShowTurnover()
	{
		return this.getBoolean("showTurnOver");
	}
	
	public String getTimeZone()
	{
		return this.getString("timezone");
	}

	public String getLocale()
	{
		return this.getString("locale");	
	}

}
