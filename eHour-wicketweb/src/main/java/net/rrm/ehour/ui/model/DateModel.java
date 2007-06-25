/**
 * Created on Jun 3, 2007
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

package net.rrm.ehour.ui.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.rrm.ehour.config.EhourConfig;

import org.apache.wicket.model.Model;

/**
 * TODO 
 **/

public class DateModel extends Model
{
	public final static int	DATESTYLE_LONG = 1;
	public final static int	DATESTYLE_MONTHONLY = 2;
	public final static int DATESTYLE_DAYLONG = 3;
	
	private static final long serialVersionUID = 431440606497572025L;
	private Date				value;
	private	final SimpleDateFormat	dateFormatter;
	
	/**
	 * 
	 * @param calendar
	 * @param config
	 */
	public DateModel(Calendar calendar, EhourConfig config)
	{
		this(calendar.getTime(), config, DATESTYLE_LONG);
	}

	/**
	 * 
	 * @param date
	 * @param config
	 * @param dateStyle
	 */
	public DateModel(Date date, EhourConfig config)
	{
		this(date, config, DATESTYLE_LONG);
	}
	
	
	/**
	 * 
	 * @param date
	 * @param config
	 * @param dateStyle
	 */
	public DateModel(Date date, EhourConfig config, int dateStyle)
	{
		this.value = date;
		
		switch (dateStyle)
		{
			case DATESTYLE_MONTHONLY:
				dateFormatter = new SimpleDateFormat("MMMM yyyy", config.getLocale());
				break;
			case DATESTYLE_DAYLONG:
				dateFormatter = new SimpleDateFormat("EEE d", config.getLocale());
				break;
			default:
				dateFormatter = new SimpleDateFormat("dd MMM yyyy", config.getLocale());
				break;
		}
	}
	
	/**
	 * 
	 * @param calendar
	 * @param config
	 * @param dateStyle
	 */
	public DateModel(Calendar calendar, EhourConfig config, int dateStyle)
	{
		this(calendar.getTime(), config, dateStyle);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.Model#getObject()
	 */
	@Override
	public Object getObject()
	{
		return (value == null) ? "&infin;" : dateFormatter.format(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.Model#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(Object value)
	{
		// TODO parse it properly
		this.value = (Date)value;
	}
}
