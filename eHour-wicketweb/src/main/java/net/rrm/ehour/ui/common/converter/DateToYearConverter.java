package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.report.reports.element.LockableDate;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.LocalDate;

import java.util.Locale;

public class DateToYearConverter implements IConverter<LockableDate> {
    private static final long serialVersionUID = -5004560809428503944L;

    @Override
    public LockableDate convertToObject(String value, Locale locale) {
        return null;
    }

    @Override
    public String convertToString(LockableDate lockableDate, Locale locale) {
        if (lockableDate == null) {
            return "";
        }

        LocalDate partial = new LocalDate(lockableDate.getDate().getTime());

        return String.valueOf(partial.getYear());
    }
}
