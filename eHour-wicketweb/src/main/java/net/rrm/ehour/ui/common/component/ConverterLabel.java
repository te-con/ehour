package net.rrm.ehour.ui.common.component;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class ConverterLabel extends Label
{
	private static final long serialVersionUID = -8777767390766813803L;

	private IConverter converter;
	
	public ConverterLabel(String id, IModel<?> model, IConverter converter)
	{
		super(id, model);
		
		this.converter = converter;
	}

	@Override
	public final IConverter getConverter(Class<?> type)
	{
		return converter;
	}
}
