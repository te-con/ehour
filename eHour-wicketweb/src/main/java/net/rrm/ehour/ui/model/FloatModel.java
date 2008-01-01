/**
 * Created on Jun 2, 2007
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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Convert floats to rounded 2 digits 
 **/

public class FloatModel extends AbstractNumberModel
{
	private static final long serialVersionUID = -3297133594178935106L;
	private String defaultValue = "--";
	private EhourConfig	config;
	
	/**
	 * 
	 * @param config
	 */
	public FloatModel()
	{
		super(new Model());
	}	
	
	/**
	 * 
	 * @param config
	 */
	public FloatModel(EhourConfig config)
	{
		this(new Model(), config);
	}
	
	/**
	 * 
	 * @param value
	 * @param config
	 */
	public FloatModel(Number value, EhourConfig config)
	{
		super(value);

		this.config = config;
	}
	
	/**
	 * 
	 * @param value
	 * @param config
	 */
	public FloatModel(IModel model, EhourConfig config)
	{
		super(model);
		
		this.config = config;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.model.AbstractNumberModel#getFormatter()
	 */
	@Override
	protected NumberFormat getFormatter()
	{
		if (config == null)
		{
			config = EhourWebSession.getSession().getEhourConfig();
		}
		
		formatter = NumberFormat.getNumberInstance(config.getLocale());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		
		return formatter;
	}	
	
	/**
	 * 
	 * @param value
	 * @param config
	 */
	public FloatModel(IModel model, EhourConfig config, String defaultValue)
	{
		this(model, config);
		
		this.defaultValue = defaultValue;
	}		
	
	@Override
	protected String getDefaultValue()
	{
		return defaultValue;
	}
}
