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

import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Double range validator allowing nulls
 */

public class DoubleRangeWithNullValidator extends AbstractValidator<Number> implements INullAcceptingValidator<Number>
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
	public void onValidate(IValidatable<Number> validatable)
	{
		try
		{
			Number value = validatable.getValue();
			
			if (value.doubleValue() < minimum || value.doubleValue() > maximum)
			{
				error(validatable);
			}
		}
		catch (Throwable t)
		{
			error(validatable);
		}
	}
}
