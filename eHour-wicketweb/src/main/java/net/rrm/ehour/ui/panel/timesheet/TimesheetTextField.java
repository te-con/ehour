/**
 * Created on Jul 10, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.timesheet;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * Timesheet textfield which remembers its previous validation state
 **/

public class TimesheetTextField extends TextField
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7033801704569935582L;
	private	boolean	wasInvalid;
	private Object	previousValue;
	
	public TimesheetTextField(final String id, IModel model, Class type)
	{
		super(id, model, type);
		
		wasInvalid = false;
		
		if (model != null && model.getObject() != null)
		{
			previousValue = model.getObject();
		}
	}

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
}
