/**
 * Created on Aug 27, 2007
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

package net.rrm.ehour.ui.common.validator;

import java.util.Date;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * Checks whether the start date isn't after the end date
 * Fixes bug 1780975 
 **/

public class DateOverlapValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = -7176398632862551019L;
	private FormComponent[] components;
	
	/**
	 * 
	 * @param passwordField
	 * @param confirmField
	 */
	public DateOverlapValidator(FormComponent startDate,
								FormComponent endDate)
	{
		components = new FormComponent[]{startDate, endDate};
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
	 */
	public FormComponent[] getDependentFormComponents()
	{
		return components;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
	 */
	public void validate(Form form)
	{
		if (components[0].isVisible() 
				&& components[1].isVisible()
				&& components[0].getInput() != null
				&& components[0].getConvertedInput() != null
				&& components[1].getInput() != null
				&& components[1].getConvertedInput() != null)
		{
			Date startDate = (Date)components[0].getConvertedInput();
			Date endDate = (Date)components[1].getConvertedInput();
			
			if (endDate.before(startDate))
			{
				error(components[0], "Date.StartDateAfterEnd");
			}
		}
	}
}