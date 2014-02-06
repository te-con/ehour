package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.report.reports.element.LockableDate;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.util.convert.IConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LockableDateConverter implements IConverter<LockableDate> {
    private final SimpleDateFormat format;

    public LockableDateConverter() {
        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();

        format = new SimpleDateFormat(DateUtil.getPatternForDateLocale(locale), locale);
    }

    @Override
    public LockableDate convertToObject(String value, Locale locale) {
        try {
            if (value.indexOf('(') >= 0) {
                String d = value.substring(0, value.indexOf('(') - 2);
                return new LockableDate((Date) format.parseObject(d), true);
            } else {
                return new LockableDate((Date) format.parseObject(value), false);
            }
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String convertToString(LockableDate date, Locale locale) {
        return date == null ? "" : String.format("%s %s", format.format(date.getDate()), date.isLocked() ? "*" : "");
    }
}
