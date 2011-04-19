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

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Conditional required validator, field is only required when the model evaluates to true 
 **/

public class ConditionalRequiredValidator<T> extends AbstractValidator<T>
{
	private static final long serialVersionUID = 6633525281870496233L;
	private IModel<Boolean>	conditionalModel;
	private	boolean	reverse;

	/**
	 * 
	 * @param conditionalModel
	 */
	public ConditionalRequiredValidator(IModel<Boolean> conditionalModel)
	{
		this(conditionalModel, false);
	}

	/**
	 * 
	 * @param conditionalModel
	 * @param reverse the conditional model
	 */
	public ConditionalRequiredValidator(IModel<Boolean> conditionalModel, boolean reverse)
	{
		this.conditionalModel = conditionalModel;
	}


	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#onValidate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void onValidate(IValidatable<T> validatable)
	{
		boolean	condition = conditionalModel.getObject().booleanValue();
		
		if ( (reverse && condition || !reverse && !condition)
				&& (validatable.getValue() == null 
						|| validatable.getValue().toString().trim() == null)) 
		{
			error(validatable, "Required");
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#validateOnNullValue()
	 */
	@Override
	public boolean validateOnNullValue()
	{
		return true;
	}	
}
