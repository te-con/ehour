/**
 * Created on Jun 3, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.model;

import java.text.NumberFormat;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Base model for displaying numbers/currencies etc
 * Implementing models should be cloneable with lazy instantation
 **/

public abstract class AbstractNumberModel extends Model
{
	private static final long serialVersionUID = -3297133594178935106L;
	private Number		value;
	private	IModel		nestedModel;
	protected NumberFormat	formatter;
	
	/**
	 * Static
	 */
	public AbstractNumberModel(Number value)
	{
		this.value = value;
	}
	
	public AbstractNumberModel(IModel model)
	{
		nestedModel = model;
		
		this.value = null;
	}	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.Model#getObject()
	 */
	@Override
	public Object getObject()
	{
		Number fmt;

		if (value == null && nestedModel != null)
		{
			fmt = (Number)nestedModel.getObject();  
		}
		else
		{
			fmt = value;
		}
			
		return (fmt == null) ? getDefaultValue() : getFormatter().format(fmt); 
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.Model#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(Object value)
	{
		if (value != null)
		{
			if (nestedModel == null)
			{
				this.value = (Number)value;
			}
			else
			{
				nestedModel.setObject(getDefaultValue() != null && value.equals(getDefaultValue()) ? null : value);
			}
		}
	}
	
	protected abstract NumberFormat getFormatter();
	
	/**
	 * 
	 * @return
	 */
	protected String getDefaultValue()
	{
		return "--";
	}
	
}
