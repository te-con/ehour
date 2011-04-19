package net.rrm.ehour.ui.common.converter;

import java.text.NumberFormat;
import java.util.Locale;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.util.convert.IConverter;

public abstract class AbstractNumberConverter implements IConverter
{
	private static final long serialVersionUID = 6457762655971221986L;

	private String defaultStringValue;
	
	public AbstractNumberConverter()
	{
		this("--");
	}
	
	public AbstractNumberConverter(String defaultStringValue)
	{
		this.defaultStringValue = defaultStringValue;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
	 */
	public final String convertToString(Object value, Locale locale)
	{
		return convertToString(value, getConfig());
	}
	
	final String convertToString(Object value, EhourConfig config)
	{
		return (value == null) ? defaultStringValue : getStringFormatter(config).format(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
	 */
	public final Object convertToObject(String value, Locale locale)
	{
		return convertToObject(value, getConfig());
	}

	
	protected abstract NumberFormat getStringFormatter(EhourConfig config);
	
	protected abstract Object convertToObject(String value, EhourConfig config);
	
	private EhourConfig getConfig()
	{
		return EhourWebSession.getSession().getEhourConfig();
	}
}
