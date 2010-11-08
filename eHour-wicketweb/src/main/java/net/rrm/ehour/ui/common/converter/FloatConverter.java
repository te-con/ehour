package net.rrm.ehour.ui.common.converter;

import java.text.NumberFormat;

import net.rrm.ehour.config.EhourConfig;

import org.apache.commons.lang.StringUtils;

public class FloatConverter extends AbstractNumberConverter
{
	private static final long serialVersionUID = 3978602245247446289L;
	
	public FloatConverter()
	{
	}
	
	public FloatConverter(String defaultStringValue)
	{
		super(defaultStringValue);
	}

	@Override
	protected NumberFormat getStringFormatter(EhourConfig config)
	{
		NumberFormat formatter = NumberFormat.getNumberInstance(config.getLocale());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		
		return formatter;
	}

	protected Object convertToObject(String value, EhourConfig config)
	{
		if (!StringUtils.isBlank(value))
		{
			try
			{
				return Float.parseFloat(value.replace(",", "."));
			}
			catch (NumberFormatException nfe)
			{
				return value;
			}
		}
		else
		{
			return null;
		}
	}	
}
