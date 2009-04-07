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
		if (nestedModel == null)
		{
			this.value = (Number)value;
		}
		else
		{
			nestedModel.setObject(getDefaultValue() != null && (value != null && value.equals(getDefaultValue())) ? null : value);
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
