package net.rrm.ehour.config;

import org.apache.commons.lang.StringUtils;

import java.util.TimeZone;

public class EhourConfigUtil {
    static TimeZone getTzAsTimeZone(EhourConfig config) {
        if (StringUtils.isNotBlank(config.getTimeZone())) {
            return TimeZone.getTimeZone(config.getTimeZone());
        } else {
            return TimeZone.getDefault();
        }
    }
}
