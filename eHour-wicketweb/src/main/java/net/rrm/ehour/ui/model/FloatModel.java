/**
 * Created on Jun 2, 2007
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

import java.text.NumberFormat;

import net.rrm.ehour.config.EhourConfig;

import org.apache.wicket.model.IModel;

/**
 * Convert floats to rounded 2 digits 
 **/

public class FloatModel extends AbstractNumberModel
{
	private static final long serialVersionUID = -3297133594178935106L;
	private String defaultValue = "--";
	
	/**
	 * 
	 * @param value
	 * @param config
	 */
	public FloatModel(Number value, EhourConfig config)
	{
		super(value);
		
		formatter = NumberFormat.getNumberInstance(config.getLocale());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
	}
	
	/**
	 * 
	 * @param value
	 * @param config
	 */
	public FloatModel(IModel model, EhourConfig config)
	{
		super(model);
		
		formatter = NumberFormat.getNumberInstance(config.getLocale());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
	}
	
	/**
	 * 
	 * @param value
	 * @param config
	 */
	public FloatModel(IModel model, EhourConfig config, String defaultValue)
	{
		this(model, config);
		
		this.defaultValue = defaultValue;
	}		
	
	@Override
	protected String getDefaultValue()
	{
		return defaultValue;
	}
}
