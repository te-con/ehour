/**
 * Created on Jul 10, 2007
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

package net.rrm.ehour.ui.panel.timesheet;

import net.rrm.ehour.ui.component.CommonModifiers;

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
	
	@SuppressWarnings("unchecked")
	public TimesheetTextField(final String id, IModel model, Class type, int tabIndex)
	{
		super(id, model, type);
		
		wasInvalid = false;
		
		if (model != null && model.getObject() != null)
		{
			previousValue = model.getObject();
		}
		
		add(CommonModifiers.tabIndexModifier(tabIndex)); 
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
