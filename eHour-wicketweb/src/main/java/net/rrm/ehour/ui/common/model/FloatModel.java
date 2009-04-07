/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.model;

import java.text.NumberFormat;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;

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
	 * @see net.rrm.ehour.ui.common.model.AbstractNumberModel#getFormatter()
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
