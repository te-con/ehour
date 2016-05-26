package net.rrm.ehour.ui.common.component;

import net.rrm.ehour.ui.common.converter.CurrencyConverter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;

public class CurrencyLabel extends Label {
    private static final long serialVersionUID = -8777767390766813803L;

    public CurrencyLabel(String id, IModel<?> model) {
        super(id, model);
    }

    public CurrencyLabel(String id, Float value) {
        super(id, new Model<>(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Currency> IConverter<Currency> getConverter(Class<Currency> type) {
        return (IConverter<Currency>) CurrencyConverter.getInstance();
    }
}
