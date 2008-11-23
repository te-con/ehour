/**
 * Created on Aug 31, 2007
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

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Conditional required validator, field is only required when the model evaluates to true 
 **/

public class ConditionalRequiredValidator extends AbstractValidator implements INullAcceptingValidator
{
	private static final long serialVersionUID = 6633525281870496233L;
	private IModel	conditionalModel;
	private	boolean	reverse;

	/**
	 * 
	 * @param conditionalModel
	 */
	public ConditionalRequiredValidator(IModel conditionalModel)
	{
		this(conditionalModel, false);
	}

	/**
	 * 
	 * @param conditionalModel
	 * @param reverse the conditional model
	 */
	public ConditionalRequiredValidator(IModel conditionalModel, boolean reverse)
	{
		this.conditionalModel = conditionalModel;
	}


	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.validation.validator.AbstractValidator#onValidate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void onValidate(IValidatable validatable)
	{
		boolean	condition = ((Boolean)conditionalModel.getObject()).booleanValue();
		
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
