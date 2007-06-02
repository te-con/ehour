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
import java.util.Currency;

import net.rrm.ehour.config.EhourConfig;

import org.apache.log4j.Logger;

/**
 * Currency formatting model
 */

public class CurrencyModel extends AbstractNumberModel
{
	private static final long serialVersionUID = -3297133594178935106L;
	private	transient Logger	logger = Logger.getLogger(CurrencyModel.class);

	/**
	 * 
	 * @param value
	 * @param config
	 */
	public CurrencyModel(Number value, EhourConfig config)
	{
		Currency	currency;
		
		this.value = value;
		
		// TODO get it display the &euro; sign instead of EUR
		try
		{
			currency = Currency.getInstance(config.getCurrency());
		}
		catch (IllegalArgumentException iae)
		{
			logger.warn("Old currency selected, update the currency in the admin");
			currency = Currency.getInstance("EUR");
		}
		
		formatter = NumberFormat.getCurrencyInstance(config.getLocale());
		formatter.setCurrency(currency);
	}
}
