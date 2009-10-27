package net.rrm.ehour.config;

import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class EhourConfigUtil
{
	static TimeZone getTzAsTimeZone(EhourConfig config)
	{
		if (StringUtils.isNotBlank(config.getTimeZone()))
		{
			return TimeZone.getTimeZone(config.getTimeZone());
		}
		else
		{
			return TimeZone.getDefault();
		}
	}
}
