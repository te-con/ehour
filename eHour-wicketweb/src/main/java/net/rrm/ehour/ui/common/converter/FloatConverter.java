package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.config.EhourConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.convert.ConversionException;

import java.text.NumberFormat;

public class FloatConverter extends AbstractNumberConverter<Float> {
    private static final long serialVersionUID = 3978602245247446289L;

    public FloatConverter() {
    }

    public FloatConverter(String defaultStringValue) {
        super(defaultStringValue);
    }

    @Override
    protected NumberFormat getStringFormatter(EhourConfig config) {
        NumberFormat formatter = NumberFormat.getNumberInstance(config.getFormattingLocale());
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        return formatter;
    }

    protected Float convertToObject(String value, EhourConfig config) {
        if (!StringUtils.isBlank(value)) {
            try {
                return Float.parseFloat(value.replace(",", "."));
            } catch (NumberFormatException nfe) {
                throw new ConversionException(nfe);
            }
        } else {
            return null;
        }
    }
}
