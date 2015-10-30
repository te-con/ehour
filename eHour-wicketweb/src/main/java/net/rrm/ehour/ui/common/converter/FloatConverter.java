package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.util.convert.converter.AbstractDecimalConverter;

import java.text.NumberFormat;
import java.util.Locale;

public class FloatConverter extends AbstractDecimalConverter<Float> {
    private static final long serialVersionUID = 3978602245247446289L;

    @Override
    protected NumberFormat newNumberFormat(Locale locale) {
        EhourConfig config = EhourWebSession.getEhourConfig();

        Locale formattingLocale = config.getFormattingLocale();
        NumberFormat formatter = NumberFormat.getNumberInstance(formattingLocale);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter;
    }

    @Override
    public Float convertToObject(final String value, final Locale locale) {
        final Number number = parse(value, -Float.MAX_VALUE, Float.MAX_VALUE, locale);

        if (number == null) {
            return null;
        }

        return number.floatValue();
    }

    /**
     * @see org.apache.wicket.util.convert.converter.AbstractConverter#getTargetType()
     */
    @Override
    protected Class<Float> getTargetType() {
        return Float.class;
    }
}
