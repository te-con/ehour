package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.report.reports.element.LockableDate;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DateToMonthConverter implements IConverter<LockableDate> {
    private static final long serialVersionUID = -5004560809428503944L;

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy MMMM");

    @Override
    public LockableDate convertToObject(String value, Locale locale) {
        return null;
    }

    @Override
    public String convertToString(LockableDate lockableDate, Locale locale) {
        if (lockableDate == null) {
            return "";
        }

        return FORMATTER.print(new LocalDate(lockableDate.getDate().getTime()));
    }
}
