package net.rrm.ehour.ui.common.converter;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.convert.IConverter;

public class RoleConverter implements IConverter
{
	private static final long serialVersionUID = 5724539913385092839L;

	public String convertToString(Object value, Locale locale)
	{
		if (StringUtils.isBlank((String)value))
		{
			return "--";
		}
		else
		{
			return (String)value;
		}
	}
	
	public Object convertToObject(String value, Locale locale)
	{
		if ("--".equals(value))
		{
			return null;
		}
		else
		{
			return (String)value;
		}
	}
}
