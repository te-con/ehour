package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.config.EhourConfig;
import org.apache.log4j.Logger;

import java.text.NumberFormat;
import java.util.Currency;

public class CurrencyConverter extends AbstractNumberConverter<Currency>
{
	private static final long serialVersionUID = 1717369597555092582L;
	private static final Logger LOGGER = Logger.getLogger(CurrencyConverter.class);
	
	private static final CurrencyConverter instance = new CurrencyConverter();
	
	public Currency convertToObject(String value, EhourConfig config)
	{
		return null;
	}

	@Override
	protected NumberFormat getStringFormatter(EhourConfig config)
	{
		Currency	currency;
		
		try
		{
			currency = Currency.getInstance(config.getCurrency());
		}
		catch (IllegalArgumentException iae)
		{
			LOGGER.warn("Old currency selected, update the currency in the admin");
			currency = Currency.getInstance("EUR");
		}
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance(config.getCurrency());
		formatter.setCurrency(currency);		
		
		return formatter;
	}
	
	private CurrencyConverter()
	{
	}
	
	public static CurrencyConverter getInstance()
	{
		return instance;
	}	
}
