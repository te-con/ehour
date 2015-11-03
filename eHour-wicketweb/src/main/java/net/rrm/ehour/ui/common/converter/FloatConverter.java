package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.util.convert.ConversionException;
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
    public Float convertToObject(final String value, final Locale locale) throws ConversionException {
        String nonNumericsToPoints = value.replaceAll("['| ]", "").replaceAll(",", ".").replaceAll("[^0-9|^\\.]", "");

        int lastDotAt = nonNumericsToPoints.lastIndexOf(".");

        if (lastDotAt > -1) {
            int decimalsBehindPoint = nonNumericsToPoints.length() - nonNumericsToPoints.lastIndexOf(".");
            String withoutDots = value.replaceAll("[^0-9*]", "");

            int insertAt = (withoutDots.length() - decimalsBehindPoint) + 1;
            String toParse = new StringBuilder(withoutDots).insert(insertAt, '.').toString();

            try {
                return Float.parseFloat(toParse);
            } catch (NumberFormatException nfe) {
                throw new ConversionException(nfe);
            }
        } else {
            return parse (value);
        }
    }

    private Float parse(String toParse) {
        Number number = parse(toParse, -Float.MAX_VALUE, Float.MAX_VALUE, Locale.US);

        if (number == null) {
            return null;
        } else {
            return number.floatValue();
        }
    }

    @Override
    protected Class<Float> getTargetType() {
        return Float.class;
    }
}
