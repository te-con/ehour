package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.util.convert.IConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter implements IConverter<Date> {
    private static final long serialVersionUID = -5004560809428503944L;
    private final SimpleDateFormat format;

    public DateConverter() {
        Locale locale = EhourWebSession.getEhourConfig().getFormattingLocale();

        format = new SimpleDateFormat(DateUtil.getPatternForDateLocale(locale), locale);
    }

    @Override
    public Date convertToObject(String value, Locale locale) {
        try {
            return (Date) format.parseObject(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String convertToString(Date date, Locale locale) {
        return date == null ? "" : format.format(date);
    }
}
