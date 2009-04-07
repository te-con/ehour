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

package net.rrm.ehour.ui.common.model;

import java.text.NumberFormat;
import java.util.Currency;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.log4j.Logger;
import org.apache.wicket.model.Model;

/**
 * Currency formatting model
 */

public class CurrencyModel extends AbstractNumberModel
{
	private static final long serialVersionUID = -3297133594178935106L;
	private	final static Logger	logger = Logger.getLogger(CurrencyModel.class);
	private EhourConfig	config;
	
	/**
	 * Lazy instantation, provide value later
	 * @param config
	 */
	public CurrencyModel()
	{
		super(new Model());
	}
	
	/**
	 * Static instantion
	 * @param value
	 * @param config
	 */
	public CurrencyModel(Number value, EhourConfig config)
	{
		super(value);
		
		this.config = config;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.model.AbstractNumberModel#getFormatter()
	 */
	@Override
	protected NumberFormat getFormatter()
	{
		Currency	currency;
		
		if (config == null)
		{
			config = EhourWebSession.getSession().getEhourConfig();
		}
		
		try
		{
			currency = Currency.getInstance(config.getCurrency());
		}
		catch (IllegalArgumentException iae)
		{
			logger.warn("Old currency selected, update the currency in the admin");
			currency = Currency.getInstance("EUR");
		}
		
		formatter = NumberFormat.getCurrencyInstance(config.getCurrency());
		formatter.setCurrency(currency);		
		
		return formatter;
	}
}
