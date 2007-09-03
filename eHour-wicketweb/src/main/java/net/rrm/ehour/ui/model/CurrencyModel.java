/**
 * Created on Jun 2, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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
		super(value);
		
		Currency	currency;

		
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
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		formatter.setCurrency(currency);
	}
}
