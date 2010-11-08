package net.rrm.ehour.ui.common.component;

import net.rrm.ehour.ui.common.converter.CurrencyConverter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class CurrencyLabel extends ConverterLabel
{
	private static final long serialVersionUID = -8777767390766813803L;

	public CurrencyLabel(String id, IModel<?> model)
	{
		super(id, model, CurrencyConverter.getInstance());
	}

	public CurrencyLabel(String id, Number currency)
	{
		
		this(id, new Model<Float>(currency.floatValue()));
	}
}
