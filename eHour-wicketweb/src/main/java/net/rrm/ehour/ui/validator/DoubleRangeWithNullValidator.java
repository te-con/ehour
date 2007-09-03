/**
 * Created on Jul 7, 2007
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

package net.rrm.ehour.ui.validator;

import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Double range validator allowing nulls
 */

public class DoubleRangeWithNullValidator extends AbstractValidator implements INullAcceptingValidator
{
	private static final long serialVersionUID = -8111638763473340175L;

	private double minimum;

	private double maximum;

	public DoubleRangeWithNullValidator(double min, double max)
	{
		this.minimum = min;
		this.maximum = max;
	}

	@Override
	public void onValidate(IValidatable validatable)
	{
		Number value = (Number) validatable.getValue();
		
		if (value.doubleValue() < minimum || value.doubleValue() > maximum)
		{
			error(validatable);
		}
	}
}
