package net.rrm.ehour.ui.pm;

import com.google.common.base.Optional;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.util.Locale;

public class OptionalFloatConverter implements IConverter<Optional<Float>> {
    @Override
    public Optional<Float> convertToObject(String value, Locale locale) throws ConversionException {
        return Optional.absent();
    }

    @Override
    public String convertToString(Optional<Float> value, Locale locale) {
        return value.isPresent() ? Double.toString(Math.round(value.get() * 100.0) / 100.0) : "--";
    }
}
