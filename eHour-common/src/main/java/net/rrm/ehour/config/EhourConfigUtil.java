package net.rrm.ehour.config;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class EhourConfigUtil {
    private EhourConfigUtil() {
    }

    public static TimeZone getTzAsTimeZone(EhourConfig config) {
        if (StringUtils.isNotBlank(config.getTimeZone())) {
            return DateTimeZone.forID(config.getTimeZone()).toTimeZone();
        } else {
            return DateTimeZone.getDefault().toTimeZone();
        }
    }
}
