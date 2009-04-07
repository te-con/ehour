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

package net.rrm.ehour.ui.timesheet.panel;

import java.util.Locale;

import net.rrm.ehour.ui.common.component.CommonModifiers;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Timesheet textfield which remembers its previous validation state
 **/

public class TimesheetTextField extends TextField
{
	private static final long serialVersionUID = 7033801704569935582L;
	private	boolean	wasInvalid;
	private Object	previousValue;

	/**
	 * 
	 * @param id
	 * @param model
	 * @param type
	 * @param tabIndex
	 */
	@SuppressWarnings("unchecked")
	public TimesheetTextField(final String id, IModel model, Class type, int tabIndex)
	{
		super(id, model, type);
		
		setConvertEmptyInputStringToNull(true);
		
		wasInvalid = false;
		
		if (model != null && model.getObject() != null)
		{
			previousValue = model.getObject();
		}
		
		add(CommonModifiers.tabIndexModifier(tabIndex)); 
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.Component#getConverter(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IConverter getConverter(Class c)
	{
		return new IConverter()
		{
			private static final long serialVersionUID = 1L;

			public Object convertToObject(String value, Locale locale)
			{
				if (!StringUtils.isBlank(value))
				{
					try
					{
						return Float.parseFloat(value.replace(",", "."));
					}
					catch (NumberFormatException nfe)
					{
						return value;
					}
				}
				else
				{
					return null;
				}
			}

			public String convertToString(Object value, Locale locale)
			{
				return (String)value;
			}
			
		};
	}
	
	/**
	 * Is changed since previous submit
	 * @return
	 */
	public boolean isChanged()
	{
		if (this.getModel() != null && this.getModel().getObject() != null)
		{
			if (previousValue == null || !previousValue.equals(getModel().getObject()))
			{
				previousValue = getModel().getObject();
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @return the wasInvalid
	 */
	public boolean isWasInvalid()
	{
		return wasInvalid;
	}

	/**
	 * @param wasInvalid the wasInvalid to set
	 */
	public void setWasInvalid(boolean wasInvalid)
	{
		this.wasInvalid = wasInvalid;
		
		if (!wasInvalid)
		{
			previousValue = null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.AbstractTextComponent#isInputNullable()
	 */
	@Override
	public boolean isInputNullable()
	{
		return true;
	}
	
}
