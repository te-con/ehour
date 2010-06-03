package net.rrm.ehour.service.config;

import java.util.TimeZone;

import net.rrm.ehour.config.EhourConfig;

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
