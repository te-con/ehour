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